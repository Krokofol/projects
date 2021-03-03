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
        }

        nodes.addAll(graph2Nodes);
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
}
