package com.app.holdingUnits;

import java.util.ArrayList;

/**
 * Node is holding name of the unit and all it's rules of converting (edges).
 * Also class Node holding all units (Nodes) which have been preloaded
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lkahanskii
 *
 */
public class Node implements CompareInterface {
    public static ArrayList<Node> allNodes = new ArrayList<>();
    public static ArrayList<Graph> graphsForNames = new ArrayList<>();

    public int posNumInGraph;
    public String name;
    public ArrayList<Edge> edges;

    public static Node createNode(String name) {
        if (checkExistence(name)) {
            return null;
        }
        Node node = new Node (name);
        int pos = PosSearch.searchPosition(name, allNodes);
        allNodes.add(pos, node);
        graphsForNames.add(pos, null);
        return node;
    }

    public static boolean checkExistence (String name) {
        if (allNodes.size() == 0) {
            return false;
        }
        int pos = PosSearch.searchPosition(name, allNodes);
        if (allNodes.size() == pos) {
            return false;
        }
        return allNodes.get(pos).getName().equals(name);
    }

    private Node(String nodeName) {
        name = nodeName;
        edges = new ArrayList<>();
    }

    public static void setGraphsForName(String name, Graph graph) {
        graphsForNames.set(PosSearch.searchPosition(name, allNodes), graph);
    }

    public static Graph getGraph(int index) {
        return graphsForNames.get(index);
    }

    public static ArrayList<Node> getAllNames() {
        return allNodes;
    }

    public void createEdge(Node neighboringNode, Double quotient) {
        assert neighboringNode != null;
        String neighboringNodeName = neighboringNode.getName();
        int edgePosition = PosSearch
                .searchPosition(neighboringNodeName, edges);
        Edge newEdge = new Edge(this, neighboringNode, quotient);
        this.edges.add(edgePosition, newEdge);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setPosNumInGraph(int posNumInGraph) {
        this.posNumInGraph = posNumInGraph;
    }

    public int getPosNumInGraph() {
        return posNumInGraph;
    }

    @Override
    public int compare(String secondName) {
        return  getName().compareTo(secondName);
    }
}
