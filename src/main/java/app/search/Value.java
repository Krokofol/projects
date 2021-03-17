package app.search;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class for precise calculations.
 *
 * @version 1.0.0 17 Mar 2021
 * @author Aleksey Lakhanskii
 */
public class Value {

    /**
     * The number of significant digits. Should be not bigger then 18 (number
     * of long digits.
     */
    private static final Integer NUMBER_SIGNIFICANT_DIGITS = 15;

    /**
     * The number of insignificant digits. More numbers = more precision, but
     * spends more time for operations.
     */
    private static final Integer NUMBER_OF_INSIGNIFICANT_DIGITS = 60;

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
     * constructs "Value" by number which is written at the string.
     * @param number the number which presented as the string.
     */
    public Value(String number) {
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
     * multiplies this instance "Value" with the value. Writes
     * the result into this "Value" instance.
     * @param value the value on which this instance of "Value"
     *              multiplies.
     */
    public void multiply(Value value) {
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
     * divides this instance of "Value" to the value. Writes the result
     * into this "Value" instance.
     * @param value the value on which this instance of "Value" divides.
     */
    public void divide(Value value) {
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
     * Converts this instance of "Value" to the String.
     * @return the string with value at the normal form.
     */
    @Override
    public String toString() {
        boolean digitDontHaveNumbersBeforeDot =
                exponent > NUMBER_SIGNIFICANT_DIGITS;
        boolean digitHaveNumbersAfterDot =
                exponent >= 0;
        if (digitDontHaveNumbersBeforeDot) {
            return convertDigitLessThenOne();
        }
        if (digitHaveNumbersAfterDot) {
            return convertDigitWhichHaveNumbersAfterDot();
        }
        return convertDigitWhichDontHaveNumbersAfterDot();
    }

    /**
     * Converts digit which is less then 1 and bigger then 0.
     * @return the string form of this digit.
     */
    private String convertDigitLessThenOne() {
        StringBuilder stringForm = new StringBuilder();
        stringForm.append("0.");
        for (long i = NUMBER_SIGNIFICANT_DIGITS; i < exponent - 1; i++) {
            stringForm.append("0");
        }
        int saveInsignificantDigits = 0;
        long data = bigDecimal
                .setScale(saveInsignificantDigits, RoundingMode.DOWN)
                .longValue();
        while (data % 10 == 0) {
            data = data / 10;
        }
        stringForm.append(data);
        return stringForm.toString();
    }

    /**
     * Converts digit which is bigger or equals then 1 and less or equals then
     * 10^"NUMBER_SIGNIFICANT_DIGITS".
     * @return the string form of this digit.
     */
    private String convertDigitWhichHaveNumbersAfterDot() {
        StringBuilder stringForm = new StringBuilder();
        int saveInsignificantDigits = 0;
        BigDecimal valueCopy = bigDecimal.setScale(
                saveInsignificantDigits,
                RoundingMode.DOWN
        );
        long data = valueCopy.longValue();
        int pow = 0;
        while(data % 10 == 0 && pow < NUMBER_SIGNIFICANT_DIGITS) {
            pow++;
            data = data / 10;
        }
        for (long i = 0; i < exponent; i++) {
            valueCopy = valueCopy.divide(
                    BigDecimal.TEN,
                    NUMBER_SIGNIFICANT_DIGITS,
                    RoundingMode.DOWN
            );
        }
        valueCopy = valueCopy.setScale(
                exponent.intValue() - pow,
                RoundingMode.DOWN
        );
        stringForm.append(valueCopy.toString());
        return stringForm.toString();
    }

    /**
     * Converts digit which is bigger then 10^"NUMBER_SIGNIFICANT_DIGITS".
     * @return string form of this digit.
     */
    private String convertDigitWhichDontHaveNumbersAfterDot(){
        StringBuilder stringForm = new StringBuilder();
        int saveInsignificantDigits = 0;
        BigDecimal valueCopy = bigDecimal.setScale(
                saveInsignificantDigits,
                RoundingMode.DOWN
        );
        stringForm.append(valueCopy.toString());
        for (
                long i = exponent + NUMBER_SIGNIFICANT_DIGITS;
                i < NUMBER_SIGNIFICANT_DIGITS;
                i++
        ) {
            stringForm.append("0");
        }
        return stringForm.toString();
    }
}
