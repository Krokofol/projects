package com.app.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public int port;

    public Server(int serverPort) {
        port = serverPort;
    }

    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Speaker(socket);
                thread.start();
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
