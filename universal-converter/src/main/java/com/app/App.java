package com.app;

import com.app.holdingUnits.GraphHolder;
import com.app.web.Server;

/**
 * The main class of the project.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class App {

    /**
     * preloads graphs for converting units and starts server.
     * @param args first arg is path to file with converting rules.
     */
    public static void main (String[] args) {
        GraphHolder.readingStartInfo(args[0]);
        Server server = new Server(80);
        server.launch();
    }
}