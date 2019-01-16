package org.misha.domain.algebra.associative.impl;

import org.apache.log4j.Logger;
import org.misha.domain.algebra.associative.MonomialInterface;
import org.misha.domain.algebra.associative.RationalPolynomial;
import org.misha.domain.algebra.fox.RationalDerivative;
import org.misha.domain.algebra.scalars.impl.Rational;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: mshevelin
 * Date: 8/22/14
 * Time: 1:10 PM
 */

public final class RationalMonomial implements MonomialInterface<Rational>, Serializable, Cloneable {
    private static final Logger log = Logger.getLogger(RationalMonomial.class);
    private static final long serialVersionUID = 5526179742214316857L;
    private List<Character> sequence = new ArrayList<>();
    private Rational constant;

    @Override
    public Iterator<Character> sequenceIterator() {
        return sequence.iterator();
    }

    @Override
    public Rational getConstant() {
        return constant;
    }

    @Override
    public RationalMonomial copy() {
        return clone();
    }

    @Override
    public MonomialInterface<Rational> times(final MonomialInterface<Rational> other) {
        final RationalMonomial result = copy();
        final RationalMonomial copy = other.copy();
        result.sequence.addAll(copy.sequence);
        result.constant = result.constant.multiply(copy.constant);
        return result;
    }

    @Override
    public MonomialInterface<Rational> times(final Rational r) {
        final RationalMonomial copy = copy();
        copy.constant = copy.constant.multiply(r);
        return copy;
    }

    @Override
    public int compareTo(@Nonnull final MonomialInterface<Rational> o) {
        if (!(o instanceof RationalMonomial)) {
            throw new IllegalArgumentException("can't compare.");
        }
        final RationalMonomial monomial = (RationalMonomial) o;
        if (deg() - monomial.deg() != 0) {
            return deg() - monomial.deg();
        }
        final Iterator<Character> iterator = monomial.sequence.iterator();
        for (final Character c : sequence) {
            final int differ = c - iterator.next();
            if (differ != 0) {
                return differ;
            }
        }
        return 0;
    }

    @Override
    public int deg() {
        return sequence.size();
    }

    @Override
    public boolean isSimilar(final MonomialInterface<Rational> m) {
        return compareTo(m) == 0;
    }

    @Override
    public Rational getConst() {
        return constant;
    }

    @Override
    public void setConst(final Rational c) {
        constant = c;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RationalMonomial monomial = (RationalMonomial) o;
        return constant.equals(monomial.constant) && !(sequence != null ? !sequence.equals(
                monomial.sequence
        ) : monomial.sequence != null
        );
    }

    @Override
    public int hashCode() {
        int result = sequence != null ? sequence.hashCode() : 0;
        result = 31 * result + constant.hashCode();
        return result;
    }

    public RationalMonomial abel() {
        final RationalMonomial result = copy();
        Collections.sort(result.sequence);
        return result;
    }

    public RationalDerivative fox() {
        final Map<RationalMonomial, RationalPolynomial> result =
                new HashMap<RationalMonomial, RationalPolynomial>();
        for (final Character c : sequence) {
            final RationalMonomial key = rationalMonomial(Character.toString(c), Rational.ONE);
            RationalPolynomial value = new RationalPolynomial();
            final List<Character> newSequence;
            if (sequence.get(0).equals(c)) {
                newSequence = copy().sequence;
                newSequence.remove(0);
                value = value.plus(new RationalMonomial(newSequence, getConst()));
            }
            result.put(key, value);
        }
        final RationalDerivative derivative = new RationalDerivative();
        derivative.putAll(result);
        return derivative;
    }

    private RationalMonomial(final List<Character> sequence, final Rational constant) {
        this.constant = constant;
        this.sequence = sequence;
    }

    private RationalMonomial(final String s, final Rational constant) {
        this.constant = constant;
        for (final Character c : s.toCharArray()) {
            sequence.add(c);
        }
    }

    public static RationalMonomial rationalMonomial(final String s) {
        final Pattern pattern = Pattern.compile("(\\+|-)( *)([0-9]*/?[0-9]*)([a-zA-Z]*)");
        final Matcher matcher = pattern.matcher(s.trim());
        final String lead;
        if (matcher.find()) {
            final String sign = matcher.group(1).trim();
            if ("-".equals(sign)) {
                lead = sign + matcher.group(3).trim();
            } else {
                lead = matcher.group(3).trim();
            }
            final Rational constant = Rational.rational(0).parse(lead);
            final String core = matcher.group(4).trim();
            return new RationalMonomial(core, constant);
        }
        return new RationalMonomial("", Rational.ONE);
    }

    private static RationalMonomial rationalMonomial(final String s, final Rational constant) {
        return new RationalMonomial(s, constant);
    }

    public RationalMonomial times(final RationalMonomial other) {
        final RationalMonomial result = copy();
        final RationalMonomial copy = other.copy();
        result.sequence.addAll(copy.sequence);
        result.constant = result.constant.times(copy.constant);
        return result;
    }

    @Override
    public RationalMonomial unify() {
        final RationalMonomial copy = copy();
        copy.constant = Rational.ONE;
        return copy;
    }

    @Override
    public String toString() {
        if (deg() == 0) {
            return "" + constant;
        }
        StringBuilder sb = new StringBuilder();
        for (final Character c : sequence) {
            sb = sb.append(c);
        }
        return (constant.equals(Rational.ONE) ? "+ " :
                constant.isPositive() ? "+ " + constant : constant.equals(Rational.ONE.negate()) ? "- " :
                                                          "- " + constant.negate()
        ) + sb.toString() + " ";
    }

    @Override
    public RationalMonomial clone() {
        RationalMonomial clone = null;
        try {
            clone = (RationalMonomial) super.clone();
            int i = 0;
            for (final Character c : sequence) {
                clone.sequence.set(i, c);
                ++i;
            }
            clone.constant = Rational.rational(constant.numerator(), constant.denominator());
        } catch (CloneNotSupportedException e) {
            log.error(e);
        }
        return clone;
    }
}
