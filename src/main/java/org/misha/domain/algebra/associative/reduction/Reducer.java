package org.misha.domain.algebra.associative.reduction;

import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.associative.impl.Monomial;

import java.util.Iterator;

import static org.misha.domain.algebra.associative.impl.Monomial.*;

/**
 * author: misha
 * date: 13.11.17
 */
public class Reducer {
    private final PolynomialSet set;
    
    public Reducer(final PolynomialSet set) {
        this.set = set;
    }
    
    void reduceOnce() {
        Monomial totalBefore = total();
        Monomial totalAfter = monomial("", 1);
        while (totalBefore.compareTo(totalAfter) > 0) {
            totalBefore = total();
            Iterator<MarkedPolynomial> it = set.iterator();
            MarkedPolynomial minor = it.next();
            it.remove();
            while (it.hasNext()) {
                for (Polynomial p : new Reduction(it.next().mince(), minor.mince()).reduceOnce()) {
                    set.add(new MarkedPolynomial(p));
                }
            }
            set.add(minor);
            totalAfter = total();
        }
    }
    
    Monomial total() {
        Monomial result = set.iterator().next().elder();
        for (MarkedPolynomial p : set) {
            result = p.elder().compareTo(result) > 0 ? p.elder() : result;
        }
        return result;
    }
}
