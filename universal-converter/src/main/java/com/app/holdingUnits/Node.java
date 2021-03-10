package com.app.holdingUnits;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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

    /** allNodes contains all added nodes ordered by name. */
    public static ArrayList<Node> allNodes = new ArrayList<>();

    /**
     * graphsForNames contains graph for node with the same index in
     * allNodes array.
     */
    public static ArrayList<Graph> graphsForNames = new ArrayList<>();

    /** index of the node in graph. */
    public int posNumInGraph;

    /** name of the node. */
    public String name;

    /** all edges of the node. */
    public ArrayList<Edge> edges;

    /**
     * creates new node if node with the same name does not exist yet.
     * @param name name of the new node which need to create.
     * @return new node or null if node already exists.
     */
    public static Node createNode(String name) {
        if (checkExistence(name)) {
            return null;
        }
        Node node = new Node (name);
        int pos = PosSearch.posSearch(name, allNodes);
        allNodes.add(pos, node);
        graphsForNames.add(pos, null);
        return node;
    }

    /**
     * checks existence of the node with the same name.
     * @param name name of the node which existence need be checked.
     * @return true if node exists else return false.
     */
    public static boolean checkExistence (String name) {
        if (allNodes.size() == 0) {
            return false;
        }
        int pos = PosSearch.posSearch(name, allNodes);
        if (allNodes.size() == pos) {
            return false;
        }
        return allNodes.get(pos).getName().equals(name);
    }

    /**
     * constructs node.
     * @param nodeName name of new node.
     */
    private Node(String nodeName) {
        name = nodeName;
        edges = new ArrayList<>();
    }

    /**
     * sets graph for node.
     * @param name name of the node for which graph sets.
     * @param graph graph to set.
     */
    public static void setGraphsForName(String name, Graph graph) {
        graphsForNames.set(PosSearch.posSearch(name, allNodes), graph);
    }

    /**
     * gets graph.
     * @param index index of the graph.
     * @return graph.
     */
    public static Graph getGraph(int index) {
        return graphsForNames.get(index);
    }

    /**
     * gets all nodes.
     * @return nodes.
     */
    public static ArrayList<Node> getAllNames() {
        return allNodes;
    }

    /**
     * creates edge.
     * @param neighboringNode name of the node with which this edge connects.
     * @param quotient the rule of converting.
     */
    public void createEdge(Node neighboringNode, Double quotient) {
        assert neighboringNode != null;
        String neighboringNodeName = neighboringNode.getName();
        int edgePosition = PosSearch
                .posSearch(neighboringNodeName, edges);
        Edge newEdge = new Edge(this, neighboringNode, quotient);
        this.edges.add(edgePosition, newEdge);
    }

    /**
     * gets name.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * gets all edges of the node.
     * @return edges.
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * sets for this node index of this node in the graph.
     * @param posNumInGraph index in the graph
     */
    public void setPosNumInGraph(int posNumInGraph) {
        this.posNumInGraph = posNumInGraph;
    }

    /**
     * gets index of this node in the graph.
     * @return index.
     */
    public int getPosNumInGraph() {
        return posNumInGraph;
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
