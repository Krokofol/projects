package app;

import app.holdingUnits.MyBigDecimal;

public class MyBigDecimalTest {

    public static void main(String[] args) {
        MyBigDecimal d1 = new MyBigDecimal("5");
        MyBigDecimal d2 = new MyBigDecimal("0.1");
        MyBigDecimal d3 = new MyBigDecimal("0.5");
        d1.multiply(d2);
        d1.divide(d3);
        System.out.println(d1.toString());
    }
}
