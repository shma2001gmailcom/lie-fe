package org.misha.domain.algebra.associative.reduction;

import org.junit.Before;
import org.junit.Test;
import org.misha.domain.algebra.associative.Polynomial;

import static org.junit.Assert.assertTrue;
import static org.misha.domain.algebra.associative.PolynomialUtils.mount;

/**
 * author: misha
 * date: 11/12/17
 * time: 2:07 PM
 */
public class MarkedPolynomialTest {
    private final Polynomial polynomial = mount("+x - xy + xyx - yxy");
    private MarkedPolynomial m;

    @Before
    public void init() {
        m = new MarkedPolynomial(polynomial);
        m.addMark("mark1", "mark1");
    }

    @Test
    public void mince() throws Exception {
        assertTrue(m.mince().equals(polynomial));
    }

    @Test
    public void addMark() throws Exception {
        assertTrue(m.getMark("mark1") != null);
    }

    @Test
    public void markedAs() throws Exception {
        assertTrue(m.markedAs("mark1"));
    }

    @Test
    public void getMark() throws Exception {
        assertTrue(m.getMark("mark1").equals("mark1"));
    }

    @Test
    public void removeMark() throws Exception {
        m.removeMark("mark1");
        assertTrue(m.getMark("mark1") == null);
    }

    @Test
    public void elder() throws Exception {
        assertTrue(m.elder().equals(polynomial.elder()));
    }

    @Test
    public void plus() throws Exception {
    }

    @Test
    public void times() throws Exception {
    }

    @Test
    public void times1() throws Exception {
    }

    @Test
    public void times2() throws Exception {
    }

    @Test
    public void plus1() throws Exception {
    }

    @Test
    public void equals() throws Exception {
    }

    @Test
    public void copy() throws Exception {
    }

    @Test
    public void compareTo() throws Exception {
    }

    @Test
    public void isZero() throws Exception {
    }

    @Test
    public void compareTo1() throws Exception {
    }
}