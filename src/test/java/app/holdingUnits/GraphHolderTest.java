package app.holdingUnits;

import app.holdingUnits.containers.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test GraphHolder class.
 *
 * @version 1.0.0 17 Mar 2021
 * @author Aleksey Lakhanksii
 *
 */
class GraphHolderTest {

    /**
     * Preloads required for tests data. Builds graphs if they do not exists
     * yet and adds to them nodes.
     */
    public GraphHolderTest() {
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
     * Checks the existing of nodes which able to convert from one to other
     * in one graph.
     */
    @Test
    public void parsingTest() {
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