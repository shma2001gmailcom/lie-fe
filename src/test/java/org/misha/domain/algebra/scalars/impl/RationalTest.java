package org.misha.domain.algebra.scalars.impl;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Author: mshevelin
 * Date: 8/14/14
 * Time: 12:42 PM
 */

public class RationalTest {
    private static final int eleven = 11;
    private static final int seventeen = 17;

    @Test
    public void testRational() throws Exception {
        assertEquals(Rational.rational(2, 3).toString(), "2/3");
        assertEquals(Rational.rational(2).toString(), "2");
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals(
                Rational.rational(2 * 2 * 2 * 2, 5 * 5).multiply(Rational.rational(7, 4)), Rational.rational(
                2 * 2 * 7, 5 * 5
        )
        );
    }

    @Test
    public void testGetNumerator() throws Exception {
        assertEquals(Rational.rational(7, 8).getNumerator(), Rational.rational(7));
    }

    @Test
    public void testGetDenominator() throws Exception {
        assertEquals(Rational.rational(7, 8).getDenominator(), Rational.rational(8));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(Rational.rational(0, 1).toString(), "0");
        assertEquals(Rational.rational(3, 1).toString(), "3");
        assertEquals(Rational.rational(2 * 2 * 3, 2 * 2 * 7).toString(), "3/7");
    }

    @Test
    public void testReverse() throws Exception {
        assertEquals(Rational.rational(1, 1).reverse(), Rational.rational(1));
        assertEquals(Rational.rational(3).reverse(), Rational.rational(1, 3));
        assertEquals(Rational.rational(eleven, eleven * 3).reverse(), Rational.rational(3));
        assertEquals(Rational.rational(seventeen, 7 * 3).reverse(), Rational.rational(7 * 3, seventeen));
    }

    @Test
    public void testSum() throws Exception {
        assertEquals(Rational.rational(1, 2).minus(Rational.rational(1, 3)), Rational.rational(1, 6));
        assertEquals(Rational.rational(1, 2).sum(Rational.rational(-1, 3)), Rational.rational(1, 6));
    }

    @Test
    public void testNegate() throws Exception {
        assertEquals(Rational.rational(-1, 3), Rational.rational(1, 3).negate());
    }

    @Test
    public void testMinus() throws Exception {
        assertEquals(Rational.rational(1, 3).minus(Rational.rational(1, 4)), Rational.rational(1, 3 << 2));
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithoutDenominator() {
        Rational.rational(0).parse("0/");
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithoutNumerator() {
        Rational.rational(0).parse("/2");
    }

    @Test(expected = IllegalStateException.class)
    public void testParseZeroDenominator() {
        Rational.rational(0).parse("1/0");
    }

    @Test(expected = IllegalStateException.class)
    public void testParseUnParseAble() {
        Rational.rational(0).parse("a/3");
    }

    @Test(expected = IllegalStateException.class)
    public void testParseUnParseAbleDoubleFractionSign() {
        Rational.rational(0).parse("1/1/1");
    }

    @Test
    public void testParseRational() {
        assertEquals(Rational.rational(0).parse(StringUtils.EMPTY), Rational.ONE);
        assertEquals(Rational.rational(0).parse("0/2"), Rational.ZERO);
        assertEquals(Rational.rational(0).parse("0"), Rational.ZERO);
        assertEquals(Rational.rational(0).parse("1"), Rational.ONE);
        assertEquals(Rational.rational(0).parse("2/2"), Rational.ONE);
        assertEquals(Rational.rational(0).parse("2/3"), Rational.rational(2, 3));
        assertEquals(Rational.rational(0).parse("2/1"), Rational.rational(2));
        assertEquals(Rational.rational(0).parse("14/12"), Rational.rational(7, 6));
    }

    @Test
    public void testEquals() {
        final Rational r = Rational.rational(7, eleven);
        final Rational s = Rational.rational(2 * 7, 2 * eleven);
        assertEquals(r, s);
        assertEquals(r, Rational.rational(7, eleven));
    }

    @Test
    public void testClone() {
        Rational r = Rational.rational(7, eleven);
        final Rational clone = r.clone();
        assertEquals(r, clone);
        r = Rational.rational(1, 2);
        assertFalse(r.equals(clone));
    }
}
