package com.app.web;

import com.app.Graph;
import com.app.GraphHolder;
import com.app.Node;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
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
//        super.run();
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
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
                    if(denominatorIterator.equals(numeratorIterator)) {
                        denominator.remove(denominatorIterator);
                        break;
                    }
                    if(graph.existenceNode(denominatorIterator)) {
                        result *= node.findEdge(denominatorIterator).getQuotient();
                        denominator.remove(denominatorIterator);
                        break;
                    }
                }
                numerator.remove(numeratorIterator);
            }

            byte[] answer = String.format("%f",result).getBytes();
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
        String url = "";

        Scanner scanner = new Scanner(input).useDelimiter("\r\n");
        if (scanner.hasNext())url = scanner.next();
        else this.stop();

        url = URLDecoder.decode(url.split(" ")[1], UTF_8);
        url = url.replaceAll(" ", "");

        String[] args = url.split("\\?");
        if (args.length != 2)
            System.out.println("Ошибка ввада");
        if (!args[0].equals("/convert"))
            System.out.println("Не тот оператор");

        String[] arg1 = args[1].split("&")[0].split("=");
        String[] arg2 = args[1].split("&")[1].split("=");

        if (!(arg1[0].equals("to") && arg2[0].equals("from"))&&(!(arg1[0].equals("from") && arg2[0].equals("to"))))
            System.out.println("неверные параметры");

        if (arg1[0].equals("from"))
            return new String[]{arg1[1], arg2[1]};
        return new String[]{arg2[1], arg1[1]};
//        return url.split(" ");
    }
}
