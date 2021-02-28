package com.app;

import java.util.*;

public class Node {
    public static ArrayList<String> names = new ArrayList<>();

    public String name;
    public ArrayList<Edge> edges;

    public static Node createNode(String name) {
        if (checkExistence(name))
            return null;
        return new Node(name);
    }

    private Node(String nodeName) {
        //сортировка вставкой
        names.add(nodeName);
        name = nodeName;
        edges = new ArrayList<>();
    }

    public static boolean checkExistence (String name) {
        //бинарный поиск
        for (String namesIterator : names)
            if (name.equals(namesIterator))
                return true;
        return false;
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

}
