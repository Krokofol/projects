package com.app;

import com.app.web.Server;

public class App {
    public static void main (String[] args) {
        GraphHolder.readingStartInfo(args[0]);
        Server server = new Server(80);
        server.launch();
    }
}
