package com.app;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes;

    public Graph(Node startNode) {
        nodes = new ArrayList<>();
        nodes.add(startNode);
    }

    public boolean existenceNode(String nodeName) {
        //бинарный поиск
        for (Node nodeIterator : nodes) {
            if (nodeName.equals(nodeIterator.getName()))
                return true;
        }
        return false;
    }

    public void connect(Graph graph2, String nodeName, String graph2NodeName, Double startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        ArrayList<Node> graph2Nodes = graph2.getNodes();

        graph2Node.createEdge(node, startQuotient);
        for(Edge graph1EdgeIterator : node.edges) {
            graph2Node.createEdge(graph1EdgeIterator.getNeighboringNode(), startQuotient * graph1EdgeIterator.getQuotient());
        }

        graph2Nodes.remove(graph2Node);
        for(Node graph2NodeIterator : graph2Nodes) {
            for(Node graph1NodeIterator : nodes) {
                graph2NodeIterator.createEdge(graph1NodeIterator, graph2NodeIterator.findEdge(graph2NodeName).getQuotient() * graph2Node.findEdge(graph1NodeIterator.getName()).getQuotient());
            }
        }

        graph2Nodes.add(graph2Node);
        for(Node graph2NodeIterator : graph2Nodes) {
            for(Node graph1NodeIterator : nodes) {
                graph1NodeIterator.createEdge(graph2NodeIterator, 1 / graph2NodeIterator.findEdge(graph1NodeIterator.getName()).getQuotient());
            }
        }

        nodes.addAll(graph2Nodes);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node newNode, String neighboringNodeName, Double startQuotient) {
        //бинарный поиск
        Node neighboringNode = findNode(neighboringNodeName);
        //сортировка вставкой
        newNode.createEdge(neighboringNode, startQuotient);
        for (Edge edgeIterator : neighboringNode.edges) {
            newNode.createEdge(edgeIterator.getNeighboringNode(), startQuotient * edgeIterator.getQuotient());
        }
        for (Edge edgeIterator : newNode.edges) {
            edgeIterator.getNeighboringNode().createEdge(newNode, 1 / edgeIterator.getQuotient());
        }
        nodes.add(newNode);
    }

    public Node findNode(String neighboringNodeName) {
        for (Node nodeIterator : nodes)
            if (nodeIterator.getName().equals(neighboringNodeName))
                return nodeIterator;
        return nodes.get(0);
    }
}
