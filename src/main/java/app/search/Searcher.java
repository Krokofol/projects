package app.search;

import app.holdingUnits.containers.Graph;

/**
 * class only to parallelize converting.
 */
public class Searcher extends Thread {
    /* extends thread to start Graph.findConverting at new thread. */

    /** result of converting. */
    public Value result;

    /** the graph, where converting will be. */
    public Graph graph;

    /** name of the node FROM which we are converting. */
    public String startNodeName;

    /** name of the node TO which we are converting. */
    public String endNodeName;

    /**
     * returns result.
     * @return result.
     */
    public Value getResult() {
        return result;
    }

    /**
     * constructor.
     * @param graph the graph, where converting will be.
     * @param startNodeName name of the node FROM which we are converting.
     * @param endNodeName name of the node TO which we are converting.
     */
    public Searcher(Graph graph, String startNodeName,
                    String endNodeName) {
        this.endNodeName = endNodeName;
        this.startNodeName = startNodeName;
        this.graph = graph;
    }

    /**
     * search the converting rules.
     */
    @Override
    public void run() {
        result = graph.findConverting(startNodeName, endNodeName);
    }
}