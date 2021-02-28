package com.app.web;

import com.app.Graph;
import com.app.GraphHolder;
import com.app.Node;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Speaker extends Thread{

    public Socket socket;

    public Speaker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {
            String[] units = getUnits(inputStream);
            String[] fromUnit = units[0].split("/");
            String[] toUnit = units[1].split("/");

            StringBuilder numeratorBuilder = new StringBuilder();
            if (fromUnit.length > 0) numeratorBuilder.append(fromUnit[0]);
            if (fromUnit.length > 0 && toUnit.length > 1) numeratorBuilder.append("*");
            if (toUnit.length > 1) numeratorBuilder.append(toUnit[1]);

            StringBuilder denominatorBuilder = new StringBuilder();
            if (toUnit.length > 0) denominatorBuilder.append(toUnit[0]);
            if (toUnit.length > 0 && fromUnit.length > 1) denominatorBuilder.append("*");
            if (fromUnit.length > 1) denominatorBuilder.append(fromUnit[1]);


            ArrayList<String> numerator = new ArrayList<>();
            ArrayList<String> denominator = new ArrayList<>();
            Collections.addAll(numerator, numeratorBuilder.toString().replace(" ", "").split("\\*"));
            Collections.addAll(denominator, denominatorBuilder.toString().replace(" ", "").split("\\*"));

            Double result = 1.0;

            while (numerator.size() > 0) {
                String numeratorIterator = numerator.get(0);
                Graph graph = GraphHolder.findGraph(numeratorIterator);
                Node node = graph.findNode(numeratorIterator);
                for (String denominatorIterator : denominator) {
                    if(graph.existenceNode(denominatorIterator)) {
                        result *= node.findEdge(denominatorIterator).getQuotient();
                        denominator.remove(denominatorIterator);
                        break;
                    }
                }
                numerator.remove(numeratorIterator);
            }

            System.out.println(result);

        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private String[] getUnits(InputStream input) {
        Scanner scanner = new Scanner(input).useDelimiter("\r\n");
        String url = scanner.next();
        url = url.split(" ")[1];
        return url.split("/")[1].split(",");
    }
}
