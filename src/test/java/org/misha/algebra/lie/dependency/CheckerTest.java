/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.algebra.lie.dependency;

import org.junit.Test;
import org.misha.algebra.lie.endomorphism.Endo;
import org.misha.algebra.lie.polynomial.Polynomial;
import org.misha.algebra.lie.polynomial.monomial.Monomial;
import org.misha.algebra.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.misha.algebra.lie.polynomial.Polynomial.mount;
import static org.misha.algebra.lie.polynomial.monomial.MonomialUtils.monomial;

/**
 * Author: mshevelin
 * Date: 11/26/14
 * Time: 5:26 PM
 */

public class CheckerTest {

    @Test
    public void testLeadMonomialOfSubst() throws Exception {
        final Parser parser = new Parser("");
        final Monomial m = monomial("a");
        assertEquals(
                new Checker().leadMonomialOfSubst(
                        m, parser.parse("[x, y]"), parser.parse(
                        "[[x, y], y]"
                )
                ).toString(), "-[[y, x], y]"
        );
    }

    @Test
    public void testActBy() throws CloneNotSupportedException {
        final Polynomial polynomial = mount("+ x - [x,y] + 2[[x, z], y]");
        final Endo endo = new Endo();
        endo.mapTo(monomial("x"), mount("+ x + [y, z]"));
        endo.mapTo(monomial("y"), mount("+ y + [x, z]"));
        endo.mapTo(monomial("z"), mount("+ z + [x, y]"));
        final Polynomial expected = mount("+ x + [y, x] - [z, y] - [[z, x], x] " +
                                                  "- 2[[z, x], y] + [[z, y], y] " +
                                                  "- [[z, y], [z, x]] + 2[[[y, x], x], y] " +
                                                  "- 2[[[y, x], x], [z, x]] - 2[[[y, x], y], [z, y]] " +
                                                  "- 2[[[z, y], y], z] + 2[[[z, y], y], [y, x]] " +
                                                  "+ 2[[[z, y], z], [z, x]] - 2[[[z, y], [y, x]], [z, x]]"
        );
        final Polynomial actual = polynomial.actBy(endo);
        for (final Monomial m : actual) {
            assertTrue(expected.contains(m));
        }
        for (final Monomial m : expected) {
            assertTrue(actual.contains(m));
        }
    }
}
