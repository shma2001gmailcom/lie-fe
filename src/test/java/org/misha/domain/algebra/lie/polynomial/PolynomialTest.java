package org.misha.domain.algebra.lie.polynomial;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.misha.domain.algebra.lie.polynomial.monomial.Alphabet;
import org.misha.domain.algebra.lie.polynomial.monomial.Monomial;
import org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils;
import org.misha.domain.algebra.parser.Parser;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.misha.domain.algebra.lie.polynomial.Polynomial.mount;

/**
 * Author: mshevelin
 * Date: 3/24/14
 * Time: 6:17 PM
 */

public class PolynomialTest {
    private static final int C = -17;
    private static final Logger log = Logger.getLogger(PolynomialTest.class);
    private Monomial a;
    private Monomial b;
    private Monomial abb;
    private Polynomial p;

    @Before
    public void init() {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
        final Alphabet alphabet = Alphabet.getInstance("a", "b", "c");
        a = alphabet.get(0);
        b = alphabet.get(1);
        p = new Polynomial();
        abb = MonomialUtils.monomial(a, b, 2);
    }

    @Test
    public void testPlus() throws Exception {
        p = p.plus(a).plus(b).plus(b).plus(abb).plus(abb);
        assertEquals(p.toString(), "+ a + 2b + 2[[a, b], b]");
        assertEquals(p.getConstant(abb), 2);
        abb.setConst(-1);
        p = p.plus(abb);
        assertEquals(p.toString(), "+ a + 2b + [[a, b], b]");
        assertEquals(p.plus(p).toString(), "+ 2a + 4b + 2[[a, b], b]");
    }

    @Test
    public void testTimes() throws Exception {
        p = p.plus(a).plus(b).plus(b).plus(abb).plus(abb);
        p = p.times(a);
        assertEquals(p.toString(), "+ 2[b, a] + 2[[[a, b], b], a]");
        p = new Polynomial();
        p = p.plus(a).plus(b);
        Polynomial q = new Polynomial();
        q = q.plus(a).plus(b);
        q = q.times(q).hall();
        assertEquals(q.toString(), "");
        final Polynomial left = mount("+ a - 3[a, c]");
        final Polynomial right = mount("- x + 11[[a, b], x]");
        assertEquals(
                left.times(right).hall(), mount(
                "+ [x, a] - 3[[c, a], x] - 11[[x, a], [b, a]] + 11[[[b, a], a], x] + 33[[[b, a], x], [c, a]]"
        )
        );
    }

    @Test
    public void testSkewSymmetry() {
        assertEquals(MonomialUtils.monomial(a, b).hall().toString(), "- [b, a]");
        assertEquals(p.times(p).hall().toString(), "");
    }

    @Test
    public void testTimesConst() {
        p = new Polynomial();
        p = p.plus(a).plus(b.times(2)).plus(abb.times(C));
        assertEquals(p.toString(), "+ a + 2b - 17[[a, b], b]");
        Polynomial q = new Polynomial();
        abb.setConst(-1);
        a.setConst(-1);
        q = q.plus(a).plus(b).plus(abb);
        assertEquals(q.toString(), "- a + b - [[a, b], b]");
    }

    @Test
    public void testParse() {
        final Parser p = new Parser("- [[a, b], b] + a - 2b");
        assertEquals(p.parse().toString(), "+ a - 2b - [[a, b], b]");
        Polynomial r = new Polynomial();
        Polynomial q = new Polynomial();
        q = q.plus(mount("- 2a"));
        r = r.plus(mount("- a"));
        r = r.plus(mount("- a"));
        assertEquals(q.toString(), r.toString());
    }

    @Test
    public void testPlusAnother() {
        final Monomial x = MonomialUtils.monomial("x");
        Polynomial p = new Polynomial();
        p = p.plus(x).plus(x);
        p = p.plus(p);
        assertEquals(p, new Polynomial().plus(x.times(4)));
    }

    @Test
    public void testSubstitute() {
        final Polynomial p1 = mount(
                "+ a - 2[a, b] + 5[[a, b], b] - 3[[[a, b], b], b] + [[[[a, b], b], b], b]"
        );
        final Monomial a1 = MonomialUtils.monomial("a");
        final Polynomial s1 = mount("- a - 7[a, b]");
        assertEquals(
                p1.substitute(a1, s1), mount(
                "- a - 7[a, b] " +
                        "+ 2[a, b] + 14[[a, b], b] " +
                        "- 5[[a, b], b] - 35[[[a, b], b], b] " +
                        "+ 3[[[a, b], b], b] + 21[[[[a, b], b], b], b] " +
                        "- [[[[a, b], b], b], b] - 7[[[[[a, b], b], b], b], b]"
        )
        );
        final Polynomial p = mount(
                "+ 2a - 2[a, b] + 5[[a, b], b] - 3[[[a, b], b], b] + 13[[[[a, b], b], b], b]"
        );
        final Monomial a = MonomialUtils.monomial("a");
        final Polynomial s = mount("+ 11a - 7[a, b]");
        assertTrue(MonomialUtils.monomial("+2a").contains(a));
        assertEquals(
                p.substitute(a, s), mount(
                "+ 22a - 14[a, b] " +
                        "- 22[a, b] + 14[[a, b], b] " +
                        "+ 55[[a, b], b] - 35[[[a, b], b], b] " +
                        "- 33[[[a, b], b], b] + 21[[[[a, b], b], b], b] " +
                        "+ 143[[[[a, b], b], b], b] - 91[[[[[a, b], b], b], b], b]"
        )
        );
    }

    @Test
    public void decodeEncodeTest() {
        final Polynomial original = mount(
                "+ a - 2[a, b] + 5[[a, b], b] - 3[[[a, b], b], b] + [[[[a, b], b], b], b]"
        );
        final Polynomial encoded = original.encode();
        final Polynomial decoded = encoded.decode();
        assertEquals(decoded, original);
        assertEquals(decoded.encode(), encoded);
    }

    @Test
    public void exceptionTest() {
        try {
            mount("+ [z, [z, ssa[]]");
        } catch (final Exception e) {
            log.error(e);
        }
    }

    @Test
    public void findLettersTest() {
        final Polynomial p = mount(
                "+ a - 2[a, b] + 5[[c, x], Z] - 3[[[a, n], b], q] + [[[[a, v], b], h], j]"
        );
        final Collection<Monomial> expected = new HashSet<Monomial>();
        expected.add(MonomialUtils.monomial("a"));
        expected.add(MonomialUtils.monomial("b"));
        expected.add(MonomialUtils.monomial("c"));
        expected.add(MonomialUtils.monomial("x"));
        expected.add(MonomialUtils.monomial("Z"));
        expected.add(MonomialUtils.monomial("n"));
        expected.add(MonomialUtils.monomial("q"));
        expected.add(MonomialUtils.monomial("v"));
        expected.add(MonomialUtils.monomial("h"));
        expected.add(MonomialUtils.monomial("j"));
        assertTrue(p.letters().containsAll(expected) && expected.containsAll(p.letters()));
    }

    @Test
    public void cloneTest() {
        Polynomial p = mount(
                "+ a - 2[a, b] + 5[[c, x], Z] - 3[[[a, n], b], q] + [[[[a, v], b], h], j]"
        );
        final Polynomial clone = p.clone();
        assertEquals(p, clone);
        p = mount("+ x");
        assertFalse(p.equals(clone));
    }

    @Test
    public void testEquals() {
        final Polynomial p = mount(
                "+ a - 2[a, b] + 5[[c, x], Z] - 3[[[a, n], b], q] + [[[[a, v], b], h], j]"
        );
        final Polynomial p1 = mount(
                "+ a - 2[a, b] + 5[[c, x], Z] - 3[[[a, n], b], q] + [[[[a, v], b], h], j]"
        );
        assertEquals(p, p1);
    }
}
