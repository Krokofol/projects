package com.app;

public class Edge {
    public Node node1;
    public Node node2;
    public Double quotient;

    public Edge(Node node1, Node node2, Double quotient) {
        this.node1 = node1;
        this.node2 = node2;
        this.quotient = quotient;
    }

    public Node getNode2() {
        return node2;
    }

    public Double getQuotient() {
        return quotient;
    }

    public Node getNeighboringNode() {
        return node2;
    }
}
