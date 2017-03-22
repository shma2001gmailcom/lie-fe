/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.domain.algebra.lie.polynomial.monomial;

import org.apache.log4j.Logger;
import org.misha.domain.algebra.lie.endomorphism.Endo;
import org.misha.domain.algebra.lie.polynomial.Polynomial;
import org.misha.domain.algebra.parser.Parser;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils.getMatcher;
import static org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils.monomial;
import static org.misha.domain.algebra.lie.polynomial.monomial.Occurrence.*;

/**
 * author: misha
 * Date: 3/23/14
 * Time: 1:05 PM
 * <p/>
 * A kind of binary tree. Lie monomial.
 */

public final class Monomial implements Serializable, Comparable<Monomial>, Cloneable {
    private static final Logger log = Logger.getLogger(Monomial.class);
    private static final String MUST_BE_A_LETTER = "The %s must be a letter having constant equals to 1.";
    private static final String LEFT_BRACKET = "[";
    private static final String RIGHT_BRACKET = "]";
    private static final String MINUS = "-";
    private static final String ISN_T_A_LETTER = "the monomial %s isn't a letter.";
    private static final char SHIFT = '\u0064';
    private static final String TOO_LARGE_VALUE = "too large value for type char: %d";
    private static final String CAN_T_DECODE = "can't decode the monomial '%s'.";
    private static final String CAN_NOT_BE_DECODED = "the character '%s' can not be decoded.";
    private static final String CAN_T_ENCODE = "can't encode the monomial '%s'.";
    private static final long serialVersionUID = -3920200348047233910L;
    private Monomial left;
    private Monomial right;
    private Monomial parent;
    private int constant = 1;
    private int deg = 1;
    private String name;
    private char symbol;
    private Long id;

    public Monomial() {
    }

    Monomial(final Monomial left, final Monomial right, final int constant) {
        this.left = left;
        this.right = right;
        if (left != null) {
            left.setParent(this);
            deg += left.deg;
        }
        if (right != null) {
            right.setParent(this);
            deg += right.deg;
        }
        this.constant = constant;
    }

    /**
     * CONSTANT IS INCLUDED!
     *
     * @param s a letter leaded with coefficient
     */
    Monomial(final String s) {
        right = null;
        left = null;
        name = s;
        deg = 1;
        final String letter = getMatcher(name).group(3);
        symbol = letter.charAt(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return letter without constant or throws exception
     */
    public char getSymbol() throws IllegalStateException {
        if (!hasNoChildren()) {
            throw new IllegalStateException(String.format(ISN_T_A_LETTER, this));
        }
        final String letter = getMatcher(name).group(3);
        symbol = letter.charAt(0);
        return symbol;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public Monomial getParent() {
        return parent;
    }

    void setParent(final Monomial monomial) {
        parent = monomial;
    }

    /**
     * @return deep copy of this
     */
    public Monomial copy() {
        return clone();
    }

    public int deg() {
        return left == null ? 1 : left.deg + right.deg;
    }

    public Monomial left() {
        return left;
    }

    public Monomial right() {
        return right;
    }

    /**
     * Checks if given monomial is a letter
     *
     * @return boolean
     */
    public boolean hasNoChildren() {
        return right == null && left == null;
    }

    /**
     * @return coefficient of this monomial
     */
    public int getConst() {
        return constant;
    }

    public void setConst(final int constant) {
        this.constant = constant;
    }

    /**
     * Checks correctness of the monomial in sense of Marshall Hall
     *
     * @return boolean
     */
    public boolean isCorrect() {
        return hasNoChildren() || left.isCorrect() && right.isCorrect() && left.compareTo(right) > 0 &&
                (left.hasNoChildren() || left.right().compareTo(right) <= 0);
    }

    /**
     * Checks the similarity of two monomials
     *
     * @param m a monomial
     * @return boolean
     */
    public boolean isSimilar(final Monomial m) {//takes 27.7% of time
        if (m.deg() == 1 && deg() == 1) {
            return Parser.getCore(name).equals(Parser.getCore(m.name));
        }
        final Monomial mCopy = m.copy();
        final Monomial thisCopy = copy();
        mCopy.setConst(1);
        thisCopy.setConst(1);
        return mCopy.equals(thisCopy);
    }

    /**
     * Compares two monomials
     *
     * @param m another monomial
     * @return int
     */
    @Override
    public int compareTo(@Nonnull final Monomial m) {
        if (m.deg() == 1 && deg() == 1) {
            final Monomial m1 = m.copy();
            final Monomial t1 = copy();
            m1.setConst(1);
            t1.setConst(1);
            return t1.name.compareTo(m1.name);
        }
        return deg() != m.deg() ? deg() - m.deg() : left.compareTo(m.left()) != 0 ? left.compareTo(m.left()) :
                                                    right.compareTo(m.right());
    }

    public String toString() {
        if (constant == 0) {
            return EMPTY;
        }
        if (hasNoChildren()) {
            return constant() + name;
        }
        return constant() + LEFT_BRACKET + left + ", " + right + RIGHT_BRACKET;
    }

    private String constant() {
        return constant == 1 ? EMPTY : constant == -1 ? MINUS : valueOf(constant);
    }

    /**
     * Reduces incorrectness (see isCorrect()) of this monomial in a single position.
     * <p/>
     * Monomial m is not correct when one of the next happens:
     * 1. m.left <= m.right
     * 2. m.left > m.right and m.left is not letter and m.left.right > m.right.
     * <p/>
     * The first case becomes slightly better by transformation:
     * m.left * m.right --> - m.right * m.left.
     * The second one does so by Jacobi identity:
     * (m.left.left * m.left.right) * m.right -->
     * (m.left.left * m.right) * m.left.right + m.left.left * (m.left.right * m.right).
     */
    public Polynomial hall() {
        final Polynomial result = new Polynomial();
        if (isCorrect()) {
            return result.plus(this);
        }
        final Monomial right = right().copy();
        final Monomial left = left().copy();
        if (left.isCorrect() && right.isCorrect()) {
            if (left.compareTo(right) < 0) {
                final Monomial answer = monomial(right, left);
                answer.setConst(-constant);
                return result.plus(answer);
            }
            if (left.compareTo(right) == 0) {
                return result;
            }
            if (!left.hasNoChildren()) {
                final Monomial leftRight = left.right();
                final Monomial leftLeft = left.left();
                final Monomial first = monomial(
                        monomial(leftLeft, right), leftRight
                ).times(constant);
                final Monomial second = monomial(
                        leftLeft, monomial(
                        leftRight, right
                        ).times(constant)
                );
                return result.plus(first).plus(second);
            }
            return result.plus(this);
        }
        return result.plus(left.hall().times(constant).times(right.hall()));
    }

    public Monomial times(final int c) {
        final Monomial result = copy();
        result.setConst(getConst() * c);
        return result;
    }

    public String getString() {
        final Matcher matcher = compile("(-|\\+)([0-9]*)(.*)").matcher(toString());
        return matcher.find() ? format(" %s %s%s", matcher.group(1), matcher.group(2), matcher.group(3)) :
               EMPTY;
    }

    public org.misha.domain.algebra.associative.Polynomial expand() {
        final Monomial copy = copy();
        org.misha.domain.algebra.associative.Polynomial result = new org.misha.domain.algebra.associative.Polynomial();
        if (hasNoChildren()) {
            result = result.plus(
                    org.misha.domain.algebra.associative.impl.Monomial.monomial(
                            "" + copy.getSymbol(), copy.getConst()
                    )
            );
        } else {
            result = result.plus(
                    left().copy().expand().times(right().copy().expand()).plus(
                            (right().copy().expand().times(
                                    left().copy().expand()
                            )
                            ).times(-1)
                    ).times(copy.getConst())
            );
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
        final Monomial monomial = (Monomial) o;
        if (constant != monomial.constant) {
            return false;
        }
        if (left != null) {
            if (!left.equals(monomial.left)) {
                return false;
            }
        } else if (monomial.left != null) {
            return false;
        }
        if (right != null) {
            if (!right.equals(monomial.right)) {
                return false;
            }
        } else if (monomial.right != null) {
            return false;
        }
        return !(monomial.deg() == 1 && deg() == 1) || monomial.name.equals(name) &&
                monomial.constant == constant;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + constant;
        return result;
    }

    void setLeft(final Monomial left) {
        this.left = left;
        if (left == null) {
            throw new IllegalStateException("left multiplier has to be not null.");
        }
        left.setParent(this);
    }

    void setRight(final Monomial right) {
        this.right = right;
        right.setParent(this);
    }

    /**
     * applicable only in the case when this monomial is pending
     *
     * @param node the child to add
     */
    public void addChild(final Monomial node) {
        if (left == null) {
            setLeft(node);
        } else {
            setRight(node);
        }
    }

    /**
     * Checks if one of children is null
     *
     * @return boolean
     */
    public boolean isPending() {
        return left == null || right == null;
    }

    private Monomial getRoot() {
        Monomial monomial = this;
        while (monomial.getParent() != null) {
            monomial = monomial.getParent();
        }
        return monomial;
    }

    private void normalize() {
        final int[] constant = {1};
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(final Monomial m) {
                if (Monomial.this != m) {
                    constant[0] *= m.copy().constant;
                    m.constant = 1;
                }
            }
        }.visit(this);
        this.constant *= constant[0];
    }

    /*
        todo ms: problem is here monomial has been changed!
        m is a current monomial while visiting some tree;
        letter is that letter instead of which it is needed to substitute substitution
    */
    public void substitute(final Monomial m, final Monomial letter, final Monomial substitution) {
        if (!letter.hasNoChildren() || letter.constant != 1) {
            throw new IllegalArgumentException(String.format(MUST_BE_A_LETTER, letter));
        }
        final Monomial subst = substitution.copy();
        final int constant = subst.copy().constant;
        final Monomial parent = m.parent;
        if (m.hasNoChildren() && m.isSimilar(letter)) {
            if (parent != null && !isNullLetter(parent)) {
                substituteWithParent(m, subst, parent.copy());
            } else {
                substituteWithoutParent(m, subst);
            }
            m.setConst(constant * m.constant);
        }
        m.getRoot().normalize();
    }

    public void substitute(final Monomial letter, final Monomial substitution) {
        final Monomial fake = monomial(Character.toString((char) (SHIFT + letter.getSymbol())));
        substitute(this, letter, fake);
        substitute(this, fake, substitution);
    }

    private boolean isNullLetter(final Monomial parent) {
        return parent.symbol == '\u0000';
    }

    private void substituteWithoutParent(final Monomial m, final Monomial subst) {
        if (subst.hasNoChildren()) {
            m.name = subst.name;
        } else {
            m.left = subst.left.clone();
            m.right = subst.right.clone();
            m.name = null;
        }
    }

    //todo ms: problem!
    private void substituteWithParent(final Monomial m, final Monomial subst, final Monomial parent) {
        if (parent.left.isSimilar(m)) {
            parent.setLeft(subst);
        }
        if (parent.right.isSimilar(m)) {
            parent.setRight(subst);
        }
    }

    /**
     * todo ms: problem!
     * IMPORTANT NOTE: This method changes this. So it may be more preferable to use
     * Polynomial.substitute() which clones original before substitution.
     * <p/>
     * Substitutes substitution instead of the letter specified. does so everywhere into this monomial
     *
     * @param letter       the letter instead of which the substitution will be substituted
     * @param substitution the monomial that will be substituted instead of the letter
     */
    public void subst(final Monomial letter, final Monomial substitution) {
        final Monomial fake = monomial(Character.toString((char) (SHIFT + letter.getSymbol())));
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(final Monomial m) {
                substitute(m, letter, fake);
            }
        }.visit(this);
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(final Monomial m) {
                substitute(m, fake, substitution);
            }
        }.visit(this);
        normalize();
    }

    public void subst(final Occurrence occurrence, final Monomial substitution) {
        final Monomial letter = occurrence.getMonomial();
        final Monomial fake = monomial(Character.toString((char) (SHIFT + letter.getSymbol())));
        substitute(this, letter, fake);
        substitute(this, fake, substitution);
    }

    /**
     * Checks if this monomial contains the letter specified
     *
     * @param letter a letter
     * @return boolean
     */
    public boolean contains(final Monomial letter) {
        final boolean[] result = {false};
        if (isSimilar(letter)) {
            return true;
        }
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(final Monomial m) {
                if (result[0]) {
                    return;
                }
                result[0] |= m.isSimilar(letter);
            }
        }.visit(this);
        return result[0];
    }

    private Monomial encodeLetter() throws IllegalArgumentException {
        if (!hasNoChildren()) {
            throw new IllegalArgumentException(String.format(CAN_T_ENCODE, this));
        }
        final Monomial result = monomial(Character.toString((char) (SHIFT + getSymbol())));
        if (SHIFT + getSymbol() > Character.MAX_VALUE) {
            throw new IllegalArgumentException(String.format(TOO_LARGE_VALUE, SHIFT + getSymbol()));
        }
        result.symbol = (char) (SHIFT + getSymbol());
        return result;
    }

    private Monomial decodeLetter(final Monomial letter) {
        if (!letter.hasNoChildren()) {
            throw new IllegalArgumentException(String.format(CAN_T_DECODE, letter));
        }
        if (letter.getSymbol() < SHIFT) {
            throw new IllegalStateException(
                    String.format(CAN_NOT_BE_DECODED, Character.toString(letter.getSymbol()))
            );
        }
        final Monomial result = monomial(
                Character.toString(
                        (char) (letter.getSymbol() - SHIFT)
                )
        );
        result.symbol = (char) (letter.getSymbol() - SHIFT);
        return result;
    }

    public Monomial encode() {
        final Monomial copy = copy();
        final Collection<Monomial> symbols = new TreeSet<Monomial>();
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(final Monomial m) {
                if (m.hasNoChildren()) {
                    symbols.add(monomial(Character.toString(m.getSymbol())));
                }
            }
        }.visit(copy);
        for (final Monomial symbol : symbols) {
            copy.subst(symbol, symbol.encodeLetter());
        }
        return copy;
    }

    public Monomial decode() {
        final Monomial copy = copy();
        final Collection<Monomial> symbols = new TreeSet<Monomial>();
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(final Monomial m) {
                if (m.hasNoChildren()) {
                    symbols.add(monomial(Character.toString(m.getSymbol())));
                }
            }
        }.visit(copy);
        for (final Monomial symbol : symbols) {
            copy.subst(symbol, decodeLetter(symbol));
        }
        return copy;
    }

    public Set<Monomial> letters() {
        final Set<Monomial> letters = new HashSet<Monomial>();
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(final Monomial m) {
                if (m.hasNoChildren()) {
                    letters.add(m);
                }
            }
        }.visit(this);
        return letters;
    }

    public Map<Character, Set<Occurrence>> occurrences() {
        final Map<Character, Set<Occurrence>> occurrences = new HashMap<Character, Set<Occurrence>>();
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(final Monomial m) {
                System.out.println("--------------------------------" + m + "--" + m.getParent());
                if (m.hasNoChildren()) {
                    final Character c = m.getSymbol();
                    final Occurrence occurrence = occurrence(m);
                    if(occurrences.containsKey(c)) {
                        occurrences.get(c).add(occurrence);
                    } else {
                        Set<Occurrence> set = new HashSet<Occurrence>();
                        occurrences.put(c, set);
                        set.add(occurrence);
                    }
                }
            }
        }.visit(this);
        return occurrences;
    }

    public Iterable<Monomial> actBy(final Endo endo) throws IllegalArgumentException, CloneNotSupportedException {
        final Endo fake = new Endo();
        int i = 0;
        Monomial firstLetter = null;
        for (final Monomial letter : letters()) {
            if (i == 0) {
                Polynomial polynomial = new Polynomial();
                polynomial = polynomial.plus(copy());
                fake.mapTo(letter, polynomial);
                firstLetter = letter;
            } else {
                fake.mapTo(letter, new Polynomial());
            }
            ++i;
        }
        if (firstLetter == null) {
            throw new IllegalStateException("first letter can't be null.");
        }
        return fake.times(endo).getAt(firstLetter);
    }

    @Override
    public Monomial clone() {
        Monomial clone = null;
        if (hasNoChildren()) {
            clone = monomial(Character.toString((getSymbol())));
            clone.name = name;
            clone.id = id;
        }
        try {
            clone = (Monomial) super.clone();
            clone.left = left == null ? null : left.clone();
            clone.right = right == null ? null : right.clone();
            clone.parent = parent == null ? null : monomial(left, right);
            clone.constant = constant;
            clone.deg = deg;
            clone.name = name;
            clone.symbol = symbol;
            clone.id = id;
            assert clone.equals(this) : toString() + " is not equals to " + clone.toString();
        } catch (final CloneNotSupportedException e) {
            log.error(e);
        }
        return clone;
    }
}



















