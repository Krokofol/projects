package app.holdingUnits;

import app.holdingUnits.containers.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphHolderTest {

    @Test
    public void preloadSearchTest() {
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
    }
}