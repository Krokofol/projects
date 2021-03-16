package app.holdingUnits.containers;

import app.search.MyBigDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    @Test
    public void buildTest() {
        String node1Name = "node1EdgeTest1";
        String node2Name = "node2EdgeTest1";
        Node node1 = Node.createNode(node1Name);
        Node node2 = Node.createNode(node2Name);

        assertNotNull(node1);
        assertNotNull(node2);

        node1.createEdge(node2, new MyBigDecimal("1"));

        assertEquals(node1, node1.findEdge(node2Name).getNode1());
        assertEquals(node1, node2.findEdge(node1Name).getNode1());
        assertEquals(node2, node1.findEdge(node2Name).getNode2());
        assertEquals(node2, node2.findEdge(node1Name).getNode2());
    }

}