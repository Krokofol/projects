package app.holdingUnits;

import app.holdingUnits.containers.Graph;
import app.holdingUnits.containers.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import app.search.Value;

import java.util.ArrayList;

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
    private final static Logger logger = LoggerFactory.getLogger(GraphHolder.class);

    /**
     * Gets from line names of the nodes and converting rule. Creates nodes if it does not exists and connects them by
     * adding in one graph. If they were in two different graphs adds all nodes from the second graph to the first.
     * @param line the line with names and quotient.
     */
    public static void parseLine(String line) {
        line = line.replace(" ", "");
        logger.debug("parsing line : {}", line);
        String node1Name = line.split(",")[0];
        String node2Name = line.split(",")[1];
        Value quotient = new Value(line.split(",")[2]);
        logger.trace("unit 1 name : {}", node1Name);
        logger.trace("unit 2 name : {}", node2Name);
        logger.trace("converting rule : {}", quotient.toString());
        if (Node.checkExistence(node1Name) && Node.checkExistence(node2Name)) {
            logger.debug("connects graph with units : {}, {}", node1Name, node2Name);
            connectTwoNodes(node1Name, node2Name, quotient);
        } else {
            logger.debug("connects units : {}, {}", node1Name, node2Name);
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
            logger.trace("units {}, {} are already in one graph", node1Name, node2Name);
            return;
        }
        graphs.remove(graph2);
        logger.trace("graphs with units {}, {} are connected", node1Name, node2Name);
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
            logger.trace("unit with the name {} already exists", node1Name);
            findGraph(node1Name).addNode(node1Name, node2Name, quotient);
            return;
        }
        if (node2Ex) {
            logger.trace("unit with the name {} already exists", node2Name);
            findGraph(node2Name).addNode(node1Name, node2Name, quotient);
            return;
        }
        logger.trace("both units ({}, {}) are new", node1Name, node2Name);
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
        logger.debug("created new graph for unit {}", startNode.getName());
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