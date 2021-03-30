package app.holdingUnits;

import app.holdingUnits.containers.Graph;
import app.holdingUnits.containers.Node;
import app.search.Value;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which builds and holds all graphs.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class GraphHolder {

    /** All graphs. */
    private final static ArrayList<Graph> graphs = new ArrayList<>();

    /** Logger for this class. */
    private final static Logger logger = Logger.getLogger(GraphHolder.class.getName());

    /**
     * Gets from line names of the nodes and converting rule. Creates nodes if it does not exists and connects them by
     * adding in one graph. If they were in two different graphs adds all nodes from the second graph to the first.
     * @param line the line with names and quotient.
     */
    public static void parseLine(String line) {
        logger.log(Level.FINER, "parsing line : " + line);
        String node1Name = line.split(",")[0];
        String node2Name = line.split(",")[1];
        Value quotient = new Value(line.split(",")[2]);
        logger.log(Level.FINEST, "unit 1 name : " + node1Name);
        logger.log(Level.FINEST, "unit 2 name : " + node2Name);
        logger.log(Level.FINEST, "converting rule : " + quotient.toString());
        if (Node.checkExistence(node1Name) && Node.checkExistence(node2Name)) {
            logger.log(Level.FINER, "connects graph with unit \"" + node1Name + "\" and \"" + node2Name + "\"");
            connectTwoNodes(node1Name, node2Name, quotient);
        } else {
            logger.log(Level.FINER, "connects units \"" + node1Name + "\" and \"" + node2Name + "\"");
            addNodes(node1Name, node2Name, quotient);
        }
    }

    /**
     * Adds nodes from the second graph, to the first if they were not in one graph.
     * @param node1Name first node name.
     * @param node2Name second node name.
     * @param quotient the quotient of converting.
     */
    private static void connectTwoNodes(String node1Name, String node2Name, Value quotient) {
        Graph graph1 = findGraph(node1Name);
        Graph graph2 = findGraph(node2Name);
        if (graph1 == graph2) {
            logger.log(Level.FINEST, "units \"" + node1Name + "\", \"" + node2Name + "\" already in one graph");
            return;
        }
        graphs.remove(graph2);
        logger.log(Level.FINEST, "graphs with units \"" + node1Name + "\", \"" + node2Name + "\" are connected");
        graph1.connect(graph2, node1Name, node2Name, quotient);
    }

    /**
     * If one of node exists adds node which doesn't exists to it's graph, else creates graph from the first node and
     * adds here the second node.
     * @param node1Name first node.
     * @param node2Name second node.
     * @param quotient the quotient of converting.
     */
    public static void addNodes(String node1Name, String node2Name, Value quotient) {
        boolean node1Ex = Node.checkExistence(node1Name);
        boolean node2Ex = Node.checkExistence(node2Name);
        if (node1Ex) {
            logger.log(Level.FINEST, "unit with the name " + node1Name + " already exists");
            findGraph(node1Name).addNode(node1Name, node2Name, quotient);
            return;
        }
        if (node2Ex) {
            logger.log(Level.FINEST, "unit with the name " + node2Name + " already exists");
            findGraph(node2Name).addNode(node1Name, node2Name, quotient);
            return;
        }
        logger.log(Level.FINEST, "both units are new");
        Node node1 = Node.createNode(node1Name);
        createGraph(node1);
        findGraph(node1Name).addNode(node1Name, node2Name, quotient);
    }

    /**
     * Searches graph by node name.
     * @param nodeName node name.
     * @return if such node exists returns graph, else return null.
     */
    public static Graph findGraph(String nodeName) {
        return Node.getGraph(nodeName);
    }

    /**
     * Creates new graph for node which could not be connected to other graphs.
     * @param startNode first node of the graph.
     */
    public static void createGraph(Node startNode) {
        Graph graph = new Graph(startNode);
        graphs.add(graph);
        Node.setGraphsForName(startNode.getName(), graph);
        logger.log(Level.FINER, "created new graph for unit " + startNode);
    }

    /**
     * Deletes all existing graphs and nodes.
     */
    public static void cleanUp() {
        graphs.clear();
        Node.cleanUp();
    }

    /**
     * Gets amount of the graphs.
     * @return amount of the graphs.
     */
    public static Integer getGraphHolderSize() {
        return graphs.size();
    }
}