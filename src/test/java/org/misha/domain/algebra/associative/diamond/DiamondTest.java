package org.misha.domain.algebra.associative.diamond;

import org.junit.Test;
import org.misha.domain.algebra.associative.impl.Monomial;

import static org.junit.Assert.assertEquals;
import static org.misha.domain.algebra.associative.impl.Monomial.monomial;

/**
 * author: misha
 * date: 11/4/17
 * time: 9:39 PM
 */
public class DiamondTest {

    @Test
    public void diamond() {
        Monomial l = monomial("");
        Monomial r = monomial("");
        assertEquals(new Diamond(l, r).find().head(), l);
        assertEquals(new Diamond(l, r).find().middle(), l);
        assertEquals(new Diamond(l, r).find().tail(), l);
        l = monomial("+1xyz");
        r = monomial("+1yz");
        assertEquals(new Diamond(l, r).find().head(), monomial("+1x"));
        assertEquals(new Diamond(l, r).find().middle(), monomial("+1yz"));
        assertEquals(new Diamond(l, r).find().tail(), monomial("+1"));
        l = monomial("+1aaxyz");
        r = monomial("+1yzt");
        assertEquals(new Diamond(l, r).find().head(), monomial("+1aax"));
        assertEquals(new Diamond(l, r).find().middle(), monomial("+1yz"));
        assertEquals(new Diamond(l, r).find().tail(), monomial("+1t"));
        l = monomial("+1xyz");
        r = monomial("+1yztaa");
        assertEquals(new Diamond(l, r).find().head(), monomial("+1x"));
        assertEquals(new Diamond(l, r).find().middle(), monomial("+1yz"));
        assertEquals(new Diamond(l, r).find().tail(), monomial("+1taa"));
        l = monomial("+1uvxyzyzz");
        r = monomial("+1yzyzztaa");
        assertEquals(new Diamond(l, r).find().head(), monomial("+1uvx"));
        assertEquals(new Diamond(l, r).find().middle(), monomial("+1yzyzz"));
        assertEquals(new Diamond(l, r).find().tail(), monomial("+1taa"));
    }

}