package org.misha.algebra.lie.polynomial;

import org.apache.log4j.Logger;
import org.misha.algebra.fox.Derivative;
import org.misha.algebra.lie.endomorphism.Endo;
import org.misha.algebra.lie.polynomial.monomial.Monomial;
import org.misha.algebra.lie.polynomial.monomial.MonomialUtils;
import org.misha.algebra.parser.Parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * author: misha
 * Date: 3/23/14
 * Time: 1:12 PM
 * <p/>
 * The set of binary trees provided with coefficients. Lie polynomial.
 */

@SuppressWarnings({"ClassWithTooManyMethods", "SuppressionAnnotation"})
public final class Polynomial implements Iterable<Monomial>, Cloneable {
    private static final Logger log = Logger.getLogger(Polynomial.class);
    private final List<Monomial> monomials = new ArrayList<Monomial>();

    /**
     * Static generation method
     *
     * @param s String presentation of the polynomial
     * @return polynomial having s as a String representation
     */
    public static Polynomial mount(final String s) {
        return new Parser(s).parse();
    }

    /**
     * @param m Monomial
     * @return cloned this plus cloned m
     */
    public Polynomial plus(final Monomial m) {
        final Polynomial result = new Polynomial();
        result.addAll(monomials);//add clones
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

    Polynomial collectSimilar(
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

    /**
     * adds two polynomials this and p
     *
     * @param p a polynomial to add
     * @return sum
     */
    public Polynomial plus(final Iterable<Monomial> p) {
        Polynomial result = new Polynomial();
        result.addAll(monomials);
        for (final Monomial m : p) {
            result = result.plus(m);
        }
        result.sort();
        return result;
    }

    private void addAll(final Iterable<Monomial> moreMonomials) {
        for (final Monomial monomial : moreMonomials) {
            monomials.add(monomial.copy());//should add another references of added monomials
        }
    }

    public void sort() {
        Collections.sort(monomials);
    }

    private void add(final Monomial monomial) {
        monomials.add(monomial);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final Monomial m : monomials) {
            sb = sb.append(m.getConst() > 0 ? " + " + m : m.getString());
        }
        return sb.toString().trim();
    }

    /**
     * @param m monomial
     * @return the coefficient occur near monomial m in this polynomial.
     *         <p/>
     *         Should return such a reference to integer which does not
     *         contained neither in some monomial of this polynomial nor in m
     */
    public int getConstant(final Monomial m) {
        for (final Monomial monomial : monomials) {
            //final Monomial copy = monomial.copy();
            if (monomial.isSimilar(m)) {
                return monomial.getConst();
            }
        }
        return 0;
    }

    @Override
    public Iterator<Monomial> iterator() {
        return monomials.iterator();
    }

    public Polynomial times(final Monomial right) {
        Polynomial result = new Polynomial();
        for (final Monomial m : this) {
            result = result.plus(MonomialUtils.monomial(m, right));
        }
        return result;
    }

    /**
     * Multiplies this by polynomial right from the right
     *
     * @param right right multiplier
     * @return this times right
     */
    public Polynomial times(final Iterable<Monomial> right) {
        Polynomial result = new Polynomial();
        for (final Monomial l : this) {
            for (final Monomial r : right) {
                result = result.plus(MonomialUtils.monomial(l, r).hall());
            }
        }
        result = result.hall();
        result.sort();
        return result;
    }

    private Polynomial hallOnce() {
        Polynomial result = new Polynomial();
        for (final Monomial m : this) {
            final Polynomial mHall = m.hall();
            result = result.plus(mHall);
        }
        return result;
    }

    /**
     * Expands this polynomial by M. Hall base of the free Lee algebra
     *
     * @return expansion
     */
    public Polynomial hall() {
        Polynomial result = new Polynomial();
        result.addAll(this);
        while (result.hasIncorrect()) {
            result = result.hallOnce();
        }
        return result;
    }

    private boolean hasIncorrect() {
        for (final Monomial m : this) {
            if (!m.isCorrect()) {
                return true;
            }
        }
        return false;
    }

    public Polynomial times(final int c) {
        Polynomial result = new Polynomial();
        for (final Monomial m : this) {
            final Monomial mCopy = m.copy();
            mCopy.setConst(mCopy.getConst() * c);
            result = result.plus(mCopy);
        }
        return result;
    }

    public Polynomial copy() {
        Polynomial p = new Polynomial();
        for (final Monomial m : this) {
            p = p.plus(m.copy());
        }
        return p;
    }

    public Polynomial substitute(final Monomial letter, final Iterable<Monomial> p) {
        final Polynomial copy = copy();
        Polynomial result = new Polynomial();
        for (final Monomial m : copy) {
            if (m.contains(letter)) {
                for (final Monomial n : p) {
                    final Monomial mCopy = m.copy();
                    mCopy.subst(letter, n.copy());
                    result = result.plus(mCopy);
                }
            } else {
                final Monomial mCopy = m.copy();
                result = result.plus(mCopy);
            }
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

    public boolean contains(final Monomial letter) {
        for (final Monomial m : this) {
            if (m.contains(letter)) {
                return true;
            }
        }
        return false;
    }

    public Polynomial encode() {
        Polynomial result = new Polynomial();
        for (final Monomial monomial : this) {
            result = result.plus(monomial.encode());
        }
        return result;
    }

    public Polynomial decode() {
        Polynomial result = new Polynomial();
        for (final Monomial monomial : this) {
            result = result.plus(monomial.decode());
        }
        return result;
    }

    public org.misha.algebra.associative.Polynomial expand() {
        org.misha.algebra.associative.Polynomial result = new org.misha.algebra.associative.Polynomial();
        for (final Monomial m : copy()) {
            result = result.plus(m.expand());
        }
        return result;
    }

    public Derivative fox() {
        return expand().fox();
    }

    public Derivative foxRelative(final Iterable<Character> characters) {
        return expand().foxRelative(characters);
    }

    public Collection<Monomial> letters() {
        final Collection<Monomial> letters = new TreeSet<Monomial>();
        for (final Monomial monomial : this) {
            letters.addAll(monomial.letters());
        }
        return letters;
    }

    @Override
    public Polynomial clone() {
        Polynomial clone = null;
        try {
            clone = (Polynomial) super.clone();
            int i = 0;
            for (final Monomial monomial : monomials) {
                clone.monomials.set(i, monomial.clone());
            }
            ++i;
        } catch (CloneNotSupportedException e) {
            log.error(e);
        }
        return clone;
    }

    public Polynomial actBy(Endo endo) {
        Polynomial result = new Polynomial();
        for (Monomial m : copy()) {
            result = result.plus(m.actBy(endo));
        }
        return result;
    }
}
