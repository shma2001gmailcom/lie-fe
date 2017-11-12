package org.misha.domain.algebra.associative.reduction;

import org.junit.Test;
import org.misha.domain.algebra.associative.Polynomial;

import java.util.Set;
import java.util.TreeSet;

import static org.misha.domain.algebra.associative.PolynomialUtils.mount;
import static org.misha.domain.algebra.associative.reduction.Reduction.total;

/**
 * author: misha
 * date: 11/6/17
 * time: 6:00 PM
 */
public class ReductionTest {
    
    @Test
    public void reduce() throws Exception {
        Polynomial p = mount("+ xy");
        Polynomial q = mount("+ y - xyx");
        Polynomial s = mount("+ z - xyz - zxy");
        Polynomial t = mount("+ y + yy");
        Set<Polynomial> input = new TreeSet<Polynomial>();
        input.add(p);
        input.add(q);
        input.add(s);
        input.add(t);
        int totalAfter = 0;
        int totalBefore = total(input);
        input = doReductions(p, s, input, totalAfter, totalBefore);
        Polynomial x = input.iterator().next();
        input.remove(x);
        totalAfter = 0;
        totalBefore = total(input);
        input = doReductions(p, s, input, totalAfter, totalBefore);
        input.add(x);
        totalAfter = 0;
        totalBefore = total(input);
        input = doReductions(p, s, input, totalAfter, totalBefore);
        for (Polynomial r : input) {
            System.out.println("> " + r);
        }
    }

    private Set<Polynomial> doReductions(
            Polynomial p, Polynomial q, Set<Polynomial> input, int totalAfter, int totalBefore
    ) {
        while (totalAfter < totalBefore) {
            totalBefore = total(input);
            input = new Reduction(p, q).reduce(input);
            totalAfter = total(input);
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb");
            for (Polynomial r : input) {
                System.out.println("  " + r);
            }
            System.out.println("eeeeeeeeeeeeeeeeeeeeeeeee\n");
        }
        return input;
    }
}
