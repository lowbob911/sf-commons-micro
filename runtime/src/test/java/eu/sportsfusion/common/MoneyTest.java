package eu.sportsfusion.common;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Anatoli Pranovich
 */
public class MoneyTest {

    // -----------------------------------------------------------------------
    // TEST METHODS

    @Test
    public void testSum() {
        Money[] sumArray = new Money[] {
                new Money("2.53"), new Money("2.31"), new Money("2.35")
        };

        // Test full list sum.
        Money sum = Money.sum(sumArray);
        assertTrue(Money.equal(sum, new Money("7.19")));

        // Test partial list sum.
        sum = Money.sum(sumArray, 2);
        assertTrue(Money.equal(sum, new Money("4.84")));
    }

    @Test
    public void testCents() {
        Money m = new Money("0.8");
        assertThat(m.toString(), is("0.80"));

        Money m2 = new Money("-0.8");
        assertThat(m2.toString(), is("-0.80"));

        Money m3 = new Money("-8.7");
        assertThat(m3.toString(), is("-8.70"));
    }


    @Test
    public void testMinWithVarargs() {
        Money min = Money.min(new Money("1.30"), new Money("1.50"), new Money("1.40"));

        assertThat(min, is(new Money("1.30")));
    }

    @Test
    public void testMaxWithVarargs() {
        Money max = Money.max(new Money("1.3"), new Money("1.5"), new Money("1.4"));

        assertThat(max, is(new Money("1.5")));
    }

    @Test
    public void testMaxWithList() {
        Money[] list = new Money[] {
                new Money("2.53"), new Money("2.31"), new Money("2.35")
        };

        assertTrue(Money.equal(Money.max(list), new Money("2.53")));
    }

    @Test
    public void testMinWithList() {
        Money[] list = new Money[] {
                new Money("2.53"), new Money("2.31"), new Money("2.35")
        };

        assertTrue(Money.equal(Money.min(list), new Money("2.31")));
    }

    @Test
    public void testSplitWithEqualParts() {
        final Money originalWhole = new Money("10.00");

        Money[] parts = Money.split(originalWhole, 3);
        Money recalcWhole = Money.sum(parts);

        assertTrue(Money.equal(recalcWhole, originalWhole));
    }

    @Test
    public void testSplitWithEmptyWeightParts() {
        final Money originalWhole = new Money("1.50");
        final Money[] weights = new Money[] { };

        Money[] parts = Money.split(originalWhole, weights);

        assertThat(parts.length, is(0));
    }

    @Test
    public void testSplitWithZeroWeightParts() {
        final Money originalWhole = new Money("12.35");
        final Money[] weights = new Money[] {
                Money.ZERO, Money.ZERO, Money.ZERO
        };

        Money[] parts = Money.split(originalWhole, weights);
        Money recalcWhole = Money.sum(parts);

        assertTrue(Money.equal(recalcWhole, Money.ZERO));
    }

    @Test
    public void testSplitWithWeightParts() {
        // Positive whole.
        Money originalWhole1 = new Money("1.50");
        Money recalcWhole1 = Money.sum(splitWithWeights(originalWhole1));

        assertTrue(Money.equal(recalcWhole1, originalWhole1));


        // Zero whole.
        Money originalWhole2 = new Money("1.50");
        Money recalcWhole2 = Money.sum(splitWithWeights(originalWhole2));

        assertTrue(Money.equal(recalcWhole2, originalWhole2));

        final Money[] split800 = splitWithWeights3(new Money("8.00"));
        final Money v267 = new Money("2.67");
        final Money v266 = new Money("2.66");
        assertEquals(split800[0], v267);
        assertEquals(split800[1], v267);
        assertEquals(split800[2], v266);

        final Money[] split080 = splitWithWeights3(new Money("0.80"));
        final Money v027 = new Money("0.27");
        final Money v026 = new Money("0.26");
        assertEquals(split080[0], v027);
        assertEquals(split080[1], v027);
        assertEquals(split080[2], v026);

        final Money[] split008 = splitWithWeights3(new Money("0.08"));
        final Money v003 = new Money("0.03");
        final Money v002 = new Money("0.02");
        assertEquals(split008[0], v003);
        assertEquals(split008[1], v003);
        assertEquals(split008[2], v002);
    }

    @Test
    public void testSplitWithMap() {
        // Positive whole.
        Money originalWhole1 = new Money("28.12");
        Money recalcWhole1 = Money.sum(splitForMap(originalWhole1).values());

        assertTrue(Money.equal(recalcWhole1, originalWhole1));


        // Zero whole.
        Money originalWhole2 = Money.ZERO;
        Money recalcWhole2 = Money.sum(splitForMap(originalWhole2).values());

        assertTrue(Money.equal(recalcWhole2, originalWhole2));
    }

    @Test
    public void testSplitNegative() {
        Money originalWhole = new Money("-8.00");
        Money[] weights = new Money[] {
                new Money("1.00"),
                new Money("1.00"),
                new Money("1.00"),
                new Money("0.00")
        };

        Money[] split = Money.split(originalWhole, weights);

        assertEquals(split[0], new Money("-2.67"));
        assertEquals(split[1], new Money("-2.67"));
        assertEquals(split[2], new Money("-2.66"));
    }

    @Test
    public void testSplitWithPence() {
        Money originalWhole = new Money("0.01");

        Money[] weights = new Money[] {
                new Money("52.00"),
                new Money("26.00"),
                new Money("52.00"),
        };

        Money[] split = Money.split(originalWhole, weights);
        Money recalcWhole = Money.sum(split);

        assertTrue(Money.equal(recalcWhole, originalWhole));
        assertEquals(split[0], new Money("0.01"));
    }

    @Test
    public void testProportionWithNull() {
        Money result = Money.getProportion(new Money("175"), null, new Money("10"));

        assertEquals(result, Money.ZERO);
    }

    @Test
    public void testProportionWithZero() {
        Money result = Money.getProportion(new Money("175"), new Money("10.74"), Money.ZERO);

        assertEquals(result, Money.ZERO);
    }

    @Test
    public void testProportion() {
        Money result = Money.getProportion(new Money("175"), new Money("10.74"), new Money("10"));

        assertEquals(result, new Money("0.61"));
    }

    // -----------------------------------------------------------------------
    // HELPER METHODS

    private Money[] splitWithWeights(Money originalWhole) {
        Money[] weights = new Money[] {
                new Money("1.33"),
                new Money("1.15"),
                new Money("0.51"),
                new Money("0.00")
        };

        return Money.split(originalWhole, weights);
    }

    private Money[] splitWithWeights3(Money originalWhole) {
        Money[] weights = new Money[] {
                new Money("1.00"),
                new Money("1.00"),
                new Money("1.00"),
                new Money("0.00")
        };

        return Money.split(originalWhole, weights);
    }


    private Map<Long, Money> splitForMap(Money originalWhole) {
        final Map<Long, Money> weights = new HashMap<Long, Money>();
        weights.put(1L, new Money("24"));
        weights.put(3L, new Money("8.52"));
        weights.put(4L, new Money("5"));
        weights.put(7L, new Money("0"));

        Map<Long, Money> parts = Money.split(originalWhole, weights);

        // Weights and result parts should have the same set of keys.
        assertTrue(weights.keySet().equals(parts.keySet()));

        return parts;
    }

    @Test
    public void testExtractNetto() {
        Money money = Money.extractNetto(new Money("100"), 20);
        assertEquals("83.33", money.toString());

        money = Money.extractNetto(new Money("33"), 20);
        assertEquals("27.50", money.toString());

        money = Money.extractNetto(new Money("30"), 20);
        assertEquals("25.00", money.toString());

        money = Money.extractNetto(new Money("50"), 25);
        assertEquals("40.00", money.toString());

        money = Money.extractNetto(new Money("50"), 0);
        assertEquals("50.00", money.toString());

        money = Money.extractNetto(new Money("50"), null);
        assertEquals("50.00", money.toString());

        money = Money.extractNetto(new Money("50"), -100); // Not correct data, but should still handle it
        assertEquals("0.00", money.toString());

        money = Money.extractNetto(Money.ZERO, 10);
        assertEquals("0.00", money.toString());
    }

    @Test
    public void testToString() {
        Money money = new Money("23.00");
        assertEquals("23.00", money.toString());

        money = new Money("23.01");
        assertEquals("23.01", money.toString());

        money = new Money("23.45");
        assertEquals("23.45", money.toString());

        money = new Money("23.0");
        assertEquals("23.00", money.toString());

        money = new Money("23.5");
        assertEquals("23.50", money.toString());

        money = new Money("-23.599999999999999999");
        assertEquals("-23.60", money.toString());

        money = new Money("-23.45");
        assertEquals("-23.45", money.toString());
    }
}
