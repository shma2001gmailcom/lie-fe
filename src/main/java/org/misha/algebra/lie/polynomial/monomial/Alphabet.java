package org.misha.algebra.lie.polynomial.monomial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: misha
 * Date: 3/23/14
 * Time: 10:33 AM
 */

public final class Alphabet {
    private static final List<Monomial> monomials = new ArrayList<Monomial>();
    private static Alphabet instance;

    private Alphabet(final Monomial... toAdd) {
        Collections.addAll(monomials, toAdd);
    }

    public static Alphabet getInstance(final String... s) {
        final Monomial[] monomials = new Monomial[s.length];
        for (int i = 0; i < monomials.length; ++i) {
            monomials[i] = MonomialUtils.monomial(s[i]);
        }
        if (instance == null) {
            instance = new Alphabet(monomials);
        }//todo: getInstance has a problem
        return instance;
    }

    public Monomial get(final int i) {
        return monomials.get(i);
    }
}
