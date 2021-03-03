package com.app;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GraphHolder {
    static public ArrayList<Graph> graphs = new ArrayList<>();

    public static void createGraph(Node startNode) {
        Graph graph = new Graph(startNode);
        graphs.add(graph);
        Node.setGraphsForName(startNode.getName(), graph);
    }

    public static Graph findGraph(String neighboringNodeName) {
        return Node.getGraph(Node.findPosForName(neighboringNodeName));
    }

    public static void addNode(String nodeName, String neighboringNodeName, Double quotient) {
        Node newNode = Node.createNode(nodeName);
        if (newNode == null)
            return;
        if (!Node.checkExistence(neighboringNodeName)) {
            createGraph(newNode);
            return;
        }
        findGraph(neighboringNodeName).addNode(newNode, neighboringNodeName, quotient);
    }

    public static void readingStartInfo(String filePath) {
        try {
            BufferedReader reader = preloadReader(filePath);
            String res = reader.readLine();
            while (res != null) {
                String node1Name = res.split(",")[0];
                String node2Name = res.split(",")[1];
                double quotient = Double.parseDouble(res.split(",")[2]);

                if (Node.checkExistence(node1Name) && Node.checkExistence(node2Name)) {
                    connectTwoGraphs(node1Name, node2Name, quotient);
                } else {
                    GraphHolder.addNode(node1Name, node2Name, quotient);
                    GraphHolder.addNode(node2Name, node1Name, 1 / quotient);
                }
                res = reader.readLine();
            }
            System.out.println("preloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connectTwoGraphs(String node1Name, String node2Name, Double quotient) {
        Graph graph1 = findGraph(node1Name);
        Graph graph2 = findGraph(node2Name);
        graphs.remove(graph2);
        graph1.connect(graph2, node1Name, node2Name, 1 / quotient);
    }

    private static BufferedReader preloadReader(String filePath) throws FileNotFoundException, UnsupportedEncodingException {
        File input = new File(filePath);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(input), StandardCharsets.UTF_8);
        return   new BufferedReader(isr);
    }
}
