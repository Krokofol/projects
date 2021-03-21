package app.holdingUnits.containers;

import app.search.Value;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to hold nodes of the graph. Also it should find way to convert one
 * unit to other one.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Graph {

    /** distance to the node, which should be connected with the start node */
    private static final int CONNECT_EACH_NUMBER_OF_NODES = 500;

    /** logger for this class. */
    public static Logger logger = Logger.getLogger(Graph.class.getName());

    /** all nodes of the graph. */
    public HashMap<String, Node> nodesForNames;

    /** name of the first node. */
    public String firstNodeName;

    /**
     * constructs the graph.
     * @param startNode first graph node.
     */
    public Graph(Node startNode) {
        nodesForNames = new HashMap<>();
        nodesForNames.put(startNode.getName(), startNode);
        firstNodeName = startNode.getName();
    }

    /**
     * one of this nodes not in this graph.
     * @param node1Name the first node name.
     * @param node2Name the second node name.
     * @param startQuotient the quotient of converting.
     */
    public void addNode(String node1Name, String node2Name,
                        Value startQuotient) {
        Node existenceNode;
        Node newNode;
        Value newRule = new Value("1");
        if (existenceNode(node2Name)) {
            existenceNode = nodesForNames.get(node1Name);
            newNode = Node.createNode(node2Name);
            newRule.multiply(startQuotient);
        }
        else {
            existenceNode = nodesForNames.get(node2Name);
            newNode = Node.createNode(node1Name);
            newRule.divide(startQuotient);
        }
        assert newNode != null;
        Value rule = new Value("1");
        rule.multiply(existenceNode.getConvertingRule());
        rule.multiply(startQuotient);
        newNode.setConvertingRule(rule);
        Node.setGraphsForName(newNode.getName(), this);
    }

    /**
     * connects two graphs.
     * @param graph2 the second graph.
     * @param nodeName node name from the first graph.
     * @param graph2NodeName node name from the second graph.
     * @param startQuotient the quotient of converting.
     */
    public void connect(Graph graph2, String nodeName, String graph2NodeName,
                        Value startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        node.createEdge(graph2Node, startQuotient);

        HashMap<String, Node> nodesForNamesGraph2 = graph2.getNodesForNames();
        for (Map.Entry<String, Node> entry : nodesForNamesGraph2.entrySet()) {
            Node.setGraphsForName(entry.getValue().getName(), this);
            nodesForNames.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * checks the existence of the node by node name.
     * @param nodeName node name.
     * @return if node exists - true, else - false.
     */
    public boolean existenceNode(String nodeName) {
        return nodesForNames.get(nodeName) != null;
    }

    /**
     * gets node by name.
     * @param nodeName node name.
     * @return node.
     */
    public Node findNode(String nodeName) {
        return nodesForNames.get(nodeName);
    }

    /**
     * get HashMap with nodes.
     * @return HashMap nodeForNames.
     */
    public HashMap<String, Node> getNodesForNames() {
        return nodesForNames;
    }

    /**
     * connects two nodes in one graph.
     * @param node1Name the first node.
     * @param node2Name the second node.
     * @param quotient the conversion's quotient.
     */
    public void addEdge(String node1Name, String node2Name, Value quotient) {
        Node node1 = nodesForNames.get(node1Name);
        if (node1.findEdge(node2Name) != null)
            return;
        Node node2 = nodesForNames.get(node2Name);
        node1.createEdge(node2, quotient);
    }
}
