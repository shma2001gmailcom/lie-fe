package org.misha.domain.algebra.associative.reduction;

import org.misha.domain.algebra.associative.Polynomial;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.util.Arrays.asList;

/**
 * author: misha
 * date: 09.11.17
 */
public class PolynomialSet implements Iterable<MarkedPolynomial> {
    private final Set<MarkedPolynomial> set = new ConcurrentSkipListSet<MarkedPolynomial>()  {

        @Override
        public boolean add(final MarkedPolynomial mp) {
            return !mp.isZero() && super.add(mp);
        }
    };
    
    void add(Polynomial p) {
        set.add(new MarkedPolynomial(p));
    }

    void add(MarkedPolynomial p) {
        set.add(p);
    }
    
    void addAll(Polynomial... polynomials) {
        for (Polynomial p : polynomials) set.add(new MarkedPolynomial(p));
    }
    
    void addAll(MarkedPolynomial... polynomials) {
        set.addAll(asList(polynomials));
    }

    void addMarked(Collection<MarkedPolynomial> polynomials) {

        set.addAll(polynomials);
    }

    Polynomial remove(Polynomial p) {
        return set.remove(new MarkedPolynomial(p)) ? p : null;
    }
    
    MarkedPolynomial remove(MarkedPolynomial p) {
        return set.remove(p) ? p : null;
    }
    
    PolynomialSet removeAll(Polynomial... polynomials) {
       final PolynomialSet result = new PolynomialSet();
        for (Polynomial p : polynomials) {
            if (remove(p) != null) result.add(p);
        }
        return result;
    }
    
    PolynomialSet removeAll(MarkedPolynomial... polynomials) {
        final PolynomialSet result = new PolynomialSet();
        for (MarkedPolynomial p : polynomials) {
            if (remove(p) != null) result.add(p);
        }
        return result;
    }
    
    public Set<Polynomial> eraseMarks() {
        final Set<Polynomial> result = new TreeSet<Polynomial>();
        for (MarkedPolynomial mp : set) {
             result.add(mp.mince());
        }
        return result;
    }
    
    @Override
    @Nonnull
    public Iterator<MarkedPolynomial> iterator() {
        return set.iterator();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final MarkedPolynomial mp : set) sb.append(mp.mince()).append('\n');
        return sb.toString();
    }
}