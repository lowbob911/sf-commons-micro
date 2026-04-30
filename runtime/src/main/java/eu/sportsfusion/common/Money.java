package eu.sportsfusion.common;

import java.io.Serializable;
import java.util.*;

/**
 * @author Anatoli Pranovich
 */
public class Money implements Serializable, Comparable<Money> {

    private static final long serialVersionUID = 4474364849265836823L;

    public static final Money ZERO = new Money(0);

    private static final byte MONEY_SCALE = 100;

    private final long cents;

//    public Money(long units, int decimals) {
//        this.cents = toCents(units, decimals);
//    }

    public Money(String money) {
        final String[] strs = money.split("\\.");

        if (strs.length > 2) {
            throw new NumberFormatException();
        }

        final String strUnits = strs[0].trim();
        final boolean positive = '-' != strUnits.charAt(0);

        final long units = Long.valueOf(strUnits);

        long decimals = strs.length == 2 ? Long.valueOf(strs[1]) : 0;

        if (decimals > 0) {
            if (strs[1].length() == 1) {
                decimals *= 10;
            }

            if (strs[1].length() > 2) {
                decimals = Math.round(decimals / Math.pow(10, strs[1].length() - 2));
            }
        }
        this.cents = toCents(units, positive ? decimals : -decimals);
    }


    private Money(long cents) {
        this.cents = cents;
    }

    private long toCents(long units, long decimals) {
        return units * MONEY_SCALE + decimals;
    }

    public static Money add(Money augend, Money addend) {
        return new Money(getNullAsZero(augend).cents + getNullAsZero(addend).cents);
    }

    public static Money sum(Money ... list) {
        return sum(Arrays.asList(list), list.length);
    }

    public static Money sum(Money[] list, Integer nItems) {
        return sum(Arrays.asList(list), nItems);
    }

    public static Money sum(Collection<Money> list) {
        return sum(list, list.size());
    }

    public static Money sum(Collection<Money> list, Integer nItems) {
        Money sum = ZERO;

        Iterator<Money> iter = list.iterator();
        for (int i = 0; i < nItems && iter.hasNext(); i++) {
            sum = add(sum, iter.next());
        }

        return sum;
    }

    public static Money sub(Money minuend, Money subtrahend) {
        return new Money(getNullAsZero(minuend).cents - getNullAsZero(subtrahend).cents);
    }

    public static Money sub(Money minuend, Money[] list) {
        return new Money(getNullAsZero(minuend).cents - sum(list).cents);
    }

    public static Money sub(Money minuend, Collection<Money> list) {
        return new Money(getNullAsZero(minuend).cents - sum(list).cents);
    }

//    public static Money mul(Money multiplicand, Money multiplier) {
//        return getNullAsZero(multiplicand).multiply(getNullAsZero(multiplier)));
//    }

    public static Money mul(Money multiplicand, Number multiplier) {
        if (multiplier == null) {
            return ZERO;
        }
        return new Money(Math.round(getNullAsZero(multiplicand).cents * multiplier.doubleValue()));
    }

    public static Money applyPercent(final Money base,
                                     final Number percent) {

        if (percent == null) {
            return ZERO;
        }

        return new Money(Math.round(getNullAsZero(base).cents * percent.doubleValue() / 100));
    }

    public static Money extractNetto(final Money brutto, final Number percent) {
        if (percent != null && percent.doubleValue() == -100) {
            return ZERO; // prevent division by zero
        }
        return div(brutto, 1 + ((percent != null ? percent.doubleValue() : 0) / 100));
    }


//    public static BigDecimal calculatePercent(Money whole, Money percentage) {
//        BigDecimal wholeInCents = new BigDecimal(whole.cents);
//        BigDecimal percentageInCents = new BigDecimal(percentage.cents);
//        BigDecimal hundred = new BigDecimal(100);
//        return percentageInCents.multiply(hundred).divide(wholeInCents, 2, RoundingMode.HALF_UP);
//    }

    private static Money getNullAsZero(Money op) {
        return op == null ? ZERO : op;
    }

    private static int compare(Money op1, Money op2) {
        return getNullAsZero(op1).compareTo(getNullAsZero(op2));
    }

    public static boolean equal(Money op1, Money op2) {
        return compare(op1, op2) == 0;
    }

    public static boolean less(Money op1, Money op2) {
        return compare(op1, op2) == -1;
    }

    public static boolean greater(Money op1, Money op2) {
        return compare(op1, op2) == 1;
    }

    public static boolean lessOrEqual(Money op1, Money op2) {
        return compare(op1, op2) <= 0;
    }

    public static boolean greaterOrEqual(Money op1, Money op2) {
        return compare(op1, op2) >= 0;
    }

    public static Money max(Money x, Money y) {
        return greaterOrEqual(x, y) ? x : y;
    }

    public static Money max(Money ... list) {
        Money result = null;

        for (Money v : list) {
            if (result == null || greater(v, result)) {
                result = v;
            }
        }

        return result;
    }

    public static Money min(Money x, Money y) {
        return lessOrEqual(x, y) ? x : y;
    }

    public static Money min(Money ... list) {
        Money result = null;

        for (Money v : list) {
            if (result == null || less(v, result)) {
                result = v;
            }
        }

        return result;
    }

    // do not make this method public
    // use split(Money whole, Integer numParts) for public div
    private static Money div(Money dividend, Number divisor) {
        if (divisor == null) {
            throw new IllegalArgumentException("Divisor is undefined");
        }

        return new Money(Math.round(getNullAsZero(dividend).cents / divisor.doubleValue()));
    }

    public static Money[] split(Money whole, Integer numParts) {
        Money minorPart = div(whole, numParts);

        Money[] result = new Money[numParts];
        for (int i = 0; i < numParts; i++) {
            result[i] = minorPart;
        }

        // Calculate and set major part.
        result[numParts - 1] = add(minorPart, sub(whole, mul(minorPart, numParts)));

        return result;
    }

    public static Money[] split(Money whole, Money[] weights) {
        return split(whole, Arrays.asList(weights));
    }

    public static Money[] split(Money whole, Collection<Money> weights) {
        Money weightsSum = sum(weights);

        // Return empty result if there are no weights greater than 0.
        if (equal(weightsSum, ZERO)) {
            return weights.toArray(new Money[weights.size()]);
        }

        // Calculate proportion for each weight.
        List<Money> result = new ArrayList<>(weights.size());
        for (Money weight : weights) {
            result.add(new Money(Math.round(getNullAsZero(weight).cents * getNullAsZero(whole).cents / ((double) weightsSum.cents))));
        }

        // Check and update received parts, so that the sum is equal to whole.
        Money recalcWhole = sum(result);
        if (!equal(whole, Money.ZERO) && !recalcWhole.equals(whole)) {
            // Find the index of the latest non-zero part.
            int lastIndex = -1;
            for (int i = result.size() - 1; i >= 0; i--) {
                if (result.get(i).cents != 0) {
                    lastIndex = i;
                    break;
                }
            }
            if (lastIndex == -1) {
                lastIndex = 0;
//                throw new RuntimeException("Unexpected index - couldn't find part greater than 0");
            }

            Money lastPart = result.get(lastIndex);
            result.set(lastIndex, new Money(lastPart.cents + getNullAsZero(whole).cents - recalcWhole.cents));
        }

        return result.toArray(new Money[result.size()]);
    }

    public static <T> Map<T, Money> split(Money whole, Map<T, Money> weightsMap) {
        List<T> keys = new ArrayList<>();
        List<Money> values = new ArrayList<>();

        // Build ordered lists for keys and values of original weights map.
        for (T k : weightsMap.keySet()) {
            keys.add(k);
            values.add(weightsMap.get(k));
        }

        Money[] parts = split(whole, values);

        // Build a result map using calculated parts.
        Map<T, Money> results = new HashMap<>(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            results.put(keys.get(i), parts[i]);
        }
        return results;
    }

    public static Money getProportion(Money whole, Money value, Money wholePart) {
        if (whole == null || wholePart == null || value == null ||
                whole.cents == 0 || wholePart.cents == 0 || value.cents == 0) {
            return ZERO;
        }

        return div(mul(wholePart, value.doubleValue()), whole.doubleValue());
    }

    public boolean isGreaterThan0() {
        return cents > 0;
    }

    @Override
    public int compareTo(Money o) {
        long oCents = getNullAsZero(o).cents;
        return (cents < oCents) ? -1 : ((cents == oCents) ? 0 : 1);
    }

    public static Money negate(Money op) {
        return new Money(-getNullAsZero(op).cents);
    }

    public static Money negativeValue(Money op) {
        return greater(op, ZERO) ? negate(op) : op;
    }

    public static Money positiveValue(Money op) {
        return greater(op, ZERO) ? op : negate(op);
    }

    public long getCents() {
        return cents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return cents == money.cents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cents);
    }

    @Override
    public String toString() {
        long units = cents / MONEY_SCALE;
        long decimals = cents % MONEY_SCALE;

        String sign = "";
        if (decimals < 0L ) {
            if (units == 0L) {
                sign = "-";
            }
            decimals *= -1;
        }
        return sign + units + "." + (decimals < 10 ? "0" + decimals : decimals);
    }

    public double doubleValue() {
        return ((double) cents) / ((double) MONEY_SCALE);
    }
}
