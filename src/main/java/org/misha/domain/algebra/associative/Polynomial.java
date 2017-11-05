package org.misha.domain.algebra.associative;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.misha.domain.algebra.associative.impl.Monomial;
import org.misha.domain.algebra.fox.Derivative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.misha.domain.algebra.associative.impl.Monomial.monomial;

/**
 * Author: mshevelin
 * Date: 5/26/14
 * Time: 11:39 AM
 */

public final class Polynomial implements Iterable<Monomial>, Serializable, Cloneable {
    private static final Logger log = Logger.getLogger(org.misha.domain.algebra.lie.polynomial.Polynomial.class);
    private static final long serialVersionUID = -2597630069340261466L;
    private List<Monomial> monomials = new ArrayList<Monomial>();

    public int size() {
        return monomials.size();
    }

    void sort() {
        Collections.sort(monomials);
    }

    public Polynomial plus(final Monomial m) {
        final Polynomial result = new Polynomial();
        result.addAll(copy().monomials);//add clones
        final Monomial copy = m.copy();//m should be cloned
        final List<Monomial> resultMonomials = result.monomials;
        for (final Iterator<Monomial> it = resultMonomials.iterator(); it.hasNext(); ) {
            final Monomial monomial = it.next();
            if (monomial.isSimilar(m)) {
                return result.collectSimilar(copy, it, monomial);
            }
        }
        if (copy.getConst() != 0) {
            result.add(copy);
        }
        result.sort();
        return result;
    }

    private void addAll(final Collection<Monomial> monomials) {
        this.monomials.addAll(monomials);
    }

    private void add(final Monomial copy) {
        monomials.add(copy);
    }

    private Polynomial copy() {
        return clone();
    }

    private Polynomial collectSimilar(
            final Monomial m, final Iterator<Monomial> iterator, final Monomial monomial
    ) {
        final int mConst = monomial.getConst();
        final int cConst = m.getConst();
        if (mConst == -cConst) {
            iterator.remove();
        } else {
            monomial.setConst(mConst + cConst);
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final Monomial m : monomials) {
            sb = sb.append(m.toString());
        }
        log.debug(sb.toString());
        return StringUtils.EMPTY.equals(sb.toString().trim()) ? "0" : sb.toString().trim();
    }

    public Polynomial times(final Polynomial p) {
        final Polynomial copy = copy();
        final Polynomial pCopy = p.copy();
        Polynomial result = new Polynomial();
        for (final Monomial m : copy) {
            for (final Monomial n : pCopy) {
                final Monomial mM = m.copy().times(n.copy());
                result = result.plus(mM);
            }
        }
        result.sort();
        return result;
    }

    public Polynomial times(final int c) {
        Polynomial result = new Polynomial();
        for (final Monomial m : this) {
            final Monomial mCopy = m.copy();
            mCopy.setConst(c * mCopy.getConst());
            result = result.plus(mCopy);
        }
        return result;
    }

    @Override
    public Iterator<Monomial> iterator() {
        return monomials.iterator();
    }

    public Polynomial plus(final Polynomial polynomial) {
        Polynomial result = new Polynomial();
        for (final Monomial m : copy()) {
            result = result.plus(m);
        }
        for (final Monomial m : polynomial.copy()) {
            result = result.plus(m);
        }
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Polynomial monomials1 = (Polynomial) o;
        return monomials.equals(monomials1.monomials);
    }

    @Override
    public int hashCode() {
        return monomials.hashCode();
    }

    Polynomial abel() {
        Polynomial result = new Polynomial();
        for (final Monomial m : this) {
            result = result.plus(m.abel());
        }
        return result;
    }

    public Derivative fox() {
        final Collection<Character> characters = new TreeSet<Character>();
        for (final Monomial m : copy()) {
            for (final Iterator<Character> it = m.iterator(); it.hasNext(); ) {
                characters.add(it.next());
            }
        }
        return foxRelative(characters);
    }

    public Derivative foxRelative(final Iterable<Character> characters) {
        final Map<Monomial, Polynomial> result = new TreeMap<Monomial, Polynomial>();
        for (final Character c : characters) {
            final Monomial letter = monomial(Character.toString(c), 1);
            Polynomial polynomial = new Polynomial();
            for (final Monomial m : copy()) {
                polynomial = polynomial.plus(m.fox().get(letter));
            }
            result.put(letter, polynomial.abel());
        }
        final Derivative derivative = new Derivative();
        derivative.putAll(result);
        return derivative;
    }

    @Override
    public Polynomial clone() {
        Polynomial clone = null;
        try {
            clone = (Polynomial) super.clone();
            clone.monomials = new ArrayList<Monomial>();
            for (final Monomial m : monomials) {
                clone.monomials.add(m.clone());
            }
        } catch (CloneNotSupportedException e) {
            log.error(e);
        }
        return clone;
    }
}
