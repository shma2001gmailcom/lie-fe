package org.misha.domain.algebra.associative.impl;

import org.apache.log4j.Logger;
import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.fox.Derivative;

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
 * Date: 5/26/14
 * Time: 9:53 AM
 */

public final class Monomial implements Comparable<Monomial>, Serializable, Cloneable {
    private static final Logger log = Logger.getLogger(Monomial.class);
    private static final long serialVersionUID = -7758209558569173319L;
    private List<Character> sequence = new ArrayList<Character>();
    private int constant;

    private Monomial(final String s, final int constant) {
        this.constant = constant;
        for (final Character c : s.toCharArray()) {
            sequence.add(c);
        }
    }

    public Iterator<Character> sequenceIterator() {
        return sequence.iterator();
    }

    int getConstant() {
        return constant;
    }

    private Monomial(final List<Character> sequence, final int constant) {
        this.constant = constant;
        this.sequence = sequence;
    }

    public static Monomial monomial(final String s) {
        final Pattern pattern = Pattern.compile("([+-])( *)([0-9]*)([a-zA-Z]*)");
        final Matcher matcher = pattern.matcher(s.trim());
        final String lead;
        if (matcher.find()) {
            final String sign = matcher.group(1).trim();
            if ("-".equals(sign)) {
                lead = sign + matcher.group(3).trim();
            } else {
                lead = matcher.group(3).trim();
            }
            final String core = matcher.group(4).trim();
            return new Monomial(core, Integer.parseInt(lead));
        }
        return new Monomial("", 1);
    }

    public static Monomial monomial(final String s, final int constant) {
        return new Monomial(s, constant);
    }

    public Monomial copy() {
        return clone();
    }

    int deg() {
        return sequence.size();
    }

    public Monomial times(@Nonnull final Monomial other) {
        final Monomial result = copy();
        final Monomial copy = other.copy();
        if (result != null && copy != null) {
            result.sequence.addAll(copy.sequence);
            result.constant *= copy.constant;
        }
        return result;
    }

    public Monomial times(final int i) {
        final Monomial copy = copy();
        if (copy != null) {
            copy.constant *= i;
        }
        return copy;
    }

    Monomial unify() {
        final Monomial copy = copy();
        if (copy != null) {
            copy.constant = 1;
        }
        return copy;
    }

    @Override
    public int compareTo(@Nonnull final Monomial o) {
        if (deg() - o.deg() != 0) {
            return deg() - o.deg();
        }
        final Iterator<Character> iterator = o.sequence.iterator();
        for (final Character c : sequence) {
            final int differ = c - iterator.next();
            if (differ != 0) {
                return differ;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        if (deg() == 0) {
            return "" + constant;
        }
        final StringBuilder sb = new StringBuilder();
        for (final Character c : sequence) {
            sb.append(c);
        }
        final String s1 = constant == -1 ? "- " : "- " + -constant;
        final String s = constant > 0 ? "+ " + constant : s1;
        return (constant == 1 ? "+ " :  s) + sb.toString() + " ";
    }

    public boolean isSimilar(final Monomial m) {
        return compareTo(m) == 0;
    }

    public int getConst() {
        return constant;
    }

    public void setConst(final int i) {
        constant = i;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Monomial monomial = (Monomial) o;
        return constant == monomial.constant && !(sequence != null ? !sequence.equals(monomial.sequence) :
                                                  monomial.sequence != null
        );
    }

    @Override
    public int hashCode() {
        int result = sequence != null ? sequence.hashCode() : 0;
        result = 31 * result + constant;
        return result;
    }

    public Monomial abel() {
        final Monomial result = copy();
        if (result != null)
            Collections.sort(result.sequence);
        return result;
    }

    public Derivative fox() {
        final Map<Monomial, Polynomial> result = new HashMap<Monomial, Polynomial>();
        for (int i = 0; i < sequence.size(); i++) {
            final Character c = sequence.get(i);
            final Monomial key = monomial(Character.toString(c), 1);
            Polynomial value = new Polynomial();
            List<Character> newSequence = null;
            if (sequence.get(0).equals(c)) {
                final Monomial copy = copy();
                if (copy != null) {
                    newSequence = copy.sequence;
                    newSequence.remove(0);
                }
                value = value.plus(new Monomial(newSequence, getConst()));
            }
            result.put(key, value);
        }
        final Derivative derivative = new Derivative();
        derivative.putAll(result);
        return derivative;
    }

    @Override
    public Monomial clone() {
        Monomial clone = null;
        try {
            clone = (Monomial) super.clone();
            clone.sequence = new ArrayList<Character>();
            clone.sequence.addAll(sequence);
            clone.constant = constant;
        } catch (CloneNotSupportedException e) {
            log.error(e);
        }
        assert equals(clone);
        return clone;
    }
}
