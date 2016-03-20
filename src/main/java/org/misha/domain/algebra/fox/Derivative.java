package org.misha.domain.algebra.fox;

import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.associative.impl.Monomial;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: mshevelin
 * Date: 5/26/14
 * Time: 5:24 PM
 */

public final class Derivative implements Iterable<Map.Entry<Monomial, Polynomial>> {
    private final Map<Monomial, Polynomial> partials = new TreeMap<Monomial, Polynomial>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (final Map.Entry<Monomial, Polynomial> entry : partials.entrySet()) {
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
    public Polynomial get(final Monomial letter) {
        if (partials.containsKey(letter)) {
            return partials.get(letter);
        }
        return new Polynomial();
    }

    @Override
    public Iterator<Map.Entry<Monomial, Polynomial>> iterator() {
        return partials.entrySet().iterator();
    }

    @SuppressWarnings("unused declaration")
    public void put(final Monomial letter, final Polynomial value) {
        partials.put(letter, value);
    }

    public void putAll(final Map<Monomial, Polynomial> map) {
        partials.putAll(map);
    }
}
