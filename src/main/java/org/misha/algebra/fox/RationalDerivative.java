package org.misha.algebra.fox;

import org.misha.algebra.associative.RationalPolynomial;
import org.misha.algebra.associative.impl.RationalMonomial;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: mshevelin
 * Date: 9/1/14
 * Time: 2:40 PM
 */

public final class RationalDerivative implements Iterable<Map.Entry<RationalMonomial, RationalPolynomial>> {
    private final Map<RationalMonomial, RationalPolynomial> partials =
            new TreeMap<RationalMonomial, RationalPolynomial>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (final Map.Entry<RationalMonomial, RationalPolynomial> entry : partials.entrySet()) {
            sb = sb.append(entry.getValue().toString()).append("; ");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        sb = sb.deleteCharAt(sb.length() - 1);
        sb = sb.append(")");
        return sb.toString();
    }

    /**
     * @param letter the letter which is mapped to something by this derivative
     * @return the polynomial in which given letter is mapped to.
     */
    public RationalPolynomial get(final RationalMonomial letter) {
        if (partials.containsKey(letter)) {
            return partials.get(letter);
        }
        return new RationalPolynomial();
    }

    @Override
    public Iterator<Map.Entry<RationalMonomial, RationalPolynomial>> iterator() {
        return partials.entrySet().iterator();
    }

    @SuppressWarnings("unused declaration")
    public void put(final RationalMonomial letter, final RationalPolynomial value) {
        partials.put(letter, value);
    }

    public void putAll(final Map<RationalMonomial, RationalPolynomial> map) {
        partials.putAll(map);
    }
}
