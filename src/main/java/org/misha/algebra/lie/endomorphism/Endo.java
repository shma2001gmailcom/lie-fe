package org.misha.algebra.lie.endomorphism;

import org.apache.log4j.Logger;
import org.misha.algebra.lie.polynomial.Polynomial;
import org.misha.algebra.lie.polynomial.monomial.Monomial;
import org.misha.domain.JacobiMatrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.misha.algebra.lie.polynomial.Polynomial.mount;

/**
 * Author: mshevelin
 * Date: 5/13/14
 * Time: 4:20 PM
 * <p/>
 * Lie algebra endomorphism
 */

public final class Endo implements Iterable<Tuple.Pair<Monomial, Polynomial>> {
    private final Tuple tuple = new Tuple();
    private static final Logger log = Logger.getLogger(Endo.class);

    public void mapTo(final Monomial letter, final Polynomial polynomial) {
        try {
            tuple.mapTo(letter, polynomial.hall());
        } catch (Throwable e) {
            log.trace(e, e);
        }
    }

    public Polynomial getAt(final Monomial letter) {
        return tuple.getAt(letter).hall();
    }

    private Endo multiplySimple(final Endo e) {
        final Endo result = new Endo();
        for (final Tuple.Pair<Monomial, Polynomial> pair : tuple) {
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
        for (final Tuple.Pair<Monomial, Polynomial> pair : tuple) {
            result.mapTo(pair.getArgument(), pair.getValue().encode().hall());
        }
        return result;
    }

    private Endo encodeArguments() {
        final Endo result = new Endo();
        for (final Tuple.Pair<Monomial, Polynomial> pair : tuple) {
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

    /*
    todo: ought to be done some else way
     */
    private Polynomial copyClear(final Endo endo, final Monomial letter) {
        return mount(endo.getAt(letter).toString()).hall();
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
        for (final Tuple.Pair<Monomial, Polynomial> pair : tuple) {
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
        for (final Tuple.Pair<Monomial, Polynomial> pair : this) {
            characters.add(pair.getArgument().getSymbol());
        }
        for (final Tuple.Pair<Monomial, Polynomial> pair : this) {
            result.add(getAt(pair.getArgument()).foxRelative(characters));
        }
        return result;
    }

    @Override
    public Iterator<Tuple.Pair<Monomial, Polynomial>> iterator() {
        return tuple.iterator();
    }
}
