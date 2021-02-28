package com.app;

import com.app.web.Server;

public class App {
    public static void main (String[] args) {
        GraphHolder.readingStartInfo(args[0]);
        System.out.println(GraphHolder.graphs.size());
        new Server(80).launch();
    }
}
