package com.app;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes;

    public Graph(Node startNode) {
        nodes = new ArrayList<>();
        nodes.add(startNode);
    }

    public boolean existenceNode(String nodeName) {
        if (nodes.size() == 0) return false;
        int pos = findPos(nodeName);
        if (nodes.size() == pos) return false;
        return nodes.get(pos).getName().equals(nodeName);
    }

    public void connect(Graph graph2, String nodeName, String graph2NodeName, Double startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        ArrayList<Node> graph2Nodes = graph2.getNodes();

        graph2Node.createEdge(node, startQuotient);
        node.createEdge(graph2Node, 1 / startQuotient);

        for (Node nodeIterator : graph2Nodes) {
            Node.setGraphsForName(nodeIterator.getName(), this);
            nodes.add(findPos(nodeIterator.getName()), nodeIterator);
        }
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node newNode, String neighboringNodeName, Double startQuotient) {
        Node neighboringNode = findNode(neighboringNodeName);

        newNode.createEdge(neighboringNode, startQuotient);
        neighboringNode.createEdge(newNode, 1 / startQuotient);

        Node.setGraphsForName(newNode.getName(), this);
        nodes.add(findPos(newNode.getName()), newNode);
    }

    public Node findNode(String neighboringNodeName) {
        return nodes.get(findPos(neighboringNodeName));
    }

    public int findPos(String nodeName) {
        int leftPos = 0;
        int rightPos = nodes.size();

        int mid = (leftPos + rightPos) / 2;

        while (leftPos < rightPos) {
            int compare = nodes.get(mid).getName().compareTo(nodeName);
            if (compare == 0) {
                return mid;
            }
            if (compare > 0) {
                rightPos = mid;
            } else {
                leftPos = mid + 1;
            }
            mid = (leftPos + rightPos) / 2;
        }
        return Math.max(leftPos, rightPos);
    }

    public Double findWay(String startNodeName, String endNodeName) {
        ArrayList<Node> changedNodes = new ArrayList<>();
        ArrayList<Node> queue = new ArrayList<>();
        ArrayList<Double> quotients = new ArrayList<>();

        Node startNode = findNode(startNodeName);
        startNode.setVisit(Visit.inQueue);

        changedNodes.add(startNode);
        queue.add(startNode);
        quotients.add(1.0);

        while (!queue.get(0).getName().equals(endNodeName)) {
            Node node = queue.get(0);
            Double quotient = quotients.get(0);
            for (Edge edgeIterator : node.getEdges()) {
                Node neighboringNode = edgeIterator.getNode2();
                if(neighboringNode.getVisit() == Visit.notVisited) {
                    neighboringNode.setVisit(Visit.inQueue);
                    changedNodes.add(neighboringNode);
                    queue.add(queue.size(), neighboringNode);
                    quotients.add(quotients.size(), quotient * edgeIterator.getQuotient());
                }
            }
            node.setVisit(Visit.visited);
            queue.remove(0);
            quotients.remove(0);
        }

        Double result = quotients.get(0);

        for (Node nodeIterator : changedNodes) {
            nodeIterator.setVisit(Visit.notVisited);
        }

        return result;
    }

}
