package com.app.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int port;

    public static void preload(int serverPort) {
        port = serverPort;
        start();
    }

    public static void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Speaker(socket).start();
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
