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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for processing requests.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
@Controller
public class RequestController {

    /** Logger for this class. */
    private final static Logger logger = Logger.getLogger(RequestController.class.getName());

    /**
     * Processes requests with "convert" address. Also before converting waits the ending of preloading thread to be
     * sure that all units are preloaded.
     * @param body body of the request.
     * @return response, which consist of the text and Http status.
     */
    @RequestMapping("convert")
    @PostMapping
    public ResponseEntity<String> convert(@RequestBody Map<String, String> body) {
        String from = body.get("from");
        String to = body.get("to");

        try {
            Preloader.getPreloadingThread().join();
            logger.log(Level.FINE, "preloading thread join is successful");
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "preloading thread join error");
            e.printStackTrace();
        }

        if (checkInput(from, to)) {
            logger.log(Level.FINER, "\"from\" or \"to\" is not declared");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.log(Level.FINER, "got body with such \"from\" and \"to\"\n-from : " + from + "\n-to : " + to);
        String[] fromTo = refactorArgs(from, to);
        if (fromTo == null || (fromTo[0].equals("") && fromTo[1].equals(""))) {
            logger.log(Level.FINER, "bad form of \"from\" or \"to\" \n-from : " + from + "\n-to : " + to);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String[] fromSeparated = fromTo[0].split("\\*");
        String[] toSeparated = fromTo[1].split("\\*");
        boolean allUnitsFromExist = checkExistence(fromSeparated);
        boolean allUnitsToExist = checkExistence(toSeparated);
        if (allUnitsToExist || allUnitsFromExist) {
            //is already logged in function "checkExistence"
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String result = calculateResult(
                new ArrayList<>(Arrays.asList(fromSeparated)),
                new ArrayList<>(Arrays.asList(toSeparated))
        );

        if (result == null) {
            logger.log(Level.FINER, "unable to convert \nfrom : (" + from + ") \nto : (" + to + ")");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.log(Level.FINER, "converting result \nfrom : (" + from + ") \nto : (" + to + ") \nis : " + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Checks is input empty.
     * @param from input string with units from which converts.
     * @param to input string with units to which converts.
     * @return if one of inputs is empty returns true, else false.
     */
    private boolean checkInput(String from, String to) {
        return from == null || to == null;
    }

    /**
     * Splits numerator and denominator of "from" and "to" units. Multiplies numerator of "from" and denominator of
     * "to" and writs it into "from" and also multiplies numerator of "to" and denominator of "from" and writes it
     * into "to".
     * @param from string with the units from which converts.
     * @param to string with the units to which converts.
     * @return if form of "from" and "to" string is correct returns two elements, first is new "from" string, second is
     * new "to" string.
     */
    private static String[] refactorArgs(String from, String to) {
        boolean fromContainsDividing = from.contains("/");
        boolean toContainsDividing = to.contains("/");
        String[] fromUnits = from.replace(" ", "").split("/");
        String[] toUnits = to.replace(" ", "").split("/");
        if ((fromContainsDividing && fromUnits.length == 1) || (toContainsDividing && toUnits.length == 1)) {
            return null;
        }
        if (fromUnits[0].equals("1")) {
            fromUnits[0] = "";
        }
        if (toUnits[0].equals("1")) {
            toUnits[0] = "";
        }
        return new String[]{
                buildArgs(fromUnits, toUnits).toString(),
                buildArgs(toUnits, fromUnits).toString()
        };
    }

    /**
     * Multiplies numerator (units before "/") of the first units and denominator (units after "/") of the second
     * units.
     * @param units1 first units set.
     * @param units2 second units set.
     * @return string with multiplication numerator of the first units and denominator of the second units.
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
     * Checks existence of the units.
     * @param units units which existence should be checked.
     * @return if all units exists returns false, else returns true.
     */
    private boolean checkExistence(String[] units) {
        for (String nameIterator : units) {
            if (nameIterator.equals("")) {
                continue;
            }
            if (!Node.checkExistence(nameIterator)) {
                logger.log(Level.FINER, "there is not such unit : " + nameIterator);
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates converting from "from" units to "to" units. Iterates through "toUnits" and tries to find conversion
     * to one of "fromUnits".
     * @param toUnits units to which converting rule is searches.
     * @param fromUnits units from which converting rule is searches.
     * @return if finds all conversions then return the result as string, if doesn't finds one of conversions returns
     * null.
     */
    private String calculateResult (ArrayList<String> toUnits, ArrayList<String> fromUnits) {
        if (fromUnits.size() != toUnits.size()) {
            return null;
        }
        final Value result = new Value();
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
     * Finds units which are able to convert from one to other and adds searching thread to searching array.
     * @param toUnits to which units convert;
     * @param fromUnits from which units convert;
     * @param searchThreads collection of prepared to start searching threads.
     * @return true if impossible to convert units, else returns false.
     */
    private boolean getConvertingWays(ArrayList<String> toUnits, ArrayList<String> fromUnits,
                                      ArrayList<Searcher> searchThreads) {
        whileLoop:
        while (fromUnits.size() > 0) {
            String numeratorIterator = fromUnits.get(0);
            Graph graph = GraphHolder.findGraph(numeratorIterator);
            for (String denominatorIterator : toUnits) {
                if(graph.existenceNode(denominatorIterator)) {
                    searchThreads.add(new Searcher(graph, numeratorIterator, denominatorIterator));
                    toUnits.remove(denominatorIterator);
                    fromUnits.remove(numeratorIterator);
                    continue whileLoop;
                }
            }
            return true;
        }
        return false;
    }
}
