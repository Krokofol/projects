package app.holdingUnits;

import app.holdingUnits.containers.Graph;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Special class to preload data.
 *
 * @version 1.0.0 19 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Preloader extends Thread {
    /* extends Thread to preload data and other threads were able to join. */

    /** logger for this class. */
    public static Logger logger = Logger.getLogger(Preloader.class.getName());

    /** preloader. */
    public static Preloader preloader;

    /** path to the file with rules. */
    public String path;

    /**
     * creates instance of GraphHolder and starts new thread to preload units
     * and converting rules.
     * @param path path to the file with units and converting rules.
     */
    public static void preload(String path) {
        logger.log(Level.INFO, "start preloading");
        preloader = new Preloader(path);
        preloader.start();
        try {
            preloader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.log(Level.FINE, "preloading is done");
    }

    /**
     * constructs GraphHolder.
     * @param path path to the file with units and converting rules.
     */
    public Preloader(String path) {
        this.path = path;
    }

    /**
     * runs thread and preloads units and converting rules.
     */
    @Override
    public void run() {
        readingStartInfo(path);
    }

    /**
     * preloading graphs.
     * @param filePath path to file with converting rules.
     */
    public static void readingStartInfo(String filePath) {
        try (BufferedReader reader = preloadReader(filePath)) {
            logger.log(Level.INFO, "start reading converting rules");
            reader.lines().forEach(GraphHolder::parseLine);
            logger.log(Level.FINE, "reading converting rules is done");
            logger.log(Level.INFO, "start adding new converting rules");
//            GraphHolder.getGraphs().forEach(Graph::completionGraph);
            logger.log(Level.FINE, "adding new converting rules is done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * preloads buffer reader of file.
     * @param filePath path to the file with converting rules.
     * @return buffer reader.
     */
    private static BufferedReader preloadReader(String filePath) {
        logger.log(Level.INFO, "searching the file");
        File input = new File(filePath);
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(input),
                    StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (isr == null) {
            logger.log(Level.SEVERE, "no such file");
            System.exit(13);
        }
        logger.log(Level.FINE, "file is found");
        return new BufferedReader(isr);
    }





}
