package org.misha.domain.algebra.associative.reduction;

import org.junit.Test;
import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.associative.impl.Monomial;

import java.util.Set;
import java.util.TreeSet;

import static org.misha.domain.algebra.associative.PolynomialUtils.mount;
import static org.misha.domain.algebra.associative.impl.Monomial.monomial;
import static org.misha.domain.algebra.associative.reduction.Reduction.total;

/**
 * author: misha
 * date: 11/6/17
 * time: 6:00 PM
 */
public class ReductionTest {
    
    @Test
    public void reduce() throws Exception {
        Polynomial p = mount("+ + xy - x");
        Polynomial q = mount("- x - y - z - xy - yy - zx + zxy");
        Polynomial s = mount("+ xz - x");
        Polynomial t = mount("+ zy -y");
        Set<Polynomial> input = new TreeSet<Polynomial>();
        input.add(p);
        input.add(q);
        input.add(s);
        input.add(t);
        Monomial totalAfter = monomial("", 1);
        Monomial totalBefore = total(input);
        input = doReductions(input, totalAfter, totalBefore);
        Polynomial x;
        while (totalAfter.compareTo(totalBefore) < 0) {
            x = input.iterator().next();
            input.remove(x);
            totalBefore = total(input);
            input = doReductions(input, totalAfter, totalBefore);
            totalAfter = total(input);
            input.add(x);
        }
        for (Polynomial r : input) {
            System.out.println("> " + r);
        }
    }
    
    private Set<Polynomial> doReductions(
            Set<Polynomial> input, Monomial totalAfter, Monomial totalBefore
    ) {
        while (totalAfter.compareTo(totalBefore) < 0) {
            totalBefore = total(input);
            input = Reduction.reduce(input);
            totalAfter = total(input);
        }
        return input;
    }
}
