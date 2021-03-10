package com.app.holdingUnits;

import java.util.ArrayList;

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
    public ArrayList<Node> nodes;

    /**
     * constructs the graph.
     * @param startNode first graph node.
     */
    public Graph(Node startNode) {
        nodes = new ArrayList<>();
        nodes.add(startNode);
        startNode.setPosNumInGraph(0);
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
        neighboringNode.createEdge(newNode, 1 / startQuotient);

        Node.setGraphsForName(newNode.getName(), this);
        int pos = PosSearch.posSearch(newNode.getName(), nodes);
        nodes.add(pos, newNode);
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
        ArrayList<Node> graph2Nodes = graph2.getNodes();

        graph2Node.createEdge(node, startQuotient);
        node.createEdge(graph2Node, 1 / startQuotient);

        for (Node nodeIterator : graph2Nodes) {
            Node.setGraphsForName(nodeIterator.getName(), this);
            int pos = PosSearch.posSearch(nodeIterator.getName(), nodes);
            nodes.add(pos, nodeIterator);
            nodeIterator.setPosNumInGraph(pos);
        }
    }

    /**
     * checks the existence of the node by node name.
     * @param nodeName node name.
     * @return if node exists - true, else - false.
     */
    public boolean existenceNode(String nodeName) {
        if (nodes.size() == 0) return false;
        int pos = PosSearch.posSearch(nodeName, nodes);
        if (nodes.size() == pos) return false;
        return nodes.get(pos).getName().equals(nodeName);
    }

    /**
     * indexes nodes.
     */
    public void setNodesIndexes() {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setPosNumInGraph(i);
        }
    }

    /**
     * gets all nodes of the graph.
     * @return nodes.
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * gets node by name.
     * @param nodeName node name.
     * @return node.
     */
    public Node findNode(String nodeName) {
        return nodes.get(PosSearch.posSearch(nodeName, nodes));
    }

    /**
     * connects two nodes in one graph.
     * @param node1Name the first node.
     * @param node2Name the second node.
     * @param quotient the conversion's quotient.
     */
    public void addEdge(String node1Name, String node2Name, Double quotient) {
        Node node1 = nodes.get(PosSearch.posSearch(node1Name, nodes));
        Node node2 = nodes.get(PosSearch.posSearch(node2Name, nodes));
        node1.createEdge(node2, quotient);
        node2.createEdge(node1, 1 / quotient);
    }

    /**
     * finds the nearest converting rule from one node to the other one.
     * @param startNodeName node name from which we are converting.
     * @param endNodeName node name to where we are converting.
     * @return the quotient of converting.
     */
    public Double findWay(String startNodeName, String endNodeName) {
        Visit[] nodesStatus = new Visit[nodes.size()];
        ArrayList<Node> queue = new ArrayList<>();
        ArrayList<Double> quotients = new ArrayList<>();

        Node startNode = findNode(startNodeName);
        nodesStatus[startNode.getPosNumInGraph()] = Visit.inQueue;

        queue.add(startNode);
        quotients.add(1.0);

        while (!queue.get(0).getName().equals(endNodeName)) {
            Node node = queue.get(0);
            Double quotient = quotients.get(0);
            for (Edge edgeIterator : node.getEdges()) {
                Node neighboringNode = edgeIterator.getNode2();
                int posInGraph = neighboringNode.getPosNumInGraph();
                if(nodesStatus[posInGraph] == null) {
                    nodesStatus[posInGraph] = Visit.inQueue;
                    queue.add(queue.size(), neighboringNode);
                    quotients.add(quotients.size(),
                            quotient * edgeIterator.getQuotient());
                }
            }
            nodesStatus[node.getPosNumInGraph()] = Visit.visited;
            queue.remove(0);
            quotients.remove(0);
        }

        return quotients.get(0);
    }
}
