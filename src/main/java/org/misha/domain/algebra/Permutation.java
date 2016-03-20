/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.domain.algebra;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Author: mshevelin
 * Date: 11/27/14
 * Time: 4:40 PM
 */

public final class Permutation {
    private final int degree;
    private final int[] values;

    private Permutation(final int degree, final int... values) {
        this.degree = degree;
        this.values = values;
    }

    public static Permutation create(final int degree, final int... values) {
        check(degree, values);
        return new Permutation(degree, values);
    }

    private static void check(final int degree, final int[] values) {
        if (values.length != degree || !isValuesValid(values, degree)) {
            throw new IllegalArgumentException("invalid degree for values or values itself.");
        }
    }

    private static boolean isValuesValid(final int[] values, final int degree) {
        final Collection<Integer> tmp = new TreeSet<Integer>();
        for (final int value : values) {
            tmp.add(value);
        }
        final Iterator<Integer> it = tmp.iterator();
        return tmp.size() == degree && it.next() == 0;
    }

    public int getAt(final int i) {
        return values[i];
    }

    public Permutation times(final Permutation other) {
        final int[] values = new int[degree];
        for (int i = 0; i < degree; ++i) {
            values[i] = other.getAt(getAt(i));
        }
        return new Permutation(degree, values);
    }

    public Permutation reverse() {
        final int[] values = new int[degree];
        for (int i = 0; i < degree; ++i) {
            values[getAt(i)] = i;
        }
        return new Permutation(degree, values);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Permutation that = (Permutation) o;
        return degree == that.degree && Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        int result = degree;
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("degree", degree).append(
                "values", values
        ).toString();
    }
}
