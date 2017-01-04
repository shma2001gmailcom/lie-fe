/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.domain.algebra.lie.dependency;

import org.junit.Test;
import org.misha.domain.algebra.lie.polynomial.monomial.Monomial;
import org.misha.domain.algebra.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils.monomial;

/**
 * Author: mshevelin
 * Date: 11/26/14
 * Time: 5:26 PM
 */

public class CheckerTest {

    @Test
    public void testLeadMonomialOfSubst() throws Exception {
        final Parser parser = new Parser();
        final Monomial m = monomial("a");
        assertEquals(
                new Checker().leadMonomialOfSubst(m, parser.parse("[x, y]"), parser.parse("[[x, y], y]")).toString(),
                "-[[y, x], y]"
        );
    }
}
