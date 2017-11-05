package org.misha.domain.algebra.lie.polynomial.monomial;

import org.junit.Test;


/**
 * author: misha
 * date: 3/15/17.
 */
public class OccurrenceTest {

    @Test
    public void occurrence() throws Exception {
        Monomial c = new Monomial("c");
        Monomial a = new Monomial("a");
        Monomial b = new Monomial("b");
        Monomial m  = new Monomial(c, a, 1);
        m = new Monomial(m, a, 1);
        Monomial n = new Monomial(c, b, 1);
        for(Occurrence o : m.occurrences().get('a')) {
           m.substitute(o.getMonomial(), n);
        }
    }

}