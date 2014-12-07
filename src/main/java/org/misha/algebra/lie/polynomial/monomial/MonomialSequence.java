package org.misha.algebra.lie.polynomial.monomial;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Author: mshevelin
 * Date: 11/27/14
 * Time: 1:13 PM
 */

public class MonomialSequence {
    private final Collection<Monomial> sequence = new TreeSet<Monomial>();
    private final Monomial lastLetter;

    public MonomialSequence(final String... rawAlphabet) {
        Monomial mLetter = null;
        for (final String letter : rawAlphabet) {
            if (letter.length() == 1) {
                mLetter = MonomialUtils.monomial(letter);
                sequence.add(mLetter);
            }
        }
        lastLetter = mLetter;
    }

    public Monomial getNextMonomial(final Monomial m) {
        if (!m.isCorrect()) {
            throw new IllegalArgumentException("The monomial '" + m + "' must be correct.");
        }
        Monomial product;
        for (final Monomial left : sequence) {
            for (final Monomial right : sequence) {
                product = MonomialUtils.monomial(left, right);
                if (product.isCorrect() && product.compareTo(m) > 0) {
                    sequence.add(product);
                    return product.copy();
                }
            }
        }
        return null;
    }

    public Monomial getLastLetter() {
        return lastLetter.copy();
    }
}
