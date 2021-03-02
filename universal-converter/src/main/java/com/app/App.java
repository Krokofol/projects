package com.app;

import com.app.web.Server;

public class App {
    public static void main (String[] args) {
        GraphHolder.readingStartInfo(args[0]);
        System.out.println(GraphHolder.graphs.size());
        Server server = new Server(8081);
        server.launch();
    }
}
