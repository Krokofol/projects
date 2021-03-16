package app.holdingUnits;

import app.holdingUnits.containers.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphHolderTest {

    @Test
    public void preloadSearchTest() {
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

        assertEquals(3, GraphHolder.graphs.size());

        String fromNodeName = "км";
        String toNodeName = "пм";
        String noSuchUnit = "unit";

        assertFalse(Node.checkExistence(noSuchUnit));
        assertTrue(Node.checkExistence(fromNodeName));
        assertTrue(Node.checkExistence(toNodeName));
        assertNotNull(Node.getGraph(fromNodeName));
        assertEquals(Node.getGraph(fromNodeName), Node.getGraph(toNodeName));
    }
}