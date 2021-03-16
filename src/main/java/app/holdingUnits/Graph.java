package app.holdingUnits;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public HashMap<String, Node> nodesForNames;

    /**
     * constructs the graph.
     * @param startNode first graph node.
     */
    public Graph(Node startNode) {
        nodesForNames = new HashMap<>();
        nodesForNames.put(startNode.getName(), startNode);
    }

    /**
     * one of this nodes not in this graph.
     * @param node1Name the first node name.
     * @param node2Name the second node name.
     * @param startQuotient the quotient of converting.
     */
    public void addNode(String node1Name, String node2Name,
                        BigDecimal startQuotient) {
        Node node1 = nodesForNames.get(node1Name);
        if (node1 == null) {
            node1 = Node.createNode(node1Name);
            Node.setGraphsForName(node1Name, this);
            nodesForNames.put(node1Name, node1);
        }
        Node node2 = nodesForNames.get(node2Name);
        if (node2 == null) {
            node2 = Node.createNode(node2Name);
            Node.setGraphsForName(node2Name, this);
            nodesForNames.put(node2Name, node2);
        }
        assert node1 != null;
        node1.createEdge(node2, startQuotient);
    }

    /**
     * connects two graphs.
     * @param graph2 the second graph.
     * @param nodeName node name from the first graph.
     * @param graph2NodeName node name from the second graph.
     * @param startQuotient the quotient of converting.
     */
    public void connect(Graph graph2, String nodeName, String graph2NodeName,
                        BigDecimal startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        graph2Node.createEdge(node, startQuotient);

        HashMap<String, Node> nodesForNamesGraph2 = graph2.getNodesForNames();
        for (Map.Entry<String, Node> entry : nodesForNamesGraph2.entrySet()) {
            Node.setGraphsForName(entry.getValue().getName(), this);
            nodesForNames.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * checks the existence of the node by node name.
     * @param nodeName node name.
     * @return if node exists - true, else - false.
     */
    public boolean existenceNode(String nodeName) {
        return nodesForNames.get(nodeName) != null;
    }

    /**
     * gets node by name.
     * @param nodeName node name.
     * @return node.
     */
    public Node findNode(String nodeName) {
        return nodesForNames.get(nodeName);
    }

    /**
     * get HashMap with nodes.
     * @return HashMap nodeForNames.
     */
    public HashMap<String, Node> getNodesForNames() {
        return nodesForNames;
    }

    /**
     * connects two nodes in one graph.
     * @param node1Name the first node.
     * @param node2Name the second node.
     * @param quotient the conversion's quotient.
     */
    public void addEdge(String node1Name, String node2Name, BigDecimal quotient) {
        Node node1 = nodesForNames.get(node1Name);
        if (node1.getEdgeBySecondNodeName(node2Name) != null)
            return;
        Node node2 = nodesForNames.get(node2Name);
        node1.createEdge(node2, quotient);
    }

    /**
     * Dijkstra search, but the weight is the inaccuracy in the representation
     * of a number in binary form.
     * @param startNodeName node name from which we are converting.
     * @param endNodeName node name to where we are converting.
     * @return the quotient of converting.
     */
    public BigDecimal findConverting(String startNodeName, String endNodeName) {
        Node startNode = nodesForNames.get(startNodeName);
        Node endNode = nodesForNames.get(endNodeName);

        HashMap<Node, BigDecimal> queue = new HashMap<>();
        HashSet<Node> visitedNodes = new HashSet<>();

        queue.put(startNode, new BigDecimal(0));

        BigDecimal converting;
        Node workingNode;
        do {
            if (queue.entrySet().stream().findFirst().isEmpty())
                return null;
            workingNode = queue.entrySet().stream().findFirst().get().getKey();
            converting = queue.entrySet().stream().findFirst().get().getValue();
            queue.remove(workingNode);
            visitedNodes.add(workingNode);
            Set<Map.Entry<String, Edge>> entrySet = workingNode.getAllEdges()
                    .entrySet();
            for(Map.Entry<String, Edge> entry : entrySet) {
                Edge edge = entry.getValue();
                if (visitedNodes.contains(edge.getNode2()))
                    continue;
                BigDecimal quotient = entry.getValue().getQuotient()
                        .multiply(converting);
                queue.put(edge.getNode2(), quotient);
            }
        } while (workingNode != endNode);

        return converting;
    }
}
