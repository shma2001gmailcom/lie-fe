package org.misha.algebra.associative;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.misha.algebra.associative.PolynomialUtils.mount;

/**
 * Author: mshevelin
 * Date: 5/26/14
 * Time: 12:10 PM
 */

public class PolynomialTest {
    @Before
    public void init() {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
    }

    @Test
    public void testMount() throws Exception {
        assertEquals(mount("+ 2a + ab - 17abc - ab").toString(), "+ 2a - 17abc");
        assertEquals(mount("+ abb - bab - bab + bba").toString(), "+ abb - 2bab + bba");
    }

    @Test
    public void testExpand() throws Exception {
        final org.misha.algebra.lie.polynomial.Polynomial polynomial =
                org.misha.algebra.lie.polynomial.Polynomial.mount("+ [[a, b], b] - [[b, a], a]");
        assertEquals(polynomial.expand(), mount("- aab + 2aba + abb - baa - 2bab + bba"));
    }

    @Test
    public void testAbel() {
        final org.misha.algebra.lie.polynomial.Polynomial polynomial =
                org.misha.algebra.lie.polynomial.Polynomial.mount("+ [[a, b], b] - [[b, a], a]");
        assertEquals(polynomial.expand().abel().toString(), "0");
    }

    @Test
    public void testFox() {
        org.misha.algebra.lie.polynomial.Polynomial polynomial =
                org.misha.algebra.lie.polynomial.Polynomial.mount(
                        "+ [[[[b, a], a], a], c] - [[[[c, a], a], a], b]"
                );
        final Polynomial expanded = polynomial.expand();
        assertEquals(expanded.fox().toString(), "(0; + aaac; - aaab)");
        polynomial = org.misha.algebra.lie.polynomial.Polynomial.mount(
                "+ [[[[c, b], a], a], a] + [[[[b, a], a], a], c] - [[[[c, a], a], a], b]"
        );
        assertEquals(polynomial.expand().fox().toString(), "(0; 0; 0)");
    }

    @Test
    public void timesTest() {
        final Polynomial p = mount("+ ab - ba");
        final Polynomial q = mount("- c");
        assertTrue(q.times(p).equals(mount("- cab + cba")));
    }

    @Test
    public void testPlus() {
        final Polynomial ba = mount("- ba");
        final Polynomial ab = mount("+ ab");
        final Polynomial c = mount("- c");
        assertEquals(ab.plus(ba).plus(c), mount("-c + ab - ba"));
    }
}