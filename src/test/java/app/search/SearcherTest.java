package app.search;

import app.holdingUnits.containers.Graph;
import app.holdingUnits.containers.Node;
import app.holdingUnits.GraphHolder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearcherTest {

    @Test
    public void testSearch() {
        GraphHolder.preload("target/test-classes/smallTestData.csv");
        String fromNodeName = "км";
        String toNodeName = "пм";
        String noSuchUnit = "unit";
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
}