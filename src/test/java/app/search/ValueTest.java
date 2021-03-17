package app.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test Value class.
 *
 * @version 1.0.0 17 Mar 2021.
 * @author Aleksey Lakhanksii.
 */
class ValueTest {

    /**
     * Creates three types of instances of "Value" and converts them
     * back to string. Checks the result (the should be the same as the were).
     */
    @Test
    public void testConstructorAndToString() {
        String expectedEndData1 = "16829241443.8341";
        assertEquals(expectedEndData1, new Value(expectedEndData1)
                .toString());
        String expectedEndData2 = "1234567890123450000000000000000";
        assertEquals(expectedEndData2, new Value(expectedEndData2)
                .toString());
        String expectedEndData3 = "0.000000000000000123456789012345";
        assertEquals(expectedEndData3, new Value(expectedEndData3)
                .toString());
    }

    /**
     * Creates some instances of "Value" and multiply them with the
     * other one. Checks the result of multiplication.
     */
    @Test
    public void testMultiplication() {
        String startData11 = "123.01";
        String startData12 = "10";
        String expectedData1 = "1230.1";
        Value data1 = new Value(startData11);
        data1.multiply(new Value(startData12));
        assertEquals(expectedData1, data1.toString());

        String startData21 = "1234567890123450000000000000000";
        String startData22 = "100";
        String expectedData2 = "123456789012345000000000000000000";
        Value data2 = new Value(startData21);
        data2.multiply(new Value(startData22));
        assertEquals(expectedData2, data2.toString());

        String startData31 = "0.00000000000000012345678901234";
        String startData32 = "1000";
        String expectedData3 = "0.00000000000012345678901234";
        Value data3 = new Value(startData31);
        data3.multiply(new Value(startData32));
        assertEquals(expectedData3, data3.toString());
    }

    /**
     * Creates some instances of "Value" and divide them on the other
     * one. Checks the result of division.
     */
    @Test
    public void testDivision() {
        String startData11 = "1230.1";
        String startData12 = "10";
        String expectedData1 = "123.01";
        Value data1 = new Value(startData11);
        data1.divide(new Value(startData12));
        assertEquals(expectedData1, data1.toString());

        String startData21 = "123456789012345000000000000000000";
        String startData22 = "100";
        String expectedData2 = "1234567890123450000000000000000";
        Value data2 = new Value(startData21);
        data2.divide(new Value(startData22));
        assertEquals(expectedData2, data2.toString());

        String startData31 = "0.000000000000123456789012345";
        String startData32 = "1000";
        String expectedData3 = "0.000000000000000123456789012345";
        Value data3 = new Value(startData31);
        data3.divide(new Value(startData32));
        assertEquals(expectedData3, data3.toString());
    }
}