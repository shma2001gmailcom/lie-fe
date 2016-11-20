package org.misha.domain.algebra.scalars.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.misha.domain.algebra.scalars.Scalar;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

/**
 * Author: mshevelin
 * Date: 9/1/14
 * Time: 2:47 PM
 */

@Immutable
final class Int implements Serializable, Scalar<Int>, Cloneable {
    private static final Logger log = Logger.getLogger(Int.class);
    static final Int ZERO = new Int(0);
    static final Int ONE = new Int(1);
    private static final long serialVersionUID = -4640317405606449771L;
    private final int value;

    private Int(final int i) {
        value = i;
    }

    static Int newInt(final int i) {
        return new Int(i);
    }

    public int getValue() {
        return value;
    }

    private static Int sum(final Int i, final Int j) {
        return newInt(i.value + j.value);
    }

    private static Int negate(final Int i) {
        return newInt(-i.value);
    }

    private static Int multiply(final Int i, final Int j) {
        return newInt(i.value * j.value);
    }

    private static Int reminder(final Int i, final Int j) {
        if (j.value == 0) {
            throw new IllegalStateException("can't divide by zero.");
        }
        return newInt(i.value % j.value);
    }

    private static Int parseInt(String s) {
        boolean isNegative = false;
        //remove sign holding it up
        if (StringUtils.startsWith(s, "+")) {
            s = StringUtils.removeStart(s, "+");
        }
        if (StringUtils.startsWith(s, "-")) {
            s = StringUtils.removeStart(s, "-");
            isNegative = true;
        }
        //parse int and join the sign held back
        if (StringUtils.isNumeric(s)) {
            return isNegative ? newInt(Integer.parseInt(s)).negate() : newInt(Integer.parseInt(s));
        }
        throw new IllegalStateException("can't parse as int: " + s);
    }

    private static Int gcd(final Int i, final Int j) {
        return newInt(Rational.gcd(i.value, j.value));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Int intO = (Int) o;
        return value == intO.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    public boolean equals(final int i) {
        return i == value;
    }

    public Int add(final Int i) {
        return sum(this, i);
    }

    @Override
    public Int multiply(final Int y) {
        return times(y);
    }

    @Override
    public Int reverse() throws IllegalArgumentException {
        throw new IllegalArgumentException("can't inverse Int.");
    }

    @Override
    public Int sum(final Int y) {
        return add(y);
    }

    @Override
    public Int negate() {
        return negate(this);
    }

    @Override
    public Int minus(final Int i) {
        return sum(this, i.negate());
    }

    @Override
    public boolean isPositive() {
        return value > 0;
    }

    @Override
    public Int parse(final String s) {
        return parseInt(s);
    }

    public Int times(final Int i) {
        return multiply(this, i);
    }

    Int mod(final Int modulo) {
        return reminder(this, modulo);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    Int gcdWith(final Int i) {
        return gcd(this, i);
    }

    @Override
    public Int clone() {
        Int clone = null;
        try {
            clone = (Int) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error(e);
        }
        return clone;
    }
}
