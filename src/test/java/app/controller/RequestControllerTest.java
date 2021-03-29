package app.controller;

import app.holdingUnits.Preloader;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test RequestController class.
 *
 * @version 1.0.0 29 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
class RequestControllerTest {

    public RequestControllerTest() {
        Preloader.preload("target/test-classes/smallTestData.csv");
    }

    @Test
    void convert() {
        RequestController controller = new RequestController();
        HashMap<String, String> startData = new HashMap<>();
        String result;

        System.out.println("here");
        startData.put("from", "км * м");
        startData.put("to", "м * м");
        result = controller.convert(startData).getBody();
        assertEquals("1000", result);

        startData.put("from", "час");
        startData.put("to", "с");
        result = controller.convert(startData).getBody();
        assertEquals("3600", result);

        startData.put("from", "м / с");
        startData.put("to", "км / час");
        result = controller.convert(startData).getBody();
        assertEquals("3.6", result);
    }
}