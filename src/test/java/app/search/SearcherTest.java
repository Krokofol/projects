package app.search;

import app.holdingUnits.containers.Graph;
import app.holdingUnits.containers.Node;
import app.holdingUnits.GraphHolder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearcherTest {

    @Test
    public void preload() {
        String[] args = {
                "км,м,1000",
                "м,мм,1000",
                "мм,мкм,1000",
                "мкм,нм,1000",
                "нм,пм,1000",
                "час,мин,60",
                "кг,г,1000"
        };
        for (String string : args) {
            GraphHolder.parseLine(string);
        }
    }

    @Test
    public void converting1() {
        preload();
        String fromNodeName = "км";
        String toNodeName = "пм";
        String expectedResult = "1000000000000000";
        Graph graph = Node.getGraph(fromNodeName);
        Searcher searcher = new Searcher(graph, fromNodeName, toNodeName);
        searcher.start();
        try {
            searcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String convertingResult = searcher.getResult().toString();
        assertEquals(expectedResult, convertingResult);
    }

    @Test
    public void converting2() {
        preload();
        String fromNodeName = "пм";
        String toNodeName = "км";
        String expectedResult = "0.000000000000001";
        Graph graph = Node.getGraph(fromNodeName);
        Searcher searcher = new Searcher(graph, fromNodeName, toNodeName);
        searcher.start();
        try {
            searcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String convertingResult = searcher.getResult().toString();
        assertEquals(expectedResult, convertingResult);
    }
}