package com.app;

import java.util.*;

public class Node {
    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<Graph> graphsForNames = new ArrayList<>();

    public String name;
    public ArrayList<Edge> edges;

    public static Node createNode(String name) {
        if (checkExistence(name))
            return null;
        return new Node(name);
    }

    private Node(String nodeName) {
        int pos = findPos(nodeName);
        names.add(pos, nodeName);
        graphsForNames.add(pos, null);

        name = nodeName;
        edges = new ArrayList<>();
    }

    public static boolean checkExistence (String name) {
        if (names.size() == 0) return false;
        int pos = findPos(name);
        if (names.size() == pos) return false;
        return names.get(pos).equals(name);
    }


    public Edge findEdge(String nodeName) {
        //бинарный поиск
        for (Edge edgeIterator : edges) {
            if (edgeIterator.getNode2().getName().equals(nodeName))
                return edgeIterator;
        }
        return edges.get(0);
    }

    public void createEdge(Node neighboringNode, Double quotient) {
        //сортировка вставкой
        edges.add(new Edge(this, neighboringNode, quotient));
    }

    public String getName() {
        return name;
    }

    public static int findPos(String name) {
        int leftPos = 0;
        int rightPos = names.size();
        int mid = (leftPos + rightPos) / 2;

        while (leftPos < rightPos) {
            int compare = names.get(mid).compareTo(name);
            if (compare == 0) {
                return mid;
            }
            if (compare > 0) {
                rightPos = mid;
            } else {
                leftPos = mid + 1;
            }
            mid = (leftPos + rightPos) / 2;
        }
        return Math.max(leftPos, rightPos);
    }

    public static void setGraphsForName(String name, Graph graph) {
        graphsForNames.set(findPos(name), graph);
    }

    public static Graph getGraph(int index) {
        return graphsForNames.get(index);
    }

}
