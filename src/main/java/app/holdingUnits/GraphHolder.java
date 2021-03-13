package app.holdingUnits;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Class which builds and holds all graphs.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class GraphHolder {

    /** all graphs */
    public static ArrayList<Graph> graphs = new ArrayList<>();

    /**
     * preloading graphs.
     * @param filePath path to file with converting rules.
     */
    public static void readingStartInfo(String filePath) {
        try (BufferedReader reader = preloadReader(filePath)) {
            String res = reader.readLine();
            while (res != null) {
                String node1Name = res.split(",")[0];
                String node2Name = res.split(",")[1];

                double quotient = Double.parseDouble(res.split(",")[2]);

                if (Node.checkExistence(node1Name)
                        && Node.checkExistence(node2Name)) {
                    connectTwoNodes(node1Name, node2Name, quotient);
                } else {
                    addNode(node1Name, node2Name, quotient);
                    addNode(node2Name, node1Name, 1 / quotient);
                }
                res = reader.readLine();
            }
            System.out.println("preloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * preloads buffer reader of file.
     * @param filePath path to the file with converting rules.
     * @return buffer reader.
     */
    private static BufferedReader preloadReader(String filePath) {
        File input = new File(filePath);
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(input),
                    StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert isr != null;
        return new BufferedReader(isr);
    }

    /**
     * tries to connect two already existing nodes.
     * @param node1Name first node name.
     * @param node2Name second node name.
     * @param quotient the quotient of converting.
     */
    private static void connectTwoNodes(String node1Name, String node2Name,
                                        Double quotient) {
        Graph graph1 = findGraph(node1Name);
        Graph graph2 = findGraph(node2Name);
        if (graph1 == graph2) {
            graph1.addEdge(node1Name, node2Name, quotient);
            return;
        }
        graphs.remove(graph2);
        graph1.connect(graph2, node1Name, node2Name, 1 / quotient);
    }

    /**
     * adds node to on of the graph if the second node exists.
     * @param nodeName first node.
     * @param neighboringNodeName second node.
     * @param quotient the quotient of converting.
     */
    public static void addNode(String nodeName, String neighboringNodeName,
                               Double quotient) {
        Node newNode = Node.createNode(nodeName);
        if (newNode == null)
            return;
        if (!Node.checkExistence(neighboringNodeName)) {
            createGraph(newNode);
            return;
        }
        findGraph(neighboringNodeName).addNode(newNode, neighboringNodeName,
                quotient);
    }

    /**
     * search graph for the node by node name. Node must exist.
     * @param nodeName node name.
     * @return graph.
     */
    public static Graph findGraph(String nodeName) {
        return Node.getGraph(nodeName);
    }

    /**
     * creates new graph for node which could not be connected to other graphs.
     * @param startNode first node in the graph.
     */
    public static void createGraph(Node startNode) {
        Graph graph = new Graph(startNode);
        graphs.add(graph);
        Node.setGraphsForName(startNode.getName(), graph);
    }
}
