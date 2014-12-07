package org.misha.algebra.lie.dependency;

import org.misha.algebra.lie.endomorphism.Endo;
import org.misha.algebra.lie.polynomial.Polynomial;
import org.misha.algebra.lie.polynomial.monomial.Monomial;

import java.util.Iterator;
import java.util.Set;

/**
 * Author: mshevelin
 * Date: 6/10/14
 * Time: 1:15 PM
 */

public class Checker {

    public Monomial leadMonomialOfSubst(final Monomial monomial, final Monomial... monomials) {
        final Set<Monomial> letters = monomial.letters();
        final Endo endo = new Endo();
        Polynomial polynomial = new Polynomial();
        for (final Monomial mono : monomials) {
            polynomial = polynomial.plus(mono);
        }
        for (final Monomial letter : letters) {
            endo.mapTo(letter, polynomial);
        }
        final Iterator<Monomial> it = monomial.actBy(endo).iterator();
        Monomial result = null;
        while (it.hasNext()) {
            result = it.next();
        }
        return result;
    }

    public boolean fallIn(final Polynomial p, final Polynomial... generators) {
        if (!isHomogeneousAll(generators) || !isHomogeneous(p)) {
            throw new IllegalArgumentException("all polynomials must be homogeneous.");
        }
        //Polynomial evaluation = new Polynomial();
        return false;
    }

    public boolean isHomogeneous(final Iterable<Monomial> p) {
        int previousDeg = -1;
        int i = 0;
        for (final Monomial mono : p) {
            final int deg = mono.deg();
            if (i == 0) {
                previousDeg = deg;
            } else {
                if (previousDeg != deg) {
                    return false;
                } else {
                    previousDeg = deg;
                }
            }
            ++i;
        }
        return true;
    }

    public boolean isHomogeneousAll(final Polynomial... polynomials) {
        for (final Polynomial p : polynomials) {
            if (!isHomogeneous(p)) {
                return false;
            }
        }
        return true;
    }
}
