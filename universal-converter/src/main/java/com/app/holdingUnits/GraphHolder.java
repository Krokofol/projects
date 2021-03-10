package com.app.holdingUnits;

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
    static public ArrayList<Graph> graphs = new ArrayList<>();

    public static void readingStartInfo(String filePath) {
        try {
            BufferedReader reader = preloadReader(filePath);
            String res = reader.readLine();
            while (res != null) {
                String node1Name = res.split(",")[0];
                String node2Name = res.split(",")[1];

                double quotient = Double.parseDouble(res.split(",")[2]);

                if (Node.checkExistence(node1Name)
                        && Node.checkExistence(node2Name)) {
                    connectTwoGraphs(node1Name, node2Name, quotient);
                } else {
                    addNode(node1Name, node2Name, quotient);
                    addNode(node2Name, node1Name, 1 / quotient);
                }
                res = reader.readLine();
            }
            setNodesIndexesInGraph();
//            System.out.println("preloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private static void connectTwoGraphs(String node1Name, String node2Name,
                                         Double quotient) {
        Graph graph1 = findGraph(node1Name);
        Graph graph2 = findGraph(node2Name);
        graphs.remove(graph2);
        graph1.connect(graph2, node1Name, node2Name, 1 / quotient);
    }

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

    public static Graph findGraph(String neighboringNodeName) {
        return Node.getGraph(PosSearch.searchPosition(neighboringNodeName,
                Node.getAllNames()));
    }

    public static void createGraph(Node startNode) {
        Graph graph = new Graph(startNode);
        graphs.add(graph);
        Node.setGraphsForName(startNode.getName(), graph);
    }

    private static void setNodesIndexesInGraph() {
        for (Graph graphIterator : graphs) {
            graphIterator.setNodesIndexes();
        }
    }
}
