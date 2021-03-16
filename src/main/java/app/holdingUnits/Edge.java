package app.holdingUnits;

/**
 * Edge which connects two nodes and contains converting quotient.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Edge {

    /** node from which this edge is going. */
    public Node node1;

    /** node to where this edge is going. */
    public Node node2;

    /** the quotient of converting. */
    public MyBigDecimal quotient;

    /**
     * constructs edge.
     * @param node1 node from which converting is going.
     * @param node2 node to where converting is going.
     * @param quotient the quotient of converting.
     */
    public Edge(Node node1, Node node2, MyBigDecimal quotient) {
        this.node1 = node1;
        this.node2 = node2;
        this.quotient = quotient;
    }

    /**
     * gets node from where converting is going.
     * @return the node.
     */
    public Node getNode1() {
        return node1;
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
    public MyBigDecimal getQuotient() {
        return quotient;
    }
}
