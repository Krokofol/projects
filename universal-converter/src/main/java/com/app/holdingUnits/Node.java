package com.app.holdingUnits;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Node is holding name of the unit and all its rules converting (edges).
 * Also class Node holding all units (Nodes) which have been preloaded.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lkahanskii
 *
 */
public class Node implements Comparable<String> {
    /* implements Comparable for searching position in PosSearch. */

    /** contains all added nodes. */
    public static HashMap<String, Node> nodesForNames = new HashMap<>();

    /** contains graph for existing nodes. */
    public static HashMap<String, Graph> graphsForNames = new HashMap<>();

    /** name of the node. */
    public String name;

    /** all edges of the node. */
    public HashMap<String, Edge> edgesForSecondNodeName;

    /**
     * creates new node if node with the same name does not exist yet.
     * @param name name of the new node which need to create.
     * @return new node or null if node already exists.
     */
    public static Node createNode(String name) {
        if (checkExistence(name)) {
            return null;
        }
        return new Node(name);
    }

    /**
     * checks existence of the node with the same name.
     * @param name name of the node which existence need be checked.
     * @return true if node exists else return false.
     */
    public static boolean checkExistence (String name) {
        return nodesForNames.get(name) != null;
    }

    /**
     * constructs node.
     * @param nodeName name of new node.
     */
    private Node(String nodeName) {
        name = nodeName;
        edgesForSecondNodeName = new HashMap<>();
    }

    /**
     * sets graph for node.
     * @param name name of the node for which graph sets.
     * @param graph graph to set.
     */
    public static void setGraphsForName(String name, Graph graph) {
        graphsForNames.put(name, graph);
    }

    /**
     * gets graph.
     * @param name node name.
     * @return graph.
     */
    public static Graph getGraph(String name) {
        return graphsForNames.get(name);
    }

    /**
     * creates edge.
     * @param secondNode node with which this edge connects.
     * @param quotient the rule of converting.
     */
    public void createEdge(Node secondNode, Double quotient) {
        Edge edge = new Edge(this, secondNode, quotient);
        edgesForSecondNodeName.put(secondNode.getName(), edge);
        secondNode.addEdge(this.getName(), edge);
    }

    /**
     * adds existing edge to node.
     * @param secondNodeName node with which this edge connects.
     * @param edge the edge with converting rule.
     */
    public void addEdge(String secondNodeName, Edge edge) {
        edgesForSecondNodeName.put(secondNodeName, edge);
    }

    /**
     * gets name.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * comparing for search in PosSearch.
     * @param secondName name of comparing node.
     * @return if equal - 0, bigger > 0 and smaller < 0.
     */
    @Override
    public int compareTo(@NotNull String secondName) {
        return  getName().compareTo(secondName);
    }
}
