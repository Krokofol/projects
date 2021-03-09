package app;

public class App {
    public static void main(String[] args) {
        System.out.println("start");
        GraphHolder.readingStartInfo(args[0]);
        System.out.println(GraphHolder.graphs.size());
        System.out.println("end");
    }

}
