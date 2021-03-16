package app.search;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class for precise calculations.
 *
 * @version 1.0.0 17 Mar 2021
 * @author Aleksey Lakhanskii
 */
public class MyBigDecimal {

    private static final Integer NUMBER_OF_INSIGNIFICANT_DIGITS = 60;
    private static final Integer NUMBER_SIGNIFICANT_DIGITS = 15;

    /**
     * the number that has NUMBER_SIGNIFICANT_DIGITS before "." and
     * NUMBER_OF_INSIGNIFICANT_DIGITS after ".".
     */
    public BigDecimal bigDecimal;

    /**
     * the exponent on which "bigDecimal" should be multiplied to convert it
     * to the normal form
     */
    public Long exponent;

    /**
     * constructs "MyBigDecimal" by number which is written at the string.
     * @param number the number which presented as the string.
     */
    public MyBigDecimal(String number) {
        bigDecimal = new BigDecimal(number);
        long startExponent = Math.round(
                Math.floor(Math.log10(bigDecimal.doubleValue()))
        );
        exponent = NUMBER_SIGNIFICANT_DIGITS - startExponent;
        if (startExponent < NUMBER_SIGNIFICANT_DIGITS) {
            for (long i = startExponent; i < NUMBER_SIGNIFICANT_DIGITS; i++) {
                bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
            }
        } else {
            for (long i = startExponent; i > NUMBER_SIGNIFICANT_DIGITS; i--) {
                bigDecimal = bigDecimal.divide(
                        BigDecimal.TEN,
                        NUMBER_OF_INSIGNIFICANT_DIGITS,
                        RoundingMode.DOWN
                );
            }
        }
        bigDecimal = bigDecimal.setScale(
                NUMBER_OF_INSIGNIFICANT_DIGITS,
                RoundingMode.DOWN
        );
    }

    /**
     * multiplies this instance "MyBigDecimal" with the value. Writes
     * the result into this "MyBigDecimal" instance.
     * @param value the value on which this instance of "MyBigDecimal"
     *              multiplies.
     */
    public void multiply(MyBigDecimal value) {
        exponent += value.exponent;
        bigDecimal = bigDecimal.multiply(value.bigDecimal);
        long deltaExponent = Math.round(
                Math.floor(Math.log10(bigDecimal.doubleValue()))
        );
        exponent += NUMBER_SIGNIFICANT_DIGITS - deltaExponent;
        for (long i = NUMBER_SIGNIFICANT_DIGITS; i < deltaExponent; i++) {
            bigDecimal = bigDecimal.divide(
                    BigDecimal.TEN,
                    NUMBER_OF_INSIGNIFICANT_DIGITS,
                    RoundingMode.DOWN
            );
        }
        bigDecimal = bigDecimal.setScale(
                NUMBER_OF_INSIGNIFICANT_DIGITS,
                RoundingMode.DOWN
        );
    }

    /**
     * divides this instance of "MyBigDecimal" to the value. Writes the result
     * into this "MyBigDecimal" instance.
     * @param value the value on which this instance of "MyBigDecimal" divides.
     */
    public void divide(MyBigDecimal value) {
        exponent -= value.exponent;
        bigDecimal = bigDecimal.divide(
                value.bigDecimal,
                NUMBER_OF_INSIGNIFICANT_DIGITS * 2,
                RoundingMode.DOWN
        );
        long ex = Math.round(Math.floor(Math.log10(bigDecimal.doubleValue())));
        exponent += NUMBER_SIGNIFICANT_DIGITS - ex;
        for (long i = ex; i < NUMBER_SIGNIFICANT_DIGITS; i++) {
            bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
        }
        bigDecimal = bigDecimal.setScale(
                NUMBER_OF_INSIGNIFICANT_DIGITS,
                RoundingMode.DOWN
        );
    }

    /**
     * Converts this instance of "MyBigDecimal" to the String.
     * !!!THIS INSTANCE IS UNUSABLE AFTER CONVERTING!!!
     * @return the string with value at the normal form.
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        if (exponent > NUMBER_SIGNIFICANT_DIGITS) {
            res.append("0.");
            for (long i = NUMBER_SIGNIFICANT_DIGITS; i < exponent - 1; i++) {
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
                        NUMBER_SIGNIFICANT_DIGITS,
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
        for (long i = exponent + NUMBER_SIGNIFICANT_DIGITS; i < NUMBER_SIGNIFICANT_DIGITS; i++) {
            res.append("0");
        }
        return res.toString();
    }
}
