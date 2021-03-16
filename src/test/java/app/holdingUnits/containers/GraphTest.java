package app.holdingUnits.containers;

import app.search.MyBigDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    public void buildTest() {
        String startNodeName = "startNodeGraphTest1";
        Node startNode = Node.createNode(startNodeName);
        Graph graph = new Graph(startNode);

        assertEquals(1, graph.nodesForNames.size());
    }

    @Test
    public void searchAddTest() {
        String node1Name = "node1GraphTest2";
        String node2Name = "node2GraphTest2";

        assertFalse(Node.checkExistence(node1Name));
        assertFalse(Node.checkExistence(node2Name));

        Node node1 = Node.createNode(node1Name);
        Graph graph = new Graph(node1);

        assertNotNull(node1);
        assertTrue(graph.existenceNode(node1Name));
        assertFalse(graph.existenceNode(node2Name));
        assertEquals(node1, graph.findNode(node1Name));
        graph.addNode(node1Name, node2Name, new MyBigDecimal("1"));
        assertTrue(graph.existenceNode(node2Name));
        assertEquals(node1.edgesForSecondNodeName.size(), 1);
    }

    @Test
    public void connectionsTest() {
        String node1Name = "node1GraphTest3";
        Node node1 = Node.createNode(node1Name);
        String node2Name = "node2GraphTest3";
        String node3Name = "node3GraphTest3";
        Node node3 = Node.createNode(node3Name);
        Graph graph12 = new Graph(node1);
        graph12.addNode(node1Name, node2Name, new MyBigDecimal("1"));
        Graph graph3 = new Graph(node3);
        graph12.connect(graph3, node1Name, node3Name, new MyBigDecimal("1"));

        assertNotNull(node1);
        assertNotNull(node3);
        assertTrue(graph12.existenceNode(node1Name));
        assertTrue(graph12.existenceNode(node3Name));
        assertEquals(node1, graph12.findNode(node1Name));
        assertEquals(node3, graph12.findNode(node3Name));
        assertEquals(2, node1.edgesForSecondNodeName.size());
    }

}