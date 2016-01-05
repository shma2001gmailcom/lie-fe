package org.misha.algebra.associative;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.misha.algebra.associative.impl.RationalMonomial;
import org.misha.algebra.parser.ThatSummand;
import org.misha.algebra.scalars.impl.Rational;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.remove;

/**
 * Author: mshevelin
 * Date: 8/26/14
 * Time: 2:09 PM
 */

public final class RationalPolynomial implements Iterable<RationalMonomial>, Serializable, Cloneable {
    private static final Logger log = Logger.getLogger(RationalPolynomial.class);
    private static final String SPACE = " ";
    private static final String PLUS = "+";
    private static final long serialVersionUID = 433506918985690456L;
    private final List<RationalMonomial> monomials = new ArrayList<RationalMonomial>();

    @Override
    public Iterator<RationalMonomial> iterator() {
        return monomials.iterator();
    }

    private static Iterable<ThatSummand<Rational>> getSummands(final String expression) {
        String summands = StringUtils.remove(expression, " ");
        final Collection<ThatSummand<Rational>> result = new ArrayList<ThatSummand<Rational>>();
        String pattern = "(\\+|\\-)([ \\d]*)(/*)([ \\d]*)([ a-zA-Z]*)(\\+|\\-)";
        summands = findNextSummand(pattern, summands, result);
        pattern = "(\\+|\\-)([ \\d]*)(/*)([ \\d]*)([ a-zA-Z]*)";
        findNextSummand(pattern, summands, result);
        return result;
    }

    private static String findNextSummand(
            final String pattern, String s, final Collection<ThatSummand<Rational>> result
    ) {
        Matcher matcher;
        String core;
        String constant;
        while (StringUtils.isNotEmpty(s)) {
            matcher = compile(pattern).matcher(s);
            if (matcher.find()) {
                core = matcher.group(5);
                constant = matcher.group(1) + matcher.group(2) + matcher.group(3) + matcher.group(4);
                final ThatSummand<Rational> summand = new ThatSummand<Rational>(constant(constant), core);
                result.add(summand);
                s = StringUtils.remove(s, constant + core);
            } else {
                break;
            }
        }
        return s;
    }

    private static Rational constant(String constant) {
        constant = remove(constant, SPACE);
        if ("-".equals(constant)) {
            return Rational.rational(-1);
        }
        if (PLUS.equals(constant) || StringUtils.isBlank(constant)) {
            return Rational.ONE;
        }
        constant = StringUtils.removeStart(constant, PLUS);
        return Rational.rational(0).parse(constant);
    }

    public RationalPolynomial plus(final RationalMonomial m) {
        final RationalPolynomial result = new RationalPolynomial();
        result.addAll(monomials);//add clones
        final RationalMonomial copy = m.copy();//m should be cloned
        final List<RationalMonomial> resultMonomials = result.monomials;
        for (final Iterator<RationalMonomial> it = resultMonomials.iterator(); it.hasNext(); ) {
            final RationalMonomial monomial = it.next();
            if (monomial.isSimilar(m)) {
                return result.collectSimilar(copy, it, monomial);
            }
        }
        if (!copy.getConst().equals(Rational.ZERO)) {
            result.add(copy);
        }
        result.sort();
        return result;
    }

    private void addAll(final Collection<RationalMonomial> monomials) {
        this.monomials.addAll(monomials);
    }

    private void add(final RationalMonomial copy) {
        monomials.add(copy);
    }

    public RationalPolynomial copy() {
        return clone();
    }

    private RationalPolynomial collectSimilar(
            final RationalMonomial m, final Iterator<RationalMonomial> iterator,
            final RationalMonomial monomial
    ) {
        final Rational mConst = monomial.getConst();
        final Rational cConst = m.getConst();
        if (mConst.equals(cConst.negate())) {
            iterator.remove();
        } else {
            monomial.setConst(mConst.sum(cConst));
        }
        return this;
    }

    private void sort() {
        Collections.sort(monomials);
    }

    public RationalPolynomial times(final RationalPolynomial p) {
        final RationalPolynomial copy = copy();
        final RationalPolynomial pCopy = p.copy();
        RationalPolynomial result = new RationalPolynomial();
        for (final RationalMonomial m : copy) {
            for (final RationalMonomial n : pCopy) {
                result = result.plus(m.times(n));
            }
        }
        result.sort();
        return result;
    }

    public RationalPolynomial times(final Rational c) {
        RationalPolynomial result = new RationalPolynomial();
        for (final RationalMonomial m : this) {
            final RationalMonomial mCopy = m.copy();
            mCopy.setConst(c.times(mCopy.getConst()));
            result = result.plus(mCopy);
        }
        return result;
    }

    public static RationalPolynomial mount(final String expression) {
        RationalPolynomial result = new RationalPolynomial();
        for (final ThatSummand<Rational> summand : getSummands(expression)) {
            if (isNotEmpty(summand.getCore())) {
                final RationalMonomial m = RationalMonomial.rationalMonomial(summand.toString());
                result = result.plus(m);
            }
        }
        result.sort();
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
        final RationalPolynomial monomials1 = (RationalPolynomial) o;
        return monomials.equals(monomials1.monomials);
    }

    @Override
    public int hashCode() {
        return monomials.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final RationalMonomial m : monomials) {
            sb = sb.append(m.toString());
        }
        return StringUtils.EMPTY.equals(sb.toString().trim()) ? "0" : sb.toString().trim();
    }

    @Override
    public RationalPolynomial clone() {
        RationalPolynomial clone = null;
        try {
            clone = (RationalPolynomial) super.clone();
            clone.monomials.clear();
            for (final RationalMonomial m : monomials) {
                clone.monomials.add(m.clone());
            }
        } catch (CloneNotSupportedException e) {
            log.error(e);
        }
        return clone;
    }
}
