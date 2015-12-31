package org.misha.algebra.lie.polynomial.monomial;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Author: mshevelin
 * Date: 11/27/14
 * Time: 1:13 PM
 */

public class MonomialSequence {
    private final Collection<Monomial> sequence = new TreeSet<Monomial>();
    private final Monomial lastLetter;

    public MonomialSequence(final String... rawAlphabet) {
        Monomial mLetter = null;
        for (final String letter : rawAlphabet) {
            if (letter.length() == 1) {
                mLetter = MonomialUtils.monomial(letter);
                sequence.add(mLetter);
            }
        }
        lastLetter = mLetter;
    }

    public MonomialSequence(final Monomial... alphabet) {
        Monomial currentLetter = null;
        for (final Monomial letter : alphabet) {
            sequence.add(letter);
            currentLetter = letter;
        }
        lastLetter = currentLetter;
    }

    public Monomial getNextMonomial(final Monomial m) {
        if (!m.isCorrect()) {
            throw new IllegalArgumentException("The monomial '" + m + "' must be correct.");
        }
        Monomial product;
        for (final Monomial left : sequence) {
            for (final Monomial right : sequence) {
                product = MonomialUtils.monomial(left, right);
                if (product.isCorrect() && product.compareTo(m) > 0) {
                    sequence.add(product);
                    return product.copy();
                }
            }
        }
        return null;
    }

    public Monomial getNextDbMonomial(final Monomial m) {
        if (!m.isCorrect()) {
            throw new IllegalArgumentException("The monomial '" + m + "' must be correct.");
        }
        Monomial product;
        for (final Monomial left : sequence) {
            for (final Monomial right : sequence) {
                product = MonomialUtils.monomial(left, right);
                if (product.isCorrect() && product.compareTo(m) > 0) {
                    sequence.add(product);
                    return product;
                }
            }
        }
        return null;
    }

    public Monomial getLastLetter() {
        return lastLetter.copy();
    }

    public static void main(String... args) {
        final Monomial[] alphabet = new Monomial[3];
        final BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
        MonomialService monomialService = (MonomialService) factory.getBean("monomialService");
        for (int i = 0; i < 3; ++i) {
            alphabet[i] = monomialService.findBySmallId((long) i + 1);
        }
        final MonomialSequence monomialSequence = new MonomialSequence(alphabet);
        int i = 0;
        Monomial monomial = monomialSequence.getNextDbMonomial(alphabet[2]);
        while (i < 1000) {
            monomial.setId(monomialService.write(monomial));
            monomial = monomialSequence.getNextDbMonomial(monomial);
            ++i;
        }
    }
}
