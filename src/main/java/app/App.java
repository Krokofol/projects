package app;

import app.holdingUnits.GraphHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class of the project.
 *
 * @version 1.0.0 10 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
@SpringBootApplication
public class App {

    /**
     * preloads graphs for converting units and starts server.
     * @param args first arg is path to file with converting rules.
     */
    public static void main (String[] args) {
        SpringApplication.run(App.class, args);
        GraphHolder.readingStartInfo(args[0]);
    }
}