package app.holdingUnits.containers;

import app.holdingUnits.GraphHolder;
import app.search.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test Graph class.
 *
 * @version 1.0.0 17 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
class GraphTest {

    /**
     * deletes all graphs and nodes which could be created at other tests.
     */
    public GraphTest() {
        GraphHolder.cleanUp();
    }

    /**
     * Creates graph with only one node. Checks does graph have only one node.
     */
    @Test
    public void buildTest() {
        String startNodeName = "startNode";
        Node startNode = Node.createNode(startNodeName);
        Graph graph = new Graph(startNode);
        Integer expectedGraphSize = 1;
        assertEquals(expectedGraphSize, graph.getGraphSize());
        assertTrue(graph.existenceNode(startNodeName));
        assertEquals(startNode, graph.findNode(startNodeName));
    }

    /**
     * Creates graph with only one node. Connect to the existing node one more node. Checks are the bose nodes in the
     * graph.
     */
    @Test
    public void searchAddTest() {
        String node1Name = "node1";
        String node2Name = "node2";

        assertFalse(Node.checkExistence(node1Name));
        assertFalse(Node.checkExistence(node2Name));

        Node node1 = Node.createNode(node1Name);
        Graph graph = new Graph(node1);

        assertNotNull(node1);
        assertTrue(graph.existenceNode(node1Name));
        assertFalse(graph.existenceNode(node2Name));
        assertEquals(node1, graph.findNode(node1Name));
        graph.addNode(node1Name, node2Name, new Value("0.1"));
        assertTrue(graph.existenceNode(node2Name));
        Value convertingResult = graph.findConverting(node2Name,node1Name);
        String expectedConvertingResult = "10";
        assertEquals(expectedConvertingResult, convertingResult.toString());
    }

    /**
     * Creates graph with only one node. Connects to the existing node one more node. Creates one more graph with the
     * third node. Connects to graphs together. Checks are all nodes in graph to which was connected second graph.
     */
    @Test
    public void connectionsTest() {
        String node1Name = "node1";
        Node node1 = Node.createNode(node1Name);
        String node2Name = "node2";
        String node3Name = "node3";
        Node node3 = Node.createNode(node3Name);
        Graph graph12 = new Graph(node1);
        graph12.addNode(node1Name, node2Name, new Value("10"));
        Graph graph3 = new Graph(node3);
        graph12.connect(graph3, node1Name, node3Name, new Value("0.1"));

        assertNotNull(node1);
        assertNotNull(node3);
        assertTrue(graph12.existenceNode(node1Name));
        assertTrue(graph12.existenceNode(node3Name));
        assertEquals(node1, graph12.findNode(node1Name));
        assertEquals(node3, graph12.findNode(node3Name));

        String expectedConvertingResult = "1";
        Value convertingResult = graph12.findConverting(node2Name, node3Name);
        assertEquals(expectedConvertingResult, convertingResult.toString());

        expectedConvertingResult = "0.1";
        convertingResult = graph12.findConverting(node2Name, node1Name);
        assertEquals(expectedConvertingResult, convertingResult.toString());

        expectedConvertingResult = "10";
        convertingResult = graph12.findConverting(node1Name, node3Name);
        assertEquals(expectedConvertingResult, convertingResult.toString());
    }
}