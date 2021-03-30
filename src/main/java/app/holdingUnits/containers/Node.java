package app.holdingUnits.containers;

import app.search.Value;

import java.util.HashMap;

/**
 * Node is holding name of the unit and all its rules converting (edges). Also class Node holding all units (Nodes)
 * which have been preloaded.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lkahanskii
 *
 */
public class Node {

    /** Contains all added nodes. */
    private final static HashMap<String, Node> nodesForNames = new HashMap<>();

    /** Contains graph for existing nodes. */
    private final static HashMap<String, Graph> graphsForNames = new HashMap<>();

    /** Name of the node. */
    private final String name;

    /** Converting rule to the first unit in the graph. */
    private Value convertingRule;

    /**
     * Creates new node if node with the same name does not exist yet.
     * @param name name of the new node which need to create.
     * @return new node or null if node already exists.
     */
    public static Node createNode(String name) {
        if (checkExistence(name)) {
            return null;
        }
        Node newNode = new Node(name);
        nodesForNames.put(name, newNode);
        return newNode;
    }

    /**
     * Checks existence of the node with the same name.
     * @param name name of the node which existence need be checked.
     * @return true if node exists else return false.
     */
    public static boolean checkExistence (String name) {
        return nodesForNames.get(name) != null;
    }

    /**
     * Constructs node.
     * @param nodeName name of new node.
     */
    private Node(String nodeName) {
        name = nodeName;
        convertingRule = new Value();
    }

    /**
     * Gets converting rule from this node to the first node of the graph in which it's exists.
     * @return the converting rule to the start node of the graph.
     */
    public Value getConvertingRule() {
        return convertingRule;
    }

    /**
     * Sets new converting rule from this node to the first node of the graph in which it's exists.
     * @param convertingRule new converting rule to the start node of the graph.
     */
    public void setConvertingRule(Value convertingRule) {
        this.convertingRule = convertingRule;
    }

    /**
     * Sets graph for node with such name.
     * @param name name of the node for which graph sets.
     * @param graph graph to set.
     */
    public static void setGraphsForName(String name, Graph graph) {
        graphsForNames.put(name, graph);
    }

    /**
     * Gets graph in which Node with such name is located.
     * @param name node name.
     * @return if node with such name exists returns graph, else returns null.
     */
    public static Graph getGraph(String name) {
        return graphsForNames.get(name);
    }

    /**
     * Gets name of the node.
     * @return name name of the node.
     */
    public String getName() {
        return name;
    }

    /**
     * Finds node with this name.
     * @param nodeName name of the Node.
     * @return if such Node exists returns it, else returns null.
     */
    public static Node findNode(String nodeName) {
        return nodesForNames.get(nodeName);
    }

    /**
     * Deletes all nodes.
     */
    public static void cleanUp() {
        nodesForNames.clear();
    }
}
