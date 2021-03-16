package app.holdingUnits;

import app.holdingUnits.containers.Graph;
import app.holdingUnits.containers.Node;
import app.search.MyBigDecimal;

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
public class GraphHolder implements Runnable {
    /* implements Runnable to start preloading thread. */

    /** all graphs. */
    public static ArrayList<Graph> graphs = new ArrayList<>();

    /** thread which preloads units and converting rules. */
    public static Thread preloader;

    /** path to the file with rules. */
    public String path;

    /**
     * creates instance of GraphHolder and starts new thread to preload units
     * and converting rules.
     * @param path path to the file with units and converting rules.
     */
    public static void preload(String path) {
        GraphHolder.preloader = new Thread(new GraphHolder(path));
        preloader.start();
        try {
            preloader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * constructs GraphHolder.
     * @param path path to the file with units and converting rules.
     */
    public GraphHolder(String path) {
        this.path = path;
    }

    /**
     * runs thread and preloads units and converting rules.
     */
    @Override
    public void run() {
        GraphHolder.readingStartInfo(path);
    }

    /**
     * preloading graphs.
     * @param filePath path to file with converting rules.
     */
    public static void readingStartInfo(String filePath) {
        try (BufferedReader reader = preloadReader(filePath)) {
            reader.lines().forEach(GraphHolder::parseLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets from line names of the nodes. Adds node if it does not exists and
     * after checking both nodes connect them by edge.
     * @param line the line with names and quotient.
     */
    private static void parseLine(String line) {
        String node1Name = line.split(",")[0];
        String node2Name = line.split(",")[1];
        MyBigDecimal quotient = new MyBigDecimal(line.split(",")[2]);
        if (Node.checkExistence(node1Name) && Node.checkExistence(node2Name)) {
            connectTwoNodes(node1Name, node2Name, quotient);
        } else {
            addNodes(node1Name, node2Name, quotient);
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
                                        MyBigDecimal quotient) {
        Graph graph1 = findGraph(node1Name);
        Graph graph2 = findGraph(node2Name);
        if (graph1 == graph2) {
            graph1.addEdge(node1Name, node2Name, quotient);
            return;
        }
        graphs.remove(graph2);
        graph1.connect(graph2, node1Name, node2Name, quotient);
    }

    /**
     * adds node to on of the graph if the second node exists.
     * @param node1Name first node.
     * @param node2Name second node.
     * @param quotient the quotient of converting.
     */
    public static void addNodes(String node1Name, String node2Name,
                                MyBigDecimal quotient) {
        boolean node1Ex = Node.checkExistence(node1Name);
        boolean node2Ex = Node.checkExistence(node2Name);
        if (node1Ex) {
            findGraph(node1Name).addNode(node1Name, node2Name, quotient);
            return;
        }
        if (node2Ex) {
            findGraph(node2Name).addNode(node1Name, node2Name, quotient);
            return;
        }
        Node node1 = Node.createNode(node1Name);
        createGraph(node1);
        findGraph(node1Name).addNode(node1Name, node2Name, quotient);
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
