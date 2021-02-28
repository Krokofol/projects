package com.app;

import java.io.*;
import java.util.ArrayList;

public class GraphHolder {
    static public ArrayList<Graph> graphs = new ArrayList<>();

    public static void createGraph(Node startNode) {
        graphs.add(new Graph(startNode));
    }

    public static Graph findGraph(String neighboringNodeName) {
        for (Graph graphIterator : graphs)
            if(graphIterator.existenceNode(neighboringNodeName))
                return graphIterator;
        return graphs.get(0);
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
            System.out.println("done");
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

    private static BufferedReader preloadReader(String filePath) throws FileNotFoundException {
        File input = new File(filePath);
        FileReader fr = new FileReader(input);
        return new BufferedReader(fr);
    }
}
