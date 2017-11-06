package org.misha.domain.algebra.associative;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.misha.domain.algebra.associative.PolynomialUtils.mount;
import static org.misha.domain.algebra.associative.impl.Monomial.monomial;

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
        final org.misha.domain.algebra.lie.polynomial.Polynomial polynomial =
                org.misha.domain.algebra.lie.polynomial.Polynomial.mount("+ [[a, b], b] - [[b, a], a]");
        assertEquals(polynomial.expand(), mount("- aab + 2aba + abb - baa - 2bab + bba"));
    }

    @Test
    public void testAbel() {
        final org.misha.domain.algebra.lie.polynomial.Polynomial polynomial =
                org.misha.domain.algebra.lie.polynomial.Polynomial.mount("+ [[a, b], b] - [[b, a], a]");
        assertEquals(polynomial.expand().abel().toString(), "0");
    }

    @Test
    public void testFox() {
        org.misha.domain.algebra.lie.polynomial.Polynomial polynomial =
                org.misha.domain.algebra.lie.polynomial.Polynomial.mount(
                        "+ [[[[b, a], a], a], c] - [[[[c, a], a], a], b]"
                );
        final Polynomial expanded = polynomial.expand();
        assertEquals(expanded.fox().toString(), "(0; + aaac; - aaab)");
        polynomial = org.misha.domain.algebra.lie.polynomial.Polynomial.mount(
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

    @Test
    public void elder() {
        final Polynomial p = mount("-x + y + xy - xyx + yxy - xxy + yyx");
        assertTrue(p.elder().equals(monomial("+1yyx")));
        assertTrue(p.elder().getConst() == 1);
    }
}