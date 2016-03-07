/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.algebra.lie.endomorphism;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.misha.algebra.lie.polynomial.monomial.Monomial;
import org.misha.algebra.lie.polynomial.monomial.MonomialUtils;
import org.misha.algebra.parser.Parser;
import org.misha.service.impl.EndoServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.misha.algebra.lie.polynomial.Polynomial.mount;

/**
 * Author: mshevelin
 * Date: 5/13/14
 * Time: 5:12 PM
 */

public class EndoTest {
    private Endo e;
    private Endo f;
    private Endo s;
    private Endo r;
    private Monomial a;
    private Monomial b;
    private Monomial c;

    @Before
    public void setUp() throws Exception {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
        e = new Endo();
        f = new Endo();
        r = new Endo();
        s = new Endo();
        a = MonomialUtils.monomial("a");
        b = MonomialUtils.monomial("b");
        c = MonomialUtils.monomial("c");
    }

    @Test
    public void testTimesLinear() throws Exception {
        e.mapTo(a, mount("+ a - b"));
        e.mapTo(b, mount("+ b + a"));
        e.mapTo(c, mount("+ c"));
        f.mapTo(a, mount("+ a + b"));
        f.mapTo(b, mount("- a + b"));
        f.mapTo(c, mount("+ c"));
        r.mapTo(a, mount("+ 2a"));
        r.mapTo(b, mount("+ 2b"));
        r.mapTo(c, mount("+ c"));
        assertEquals(e.times(f), r);
    }

    @Test
    public void testTimesCommon() throws Exception {
        e.mapTo(a, mount("+ a + [b, c]"));
        e.mapTo(b, mount("+ b"));
        e.mapTo(c, mount("+ c"));
        f.mapTo(a, mount("+ a"));
        f.mapTo(b, mount("+ b + [a, c]"));
        f.mapTo(c, mount("+ c"));
        r.mapTo(a, mount("+ a + [b, c] + [[a, c], c]"));
        r.mapTo(b, mount("+ b + [a, c]"));
        r.mapTo(c, mount("+ c"));
        assertEquals(e.times(f), r);
    }

    @Test
    public void testInvertElementary() throws CloneNotSupportedException {
        final Endo e_;
        final Endo e1_;
        final Endo e1;
        e = new Endo();
        e1 = new Endo();
        e_ = new Endo();
        e1_ = new Endo();
        r = new Endo();
        s = new Endo();
        e.mapTo(a, mount("+ a + [b, c]"));
        e.mapTo(b, mount("+ b"));
        e.mapTo(c, mount("+ c"));
        e_.mapTo(a, mount("+ a - [b, c]"));
        e_.mapTo(b, mount("+ b"));
        e_.mapTo(c, mount("+ c"));
        e1.mapTo(a, mount("+ a"));
        e1.mapTo(b, mount("+ b + [a, c]"));
        e1.mapTo(c, mount("+ c"));
        e1_.mapTo(a, mount("+ a"));
        e1_.mapTo(b, mount("+ b - [a, c]"));
        e1_.mapTo(c, mount("+ c"));
        s = e.times(e1);
        s = s.times(e_);
        s = s.times(e1_);
        r.mapTo(a, mount("+ a + [[a, c], c] - [[[b, c], c], c] + [[[[a, c], c], c], c]"));
        r.mapTo(b, mount("+ b - [[b, c], c] + [[[a, c], c], c]"));
        r.mapTo(c, mount("+ c"));
        assertEquals(s, r);
    }

    @Test
    public void testParseEndo() {
        final String s = "(+ a + [b, c]; + c - [a, b]; - b + [a, c])";
        assertEquals(new Parser().parseEndo(s).toString(), "(+ a - [c, b]; + c + [b, a]; - b - [c, a])");
    }

    @Test
    public void testMultiply() {
        final String input = "(- a - 2[b, c] + 2[c ,[a, c]]; + b + 2[a, c] - 2[c, [b, c]] + 2[c, [c, [a, " +
                "c]]]; + c) * (- a - 2[b, c] + 2[c ,[a, c]]; + b + 2[a, c] - 2[c, [b, c]] + 2[c, " +
                "[c, [a, c]]]; + c)";
        final String result = new EndoServiceImpl().getProductOf(input);
        assertEquals(result, "(+ a; + b; + c)");
    }

    @Test
    public void testJacobiMatrix() {
        e.mapTo(a, mount("-2a + [b, c]"));
        e.mapTo(b, mount("+ b"));
        e.mapTo(c, mount("+ c"));
        assertEquals(e.fox().toString(), "(-2; + c; - b)\n(0; 1; 0)\n(0; 0; 1)\n");
    }
}
