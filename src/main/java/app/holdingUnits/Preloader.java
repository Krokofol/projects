package app.holdingUnits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Special class to preload data.
 *
 * @version 1.0.0 19 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Preloader extends Thread{
    /* extends Thread to preload at new thread */

    /** Logger for this class. */
    private final static Logger logger = LoggerFactory.getLogger(Preloader.class);

    /** Exit status if no file with converting rules. */
    private final static int noFileExitStatus = 13;

    /** Preloading thread. */
    private static Thread preloader;

    /** Path to the file with converting rules. */
    private final String path;

    /**
     * Creates instance of Preloader and starts it to preload units and converting rules.
     * @param path path to the file with units and converting rules.
     */
    public static void preload(String path) {
        logger.info("start preloading");
        preloader = new Preloader(path);
        preloader.start();
        try {
            preloader.join();
        } catch (InterruptedException e) {
            logger.warn("preloading join error");
            e.printStackTrace();
        }
        logger.info("preloading is done");
    }

    /**
     * Constructor of the preloader.
     * @param path path to preloading file.
     */
    public Preloader(String path) {
        this.path = path;
    }

    /**
     * Main body of the thread. Preloads data.
     */
    @Override
    public void run() {
        readingStartInfo(path);
    }

    /**
     * Reads converting rules and pars it.
     * @param filePath path to file with converting rules.
     */
    public static void readingStartInfo(String filePath) {
        try (BufferedReader reader = preloadReader(filePath)) {
            logger.debug("start reading converting rules");
            reader.lines().forEach(GraphHolder::parseLine);
            logger.debug("reading converting rules is done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Preloads buffer reader of the file.
     * @param filePath path to the file with converting rules.
     * @return buffer reader.
     */
    private static BufferedReader preloadReader(String filePath) {
        logger.debug("searching the file");
        File input = new File(filePath);
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(input),
                    StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (isr == null) {
            logger.error("no such file : {}", filePath);
            System.exit(noFileExitStatus);
        }
        logger.debug("file is founded");
        return new BufferedReader(isr);
    }

    /**
     * Gets preloading thread.
     * @return preloading thread.
     */
    public static Thread getPreloadingThread() {
        return preloader;
    }
}
