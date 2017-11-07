package org.misha.domain.algebra.associative.diamond;

import org.junit.Test;
import org.misha.domain.algebra.associative.Polynomial;

import java.util.Set;
import java.util.TreeSet;

import static org.misha.domain.algebra.associative.PolynomialUtils.mount;

/**
 * author: misha
 * date: 11/6/17
 * time: 6:00 PM
 */
public class ReductionTest {
    
    @Test
    public void reduce() throws Exception {
        Polynomial p = mount("+ y - xyx + yz + z + xyxyxyx");
        Polynomial q = mount("+ x - xyx");
        Set<Polynomial> input = new TreeSet<Polynomial>();
        input.add(p);
        input.add(q);
        Set<Polynomial> input1 = input;
        for (int i = 0 ; i < 3; ++ i) {
            input1 = new Reduction(p, q).reduce(input1);
        }
        for (Polynomial r : input1) {
            System.out.println("--" + r);
        }
    }
}
