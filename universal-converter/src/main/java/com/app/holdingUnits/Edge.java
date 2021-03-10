package com.app.holdingUnits;

/**
 * Edge which connects two nodes and contains converting rule.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Edge implements CompareInterface{
    /* implements CompareInterface for searching position in PosSearch */

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

    @Override
    public int compare(String secondName) {
        return getNode2().getName().compareTo(secondName);
    }
}
