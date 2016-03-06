package org.misha.algebra.associative.impl;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.misha.algebra.associative.impl.Monomial.monomial;

/**
 * Author: mshevelin
 * Date: 5/26/14
 * Time: 10:41 AM
 */

public class MonomialTest {
    @Test
    public void testMonomial() {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
        final Monomial abc = monomial("+ 30abc");
        assertEquals(abc.deg(), 3);
        assertEquals(abc.getConstant(), 3 * 10);
        assertEquals(abc.toString(), "+ 30abc ");
        final Monomial abcAnother = abc.unify();
        assertEquals(abcAnother.getConstant(), 1);
        assertFalse(abc == abcAnother);
        final Monomial product = abc.times(abcAnother);
        assertEquals(product.toString(), "+ 30abcabc ");
    }


}
