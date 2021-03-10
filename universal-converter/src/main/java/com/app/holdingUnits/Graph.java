package com.app.holdingUnits;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes;

    public Graph(Node startNode) {
        nodes = new ArrayList<>();
        nodes.add(startNode);
        startNode.setPosNumInGraph(0);
    }

    public boolean existenceNode(String nodeName) {
        if (nodes.size() == 0) return false;
        int pos = PosSearch.searchPosition(nodeName, nodes);
        if (nodes.size() == pos) return false;
        return nodes.get(pos).getName().equals(nodeName);
    }

    public void connect(Graph graph2, String nodeName, String graph2NodeName,
                        Double startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        ArrayList<Node> graph2Nodes = graph2.getNodes();

        graph2Node.createEdge(node, startQuotient);
        node.createEdge(graph2Node, 1 / startQuotient);

        for (Node nodeIterator : graph2Nodes) {
            Node.setGraphsForName(nodeIterator.getName(), this);
            int pos = PosSearch.searchPosition(nodeIterator.getName(), nodes);
            nodes.add(pos, nodeIterator);
            nodeIterator.setPosNumInGraph(pos);
        }
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node newNode, String neighboringNodeName,
                        Double startQuotient) {
        Node neighboringNode = findNode(neighboringNodeName);

        newNode.createEdge(neighboringNode, startQuotient);
        neighboringNode.createEdge(newNode, 1 / startQuotient);

        Node.setGraphsForName(newNode.getName(), this);
        int pos = PosSearch.searchPosition(newNode.getName(), nodes);
        nodes.add(pos, newNode);
    }

    public void setNodesIndexes() {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setPosNumInGraph(i);
        }
    }

    public Node findNode(String neighboringNodeName) {
        return nodes.get(PosSearch.searchPosition(neighboringNodeName, nodes));
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
