package com.app.holdingUnits;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to hold nodes of the graph. Also it should find way to convert one
 * unit to other one.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Graph {

    /** all nodes of the graph. */
    public HashMap<String, Node> nodesForNames;

    /**
     * constructs the graph.
     * @param startNode first graph node.
     */
    public Graph(Node startNode) {
        nodesForNames = new HashMap<>();
        nodesForNames.put(startNode.getName(), startNode);
    }

    /**
     * adds node to the graph.
     * @param newNode new node.
     * @param neighboringNodeName node with which new node has edge.
     * @param startQuotient the quotient of converting.
     */
    public void addNode(Node newNode, String neighboringNodeName,
                        Double startQuotient) {
        Node neighboringNode = findNode(neighboringNodeName);
        newNode.createEdge(neighboringNode, startQuotient);
        Node.setGraphsForName(newNode.getName(), this);
        nodesForNames.put(newNode.getName(), newNode);
    }

    /**
     * connects two graphs.
     * @param graph2 the second graph.
     * @param nodeName node name from the first graph.
     * @param graph2NodeName node name from the second graph.
     * @param startQuotient the quotient of converting.
     */
    public void connect(Graph graph2, String nodeName, String graph2NodeName,
                        Double startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        graph2Node.createEdge(node, startQuotient);

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
    public void addEdge(String node1Name, String node2Name, Double quotient) {
        Node node1 = nodesForNames.get(node1Name);
        Node node2 = nodesForNames.get(node2Name);
        node1.createEdge(node2, quotient);
    }

    /**
     * Dijkstra search, but the weight is the inaccuracy in the representation
     * of a number in binary form
     * @param startNodeName node name from which we are converting.
     * @param endNodeName node name to where we are converting.
     * @return the quotient of converting.
     */
    public Double findWay(String startNodeName, String endNodeName) {
        Node startNode = nodesForNames.get(startNodeName);
        Node endNode = nodesForNames.get(endNodeName);

        HashMap<Node, Double> inaccuracyForNodes = new HashMap<>();
        HashMap<Node, Node> prevNodeForNodes = new HashMap<>();
        HashSet<Node> visitedNodes = new HashSet<>();

        inaccuracyForNodes.put(startNode, 0.0);

        Node workingNode;
        do {
            Double minInaccuracy = Double.POSITIVE_INFINITY;
            workingNode = null;
            for(Map.Entry<Node, Double> entry : inaccuracyForNodes.entrySet())
            {
                if(Math.abs(entry.getValue()) < Math.abs(minInaccuracy)) {
                    minInaccuracy = entry.getValue();
                    workingNode = entry.getKey();
                }
            }

            assert workingNode != null;
            if (workingNode == endNode)
                System.out.println(minInaccuracy);

            inaccuracyForNodes.remove(workingNode);
            visitedNodes.add(workingNode);
            Set<Map.Entry<String, Edge>> entrySet = workingNode
                    .edgesForSecondNodeName.entrySet();
            for(Map.Entry<String, Edge> entry : entrySet) {
                Double quotient = entry.getValue().getQuotient();
                Edge edge = entry.getValue();
                if (visitedNodes.contains(edge.getNode2()))
                    continue;
                Double newInaccuracy = minInaccuracy;
                if (Math.abs(minInaccuracy + Math.log10(quotient))
                        > Math.abs(minInaccuracy))
                    newInaccuracy = minInaccuracy + Math.log10(quotient);
                Double oldInaccuracy = inaccuracyForNodes.get(edge.getNode2());
                if (oldInaccuracy == null) {
                    inaccuracyForNodes.put(edge.getNode2(), newInaccuracy);
                    prevNodeForNodes.put(edge.getNode2(), workingNode);
                    continue;
                }
                if (Math.abs(oldInaccuracy) > Math.abs(newInaccuracy)) {
                    inaccuracyForNodes.put(edge.getNode2(), newInaccuracy);
                    prevNodeForNodes.put(edge.getNode2(), workingNode);
                }
            }
        } while (workingNode != endNode);



        Double result = 1.0;
        while (workingNode != startNode) {
            Node prevNode = prevNodeForNodes.get(workingNode);
            result *= prevNode.getEdgeBySecondNodeName(workingNode.getName())
                    .getQuotient();
            workingNode = prevNode;
        }

        return result;
    }
}
