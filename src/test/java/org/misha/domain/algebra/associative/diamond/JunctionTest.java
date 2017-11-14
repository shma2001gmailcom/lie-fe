package org.misha.domain.algebra.associative.diamond;

import org.junit.Test;
import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.associative.impl.Monomial;
import org.misha.domain.algebra.associative.reduction.Reduction;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertTrue;
import static org.misha.domain.algebra.associative.PolynomialUtils.mount;
import static org.misha.domain.algebra.associative.impl.Monomial.monomial;
import static org.misha.domain.algebra.associative.reduction.Reduction.total;

/**
 * author: misha
 * date: 11/6/17
 * time: 12:46 PM
 */
public class JunctionTest {
    
    @Test
    public void testJoin() throws Exception {
        Polynomial first = mount("+ xxy - xyx - xyx + yxx");
        Polynomial second = mount("+ xyy - yxy - yxy + yyx");
        Polynomial junction21 = new Junction(second, first).join();
        System.out.println(junction21);
        Set<Polynomial> input = new TreeSet<Polynomial>();
        input.add(first);
        input.add(second);
        input.add(junction21);
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