package com.app.holdingUnits;

import org.jetbrains.annotations.NotNull;

/**
 * Edge which connects two nodes and contains converting quotient.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Edge implements Comparable<String>{
    /* implements Comparable for searching position in PosSearch. */

    /** node from which this edge is going. */
    public Node node1;

    /** node to where this edge is going. */
    public Node node2;

    /** the quotient of converting. */
    public Double quotient;

    /**
     * constructs edge.
     * @param node1 node from which converting is going.
     * @param node2 node to where converting is going.
     * @param quotient the quotient of converting.
     */
    public Edge(Node node1, Node node2, Double quotient) {
        this.node1 = node1;
        this.node2 = node2;
        this.quotient = quotient;
    }

    /**
     * gets node to where converting is going.
     * @return the node.
     */
    public Node getNode2() {
        return node2;
    }

    /**
     * gets the quotient of converting.
     * @return the quotient.
     */
    public Double getQuotient() {
        return quotient;
    }

    /**
     * comparing for search in PosSearch.
     * @param secondName name of the node to where converting is going in edge.
     * @return if equal - 0, bigger > 0 and smaller < 0.
     */
    @Override
    public int compareTo(@NotNull String secondName) {
        return getNode2().getName().compareTo(secondName);
    }
}
