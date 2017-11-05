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

    public Iterator<Character> iterator() {
        return sequence.iterator();
    }

    public Character getAt(int i) {
        if (i < 0 || i > sequence.size() - 1) return null;
        return sequence.get(i);
    }

    public Monomial subWord(int fromIndex, int toIndex) {
        return monomial(sequence.subList(fromIndex, toIndex));
    }

    public Monomial head(int toIndex) {
        return subWord(0, toIndex);
    }

    public Monomial tail(int fromIndex) {
        return subWord(fromIndex, deg());
    }

    int getConstant() {
        return constant;
    }

    private Monomial(final List<Character> sequence, final int constant) {
        this.constant = constant;
        this.sequence = sequence;
    }

    public static Monomial monomial(final String s) {
        final Pattern pattern = Pattern.compile("(\\+|-)( *)([0-9]*)([a-zA-Z]*)");
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

    public static Monomial monomial(final List<Character> characters, final int constant) {
        return new Monomial(characters, constant);
    }

    public static Monomial monomial(final List<Character> characters) {
        return new Monomial(characters, 1);
    }

    public Monomial copy() {
        return clone();
    }

    public int deg() {
        return sequence.size();
    }

    public Monomial times(final Monomial other) {
        final Monomial result = copy();
        final Monomial copy = other.copy();
        result.sequence.addAll(copy.sequence);
        result.constant *= copy.constant;
        return result;
    }

    public Monomial times(final int i) {
        final Monomial copy = copy();
        copy.constant *= i;
        return copy;
    }

    Monomial unify() {
        final Monomial copy = copy();
        copy.constant = 1;
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
        StringBuilder sb = new StringBuilder();
        for (final Character c : sequence) {
            sb = sb.append(c);
        }
        return (constant == 1 ? "+ " :
                constant > 0 ? "+ " + constant : constant == -1 ? "- " : "- " + -constant
        ) +
                sb.toString() + " ";
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
        Collections.sort(result.sequence);
        return result;
    }

    public Derivative fox() {
        final Map<Monomial, Polynomial> result = new HashMap<Monomial, Polynomial>();
        for (int i = 0; i < sequence.size(); i++) {
            final Character c = sequence.get(i);
            final Monomial key = monomial(Character.toString(c), 1);
            Polynomial value = new Polynomial();
            final List<Character> newSequence;
            if (sequence.get(0).equals(c)) {
                newSequence = copy().sequence;
                newSequence.remove(0);
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
            for (final char c : sequence) {
                clone.sequence.add(c);
            }
            clone.constant = constant;
        } catch (CloneNotSupportedException e) {
            log.error(e);
        }
        assert equals(clone);
        return clone;
    }
}
