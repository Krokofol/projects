package app.search;

import app.holdingUnits.containers.Graph;
import app.holdingUnits.containers.Node;
import app.holdingUnits.GraphHolder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test Searcher class.
 *
 * @version 1.0.0 17 Mar 2021
 * @author Aleksey Lakhanksii
 *
 */
class SearcherTest {

    /**
     * Preloads required for tests data. Builds graphs if they do not exists
     * yet and adds to them nodes.
     */
    public SearcherTest() {
        String[] args = {
                "км,м,1000",
                "м,мм,1000",
                "мм,мкм,1000",
                "мкм,нм,1000",
                "нм,пм,1000",
                "час,мин,60",
                "кг,г,1000"
        };
        for (String string : args) {
            GraphHolder.parseLine(string);
        }
    }

    /**
     * Converts one units to the other (to which is able to convert) and checks
     * the result of converting.
     */
    @Test
    public void converting1() {
        String fromNodeName = "км";
        String toNodeName = "пм";
        String expectedResult = "1000000000000000";
        Graph graph = Node.getGraph(fromNodeName);
        Searcher searcher = new Searcher(graph, fromNodeName, toNodeName);
        searcher.start();
        try {
            searcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String convertingResult = searcher.getResult().toString();
        assertEquals(expectedResult, convertingResult);
    }

    /**
     * Converts one units to the other (to which is able to convert) and checks
     * the result of converting. (reverse direction)
     */
    @Test
    public void converting2() {
        String fromNodeName = "пм";
        String toNodeName = "км";
        String expectedResult = "0.000000000000001";
        Graph graph = Node.getGraph(fromNodeName);
        Searcher searcher = new Searcher(graph, fromNodeName, toNodeName);
        searcher.start();
        try {
            searcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String convertingResult = searcher.getResult().toString();
        assertEquals(expectedResult, convertingResult);
    }
}