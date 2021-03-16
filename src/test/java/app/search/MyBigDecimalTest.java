package app.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test MyBigDecimal class.
 *
 * @version 1.0.0 17 Mar 2021.
 * @author Aleksey Lakhanksii.
 */
class MyBigDecimalTest {

    /**
     * Creates three types of instances of "MyBigDecimal" and converts them
     * back to string. Checks the result (the should be the same as the were).
     */
    @Test
    public void testConstructorAndToString() {
        String expectedEndData1 = "16829241443.8341";
        assertEquals(expectedEndData1, new MyBigDecimal(expectedEndData1)
                .toString());
        String expectedEndData2 = "1234567890123450000000000000000";
        assertEquals(expectedEndData2, new MyBigDecimal(expectedEndData2)
                .toString());
        String expectedEndData3 = "0.000000000000000123456789012345";
        assertEquals(expectedEndData3, new MyBigDecimal(expectedEndData3)
                .toString());
    }

    /**
     * Creates some instances of "MyBigDecimal" and multiply them with the
     * other one. Checks the result of multiplication.
     */
    @Test
    public void testMultiplication() {
        String startData11 = "123.01";
        String startData12 = "10";
        String expectedData1 = "1230.1";
        MyBigDecimal data1 = new MyBigDecimal(startData11);
        data1.multiply(new MyBigDecimal(startData12));
        assertEquals(expectedData1, data1.toString());

        String startData21 = "1234567890123450000000000000000";
        String startData22 = "100";
        String expectedData2 = "123456789012345000000000000000000";
        MyBigDecimal data2 = new MyBigDecimal(startData21);
        data2.multiply(new MyBigDecimal(startData22));
        assertEquals(expectedData2, data2.toString());

        String startData31 = "0.000000000000000123456789012345";
        String startData32 = "1000";
        String expectedData3 = "0.000000000000123456789012345";
        MyBigDecimal data3 = new MyBigDecimal(startData31);
        data3.multiply(new MyBigDecimal(startData32));
        assertEquals(expectedData3, data3.toString());
    }

    /**
     * Creates some instances of "MyBigDecimal" and divide them on the other
     * one. Checks the result of division.
     */
    @Test
    public void testDivision() {
        String startData11 = "1230.1";
        String startData12 = "10";
        String expectedData1 = "123.01";
        MyBigDecimal data1 = new MyBigDecimal(startData11);
        data1.divide(new MyBigDecimal(startData12));
        assertEquals(expectedData1, data1.toString());

        String startData21 = "123456789012345000000000000000000";
        String startData22 = "100";
        String expectedData2 = "1234567890123450000000000000000";
        MyBigDecimal data2 = new MyBigDecimal(startData21);
        data2.divide(new MyBigDecimal(startData22));
        assertEquals(expectedData2, data2.toString());

        String startData31 = "0.000000000000123456789012345";
        String startData32 = "1000";
        String expectedData3 = "0.000000000000000123456789012345";
        MyBigDecimal data3 = new MyBigDecimal(startData31);
        data3.divide(new MyBigDecimal(startData32));
        assertEquals(expectedData3, data3.toString());
    }
}