package org.misha.algebra.scalars;

/**
 * Author: mshevelin
 * Date: 8/14/14
 * Time: 3:46 PM
 */
public interface Scalar<T extends Scalar> {

    T multiply(T y);

    T reverse() throws IllegalArgumentException;

    T sum(T y);

    T negate();

    T minus(T y);

    boolean isPositive();

    T parse(String s);
}
