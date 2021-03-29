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

    /**
     * preloads data for test.
     */
    public RequestControllerTest() {
        Preloader.preload("target/test-classes/smallTestData.csv");
    }

    /**
     * searches some converting rules from ONLY ONE UNIT to OTHER ONE and compare it with the answer.
     */
    @Test
    void convertFromOnlyOneUnitToOther() {
        RequestController controller = new RequestController();
        HashMap<String, String> startData = new HashMap<>();
        String result;

        startData.put("from", "км");
        startData.put("to", "м");
        result = controller.convert(startData).getBody();
        assertEquals("1000", result);

        startData.put("from", "час");
        startData.put("to", "с");
        result = controller.convert(startData).getBody();
        assertEquals("3600", result);

        startData.put("from", "месяц");
        startData.put("to", "день");
        result = controller.convert(startData).getBody();
        assertEquals("31", result);
    }

    /**
     * searches some converting rules from ONE MULTIPLICATION to OTHER ONE and compare it with the answer.
     */
    @Test
    void convertFromMultiplicationToOtherOne() {
        RequestController controller = new RequestController();
        HashMap<String, String> startData = new HashMap<>();
        String result;

        startData.put("from", "км * м");
        startData.put("to", "м * км");
        result = controller.convert(startData).getBody();
        assertEquals("1", result);

        startData.put("from", "км * м");
        startData.put("to", "м * м");
        result = controller.convert(startData).getBody();
        assertEquals("1000", result);

        startData.put("from", "км * час");
        startData.put("to", "м * с");
        result = controller.convert(startData).getBody();
        assertEquals("3600000", result);

        startData.put("from", "час * год");
        startData.put("to", "мин * месяц");
        result = controller.convert(startData).getBody();
        assertEquals("720", result);

        startData.put("from", "м * кг * мин");
        startData.put("to", "мм * т * с");
        result = controller.convert(startData).getBody();
        assertEquals("60", result);

        startData.put("from", "м * кг * мин * час * с");
        startData.put("to", "мм * т * с * мин * час");
        result = controller.convert(startData).getBody();
        assertEquals("1", result);
    }

    /**
     * searches some converting rules from ONE DIVIDING to OTHER ONE and compare it with the answer.
     */
    @Test
    void convertFromOneDividingToOther() {
        RequestController controller = new RequestController();
        HashMap<String, String> startData = new HashMap<>();
        String result;

        startData.put("from", "1 / с");
        startData.put("to", "1 / час");
        result = controller.convert(startData).getBody();
        assertEquals("3600", result);

        startData.put("from", "1 / км");
        startData.put("to", "1 / м");
        result = controller.convert(startData).getBody();
        assertEquals("0.001", result);

        startData.put("from", "1 / г");
        startData.put("to", "1 / кг");
        result = controller.convert(startData).getBody();
        assertEquals("1000", result);
    }

    /**
     * searches some converting rules from ONE DIVIDING WITH MULTIPLICATION to OTHER ONE and compare it with the
     * answer.
     */
    @Test
    void convertFromOneDividingWithMultiplicationToOther() {
        RequestController controller = new RequestController();
        HashMap<String, String> startData = new HashMap<>();
        String result;

        startData.put("from", "м * м / мм * км");
        startData.put("to", "");
        result = controller.convert(startData).getBody();
        assertEquals("1", result);

        startData.put("from", "час / час");
        startData.put("to", "с / мин");
        result = controller.convert(startData).getBody();
        assertEquals("60", result);

        startData.put("from", "м * кг / час * час");
        startData.put("to", "км * кг / час * час");
        result = controller.convert(startData).getBody();
        assertEquals("0.001", result);

        startData.put("from", "м * кг / мин * мин");
        startData.put("to", "км * г / с * час");
        result = controller.convert(startData).getBody();
        assertEquals("1", result);
    }

    /**
     * checks some cases when answer is "404 NOT FOUND"
     */
    @Test
    public void NotFoundTests() {
        RequestController controller = new RequestController();
        HashMap<String, String> startData = new HashMap<>();
        Integer expectedResult = 404;
        Integer result;

        startData.put("from", "м * кг / кг * мин");
        startData.put("to", "км * г / с * час");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);

        startData.put("from", "м * с / кг * мин");
        startData.put("to", "км * г / с * час");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);

        startData.put("from", "м * с / час * мин");
        startData.put("to", "км * г / с * час");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);

        startData.put("from", "мин");
        startData.put("to", "км");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);
    }


    /**
     * checks some cases when answer is "400 BAD REQUEST"
     */
    @Test
    public void BadRequestTests() {
        RequestController controller = new RequestController();
        HashMap<String, String> startData = new HashMap<>();
        Integer expectedResult = 400;
        Integer result;

        startData.put("from", "м * кг / кг * мин");
        startData.put("to", "км * г / неСекунда * час");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);

        startData.put("from", "м * с / неКилограмм * мин");
        startData.put("to", "км * г / с * час");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);

        startData.put("from", "м * с / час * мин");
        startData.put("to", "тожеНеКилограмм * г / с * час");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);

        startData.put("from", "м / км");
        startData.put("to", "1 / ");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);

        startData.put("from", "м / ");
        startData.put("to", "км");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);

        startData.put("from", "неМинута");
        startData.put("to", "км");
        result = controller.convert(startData).getStatusCodeValue();
        assertEquals(expectedResult, result);
    }
}