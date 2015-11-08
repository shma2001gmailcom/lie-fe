/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.algebra.lie.endomorphism;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.misha.algebra.lie.polynomial.Polynomial;
import org.misha.algebra.lie.polynomial.monomial.Monomial;
import org.misha.algebra.lie.polynomial.monomial.MonomialUtils;

import static org.junit.Assert.assertEquals;
import static org.misha.algebra.lie.polynomial.Polynomial.mount;

/**
 * author: misha
 * Date: 5/3/14
 * Time: 9:00 PM
 */

public class TupleTest {
    private static Tuple tuple;
    private static Monomial a;
    private static Monomial b;
    private static Monomial c;
    private static final Logger log = Logger.getLogger(TupleTest.class);

    @BeforeClass
    public static void init() {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
        tuple = new Tuple();
        a = MonomialUtils.monomial("a");
        b = MonomialUtils.monomial("b");
        c = MonomialUtils.monomial("c");
    }

    @Test
    public void testMapTo() throws Exception {
        tuple.mapTo(a, mount("+ a + [b, c]"));
        tuple.mapTo(b, mount("+ b"));
        tuple.mapTo(c, mount("+ c"));
        assertEquals(tuple.toString(), "\n[a --> + a + [b, c], b --> + b, c --> + c]\n");
    }

    @Test
    public void testGetAt() throws Exception {
        tuple.mapTo(a, mount("+ a + [b, c]"));
        tuple.mapTo(b, mount("+ b"));
        tuple.mapTo(c, mount("+ c"));
        assertEquals(tuple.getAt(a), mount("+ a + [b, c]"));
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(tuple.size(), 3);
    }

    @Test
    public void testTupleSubst() {
        log.debug("");
        final Tuple tuple1 = new Tuple();
        tuple1.mapTo(a, mount("+a + [b, c]"));
        tuple1.mapTo(b, mount("+b"));
        tuple1.mapTo(c, mount("+c"));
        tuple1.mapTo(
                a, tuple1.getAt(a).substitute(a, mount("+ a - [b, c]")).substitute(b, mount("+b")).substitute(
                c, mount("+c")
        )
        );
        assertEquals(tuple1.toString(), "\n[a --> + a, b --> + b, c --> + c]\n");
        tuple1.mapTo(a, mount("+a + [b, c]"));
        tuple1.mapTo(b, mount("+b"));
        tuple1.mapTo(c, mount("+c"));
        final Polynomial p = tuple1.getAt(a).substitute(a, mount("+ a + [b, c]"));
        final Polynomial p1 = p.substitute(b, mount("+b"));
        tuple1.mapTo(
                a, tuple1.getAt(a).substitute(a, mount("+ a + [b, c]")).substitute(b, mount("+b")).substitute(
                c, mount("+c")
        )
        );
        tuple1.mapTo(
                b, tuple1.getAt(b).substitute(a, mount("+ a + [b, c]")).substitute(b, mount("+b")).substitute(
                c, mount("+c")
        )
        );
        tuple1.mapTo(
                c, tuple1.getAt(c).substitute(a, mount("+ a + [b, c]")).substitute(b, mount("+b")).substitute(
                c, mount("+c")
        )
        );
        assertEquals(tuple1.toString(), "\n[a --> + a + 2[b, c], b --> + b, c --> + c]\n");
        tuple1.mapTo(a, mount("+ a + [b, c]"));
    }
}
