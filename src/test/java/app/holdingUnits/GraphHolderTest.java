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
     * Checks the existing of nodes which able to convert from one to other in one graph.
     */
    @Test
    public void parsingTest() {
        GraphHolder.cleanUp();
        preloadData();
        Integer expectedGraphHolderSize = 3;
        assertEquals(expectedGraphHolderSize, GraphHolder.getGraphHolderSize());

        String fromNodeName = "км";
        String toNodeName = "пм";
        String noSuchUnit = "unit";

        assertFalse(Node.checkExistence(noSuchUnit));
        assertTrue(Node.checkExistence(fromNodeName));
        assertTrue(Node.checkExistence(toNodeName));
        assertNotNull(Node.getGraph(fromNodeName));
        assertEquals(Node.getGraph(fromNodeName), Node.getGraph(toNodeName));
    }

    /**
     * adds to the graph Holder some graphs and cleans them app. Expected that after cleaning up graph Holder size
     * will be 0.
     */
    @Test
    public void cleanUpTest() {
        Integer graphHolderEmptySize = 0;
        preloadData();
        assertNotEquals(graphHolderEmptySize, GraphHolder.getGraphHolderSize());
        GraphHolder.cleanUp();
        assertEquals(graphHolderEmptySize, GraphHolder.getGraphHolderSize());
    }

    /**
     * preloads some converting rules.
     */
    public void preloadData() {
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

}