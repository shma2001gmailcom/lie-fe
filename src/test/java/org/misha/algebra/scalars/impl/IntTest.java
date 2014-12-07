package org.misha.algebra.scalars.impl;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.misha.algebra.scalars.impl.Int.newInt;

/**
 * Author: mshevelin
 * Date: 9/1/14
 * Time: 3:20 PM
 */

public class IntTest {
    public static final int SEVENTY_EIGHT = 78;
    public static final int ELEVEN = 11;
    public static final int MINUS_ONE_HUNDRED_THIRTY_ONE = -131;
    public static final int SIXTY_FIVE = 65;
    public static final int THIRTEEN = 13;
    final Int integer0 = newInt(0);
    final Int integer1 = newInt(1);
    final Int two = newInt(2);
    final Int minusTwo = newInt(-2);

    @Test
    public void testNewInt() throws Exception {
        assertEquals(integer0, Int.ZERO);
        assertEquals(integer1, Int.ONE);
        final Int orig = newInt(5);
        assertEquals(orig.getValue(), 5);
    }

    @Test
    public void testGetValue() throws Exception {
        assertEquals(2, two.getValue());
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(two.equals(2));
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(two.add(minusTwo), Int.ZERO);
    }

    @Test
    public void testMultiply() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReverse() throws Exception {
        Int.ONE.reverse();
    }

    @Test
    public void testSum() throws Exception {
        assertEquals(Int.ONE, newInt(3).sum(minusTwo));
    }

    @Test
    public void testNegate() throws Exception {
        assertEquals(minusTwo, two.negate());
    }

    @Test
    public void testMinus() throws Exception {
    }

    @Test
    public void testIsPositive() throws Exception {
        assertTrue(Int.ONE.isPositive());
    }

    @Test
    public void testParse() throws Exception {
        assertEquals(Int.ONE.negate(), newInt(0).parse("-1"));
    }

    @Test
    public void testTimes() throws Exception {
        assertEquals(two.times(minusTwo), newInt(-4));
    }

    @Test
    public void testMod() throws Exception {
        assertEquals(newInt(SEVENTY_EIGHT).mod(newInt(ELEVEN)), newInt(1));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(newInt(MINUS_ONE_HUNDRED_THIRTY_ONE).toString(), "-131");
    }

    @Test
    public void testGcd() {
        assertEquals(newInt(SEVENTY_EIGHT).gcdWith(newInt(SIXTY_FIVE)), newInt(THIRTEEN));
    }

    @Test
    public void testClone() {
        Int orig = newInt(5);
        final Int clone = orig.clone();
        assertEquals(newInt(5), clone);
        orig = newInt(7);
        assertFalse(orig.equals(clone));
    }
}
