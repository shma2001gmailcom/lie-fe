package org.misha.domain.algebra.associative;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.misha.domain.algebra.associative.impl.RationalMonomial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.misha.domain.algebra.associative.PolynomialUtils.mount;

/**
 * Author: mshevelin
 * Date: 8/27/14
 * Time: 10:17 AM
 */

public class RationalPolynomialTest {

    @Before
    public void init() {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
    }

    @Test
    public void testPattern() {
        testPattern("(\\+|\\-)([ \\d]*)(/*)([ \\d]*)([ a-zA-Z]*)", "- 15/22xya", "-", "15", "/", "22", "xya");
        testPattern(
                "(\\+|\\-)([ \\d]*)(/*)([ \\d]*)([ a-zA-Z]*)", "- 15 / 22 xya ", "-", "15", "/", "22", "xya"
        );
        testPattern("(\\+|\\-)([ \\d]*)(/*)([ \\d]*)([ a-zA-Z]*)", "+2x", "+", "2", "", "", "x");
        testPattern(
                "(\\+|\\-)([ \\d]*)(/*)([ \\d]*)([ a-zA-Z]*)(\\+|\\-)", "-15/22xya -", "-", "15", "/", "22",
                "xya", "-"
        );
        testPattern(
                "(\\+|\\-)([ \\d]*)(/*)([ \\d]*)([ a-zA-Z]*)(\\+|\\-)", "+ 2xU -", "+", "2", "", "", "xU", "-"
        );
    }

    @Test
    public void testMount() throws Exception {
        assertEquals(
                RationalPolynomial.mount("+ 2/3a + 7/8ab - 17abc - 13/11ab").toString(),
                "+ 2/3a - 27/88ab - 17abc"
        );
    }

    @Test
    public void testIsSimilar() {
        final RationalMonomial r = RationalMonomial.rationalMonomial("+1/2ab");
        final RationalMonomial s = RationalMonomial.rationalMonomial("-1/2ab");
        assertTrue(r.isSimilar(s));
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

    private void testPattern(final String regexp, final CharSequence toCheck, final String... pieces) {
        int groups = 0;
        final char[] chars = regexp.toCharArray();
        for (final char c : chars) {
            if (c == '(' || c == ')') {
                ++groups;
            }
        }
        groups /= 2;
        final Pattern pattern = Pattern.compile(regexp);
        final Matcher matcher = pattern.matcher(toCheck);
        assertTrue(matcher.matches());
        for (int i = 0; i < groups; ++i) {
            assertEquals(matcher.group(i + 1).trim(), pieces[i]);
        }
    }

    @Test
    public void testEquals() {
        final RationalMonomial monomial = RationalMonomial.rationalMonomial("1/3abc");
        final RationalMonomial another = RationalMonomial.rationalMonomial("1/3abc");
        assertEquals(monomial, another);
        final RationalPolynomial left = RationalPolynomial.mount("+ 2/3a + 7/8ab - 17abc - 13/11ab");
        final RationalPolynomial right = RationalPolynomial.mount("+ 2/3a + 7/8ab - 17abc - 13/11ab");
        assertEquals(left, right);
    }

    @Test
    public void testClone() {
        final RationalPolynomial orig = RationalPolynomial.mount("+ 2/3a + 7/8ab - 17abc - 13/11ab");
        final RationalPolynomial clone = orig.clone();
        assertEquals(orig, clone);
    }
}
