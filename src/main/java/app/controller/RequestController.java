package app.controller;

import app.holdingUnits.*;
import app.holdingUnits.containers.Graph;
import app.holdingUnits.containers.Node;
import app.search.Value;
import app.search.Searcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Class for processing requests.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
@Controller
public class RequestController {

    /**
     * Processes requests with "convert" address. Also it waits the ending of preloading thread.
     * @param body body of the request.
     * @return response, which consist of the text and Http status.
     */
    @RequestMapping("convert")
    @PostMapping
    public ResponseEntity<String> convert(@RequestBody Map<String, String> body) {
        String from = body.get("from");
        String to = body.get("to");

        try {
            Preloader.preloader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (checkInput(from, to)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String[] fromTo = refactorArgs(from, to);
        if (fromTo == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String[] fromSeparated = fromTo[0].split("\\*");
        String[] toSeparated = fromTo[1].split("\\*");
        boolean allUnitsFromExist = checkExistence(fromSeparated);
        boolean allUnitsToExist = checkExistence(toSeparated);
        if (allUnitsToExist || allUnitsFromExist) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String result = calculateResult(
                new ArrayList<>(Arrays.asList(fromSeparated)),
                new ArrayList<>(Arrays.asList(toSeparated))
        );

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * checks is input empty.
     * @param from input string with units from which converts.
     * @param to input string with units to which converts.
     * @return if one of inputs is empty returns true, else false.
     */
    private boolean checkInput(String from, String to) {
        return from == null || to == null;
    }

    /**
     * splits numerator and denominator of "from" and "to" units. Multiplies numerator of "from" and denominator of
     * "to" and writs it into "from" and also multiplies numerator of "to" and denominator of "from" and writes it
     * into "to".
     * @param from "from" units
     * @param to "to" units
     * @return two elements, first is new "from" string, second is new "to"
     * string.
     */
    private static String[] refactorArgs(String from, String to) {
        boolean fromContainsDividing = from.contains("/");
        boolean toContainsDividing = to.contains("/");
        String[] fromUnits = from.replace(" ", "").split("/");
        String[] toUnits = to.replace(" ", "").split("/");
        if ((fromContainsDividing && fromUnits.length == 1) || (toContainsDividing && toUnits.length == 1)) {
            return null;
        }
        return new String[]{
                buildArgs(fromUnits, toUnits).toString(),
                buildArgs(toUnits, fromUnits).toString()
        };
    }

    /**
     * Multiplies numerator of the first units and denominator of the second units.
     * @param units1 first units.
     * @param units2 second units.
     * @return multiplication.
     */
    private static StringBuilder buildArgs(String[] units1, String[] units2) {
        StringBuilder result = new StringBuilder();
        if (units2.length > 0) result.append(units2[0]);
        if (units2.length > 0 && units1.length > 1) {
            if (units2[0].length() > 0) {
                result.append("*");
            }
        }
        if (units1.length > 1) result.append(units1[1]);
        return result;
    }

    /**
     * checks existence of all units.
     * @param units all units.
     * @return if everything is ok - false, else - true.
     */
    private boolean checkExistence(String[] units) {
        for (String nameIterator : units) {
            if (!Node.checkExistence(nameIterator)) {
                return true;
            }
        }
        return false;
    }

    /**
     * iterates through "toUnits" and tries to find conversion to one of "fromUnits".
     * @param toUnits units to which we are converting.
     * @param fromUnits units from which we are converting.
     * @return if it finds all conversions then return the result, if it don't finds one of conversions returns false.
     */
    private String calculateResult (ArrayList<String> toUnits, ArrayList<String> fromUnits) {
        if (fromUnits.size() != toUnits.size()) {
            return null;
        }
        final Value result = new Value("1");
        ArrayList<Searcher> searchThreads = new ArrayList<>();

        if (getConvertingWays(toUnits, fromUnits, searchThreads)) {
            return null;
        }
        searchThreads.forEach(Searcher::start);
        searchThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        searchThreads.forEach(thread -> result.multiply(thread.getResult()));

        return result.toString();
    }

    /**
     * finds units which are able to convert from one to other.
     * @param toUnits to which units convert;
     * @param fromUnits from which units convert;
     * @param searchThreads collection of prepared to start threads.
     * @return true if impossible to convert units.
     */
    private boolean getConvertingWays(ArrayList<String> toUnits, ArrayList<String> fromUnits,
                                      ArrayList<Searcher> searchThreads) {
        first:
        while (fromUnits.size() > 0) {
            String numeratorIterator = fromUnits.get(0);
            Graph graph = GraphHolder.findGraph(numeratorIterator);
            for (String denominatorIterator : toUnits) {
                if(graph.existenceNode(denominatorIterator)) {
                    searchThreads.add(new Searcher(graph, numeratorIterator,
                            denominatorIterator));
                    toUnits.remove(denominatorIterator);
                    fromUnits.remove(numeratorIterator);
                    continue first;
                }
            }
            return true;
        }
        return false;
    }
}
