package app;

import java.util.ArrayList;

public class Node {
    public static ArrayList<Node> allNodes = new ArrayList<>();
    public static ArrayList<Graph> graphsForNames = new ArrayList<>();

    public int posNumInGraph;
    public String name;
    public ArrayList<Edge> edges;

    public static Node createNode(String name) {
        if (checkExistence(name))
            return null;
        Node node = new Node (name);
        int pos = PosSearcher.searchNamePosInNodeArray(name, allNodes);
        allNodes.add(pos, node);
        graphsForNames.add(pos, null);
        return node;
    }

    private Node(String nodeName) {

        name = nodeName;
        edges = new ArrayList<>();
    }

    public static boolean checkExistence (String name) {
        if (allNodes.size() == 0) return false;
        int pos = PosSearcher.searchNamePosInNodeArray(name, allNodes);
        if (allNodes.size() == pos) return false;
        return allNodes.get(pos).getName().equals(name);
    }

    public void createEdge(Node neighboringNode, Double quotient) {
        this.edges.add(findPosForEdge(neighboringNode.getName()), new Edge(this, neighboringNode, quotient));
    }

    public String getName() {
        return name;
    }

    public int findPosForEdge(String node2Name) {
        int leftPos = 0;
        int rightPos = this.edges.size();
        int mid = (leftPos + rightPos) / 2;

        while (leftPos < rightPos) {
            int compare = edges.get(mid).getNode2().getName().compareTo(node2Name);
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
        graphsForNames.set(PosSearcher.searchNamePosInNodeArray(name, allNodes), graph);
    }

    public static Graph getGraph(int index) {
        return graphsForNames.get(index);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setPosNumInGraph(int posNumInGraph) {
        this.posNumInGraph = posNumInGraph;
    }

    public int getPosNumInGraph() {
        return posNumInGraph;
    }

    public static ArrayList<Node> getAllNames() {
        return allNodes;
    }
}
