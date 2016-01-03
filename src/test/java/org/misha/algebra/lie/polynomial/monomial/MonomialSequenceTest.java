package org.misha.algebra.lie.polynomial.monomial;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: mshevelin
 * Date: 11/27/14
 * Time: 1:34 PM
 */

public class MonomialSequenceTest {
    private static final Logger log = Logger.getLogger(MonomialSequence.class);
    private static final int nineteen = 19;
    private static final int fifty_nine = 59;

    @Test
    public void testGetNextMonomial() {
        assertEquals(getMonomial(5, "x", "y").toString(), "[[[y, x], x], y]");
        assertEquals(getMonomial(6, "x", "y", "z").toString(), "[[y, x], z]");
        assertEquals(getMonomial(3 * 3, "x", "y", "z", "t").toString(), "[[x, t], y]");
        assertEquals(getMonomial(nineteen * 2, "x", "y", "z", "t").toString(), "[[[x, t], t], [z, x]]");
        assertEquals(getMonomial(fifty_nine, "x", "y", "z", "t").toString(), "[[[y, t], x], [[x, t], z]]");
        assertEquals(
                getMonomial(5 * 2 << 3, "x", "y", "z", "t").toString(), "[[[y, t], [x, t]], [[x, t], t]]"
        );
    }

    private Monomial getMonomial(final int number, final String... alphabet) {
        final MonomialSequence sequence = new MonomialSequence(alphabet);
        Monomial current = sequence.getLastMonomial();
        for (int i = 0; i < number; ++i) {
            current = sequence.getNextMonomial(current);
            log.debug(current.toString()+'\n');
        }
        return current;
    }
}
