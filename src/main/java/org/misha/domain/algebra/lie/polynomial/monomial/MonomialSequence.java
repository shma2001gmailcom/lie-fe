package org.misha.domain.algebra.lie.polynomial.monomial;

import org.apache.log4j.Logger;
import org.misha.repository.MonomialService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * Author: mshevelin
 * Date: 11/27/14
 * Time: 1:13 PM
 */

class MonomialSequence {
    private static final Logger log = Logger.getLogger(MonomialSequence.class);
    private final Collection<Monomial> sequence = new TreeSet<Monomial>();
    private Monomial lastMonomial;

    MonomialSequence(final String... rawAlphabet) {
        Monomial mLetter = null;
        for (final String letter : rawAlphabet) {
            if (letter.length() == 1) {
                mLetter = MonomialUtils.monomial(letter);
                sequence.add(mLetter);
            }
        }
        lastMonomial = mLetter;
    }

    private MonomialSequence(final Monomial... alphabet) {
        Monomial currentLetter = null;
        for (final Monomial letter : alphabet) {
            sequence.add(letter);
            currentLetter = letter;
        }
        lastMonomial = currentLetter;
    }

    Monomial getNextMonomial(final Monomial m) {
        if (!m.isCorrect()) {
            throw new IllegalArgumentException("The monomial '" + m + "' must be correct.");
        }
        Monomial product;
        final Set<Monomial> lefts = new TreeSet<Monomial>(sequence);
        final Set<Monomial> rights = new TreeSet<Monomial>(sequence);
        for (final Monomial left : lefts) {
            for (final Monomial right : rights) {
                product = MonomialUtils.monomial(left, right);
                if (product.isCorrect() && product.compareTo(m) > 0) {
                    sequence.add(product);
                    return product;
                }
            }
        }
        return null;
    }

    private int getNextDbMonomial(final MonomialService monomialService) {
        int result = 0;
        Monomial product;
        final Set<Monomial> lefts = new TreeSet<Monomial>(sequence);
        final Set<Monomial> rights = new TreeSet<Monomial>(sequence);
        for (final Monomial left : lefts) {
            for (final Monomial right : rights) {
                product = MonomialUtils.monomial(left, right);
                if (product.isCorrect() && left.compareTo(right) > 0 && sequence.add(product)) {
                    product.setId(monomialService.write(product));
                    lastMonomial = product;
                    result = product.deg() > result ? result : product.deg();
                }
            }
        }
        return result;
    }

    Monomial getLastMonomial() {
        return lastMonomial.copy();
    }

    public static void main(String... args) {
        final BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
        final MonomialService monomialService = (MonomialService) factory.getBean("monomialService");
        final Monomial[] alphabet = new Monomial[3];
        for (int i = 0; i < 3; ++i) {
            alphabet[i] = monomialService.findBySmallId((long) i + 1);
        }
        int i = 0;
        for ( ; i < 10;  i = new MonomialSequence(alphabet).getNextDbMonomial(monomialService), ++i) ;
        log.debug(i);
    }
}
