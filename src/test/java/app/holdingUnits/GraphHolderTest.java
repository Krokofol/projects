package app.holdingUnits;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphHolderTest {

    @Test
    public void preloadAndSearchTest() {
        GraphHolder.preload("target/test-classes/smallTestData.csv");

        assertEquals(3, GraphHolder.graphs.size());

        String fromNodeName = "км";
        String toNodeName = "пм";
        String noSuchUnit = "unit";
        String expectedResult = "1000000000000000";

        assertFalse(Node.checkExistence(noSuchUnit));
        assertTrue(Node.checkExistence(fromNodeName));
        assertTrue(Node.checkExistence(toNodeName));
        assertNotNull(Node.getGraph(fromNodeName));
        assertEquals(Node.getGraph(fromNodeName), Node.getGraph(toNodeName));

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