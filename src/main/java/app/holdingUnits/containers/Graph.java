package app.holdingUnits.containers;

import app.search.Value;

import java.util.*;

/**
 * Class to hold nodes of the graph. Also it should find way to convert one unit to other one.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Graph {

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
    public void addNode(String node1Name, String node2Name, Value startQuotient) {
        Node existenceNode;
        Node newNode;
        Value newRule = new Value("1");
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
        Value rule = new Value("1");
        rule.multiply(existenceNode.getConvertingRule());
        rule.multiply(newRule);
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
    public void connect(Graph graph2, String nodeName, String graph2NodeName, Value startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        Value newRule = new Value("1");

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
     * gets converting rule from one node to other one.
     * @param startNodeName node from which rule search.
     * @param endNodeName node to which rule search.
     * @return the converting rule.
     */
    public Value findConverting(String startNodeName, String endNodeName) {
        Value result = new Value("1");
        Node startNode = nodesForNames.get(startNodeName);
        Node endNode = nodesForNames.get(endNodeName);
        result.multiply(startNode.getConvertingRule());
        result.divide(endNode.getConvertingRule());
        return result;
    }
}
