package app.search;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class for precise calculations.
 *
 * @version 1.0.0 17 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
public class Value {

    /** The number of significant digits (Should be not bigger then 18 number of long digits). */
    private static final Integer NUMBER_SIGNIFICANT_DIGITS = 15;

    /** The number of insignificant digits. More numbers = more precision, but spends more time for operations. */
    private static final Integer NUMBER_OF_INSIGNIFICANT_DIGITS = 90;

    /**
     * Numerator of the digit that has NUMBER_SIGNIFICANT_DIGITS before "." and NUMBER_OF_INSIGNIFICANT_DIGITS after
     * ".".
     */
    private BigDecimal numerator;

    /** Numerator exponent on which "bigDecimal" should be multiplied to convert it to the normal form. */
    private Long numeratorExponent;

    /** if this instance has denominator become true */
    private boolean denominatorIsInitialized;

    /**
     * Denominator of the digit that has NUMBER_SIGNIFICANT_DIGITS before "." and NUMBER_OF_INSIGNIFICANT_DIGITS
     * after ".".
     */
    private BigDecimal denominator;

    /** Denominator exponent on which "bigDecimal" should be multiplied to convert it to the normal form. */
    private Long denominatorExponent;

    /**
     * Constructs "Value" by number which is written at the string.
     * @param number the number which presented as the string.
     */
    public Value(String number) {
        denominatorIsInitialized = false;
        numerator = new BigDecimal(number);
        denominator = new BigDecimal("1");
        long startNumeratorExponent = Math.round(Math.floor(Math.log10(numerator.doubleValue())));
        denominatorExponent = 0L;
        numeratorExponent = NUMBER_SIGNIFICANT_DIGITS - startNumeratorExponent;
        if (startNumeratorExponent < NUMBER_SIGNIFICANT_DIGITS) {
            for (long i = startNumeratorExponent; i < NUMBER_SIGNIFICANT_DIGITS; i++) {
                numerator = numerator.multiply(BigDecimal.TEN);
            }
        } else {
            for (long i = startNumeratorExponent; i > NUMBER_SIGNIFICANT_DIGITS; i--) {
                numerator = numerator.divide(BigDecimal.TEN, NUMBER_OF_INSIGNIFICANT_DIGITS, RoundingMode.DOWN);
            }
        }
        numerator = numerator.setScale(NUMBER_OF_INSIGNIFICANT_DIGITS, RoundingMode.DOWN);
    }

    /**
     * Multiplies this instance "Value" with the value. Writes the result into this "Value" instance.
     * @param value the value on which this instance of "Value" multiplies.
     */
    public void multiply(Value value) {
        if (value.denominatorIsInitialized) {
            denominatorIsInitialized = true;
        }
        calculate(value.numeratorExponent, value.denominatorExponent, value.numerator, value.denominator);
    }

    /**
     * Divides this instance of "Value" to the value. Writes the result into this "Value" instance.
     * @param value the value on which this instance of "Value" divides.
     */
    public void divide(Value value) {
        denominatorIsInitialized = true;
        calculate(value.denominatorExponent, value.numeratorExponent, value.denominator, value.numerator);
    }

    /**
     * Multiplies fractions.
     * @param valueNumeratorExponent the fractions Numerator's Exponent (if it was dividing it is Denominator's).
     * @param valueDenominatorExponent the fractions Denominator's Exponent (if it was dividing it is Numerator's).
     * @param valueNumerator the fractions Numerator (if it was dividing it is Denominator).
     * @param valueDenominator the fractions Denominator (if it was dividing it is Numerator).
     */
    public void calculate(long valueNumeratorExponent, long valueDenominatorExponent,
                          BigDecimal valueNumerator, BigDecimal valueDenominator) {
        numeratorExponent += valueNumeratorExponent;
        numerator = numerator.multiply(valueNumerator);
        long deltaExponent = Math.round(Math.floor(Math.log10(numerator.doubleValue())));
        numeratorExponent += NUMBER_SIGNIFICANT_DIGITS - deltaExponent;
        for (long i = NUMBER_SIGNIFICANT_DIGITS; i < deltaExponent; i++) {
            numerator = numerator.divide(BigDecimal.TEN, NUMBER_OF_INSIGNIFICANT_DIGITS, RoundingMode.DOWN);
        }
        numerator = numerator.setScale(NUMBER_OF_INSIGNIFICANT_DIGITS, RoundingMode.DOWN);
        if (denominatorIsInitialized) {
            denominatorExponent += valueDenominatorExponent;
            denominator = denominator.multiply(valueDenominator);
            deltaExponent = Math.round(Math.floor(Math.log10(denominator.doubleValue())));
            denominatorExponent += NUMBER_SIGNIFICANT_DIGITS - deltaExponent;
            for (long i = NUMBER_SIGNIFICANT_DIGITS; i < deltaExponent; i++) {
                denominator = denominator.divide(BigDecimal.TEN, NUMBER_OF_INSIGNIFICANT_DIGITS, RoundingMode.DOWN);
            }
            denominator = denominator.setScale(NUMBER_OF_INSIGNIFICANT_DIGITS, RoundingMode.DOWN);
        }
    }

    /**
     * Converts this instance of "Value" to the String.
     * @return the string with value at the normal form.
     */
    @Override
    public String toString() {
        Value convertValue = divideNumeratorOnDenominator();
        boolean digitDontHaveNumbersBeforeDot = convertValue.numeratorExponent > NUMBER_SIGNIFICANT_DIGITS;
        boolean digitHaveNumbersAfterDot = convertValue.numeratorExponent >= 0;
        if (digitDontHaveNumbersBeforeDot) {
            return convertValue.convertDigitLessThenOne();
        }
        if (digitHaveNumbersAfterDot) {
            return convertValue.convertDigitWhichHaveNumbersAfterDot();
        }
        return convertValue.convertDigitWhichDontHaveNumbersAfterDot();
    }

    /**
     * When Value converts to string, divide Numerator Value on Denominator Value.
     * @return the result of dividing.
     */
    private Value divideNumeratorOnDenominator() {
        Value result = new Value("1");
        result.numeratorExponent = numeratorExponent - denominatorExponent;
        result.numerator = numerator.divide(denominator, NUMBER_OF_INSIGNIFICANT_DIGITS, RoundingMode.DOWN);
        long deltaExponent = Math.round(Math.floor(Math.log10(result.numerator.doubleValue())));
        result.numeratorExponent += NUMBER_SIGNIFICANT_DIGITS - deltaExponent;
        for (long i = deltaExponent; i < NUMBER_SIGNIFICANT_DIGITS; i++) {
            result.numerator = result.numerator.multiply(BigDecimal.TEN);
        }
        result.numerator = result.numerator.setScale(NUMBER_OF_INSIGNIFICANT_DIGITS, RoundingMode.DOWN);
        return result;
    }

    /**
     * Converts digit which is less then 1 and bigger then 0.
     * @return the string form of this digit.
     */
    private String convertDigitLessThenOne() {
        StringBuilder stringForm = new StringBuilder();
        stringForm.append("0.");
        for (long i = NUMBER_SIGNIFICANT_DIGITS; i < numeratorExponent - 1; i++) {
            stringForm.append("0");
        }
        int saveInsignificantDigits = 0;
        long data = numerator.setScale(saveInsignificantDigits, RoundingMode.DOWN).longValue();
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
        BigDecimal valueCopy = numerator.setScale(saveInsignificantDigits, RoundingMode.DOWN);
        long data = valueCopy.longValue();
        int pow = 0;
        while(data % 10 == 0 && pow < numeratorExponent) {
            pow++;
            data = data / 10;
        }
        for (long i = 0; i < numeratorExponent; i++) {
            valueCopy = valueCopy.divide(BigDecimal.TEN, NUMBER_SIGNIFICANT_DIGITS, RoundingMode.DOWN);
        }
        valueCopy = valueCopy.setScale(numeratorExponent.intValue() - pow, RoundingMode.DOWN);
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
        BigDecimal valueCopy = numerator.setScale(saveInsignificantDigits, RoundingMode.DOWN);
        stringForm.append(valueCopy.toString());
        for (long i = numeratorExponent + NUMBER_SIGNIFICANT_DIGITS; i < NUMBER_SIGNIFICANT_DIGITS; i++) {
            stringForm.append("0");
        }
        return stringForm.toString();
    }
}
