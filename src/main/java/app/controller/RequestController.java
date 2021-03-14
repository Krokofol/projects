package app.controller;

import app.holdingUnits.Graph;
import app.holdingUnits.GraphHolder;
import app.holdingUnits.Node;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Class for processing requests.
 */
@Controller
public class RequestController {

    /**
     * Processes requests with "convert" address.  Also it waits of ending
     * preloading thread.
     * @param body body of the request.
     * @return response, which consist of the text and Http status.
     */
    @RequestMapping("convert")
    @PostMapping
    public ResponseEntity<String> convert(@RequestBody Map<String,
            String> body) {

        String from = body.get("from");
        String to = body.get("to");

        try {
            GraphHolder.preloader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (checkInput(from, to))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String[] fromTo = refactorArgs(from, to);
        from = fromTo[0];
        to = fromTo[1];
        if (checkExistence(from.split(" \\* "))
            || checkExistence(to.split(" \\* "))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Double result = calculateResult(
                new ArrayList<>(Arrays.asList(from.split(" * "))),
                new ArrayList<>(Arrays.asList(to.split(" * ")))
        );

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new DecimalFormat("#.###############").format(result),
                HttpStatus.OK
        );
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
     * splits numerator and denominator of "from" and "to" units. Multiplies
     * numerator of "from" and denominator of "to" and writs it into "from" and
     * also multiplies numerator of "to" and denominator of "from" and writes
     * it into "to"
     * @param from "from" units
     * @param to "to" units
     * @return two elements, first is new "from" string, second is new "to"
     * string.
     */
    private static String[] refactorArgs(String from, String to) {
        String[] units1 = from.split(" / ");
        String[] units2 = to.split(" / ");

        return new String[]{
                buildArgs(units1, units2).toString(),
                buildArgs(units2, units1).toString()
        };
    }

    /**
     * Multiplies numerator of the first units and denominator of the
     * second units.
     * @param units1 first units.
     * @param units2 second units.
     * @return multiplication.
     */
    private static StringBuilder buildArgs(String[] units1, String[] units2) {
        StringBuilder result = new StringBuilder();
        if (units2.length > 0) result.append(units2[0]);
        if (units2.length > 0 && units1.length > 1) {
            if (units2[0].length() > 0) {
                result.append(" * ");
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
     * iterates through "toUnits" and tries to find conversion to one of
     * "fromUnits".
     * @param toUnits units to which we are converting.
     * @param fromUnits units from which we are converting.
     * @return if it finds all conversions then return the result, if it don't
     * finds one of conversions returns false.
     */
    private Double calculateResult (ArrayList<String> toUnits,
                                    ArrayList<String> fromUnits) {
        Double result = 1.0;

        if (fromUnits.size() != toUnits.size()) {
            return null;
        }

        first:
        if (fromUnits.size() > 0) {
            String numeratorIterator = fromUnits.get(0);
            Graph graph = GraphHolder.findGraph(numeratorIterator);

            for (String denominatorIterator : toUnits) {
                if(graph.existenceNode(denominatorIterator)) {
                    result *= graph.findConverting(numeratorIterator,
                            denominatorIterator);
                    toUnits.remove(denominatorIterator);
                    fromUnits.remove(numeratorIterator);
                    break first;
                }
            }
            return null;
        }
        return result;
    }

}
