package org.misha.domain.algebra.associative;

import org.misha.domain.algebra.scalars.Scalar;

import java.util.Iterator;

/**
 * Author: mshevelin
 * Date: 8/22/14
 * Time: 1:02 PM
 * <p/>
 * T is some class like rationals or integers modulo some prime
 */
public interface MonomialInterface<T extends Scalar> extends Comparable<MonomialInterface<T>> {
    Iterator<Character> sequenceIterator();

    T getConstant();

    <Z extends MonomialInterface<T>> Z copy();

    MonomialInterface<T> times(MonomialInterface<T> other);

    MonomialInterface<T> times(T i);

    MonomialInterface<T> unify();

    String toString();

    boolean isSimilar(MonomialInterface<T> m);

    T getConst();

    void setConst(T c);

    int deg();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
