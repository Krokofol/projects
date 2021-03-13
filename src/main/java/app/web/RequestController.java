package app.web;

import app.holdingUnits.Graph;
import app.holdingUnits.GraphHolder;
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
     * Processes requests with "convert" address.
     * @param body body of the request.
     * @return response, which consist of the text and Http status.
     */
    @RequestMapping("convert")
    @PostMapping
    public ResponseEntity<String> convert(@RequestBody Map<String,
            String> body) {

        String from = body.get("from");
        String to = body.get("to");

        if (checkInput(from, to))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String[] fromTo = refactorArgs(from, to);
        from = fromTo[0];
        to = fromTo[1];

        Double result = calculateResult(
                new ArrayList<>(Arrays.asList(from.split("\\*"))),
                new ArrayList<>(Arrays.asList(to.split("\\*")))
        );

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new DecimalFormat("#.###############").format(result),
                HttpStatus.OK
        );
    }

    private boolean checkInput(String from, String to) {
        if (from == null || to == null)
            return true;
        from = from.replace(" ", "");
        to = to.replace(" ", "");
        return from.equals("") || to.equals("");
    }


    private static String[] refactorArgs(String from, String to) {
        String[] units1 = from.split("/");
        String[] units2 = to.split("/");

        return new String[]{
                buildArgs(units1, units2).toString(),
                buildArgs(units2, units1).toString()
        };
    }


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
