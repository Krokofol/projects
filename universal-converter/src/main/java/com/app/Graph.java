package com.app;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Graph {
    public CopyOnWriteArrayList<Node> nodes;

    public Graph(Node startNode) {
        nodes = new CopyOnWriteArrayList<>();
        nodes.add(startNode);
        startNode.setPosNumInGraph(0);
    }

    public boolean existenceNode(String nodeName) {
        if (nodes.size() == 0) return false;
        int pos = PosSearcher.searchNamePosInNodeArray(nodeName, nodes);
        if (nodes.size() == pos) return false;
        return nodes.get(pos).getName().equals(nodeName);
    }

    public void connect(Graph graph2, String nodeName, String graph2NodeName, Double startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        CopyOnWriteArrayList<Node> graph2Nodes = graph2.getNodes();

        graph2Node.createEdge(node, startQuotient);
        node.createEdge(graph2Node, 1 / startQuotient);

        for (Node nodeIterator : graph2Nodes) {
            Node.setGraphsForName(nodeIterator.getName(), this);
            int pos = PosSearcher.searchNamePosInNodeArray(nodeIterator.getName(), nodes);
            nodes.add(pos, nodeIterator);
            nodeIterator.setPosNumInGraph(pos);
        }
    }

    public CopyOnWriteArrayList<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node newNode, String neighboringNodeName, Double startQuotient) {
        Node neighboringNode = findNode(neighboringNodeName);

        newNode.createEdge(neighboringNode, startQuotient);
        neighboringNode.createEdge(newNode, 1 / startQuotient);

        Node.setGraphsForName(newNode.getName(), this);
        int pos = PosSearcher.searchNamePosInNodeArray(newNode.getName(), nodes);
        nodes.add(pos, newNode);
        newNode.setPosNumInGraph(pos);
    }

    public Node findNode(String neighboringNodeName) {
        return nodes.get(PosSearcher.searchNamePosInNodeArray(neighboringNodeName, nodes));
    }

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
                if(nodesStatus[neighboringNode.getPosNumInGraph()] == null) {
                    nodesStatus[neighboringNode.getPosNumInGraph()] = Visit.inQueue;
                    queue.add(queue.size(), neighboringNode);
                    quotients.add(quotients.size(), quotient * edgeIterator.getQuotient());
                }
            }
            nodesStatus[node.getPosNumInGraph()] = Visit.visited;
            queue.remove(0);
            quotients.remove(0);
        }

        return quotients.get(0);
    }

}
