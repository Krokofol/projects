package app.holdingUnits.containers;

import app.search.Value;

import java.util.*;

/**
 * Class to hold nodes of the graph. Also it should find way to convert one unit of this graph to other one.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Graph {

    /** All nodes of the graph. */
    private final HashMap<String, Node> nodesForNames;

    /**
     * Constructs the graph and adds start node to this graph.
     * @param startNode first graph node.
     */
    public Graph(Node startNode) {
        nodesForNames = new HashMap<>();
        nodesForNames.put(startNode.getName(), startNode);
    }

    /**
     * Add one of nodes, which is not exists yet.
     * @param node1Name the first node name.
     * @param node2Name the second node name.
     * @param startQuotient the quotient of converting from the first node to the second.
     */
    public void addNode(String node1Name, String node2Name, Value startQuotient) {
        Node existenceNode;
        Node newNode;
        Value newRule = new Value();
        if (existenceNode(node1Name)) {
            existenceNode = nodesForNames.get(node1Name);
            newNode = Node.createNode(node2Name);
            newRule.divide(startQuotient);
            nodesForNames.put(node2Name, newNode);
        }
        else {
            existenceNode = nodesForNames.get(node2Name);
            newNode = Node.createNode(node1Name);
            newRule.multiply(startQuotient);
            nodesForNames.put(node1Name, newNode);
        }
        assert newNode != null;
        Value rule = new Value();
        rule.multiply(existenceNode.getConvertingRule());
        rule.multiply(newRule);
        newNode.setConvertingRule(rule);
        Node.setGraphsForName(newNode.getName(), this);
    }

    /**
     * Connects two graphs.
     * @param graph2 the second graph.
     * @param nodeName node name from the first graph.
     * @param graph2NodeName node name from the second graph.
     * @param startQuotient the quotient of converting.
     */
    public void connect(Graph graph2, String nodeName, String graph2NodeName, Value startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        Value newRule = new Value();

        newRule.multiply(startQuotient);
        newRule.multiply(node.getConvertingRule());
        newRule.divide(graph2Node.getConvertingRule());

        HashMap<String, Node> nodesForNamesGraph2 = graph2.getNodesForNames();
        for (Map.Entry<String, Node> entry : nodesForNamesGraph2.entrySet()) {
            entry.getValue().getConvertingRule().multiply(newRule);
            Node.setGraphsForName(entry.getValue().getName(), this);
            nodesForNames.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Checks the existence of the node by node name.
     * @param nodeName node name.
     * @return if node exists - true, else - false.
     */
    public boolean existenceNode(String nodeName) {
        return nodesForNames.get(nodeName) != null;
    }

    /**
     * Gets node by name.
     * @param nodeName node name.
     * @return if node with such name exists returns node, else returns null.
     */
    public Node findNode(String nodeName) {
        return nodesForNames.get(nodeName);
    }

    /**
     * Get HashMap with key as node name and value as node.
     * @return HashMap nodeForNames.
     */
    public HashMap<String, Node> getNodesForNames() {
        return nodesForNames;
    }

    /**
     * Gets converting rule from one node to other one.
     * @param startNodeName node from which rule search.
     * @param endNodeName node to which rule search.
     * @return the converting rule.
     */
    public Value findConverting(String startNodeName, String endNodeName) {
        Value result = new Value();
        Node startNode = nodesForNames.get(startNodeName);
        Node endNode = nodesForNames.get(endNodeName);
        result.multiply(startNode.getConvertingRule());
        result.divide(endNode.getConvertingRule());
        return result;
    }

    /**
     * Function to get amount of the nodes in this graph.
     * @return amount of the nodes in this graph.
     */
    public Integer getGraphSize() {
        return nodesForNames.size();
    }
}
