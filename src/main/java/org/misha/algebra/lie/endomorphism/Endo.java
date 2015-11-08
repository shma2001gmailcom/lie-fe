/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.algebra.lie.endomorphism;

import org.apache.log4j.Logger;
import org.misha.algebra.lie.endomorphism.Tuple.Pair;
import org.misha.algebra.lie.polynomial.Polynomial;
import org.misha.algebra.lie.polynomial.monomial.Monomial;
import org.misha.domain.JacobiMatrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Author: mshevelin
 * Date: 5/13/14
 * Time: 4:20 PM
 * <p/>
 * Lie algebra endomorphism
 */

public final class Endo implements Iterable<Pair<Monomial, Polynomial>>, Cloneable {
    private Tuple tuple = new Tuple();
    private static final Logger log = Logger.getLogger(Endo.class);

    public void mapTo(final Monomial letter, final Polynomial polynomial) {
        try {
            tuple.mapTo(letter, polynomial.hall());
        } catch (final Throwable e) {
            log.trace(e, e);
        }
    }

    public Polynomial getAt(final Monomial letter) {
        return tuple.getAt(letter).copy().hall();
    }

    private Endo multiplySimple(final Endo e) {
        final Endo result = new Endo();
        for (final Pair<Monomial, Polynomial> pair : tuple) {
            Polynomial value = pair.getValue().hall();
            for (final Monomial letter : tuple.letters()) {
                value = value.substitute(letter, e.getAt(letter).hall());
            }
            result.mapTo(pair.getArgument(), value.hall());
        }
        return result;
    }

    private Endo encodeValues() {
        final Endo result = new Endo();
        for (final Pair<Monomial, Polynomial> pair : tuple) {
            result.mapTo(pair.getArgument(), pair.getValue().encode().hall());
        }
        return result;
    }

    private Endo encodeArguments() {
        final Endo result = new Endo();
        for (final Pair<Monomial, Polynomial> pair : tuple) {
            result.mapTo(pair.getArgument().encode(), pair.getValue().hall());
        }
        return result;
    }

    public Endo times(final Endo e) {
        final Endo result = new Endo();
        final Endo product = encodeValues().multiplySimple(e.encodeArguments());
        for (final Monomial letter : tuple.letters()) {
            result.mapTo(letter, copyClear(product, letter).hall());
        }
        return result;
    }

    private Polynomial copyClear(final Endo endo, final Monomial letter) {
        return endo.getAt(letter).clone().hall();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Endo endo = (Endo) o;
        return tuple.equals(endo.tuple);
    }

    @Override
    public int hashCode() {
        return tuple.hashCode();
    }

    @Override
    public String toString() {
        if (!tuple.iterator().hasNext()) {
            //throw new IllegalArgumentException("an endomorphism should contain at least one mapping");
            return "[]";
        }
        StringBuilder sb = new StringBuilder("(");
        for (final Pair<Monomial, Polynomial> pair : tuple) {
            sb = sb.append(pair.getValue()).append("; ");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        sb = sb.deleteCharAt(sb.length() - 1);
        sb = sb.append(")");
        return sb.toString();
    }

    public JacobiMatrix fox() {
        final JacobiMatrix result = new JacobiMatrix();
        final Collection<Character> characters = new ArrayList<Character>();
        for (final Pair<Monomial, Polynomial> pair : this) {
            characters.add(pair.getArgument().getSymbol());
        }
        for (final Pair<Monomial, Polynomial> pair : this) {
            result.add(getAt(pair.getArgument()).foxRelative(characters));
        }
        return result;
    }

    @Override
    public Iterator<Pair<Monomial, Polynomial>> iterator() {
        return tuple.iterator();
    }

    @Override
    public Endo clone() throws CloneNotSupportedException {
        final Endo clone = (Endo) super.clone();
        clone.tuple = tuple.clone();
        return clone;
    }
}
