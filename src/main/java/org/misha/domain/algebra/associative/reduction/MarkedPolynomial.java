package org.misha.domain.algebra.associative.reduction;

import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.associative.impl.Monomial;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * author: misha
 * date: 09.11.17
 */
public class MarkedPolynomial implements Iterable<Monomial>, Serializable, Cloneable, Comparable<MarkedPolynomial> {
    private final Map<String, String> marks = new HashMap<String, String>();//todo make EnumMap
    private final Polynomial polynomial;
    
    protected MarkedPolynomial(final Polynomial p) {
        this.polynomial = p;
    }
    
    Polynomial mince() {
        return polynomial;
    }
    
    void addMark(String key, String value) {
        marks.put(key, value);
    }
    
    boolean markedAs(String key) {
        return marks.containsKey(key);
    }
    
    String getMark(String key) {
        return marks.get(key);
    }
    
    String removeMark(String key) {
       return marks.remove(key);
    }
    
    public int size() {
        return polynomial.size();
    }
    
    public int deg() {
        return polynomial.deg();
    }
    
    public void sort() {
        polynomial.sort();
    }
    
    public Monomial elder() {
        return polynomial.elder();
    }
    
    public MarkedPolynomial plus(final Monomial m) {
        return new MarkedPolynomial(polynomial.plus(m));
    }
    
    public String toString() {
        return polynomial.toString();
    }
    
    public MarkedPolynomial times(final MarkedPolynomial p) {
        return new MarkedPolynomial(polynomial.times(p.polynomial));
    }
    
    public MarkedPolynomial times(Monomial m) {
        return new MarkedPolynomial(polynomial.times(m));
    }
    
    public MarkedPolynomial times(final int c) {
        return new MarkedPolynomial(polynomial.times(c));
    }
    
    public Iterator<Monomial> iterator() {
        return polynomial.iterator();
    }
    
    public MarkedPolynomial plus(final MarkedPolynomial another) {
        return new MarkedPolynomial(polynomial.plus(another.polynomial));
    }
    
    public boolean equals(final Object o) {
        return polynomial.equals(o);
    }
    
    public int hashCode() {
        return polynomial.hashCode();
    }
    
    public MarkedPolynomial copy() {
        final MarkedPolynomial result = new MarkedPolynomial(polynomial.clone());
        for(Map.Entry<String, String> entry : marks.entrySet()) {
            result.addMark(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public int compareTo(@Nonnull Polynomial another) {
        return polynomial.compareTo(another);
    }
    
    public boolean isZero() {
        return polynomial.isZero();
    }
    
    @Override
    public int compareTo(@Nonnull final MarkedPolynomial p) {
        return polynomial.compareTo(p.polynomial);
    }
}
