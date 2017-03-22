package org.misha.domain.algebra.lie.polynomial.monomial;

import org.junit.Test;

import static java.lang.System.identityHashCode;
import static org.junit.Assert.*;

/**
 * author: misha
 * date: 3/16/17.
 */
public class MonomialVisitorTest {
    @Test
    public void testVisit() throws Exception {
        Monomial c = new Monomial("c");
        Monomial a = new Monomial("a");
        Monomial b = new Monomial("b");
        Monomial m  = new Monomial(c, a, 1);
        Monomial k = new Monomial(m, a, 1);
        Monomial s = new Monomial(k, m, 1);
        System.out.println(s);
        new MonomialVisitor() {

            @Override
            protected void doSomethingWith(Monomial m) {
                System.out.println(m + "(" + identityHashCode(m) + ") parent: " + m.getParent() + "(" + identityHashCode(m) + ")");
            }
        }.visit(s);


    }

}