package org.misha.algebra;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: mshevelin
 * Date: 11/27/14
 * Time: 5:31 PM
 */

public class PermutationTest {
    private static final Permutation permutation = Permutation.create(3, 0, 2, 1);
    private static final Permutation reversed = Permutation.create(3, 2, 0, 1);
    private static final Permutation another = Permutation.create(3, 1, 2, 0);
    private static final Permutation yetAnother = Permutation.create(3, 1, 2, 0);
    private static final Permutation product = Permutation.create(3, 1, 0, 2);

    @Test
    public void testGetAt() throws Exception {
        assertEquals(permutation.getAt(0), 0);
        assertEquals(permutation.getAt(1), 2);
        assertEquals(permutation.getAt(2), 1);
    }

    @Test
    public void testTimes() throws Exception {
        assertEquals(permutation.times(another), product);
    }

    @Test
    public void testReverse() throws Exception {
        assertEquals(yetAnother.reverse(), reversed);
    }
}
