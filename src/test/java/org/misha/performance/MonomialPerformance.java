package org.misha.performance;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.misha.algebra.lie.polynomial.Polynomial;
import org.misha.algebra.lie.polynomial.monomial.Monomial;
import org.misha.algebra.lie.polynomial.monomial.MonomialUtils;

import java.util.Date;

import static org.misha.algebra.lie.polynomial.Polynomial.mount;

/**
 * Author: mshevelin
 * Date: 5/16/14
 * Time: 2:41 PM
 */

public final class MonomialPerformance {
    private static final Logger log = Logger.getLogger(MonomialPerformance.class);
    private static final int times = 8;

    private MonomialPerformance() {
    }

    public static void main(final String... args) {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
        final Monomial a = MonomialUtils.monomial("a");
        Polynomial result = mount("+[a, b]");
        for (int i = 0; i < times; ++i) {
            final long begin = new Date().getTime();
            result = result.substitute(a, result);
            log.debug("" + i + ": " + (new Date().getTime() - begin) + "ms, deg=" + (Math.pow(2, i) + 1));
        }
    }
}
