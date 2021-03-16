package app.holdingUnits;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyBigDecimal {

    public BigDecimal bigDecimal;

    public Long exponent;

    public MyBigDecimal(String value) {
        bigDecimal = new BigDecimal(value);
        long ex = Math.round(Math.floor(Math.log10(bigDecimal.doubleValue())));
        exponent = 15 - ex;
        System.out.println(ex);
        if (ex < 15) {
            for (long i = ex; i < 15; i++) {
                bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
            }
        } else {
            for (long i = ex; i > 15; i--) {
                bigDecimal = bigDecimal.divide(BigDecimal.TEN, 60,
                        RoundingMode.DOWN);
            }
        }
        bigDecimal = bigDecimal.setScale(60, RoundingMode.DOWN);
        System.out.println(bigDecimal + " exp:" + exponent);
    }

    public void multiply(MyBigDecimal value2) {
        System.out.println("multiply");
        System.out.println(bigDecimal + " exp:" + exponent);
        System.out.println(value2.bigDecimal + " exp:" + value2.exponent);
        exponent += value2.exponent;
        bigDecimal = bigDecimal.multiply(value2.bigDecimal);
        long ex = Math.round(Math.floor(Math.log10(bigDecimal.doubleValue())));
        exponent += 15 - ex;
        for (long i = 15; i < ex; i++) {
            bigDecimal = bigDecimal.divide(BigDecimal.TEN, 60,
                    RoundingMode.DOWN);
        }
        bigDecimal = bigDecimal.setScale(60, RoundingMode.DOWN);
        System.out.println(bigDecimal.toString() + " exp:" + exponent);
    }

    public void divide(MyBigDecimal value2) {
        System.out.println("divide");
        System.out.println(bigDecimal + " exp:" + exponent);
        System.out.println(value2.bigDecimal + " exp:" + value2.exponent);
        exponent -= value2.exponent;
        bigDecimal = bigDecimal.divide(value2.bigDecimal, 120,
                RoundingMode.DOWN);
        long ex = Math.round(Math.floor(Math.log10(bigDecimal.doubleValue())));
        exponent += 15 - ex;
        for (long i = ex; i < 15; i++) {
            bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
        }
        bigDecimal = bigDecimal.setScale(60, RoundingMode.DOWN);
        System.out.println(bigDecimal.toString() + " exp:" + exponent);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        if (exponent > 15) {
            res.append("0.");
            for (long i = 15; i < exponent; i++) {
                res.append("0");
            }
            long data = bigDecimal.setScale(0, RoundingMode.DOWN)
                    .longValue();
            while (data % 10 == 0) {
                data = data / 10;
            }
            res.append(data);
            return res.toString();
        }
        if (exponent >= 0) {
            bigDecimal = bigDecimal.setScale(0, RoundingMode.DOWN);
            long data = bigDecimal.longValue();
            int pow = 0;
            while(data % 10 == 0 && pow < exponent) {
                pow++;
                data = data / 10;
            }
            for (long i = 0; i < exponent; i++) {
                bigDecimal = bigDecimal.divide(BigDecimal.TEN, 15,
                        RoundingMode.DOWN);
            }
            bigDecimal = bigDecimal.setScale(exponent.intValue() - pow,
                    RoundingMode.DOWN);
            res.append(bigDecimal.toString());
            return  res.toString();
        }
        bigDecimal = bigDecimal.setScale(0, RoundingMode.DOWN);
        res.append(bigDecimal.toString());
        for (long i = exponent + 15; i < 15; i++) {
            res.append("0");
        }
        return res.toString();

//        for (long i = 0; i < exponent; i++) {
//            bigDecimal = bigDecimal.divide(BigDecimal.TEN);
//        }
//        for (long i = exponent; i < 0; i++) {
//            bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
//        }
//        return bigDecimal.toString();
    }
}
