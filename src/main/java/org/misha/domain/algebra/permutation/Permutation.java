package org.misha.domain.algebra.permutation;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Author: mshevelin
 * Date: 11/27/14
 * Time: 4:40 PM
 */

public final class Permutation {
    private final int degree;
    private final int[] values;

    private Permutation(final int... values) {
        this.values = values;
        degree = values.length;
    }

    public static Permutation create(final int... values) {
        return new Permutation(values);
    }

    private static void check(final int[] values, final int[] anotherValues) {
        if (!isValuesValid(values, anotherValues)) {
            throw new IllegalArgumentException("invalid degree for values or values itself.");
        }
    }

    private static boolean isValuesValid(final int[] values, final int[] anotherValues) {
        final Collection<Integer> tmp = new TreeSet<Integer>();
        final Collection<Integer> temp = new TreeSet<Integer>();
        for (final int value : values) {
            tmp.add(value);
        }
        for (final int value : anotherValues) {
            temp.add(value);
        }
        return tmp.containsAll(temp) && temp.containsAll(tmp);
    }

    public int getAt(final int i) {
        return values[i];
    }

    public Permutation times(final Permutation other) {
        check(values, other.values);
        final int[] values = new int[degree];
        for (int i = 0; i < degree; ++i) {
            values[i] = other.getAt(getAt(i));
        }
        return new Permutation(values);
    }

    public Permutation reverse() {
        final int[] values = new int[degree];
        for (int i = 0; i < degree; ++i) {
            values[getAt(i)] = i;
        }
        return new Permutation(values);
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
