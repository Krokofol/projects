package com.app.web;

import com.app.Graph;
import com.app.GraphHolder;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Speaker extends Thread{

    public Socket socket;

    public Speaker(Socket socket) {
//        super();
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            String[] units = getUnits(inputStream);

            if (units == null) {
                return;
            }

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
//            boolean gotPare;

            while (numerator.size() > 0) {
                String numeratorIterator = numerator.get(0);
                Graph graph = GraphHolder.findGraph(numeratorIterator);
//                gotPare = false;

                for (String denominatorIterator : denominator) {
                    if(graph.existenceNode(denominatorIterator)) {
                        result *= graph.findWay(numeratorIterator, denominatorIterator);
                        //gotPare = true;
                    }
                }
                numerator.remove(numeratorIterator);
            }

            byte[] answer = new DecimalFormat("#.###############").format(result).getBytes();
            this.sendHeader(outputStream, "200", "OK", answer.length);
            outputStream.write(answer);

        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private void sendHeader(OutputStream output, String statusCode, String statusText, long length) {
        PrintStream printStream = new PrintStream(output);
        printStream.printf("HTTP/1.1 %s %s%n", statusCode, statusText);
        printStream.printf("Content-Type: text/plain%n");
        printStream.printf("Content-Length: %s%n%n", length);
    }


    private String[] getUnits(InputStream input) {
        Scanner scanner = new Scanner(input).useDelimiter("\r\n");

        if (!scanner.next().split(" ")[1].equals("/convert")) {
            System.out.println("wrong : URL configuration");
            return null;
        }


        String[] fromTo = new String[2];

        boolean gotFrom = false, gotTo = false;

        while(scanner.hasNext() && (!(gotFrom && gotTo))) {
            String nextString = URLDecoder.decode(scanner.next(), UTF_8);
            if (nextString.contains("from")) {
                if (gotFrom) {
                    System.out.println("wrong : second \"from\"");
                    return null;
                }
                gotFrom = true;
                fromTo[0] = Speaker.cleanUp(nextString);
            }
            if (nextString.contains("to")) {
                if (gotTo) {
                    System.out.println("wrong : second \"to\"");
                    return null;
                }
                gotTo = true;
                fromTo[1] = Speaker.cleanUp(nextString);
            }
        }

        return fromTo;
    }

    private static String cleanUp(String string) {
        return string.replace(" ", "").replace(",", "").replace(":","")
                .replace("from", "").replace("to", "").replace("\"","");
    }
}
