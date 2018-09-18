/*
 * Copyright (c) 2014. Misha's property, all rights reserved.
 */

package org.misha.domain.algebra.permutation;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.misha.domain.algebra.lie.polynomial.monomial.Monomial;
import org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author: mshevelin
 * Date: 11/28/14
 * Time: 1:37 PM
 */

public class PermutationGTest {
    private static final Logger log = Logger.getLogger(PermutationGTest.class);
    private static final PermutationG<Integer> permutation = PermutationG.create(0, 2, 1);
    private static final PermutationG<Integer> another = PermutationG.create(1, 2, 0);
    private static final PermutationG<Integer> product = PermutationG.create(1, 0, 2);
    private static final Monomial x = MonomialUtils.monomial("x");
    private static final Monomial y = MonomialUtils.monomial("y");
    private static final Monomial z = MonomialUtils.monomial("z");
    private static final PermutationG<Monomial> letterPermutation = PermutationG.create(y, z, x);
    private static final PermutationG<Monomial> letterPermutationSquared = PermutationG.create(z, x, y);

    @Before
    public void setUp() throws Exception {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
    }

    @Test
    public void testGetAt() throws Exception {
        assertTrue(permutation.getAt(0).equals(0));
        assertTrue(permutation.getAt(1).equals(2));
        assertTrue(permutation.getAt(2).equals(1));
        assertEquals(letterPermutation.getAt(x), y);
        assertEquals(letterPermutation.getAt(y), z);
        assertEquals(letterPermutation.getAt(z), x);
    }

    @Test
    public void testTimes() throws Exception {
        assertEquals(permutation.times(another), product);
        assertEquals(letterPermutation.times(letterPermutation), letterPermutationSquared);
    }

    @Test
    public void testReverse() throws Exception {
        assertEquals(permutation.reverse(), permutation);
        assertEquals(letterPermutation.reverse(), letterPermutationSquared);
    }

    @Test
    public void testCreate() {
        final PermutationG<Integer> permutation1 = PermutationG.create(Arrays.asList(new Integer[]{0, 2, 1})
        );
        assertEquals(permutation, permutation1);
        final PermutationG<Monomial> letterPermutation1 = PermutationG.create(Arrays.asList(new Monomial[]{y, z, x,})
        );
        assertEquals(letterPermutation1, letterPermutation);
    }

    @Test
    public void testParse() {
        final PermutationG<Integer> p = PermutationG.create(1, 3, 2);
        assertEquals(p, PermutationG.parse("(1, 3, 2)"));
    }

    @Test
    public void permutationSequenceTest() {
        final PermutationG<Integer> e = PermutationG.create(1, 2, 3);
        final PermutationG<Integer> t23 = PermutationG.create(1, 3, 2);
        final PermutationG<Integer> t12 = PermutationG.create(2, 1, 3);
        final PermutationG<Integer> c123 = PermutationG.create(2, 3, 1);
        final PermutationG<Integer> c132 = PermutationG.create(3, 1, 2);
        final PermutationG<Integer> t13 = PermutationG.create(3, 2, 1);
        assertTrue(e.compareTo(t23) < 0);
        assertTrue(t23.compareTo(t12) < 0);
        assertTrue(t12.compareTo(c123) < 0);
        assertTrue(c123.compareTo(t13) < 0);
        assertTrue(c132.compareTo(t13) < 0);
    }

    private PermutationG<Integer> create(final Integer... array) {
        return PermutationG.create(array);
    }

    private PermutationG<Monomial> create(final String... array) {
        final Collection<Monomial> letters = new ArrayList<Monomial>(array.length);
        for (final String s : array) {
            letters.add(MonomialUtils.monomial(s));
        }
        return PermutationG.create(letters);
    }

    @Test
    public void sym3Test() {
        final Collection<PermutationG<Integer>> set = new TreeSet<>(
                PermutationG.sequence(PermutationG.create(1, 2, 3)));
        final Collection<PermutationG<Integer>> expected = new TreeSet<PermutationG<Integer>>();
        expected.add(create(1, 2, 3));
        expected.add(create(1, 3, 2));
        expected.add(create(2, 1, 3));
        expected.add(create(2, 3, 1));
        expected.add(create(3, 1, 2));
        expected.add(create(3, 2, 1));
        assertEquals(set.size(), expected.size());
        assertTrue(set.containsAll(expected) && expected.containsAll(set));
    }

    @Test
    public void sym4Test() {
        final Collection<PermutationG<Integer>> set = new TreeSet<>(
                PermutationG.sequence(PermutationG.create(1, 2, 3, 4)));
        final Collection<PermutationG<Integer>> expected = new TreeSet<PermutationG<Integer>>();
        expected.add(create(1, 2, 3, 4));
        expected.add(create(1, 2, 4, 3));
        expected.add(create(1, 3, 2, 4));
        expected.add(create(1, 3, 4, 2));
        expected.add(create(1, 4, 2, 3));
        expected.add(create(1, 4, 3, 2));
        expected.add(create(2, 1, 3, 4));
        expected.add(create(2, 1, 4, 3));
        expected.add(create(2, 3, 1, 4));
        expected.add(create(2, 3, 4, 1));
        expected.add(create(2, 4, 1, 3));
        expected.add(create(2, 4, 3, 1));
        expected.add(create(3, 1, 2, 4));
        expected.add(create(3, 1, 4, 2));
        expected.add(create(3, 2, 1, 4));
        expected.add(create(3, 2, 4, 1));
        expected.add(create(3, 4, 1, 2));
        expected.add(create(3, 4, 2, 1));
        expected.add(create(4, 1, 2, 3));
        expected.add(create(4, 1, 3, 2));
        expected.add(create(4, 2, 1, 3));
        expected.add(create(4, 2, 3, 1));
        expected.add(create(4, 3, 1, 2));
        expected.add(create(4, 3, 2, 1));
        assertEquals(set.size(), expected.size());
        assertTrue(set.containsAll(expected) && expected.containsAll(set));
    }

    @Test
    public void sym5Test() {
        final Collection<PermutationG<Integer>> set = new TreeSet<>(
                PermutationG.sequence(PermutationG.create(1, 2, 3, 4, 5)));
        final Collection<PermutationG<Integer>> expected = new TreeSet<PermutationG<Integer>>();
        readSym5To(expected);
        assertEquals(set.size(), expected.size());
        assertTrue(set.containsAll(expected) && expected.containsAll(set));
    }

    private void readSym5To(final Collection<PermutationG<Integer>> expected) {
        try (Scanner sc = new Scanner(new File("./sym5.txt"), "UTF-8")) {
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line != null) {
                    expected.add(PermutationG.parse(line));
                }
            }
        } catch (final Throwable e) {
            log.error(e);
        }
    }

    @Test
    public void generateSequence() {
        final Collection<PermutationG<Monomial>> set = new TreeSet<>(
                PermutationG.sequence(create("a", "b", "c", "d", "e", "f")));
        assertEquals(2 * 3 * 4 * 5 * 6, set.size());
    }
}
