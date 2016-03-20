/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.domain.algebra.lie.endomorphism;

import org.misha.domain.algebra.lie.endomorphism.Tuple.Pair;
import org.misha.domain.algebra.lie.polynomial.Polynomial;
import org.misha.domain.algebra.lie.polynomial.monomial.Monomial;
import org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * author: misha
 * Date: 4/27/14
 * Time: 4:48 PM
 * <p/>
 * Background object for endomorphism
 */

final class Tuple implements Iterable<Pair<Monomial, Polynomial>>, Cloneable {
    private static final int LETTERS_COUNT = 651;
    private static final String MUST_BE_A_LETTER = "The %s must be a letter.";
    private static final String MAP_DOES_NOT_EXIST = "The map of %s does not exist.";
    private static final String MAP_VIEW = "%s --> %s";
    private final Collection<Pair<Monomial, Polynomial>> mappings = new TreeSet<Pair<Monomial, Polynomial>>();
    private final Set<Monomial> letters = new TreeSet<Monomial>();

    private boolean containsArgument(final Monomial argument) throws IllegalArgumentException {
        checkIfLetter(argument);
        for (final Pair<Monomial, Polynomial> pair : mappings) {
            if (pair.getArgument().equals(argument)) {
                return true;
            }
        }
        return false;
    }

    public void mapTo(final Monomial letter, final Polynomial p) throws IllegalArgumentException {
        checkIfLetter(letter);
        if (containsArgument(letter)) {
            for (final Pair<Monomial, Polynomial> pair : mappings) {
                if (pair.getArgument().equals(letter)) {
                    pair.setValue(p);
                }
            }
        } else {
            mappings.add(new Pair<Monomial, Polynomial>(letter.copy(), p.copy()));
        }
        addNewLetters();
    }

    private void addNewLetters() {
        for (final Pair<Monomial, Polynomial> pair : this) {
            for (char c = 1; c < LETTERS_COUNT; ++c) {
                final Monomial letter = MonomialUtils.monomial(Character.toString(c));
                if (pair.getValue().contains(letter)) {
                    letters.add(letter);
                }
            }
        }
    }

    public Iterable<Monomial> letters() {
        return Collections.unmodifiableSet(letters);
    }

    private void checkIfLetter(final Monomial letter) throws IllegalArgumentException {
        if (!letter.isLetter()) {
            throw new IllegalArgumentException(String.format(MUST_BE_A_LETTER, letter));
        }
    }

    public Polynomial getAt(final Monomial letter) throws IllegalArgumentException {
        checkIfLetter(letter);
        for (final Pair<Monomial, Polynomial> pair : mappings) {
            if (pair.getArgument().equals(letter)) {
                return pair.getValue();
            }
        }
        throw new IllegalStateException(String.format(MAP_DOES_NOT_EXIST, letter));
    }

    public int size() {
        return mappings.size();
    }

    @Override
    public String toString() {
        return "\n" + Arrays.toString(mappings.toArray()) + '\n';
    }

    @Override
    public Iterator<Pair<Monomial, Polynomial>> iterator() {
        return mappings.iterator();
    }

    /**
     * Both mappings must be a TreeSets!
     *
     * @param o an Object to compare
     * @return boolean
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Iterable pairs = (Iterable) o;
        final Iterator it = pairs.iterator();
        for (final Pair<Monomial, Polynomial> mapping : mappings) {
            if (!it.hasNext()) {
                return false;
            } else if (!mapping.equals(it.next())) {
                return false;
            }
        }
        return !it.hasNext();
    }

    @Override
    public int hashCode() {
        return mappings.hashCode();
    }

    @Override
    public Tuple clone() throws CloneNotSupportedException {
        final Tuple clone = (Tuple) super.clone();
        for (final Monomial letter : letters) {
            final Monomial letterClone = letter.clone();
            clone.letters.add(letterClone);
        }
        for (final Monomial letterClone : clone.letters) {
            clone.mapTo(letterClone, getAt(letterClone));
        }
        return clone;
    }

    public static class Pair<T extends Comparable<T>, S> implements Comparable<Pair<T, S>> {
        private final T argument;
        private S value;

        private Pair(final T argument, final S value) {
            this.argument = argument;
            this.value = value;
        }

        @Override
        public int compareTo(@Nonnull final Pair<T, S> o) {
            return argument.compareTo(o.argument);
        }

        public T getArgument() {
            return argument;
        }

        public S getValue() {
            return value;
        }

        public void setValue(final S value) {
            this.value = value;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final Pair pair = (Pair) o;
            return argument.equals(pair.argument) && value.equals(pair.value);
        }

        @Override
        public int hashCode() {
            int result = argument.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return String.format(MAP_VIEW, argument, value);
        }
    }
}
