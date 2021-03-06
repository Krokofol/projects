package com.app.web;

import com.app.Graph;
import com.app.GraphHolder;
import com.app.Node;

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
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            String[] units = getUnits(inputStream);

            if (units == null) {
                //если у нас какие-то параметры лишние
                return;
            }

            String[] fromUnit = units[0].split("/");
            String[] toUnit = units[1].split("/");

            buildArgs(toUnit, fromUnit);

            StringBuilder denominatorBuilder = buildArgs(fromUnit, toUnit);
            StringBuilder numeratorBuilder = buildArgs(toUnit, fromUnit);

            ArrayList<String> numerator = new ArrayList<>();
            ArrayList<String> denominator = new ArrayList<>();
            Collections.addAll(numerator, numeratorBuilder.toString().replace(" ", "").split("\\*"));
            Collections.addAll(denominator, denominatorBuilder.toString().replace(" ", "").split("\\*"));


            if (checkExistence(outputStream, numerator)) return;
            if (checkExistence(outputStream, denominator)) return;

            Double result = calculateResult(denominator, numerator);
            if (result == null) {
                this.sendHeader(outputStream, "404", "Not Found", 0);
                return;
            }

            byte[] answer = new DecimalFormat("#.###############").format(result).getBytes();
            this.sendHeader(outputStream, "200", "OK", answer.length);
            outputStream.write(answer);

        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private Double calculateResult (ArrayList<String> denominator, ArrayList<String> numerator) {
        Double result = 1.0;
        boolean gotPare;

        if (numerator.size() != denominator.size()) {
            return null;
        }

        while (numerator.size() > 0) {
            String numeratorIterator = numerator.get(0);
            Graph graph = GraphHolder.findGraph(numeratorIterator);
            gotPare = false;

            for (String denominatorIterator : denominator) {
                if(graph.existenceNode(denominatorIterator)) {
                    result *= graph.findWay(numeratorIterator, denominatorIterator);
                    denominator.remove(denominatorIterator);
                    gotPare = true;
                    break;
                }
            }
            if (!gotPare) {
                return null;
            }
            numerator.remove(numeratorIterator);
        }
        return result;
    }

    private boolean checkExistence(OutputStream outputStream, ArrayList<String> numerator) {
        for (String nameIterator : numerator)
            if (!Node.checkExistence(nameIterator)) {
                this.sendHeader(outputStream, "400", "Bad Request", 0);
                return true;
            }
        return false;
    }

    private StringBuilder buildArgs(String[] fromUnit, String[] toUnit) {
        StringBuilder denominatorBuilder = new StringBuilder();
        if (toUnit.length > 0) denominatorBuilder.append(toUnit[0]);
        if (toUnit.length > 0 && fromUnit.length > 1)
            if (toUnit[0].length() > 0)
                denominatorBuilder.append("*");
        if (fromUnit.length > 1) denominatorBuilder.append(fromUnit[1]);
        return denominatorBuilder;
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
