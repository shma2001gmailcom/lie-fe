/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.domain.algebra.lie.polynomial.monomial;

import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.misha.domain.algebra.lie.endomorphism.Endo;
import org.misha.domain.algebra.lie.polynomial.Polynomial;
import org.misha.domain.algebra.parser.Parser;
import org.misha.domain.algebra.parser.Summand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.misha.domain.algebra.lie.polynomial.Polynomial.mount;
import static org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils.monomial;

/**
 * author: misha
 * Date: 3/23/14
 * Time: 4:21 PM
 */

public class MonomialTest {
    private static final String SPACE = " ";
    private static final String PLUS = "+";
    private Monomial a;
    private Monomial b;
    private Monomial c;
    private Monomial ba;
    private Monomial ba_b_a;
    private Monomial ba_b_b_a;
    private Monomial cb;
    private Monomial ca;
    private Polynomial q;
    private  Monomial ba_b;

    public MonomialTest() {

    }

    @Before
    public void init() {
        ba_b = MonomialUtils.monomial(ba, b);
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
        q = new Polynomial();
        final Alphabet alphabet = Alphabet.getInstance("a", "b", "c");
        a = alphabet.get(0);
        b = alphabet.get(1);
        c = alphabet.get(2);
        ba = MonomialUtils.monomial(b, a);
        ca = MonomialUtils.monomial(c, a);
        cb = MonomialUtils.monomial(c, b);
        ba_b_a = MonomialUtils.monomial(MonomialUtils.monomial(MonomialUtils.monomial(b, a), b), a);
        ba_b_b_a = MonomialUtils.monomial(MonomialUtils.monomial(MonomialUtils.monomial(ba, b), b), a);
    }

    @Test
    public void testMonomial() throws Exception {
        final Monomial ab = MonomialUtils.monomial(a, b, 1);
        assertEquals(MonomialUtils.monomial(a, ab, 7).toString(),
                     "[[[[[[[a, [a, b]], [a, b]], [a, b]], [a, b]], [a, b]], [a, b]], [a, b]]"
        );
    }

    @Test
    public void testToString() throws Exception {
        testMonomial();
    }

    @Test
    public void testCopy() {
        final Monomial ac = a.copy();
        assertEquals(a, ac);
    }

    @Test
    public void testIsCorrect() {
        assertFalse(ba_b_a.isCorrect());
    }

    @Test
    public void testHall() {
        final Polynomial p;
        q = q.plus(ba_b_b_a);
        assertEquals(q.hall().toString(), "+ [[[b, a], b], [b, a]] + [[[[b, a], a], b], b]");
        p = mount("+ [[[[[b, a], a], [b, a]], b], b]");
        assertEquals(p.hall().toString(), "+ 2[[[[b, a], a], b], [[b, a], b]] " +
                             "- [[[[b, a], b], b], [[b, a], a]] " +
                             "+ [[[[[b, a], a], b], b], [b, a]]"
        );
        Parser parser = new Parser("+[[[[c, b], a], a], a]");
        assertEquals(parser.parse().hall().toString(), "- 3[[[b, a], a], [c, a]] " +
                             "+ 3[[[c, a], a], [b, a]] " +
                             "- [[[[b, a], a], a], c] " +
                             "+ [[[[c, a], a], a], b]"
        );
        parser = new Parser("+[[[c, b], a], a]");
        assertEquals(parser.parse().hall().toString(), "+ 2[[c, a], [b, a]] " +
                             "- [[[b, a], a], c] " +
                             "+ [[[c, a], a], b]"
        );
        parser = new Parser("+[[c, b], a]");
        assertEquals(parser.parse().hall().toString(), "- [[b, a], c] + [[c, a], b]");
    }

    @Test
    public void testVisitor() {
        a.setConst(1);
        b.setConst(1);
        ba_b_b_a.subst(a, new Parser("+[b, a]").parse().iterator().next());
        assertEquals(ba_b_b_a.toString(), "[[[[b, [b, a]], b], b], [b, a]]");
        ba.subst(a, mount("+ 7a").iterator().next());
    }

    @Test
    public void testTest() {
        assertTrue(ba_b.isCorrect());
    }

    @Test
    public void testSummands() {
        String s = "- 2a + 3[a, b] - 16[[a, b], b]";
        final Collection<Summand> result = new ArrayList<Summand>();
        String pattern = "(-|\\+)([0-9 ]*)([a-zA-Z ,\\Q[\\E\\Q]\\E]+)(-|\\+)";
        s = findSummands(pattern, s, result);
        pattern = "(-|\\+)([0-9 ]*)([a-zA-Z ,\\Q[\\E\\Q]\\E]+)";
        findSummands(pattern, s, result);
    }

    private String findSummands(final String pattern, String s, final Collection<Summand> result) {
        Matcher matcher;
        String core;
        String constant;
        while (StringUtils.isNotEmpty(s)) {
            matcher = compile(pattern).matcher(s);
            if (matcher.find()) {
                core = matcher.group(3);
                constant = matcher.group(1) + matcher.group(2);
                s = StringUtils.removeStart(s, constant + core);
                constant = StringUtils.replace(constant, SPACE, StringUtils.EMPTY);
                constant = StringUtils.removeStart(constant, PLUS);
                final Summand summand = new Summand(Integer.parseInt(StringUtils.remove(constant, SPACE)), core);
                result.add(summand);
            } else {
                break;
            }
        }
        return s;
    }

    @Test
    public void testCompareTo() {
        final Monomial cba = MonomialUtils.monomial(cb, a);
        final Monomial cab = MonomialUtils.monomial(ca, b);
        final Monomial bac = MonomialUtils.monomial(ba, c);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(c) < 0);
        assertTrue(cb.compareTo(ca) > 0);
        assertTrue(cb.compareTo(ba) > 0);
        assertTrue(cba.compareTo(cab) > 0);
        assertTrue(cba.compareTo(bac) > 0);
        Assert.assertTrue(
                MonomialUtils.monomial("- 2[[z, x], y]").compareTo(MonomialUtils.monomial("- [[z, y], [z, x]]")) < 0
        );
    }

    @Test
    public void testSymbol() {
        final Monomial one = MonomialUtils.monomial("-3x");
        final Monomial two = MonomialUtils.monomial("+ 13y");
        final Monomial three = MonomialUtils.monomial("123z");
        assertEquals(one.getSymbol(), 'x');
        assertEquals(two.getSymbol(), 'y');
        assertEquals(three.getSymbol(), 'z');
    }

    @Test
    public void fakeDeFakeTest() {
        final Monomial monomial = new Parser().parse("[a, b]");
        final Monomial faked = monomial.encode();
        assertEquals(faked.decode(), monomial);
    }

    @Test
    public void findLettersTest() {
        final Monomial monomial = new Parser().parse("[[[a, b], x],[c, b]]");
        assertEquals(Arrays.asList(monomial.letters().toArray()).toString(), "[a, b, c, x]");
    }

    @Test
    public void testActBy() throws CloneNotSupportedException, IllegalArgumentException {
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

    @Test
    public void cloneTest() {
        Monomial monomial = new Parser().parse("[[[x, y], y], [x, z]]");
        final Monomial copy = monomial.clone();
        assertEquals(monomial, copy);
        monomial = new Parser().parse("[x, a]");
        assertFalse(monomial.equals(copy));
    }

    @Test
    public void getConstantTest() {
        final Monomial monomial = new Parser().parse("[x, a]");
        monomial.setConst(3);
        assertEquals(monomial.getConst(), 3);
        Monomial copy = monomial.copy();
        assertEquals(monomial.getConst(), 3);
        copy = copy.times(100);
        assertEquals(copy.getConst(), 2 * 2 * 3 * 5 * 5);
        assertEquals(monomial.getConst(), 3);
    }

    @Test
    public void testIsRoot() throws Exception {
        final Monomial left = monomial("x");
        final Monomial right = monomial("z");
        final Monomial product = monomial(left, right);
        assertTrue(right.getSymbol() == 'z');
        assertFalse(left.isRoot());
        assertTrue(product.isRoot());
        assertTrue(left.getParent().equals(product));
        assertTrue(product.left().equals(left));
    }
}
