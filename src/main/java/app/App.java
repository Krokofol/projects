package app;

import app.holdingUnits.GraphHolder;
import app.web.Server;
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
        GraphHolder.readingStartInfo(args[0]);
//        пока не работает, потому что я не знаю как вызвать предыдущий метод 1
//        раз до старта спринга (и в него обязательно надо передать args[0]).
//        а ещё почему-то деикстра на спроинге работает дольше (наверное это
//        из-за того что у спринга куча своих потоков и они занимают время
//        процессора. Поэтому я не знаю что лучше, использовать спринг или
//        сделать на коленочках на serverSocket.
//        SpringApplication.run(App.class, args);
        Server server = new Server(80);
        server.launch();
    }
}