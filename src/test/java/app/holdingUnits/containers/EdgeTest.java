package app.holdingUnits.containers;

import app.search.MyBigDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test Edge class.
 *
 * @version 1.0.0 17 Mar 2021.
 * @author Aleksey Lakhanksii.
 */
class EdgeTest {

    /**
     * Creates two nodes and connects them by edge. Checks do both nodes have
     * the same edge. Also checks does edge have the correct quotient.
     */
    @Test
    public void buildTest() {
        String node1Name = "node1EdgeTest1";
        String node2Name = "node2EdgeTest1";
        Node node1 = Node.createNode(node1Name);
        Node node2 = Node.createNode(node2Name);

        assertNotNull(node1);
        assertNotNull(node2);

        node1.createEdge(node2, new MyBigDecimal("1"));

        Edge node1Edge = node1.findEdge(node2Name);
        Edge node2Edge = node2.findEdge(node1Name);

        assertEquals(node1Edge, node2Edge);
        assertEquals("1", node2Edge.getQuotient().toString());
    }

}