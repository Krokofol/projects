package app.search;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyBigDecimal {

    private static final Integer MAX_SIGNIFICANT_DIGITS = 60;
    private static final Integer MAX_POW = 15;

    public BigDecimal bigDecimal;

    public Long exponent;

    public MyBigDecimal(String value) {
        bigDecimal = new BigDecimal(value);
        long ex = Math.round(Math.floor(Math.log10(bigDecimal.doubleValue())));
        exponent = MAX_POW - ex;
        if (ex < MAX_POW) {
            for (long i = ex; i < MAX_POW; i++) {
                bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
            }
        } else {
            for (long i = ex; i > MAX_POW; i--) {
                bigDecimal = bigDecimal.divide(
                        BigDecimal.TEN,
                        MAX_SIGNIFICANT_DIGITS,
                        RoundingMode.DOWN
                );
            }
        }
        bigDecimal = bigDecimal.setScale(
                MAX_SIGNIFICANT_DIGITS,
                RoundingMode.DOWN
        );
    }

    public void multiply(MyBigDecimal value2) {
        exponent += value2.exponent;
        bigDecimal = bigDecimal.multiply(value2.bigDecimal);
        long ex = Math.round(Math.floor(Math.log10(bigDecimal.doubleValue())));
        exponent += MAX_POW - ex;
        for (long i = MAX_POW; i < ex; i++) {
            bigDecimal = bigDecimal.divide(
                    BigDecimal.TEN,
                    MAX_SIGNIFICANT_DIGITS,
                    RoundingMode.DOWN
            );
        }
        bigDecimal = bigDecimal.setScale(
                MAX_SIGNIFICANT_DIGITS,
                RoundingMode.DOWN
        );
    }

    public void divide(MyBigDecimal value2) {
        exponent -= value2.exponent;
        bigDecimal = bigDecimal.divide(
                value2.bigDecimal,
                MAX_SIGNIFICANT_DIGITS * 2,
                RoundingMode.DOWN
        );
        long ex = Math.round(Math.floor(Math.log10(bigDecimal.doubleValue())));
        exponent += MAX_POW - ex;
        for (long i = ex; i < MAX_POW; i++) {
            bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
        }
        bigDecimal = bigDecimal.setScale(
                MAX_SIGNIFICANT_DIGITS,
                RoundingMode.DOWN
        );
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        if (exponent > MAX_POW) {
            res.append("0.");
            for (long i = MAX_POW; i < exponent - 1; i++) {
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
                bigDecimal = bigDecimal.divide(
                        BigDecimal.TEN,
                        MAX_POW,
                        RoundingMode.DOWN
                );
            }
            bigDecimal = bigDecimal.setScale(
                    exponent.intValue() - pow,
                    RoundingMode.DOWN
            );
            res.append(bigDecimal.toString());
            return  res.toString();
        }
        bigDecimal = bigDecimal.setScale(0, RoundingMode.DOWN);
        res.append(bigDecimal.toString());
        for (long i = exponent + MAX_POW; i < MAX_POW; i++) {
            res.append("0");
        }
        return res.toString();
    }
}
