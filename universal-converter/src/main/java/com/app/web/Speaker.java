package com.app.web;

import com.app.holdingUnits.Graph;
import com.app.holdingUnits.GraphHolder;
import com.app.holdingUnits.Node;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Class which receive units from client, start converting units and then
 * deliver them to client.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Speaker extends Thread{

    /** socket with which thread will work. */
    public Socket socket;

    /**
     * constructs speaker and saves socket with which thread will work.
     * @param socket the socket.
     */
    public Speaker(Socket socket) {
        this.socket = socket;
    }

    /**
     * main method of the thread which will receive the request, calculate the
     * answer and send back to the client.
     */
    public void run() {
        try {
            InputStream inputStream = this.socket.getInputStream();
            OutputStream outputStream = this.socket.getOutputStream();

            String[] units = getUnits(inputStream);

            if (units == null) {
                this.sendHeader(outputStream, "200", "OK",
                        "1".length());
                outputStream.write("1".getBytes());
                return;
            }

            String[] fromUnit = units[0].split("/");
            String[] toUnit = units[1].split("/");

            StringBuilder fromUnitsBuilder = buildArgs(fromUnit, toUnit);
            StringBuilder toUnitsBuilder = buildArgs(toUnit, fromUnit);

            ArrayList<String> fromUnits = new ArrayList<>();
            ArrayList<String> toUnits = new ArrayList<>();
            Collections.addAll(fromUnits, toUnitsBuilder.toString()
                    .replace(" ", "").split("\\*"));
            Collections.addAll(toUnits, fromUnitsBuilder.toString()
                    .replace(" ", "").split("\\*"));


            if (checkExistence(outputStream, fromUnits)) return;
            if (checkExistence(outputStream, toUnits)) return;

            Double result = calculateResult(toUnits, fromUnits);

            if (result == null) {
                this.sendHeader(outputStream, "404", "Not Found",
                        "Not Found".length());
                outputStream.write("Not Found".getBytes());
                return;
            }
            byte[] answer = new DecimalFormat("#.###############")
                    .format(result).getBytes();
            this.sendHeader(outputStream, "200", "OK", answer.length);
            outputStream.write(answer);

        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    /**
     * reads all url and it's body and get from it units from which server is
     * converting and to which.
     * @param input receiving thread.
     * @return string array length two. first element are units from which
     * convert and the second to which.
     */
    private String[] getUnits(InputStream input) {
        Scanner scanner = new Scanner(input, UTF_8).useDelimiter("\r\n");

        if (!scanner.hasNext()) {
            return null;
        }
        if (!scanner.next().split(" ")[1].equals("/convert")) {
            return null;
        }

        String[] fromTo = new String[2];
        boolean gotFrom = false, gotTo = false;

        while(scanner.hasNext() && (!(gotFrom && gotTo))) {
            String nextString = URLDecoder.decode(scanner.next(), UTF_8);
            if (nextString.contains("from")) {
                if (gotFrom) {
                    return null;
                }
                gotFrom = true;
                fromTo[0] = Speaker.cleanUp(nextString);
            }
            if (nextString.contains("to")) {
                if (gotTo) {
                    return null;
                }
                gotTo = true;
                fromTo[1] = Speaker.cleanUp(nextString);
            }
        }

        if (!gotFrom || !gotTo) {
            return null;
        }
        return fromTo;
    }

    /**
     * removes from string extra elements.
     * @param string string to clean up.
     * @return cleaned up string.
     */
    private static String cleanUp(String string) {
        return string.replace(" ", "").replace(",", "").replace(":","")
                .replace("from", "").replace("to", "").replace("\"","");
    }

    /**
     * multiplies numerator (first element) of the first units and denominator
     * (second element) of the second units.
     * @param units1 first units.
     * @param units2 second units.
     * @return result of multiplication.
     */
    private StringBuilder buildArgs(String[] units1, String[] units2) {
        StringBuilder result = new StringBuilder();
        if (units2.length > 0) result.append(units2[0]);
        if (units2.length > 0 && units1.length > 1) {
            if (units2[0].length() > 0) {
                result.append("*");
            }
        }
        if (units1.length > 1) result.append(units1[1]);
        return result;
    }

    /**
     * checks existence of all units, if one of them does not exists sends
     * status code of error.
     * @param outputStream stream to deliver.
     * @param units all units.
     * @return if everything is ok - false, else - true.
     */
    private boolean checkExistence(OutputStream outputStream,
                                   ArrayList<String> units) {
        for (String nameIterator : units) {
            if (nameIterator.equals(""))
                continue;
            if (!Node.checkExistence(nameIterator)) {
                this.sendHeader(outputStream, "400", "Bad Request",
                        "Bad Request".length());
                try {
                    outputStream.write("Bad Request".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * calculates result.
     * @param toUnits units to which converts.
     * @param fromUnits units from which converts.
     * @return conversion result.
     */
    private Double calculateResult (ArrayList<String> toUnits,
                                    ArrayList<String> fromUnits) {
        Double result = 1.0;
        boolean gotPare;

        toUnits.remove("");
        fromUnits.remove("");

        if (fromUnits.size() != toUnits.size()) {
            return null;
        }

        while (fromUnits.size() > 0) {
            String numeratorIterator = fromUnits.get(0);
            Graph graph = GraphHolder.findGraph(numeratorIterator);
            gotPare = false;

            for (String denominatorIterator : toUnits) {
                if(graph.existenceNode(denominatorIterator)) {
                    result *= graph.findWay(numeratorIterator,
                            denominatorIterator);
                    toUnits.remove(denominatorIterator);
                    gotPare = true;
                    break;
                }
            }
            if (!gotPare) {
                return null;
            }
            fromUnits.remove(numeratorIterator);
        }
        return result;
    }

    /**
     * sends header.
     * @param output stream to deliver.
     * @param statusCode status code.
     * @param statusText status text.
     * @param length length of the data which will be send next.
     */
    private void sendHeader(OutputStream output, String statusCode,
                            String statusText, long length) {
        PrintStream printStream = new PrintStream(output);
        printStream.printf("HTTP/1.1 %s %s%n", statusCode, statusText);
        printStream.printf("Content-Type: text/plain%n");
        printStream.printf("Content-Length: %s%n%n", length);
    }
}
