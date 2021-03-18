package app.holdingUnits.containers;

import app.search.Value;

import java.util.*;

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
                        Value startQuotient) {
        Node node1 = nodesForNames.get(node1Name);
        if (node1 == null) {
            node1 = Node.createNode(node1Name);
            Node.setGraphsForName(node1Name, this);
            nodesForNames.put(node1Name, node1);
            assert node1 != null;
        }
        Node node2 = nodesForNames.get(node2Name);
        if (node2 == null) {
            node2 = Node.createNode(node2Name);
            Node.setGraphsForName(node2Name, this);
            nodesForNames.put(node2Name, node2);
            assert node2 != null;
        }
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
                        Value startQuotient) {
        Node graph2Node = graph2.findNode(graph2NodeName);
        Node node = findNode(nodeName);
        node.createEdge(graph2Node, startQuotient);

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
    public void addEdge(String node1Name, String node2Name, Value quotient) {
        Node node1 = nodesForNames.get(node1Name);
        if (node1.findEdge(node2Name) != null)
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
    public Value findConverting(
            String startNodeName,
            String endNodeName
    ) {
        Node startNode = nodesForNames.get(startNodeName);
        Node endNode = nodesForNames.get(endNodeName);
        HashMap<Node, Node> prevNodes = new HashMap<>();
        HashSet<Node> visitedNodes = new HashSet<>();
        ArrayList<Node> queue = new ArrayList<>();

        visitedNodes.add(startNode);
        queue.add(startNode);

        Node workingNode = queue.get(0);
        while (workingNode != endNode) {
            queue.remove(workingNode);
            Set<Map.Entry<String, Edge>> entrySet = workingNode.getAllEdges()
                    .entrySet();
            for(Map.Entry<String, Edge> entry : entrySet) {
                Edge edge = entry.getValue();
                Node toTheNode = edge.getNode1().equals(workingNode)
                        ? edge.getNode2()
                        : edge.getNode1();
                if (visitedNodes.contains(toTheNode))
                    continue;
                queue.add(toTheNode);
                prevNodes.put(toTheNode, workingNode);
                visitedNodes.add(toTheNode);
            }
            workingNode = queue.get(0);
        }

        Value numerator = new Value("1");
        Node prevNode;
        Edge edge;
        Node toNode;
        while (!workingNode.equals(startNode)) {
            prevNode = prevNodes.get(workingNode);
            edge = prevNode.findEdge(workingNode.getName());
            toNode = edge.getNode2();
            if (workingNode.equals(toNode)) {
                numerator.multiply(edge.getQuotient());
            }
            else {
                numerator.divide(edge.getQuotient());
            }
            workingNode = prevNode;
        }
        return numerator;
    }
}
