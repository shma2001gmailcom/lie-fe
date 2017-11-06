package org.misha.domain.algebra.associative.diamond;

import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.associative.impl.Monomial;

import static org.misha.domain.algebra.associative.PolynomialUtils.mount;
import static org.misha.domain.algebra.associative.diamond.Diamond.*;

/**
 * author: misha
 * date: 11/6/17
 * time: 11:24 AM
 */
public class Junction {
    private final Polynomial first;
    private final Polynomial second;

    public Junction(final Polynomial first, final Polynomial second) {
        this.first = first;
        this.second = second;
    }

    public Polynomial join() {
        final Monomial left = first.elder();
        final int leftConst = left.getConst();
        final Monomial right = first.elder();
        final int rightConst = right.getConst();
        final Top top = new Diamond(first.elder(), second.elder()).find();
        System.out.println(top.toString());
        return mount(top.head()).times(second).times(leftConst)
                                .plus(first.times(mount(top.tail())).times(rightConst));
    }
}
