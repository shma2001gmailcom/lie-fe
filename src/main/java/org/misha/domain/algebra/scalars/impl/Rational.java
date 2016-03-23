/*
 * Copyright (c) 2014. Misha's property, all rights reserved.
 */

package org.misha.domain.algebra.scalars.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.misha.domain.algebra.scalars.Scalar;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: mshevelin
 * Date: 8/13/14
 * Time: 6:03 PM
 */

@SuppressWarnings("ClassWithTooManyMethods")
@Immutable
public final class Rational implements Scalar<Rational>, Serializable, Cloneable {
    private static final Logger log = Logger.getLogger(Rational.class);
    private static final String EMPTY_NUMERATOR = "a fraction must have some numerator: ";
    private static final String EMPTY_DENOMINATOR = "a fraction must have the denominator: ";
    private static final String ZERO_DENOMINATOR = "a fraction can't have zero as a denominator: ";
    private static final String CAN_T_PARSE = "can't parse:";
    private static final String CAN_T_REVERSE_ZERO = "Can't reverse zero.";
    private static final String EUCLID_REJECTS_BOTH_ZEROS =
            "At least one number in Euclidean algorithm must be non-zero.";
    private static final long serialVersionUID = -2176000802033967957L;
    private final int numerator;
    private final int denominator;
    public static final Rational ONE = rational(1);
    public static final Rational ZERO = rational(0);

    private Rational(final int numerator, final int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * public factory method
     * @param numerator numerator
     * @param denominator denominator
     * @return new rational with numerator and denominator are given, if possible,
     * elsewhere
     * @throws IllegalArgumentException
     */
    public static Rational rational(final int numerator, final int denominator) throws IllegalArgumentException {
        if (denominator == 0) {
            throw new IllegalArgumentException(ZERO_DENOMINATOR);
        }
        final int gcd = gcd(numerator, denominator);
        if (denominator < 0) {
            return new Rational(-numerator / gcd, -denominator / gcd);
        }
        return new Rational(numerator / gcd, denominator / gcd);
    }

    public static Rational rational(final int x) {
        return rational(x, 1);
    }

    @Override
    public Rational multiply(final Rational y) {
        return rational(numerator * y.numerator, denominator * y.denominator);
    }

    public Rational getNumerator() {
        return rational(numerator, 1);
    }

    public Rational getDenominator() {
        return rational(denominator, 1);
    }

    public int numerator() {
        return numerator;
    }

    public int denominator() {
        return denominator;
    }

    static int gcd(final int a, final int b) throws IllegalArgumentException {
        int x = a < 0 ? -a : a;
        int y = b < 0 ? -b : b;
        if (a == 0) {
            if (y == 0) {
                throw new IllegalArgumentException(EUCLID_REJECTS_BOTH_ZEROS);
            }
            return y;
        }
        while (y > 0) {
            if (x > y) {
                final int t = y;
                y = x - x / y * y;
                x = t;
            } else {
                y -= y / x * x;
            }
        }
        return x;
    }

    public String toString() {
        if (denominator == 1) {
            return String.valueOf(numerator);
        }
        if (numerator == 0) {
            return String.valueOf(0);
        }
        return numerator + "/" + denominator;
    }

    private static Rational reverse(final Rational x) throws IllegalArgumentException {
        if (x.numerator == 0) {
            throw new IllegalArgumentException(CAN_T_REVERSE_ZERO);
        }
        return rational(x.denominator, x.numerator);
    }

    private static Rational sum(final Rational x, final Rational y) {
        return rational(x.numerator * y.denominator + x.denominator * y.numerator, x.denominator * y.denominator
        );
    }

    private static Rational minus(final Rational x, final Rational z) {
        return sum(x, negate(z));
    }

    private static Rational negate(final Rational x) {
        return rational(-x.numerator, x.denominator);
    }

    @Override
    public Rational reverse() {
        return reverse(this);
    }

    @Override
    public Rational sum(final Rational y) {
        return sum(this, y);
    }

    @Override
    public Rational negate() {
        return negate(this);
    }

    @Override
    public Rational minus(final Rational y) {
        return minus(this, y);
    }

    @Override
    public boolean isPositive() {
        return numerator > 0 && denominator > 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Rational.class != o.getClass()) {
            return false;
        }
        final Rational rational = (Rational) o;
        return denominator == rational.denominator && numerator == rational.numerator;
    }

    @Override
    public int hashCode() {
        int result = numerator;
        result = 31 * result + denominator;
        return result;
    }

    /**
     * factory method
     * @param s a String potentially representing a rational
     * @return a rational if possible, elsewhere
     * @throws IllegalStateException
     */
    @Override
    public Rational parse(final String s) throws IllegalStateException {
        if (StringUtils.isEmpty(s)) {
            return ONE;
        }
        final Pattern pattern = Pattern.compile("(\\+|-)*([0-9]+)+(/?)([0-9]*)");
        final Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalStateException(CAN_T_PARSE);
        }
        final String sign = StringUtils.isEmpty(matcher.group(1)) ? "" : "-";
        final String numerator = sign + matcher.group(2);
        final String fractionSign = matcher.group(3);
        final String denominator = matcher.group(4);
        if (StringUtils.isEmpty(numerator)) {
            throw new IllegalStateException(String.format("%s%s.", EMPTY_NUMERATOR, s));
        }
        final int num = Integer.parseInt(numerator);
        if (StringUtils.isEmpty(fractionSign)) {
            return rational(num);
        }
        if (StringUtils.isEmpty(denominator)) {
            throw new IllegalStateException(String.format("%s%s.", EMPTY_DENOMINATOR, s));
        }
        final int den = Integer.parseInt(denominator);
        if (den == 0) {
            throw new IllegalStateException(String.format("%s%s.", ZERO_DENOMINATOR, s));
        }
        return rational(num, den);
    }

    public Rational times(final Rational other) {
        return rational(numerator * other.numerator, denominator * other.denominator);
    }

    @Override
    public Rational clone() {
        Rational clone = null;
        try {
            clone = (Rational) super.clone();
        } catch (final CloneNotSupportedException e) {
            log.error(e);
        }
        return clone;
    }
}

