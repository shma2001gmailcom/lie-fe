package org.misha.domain.algebra.associative.reduction;

import org.misha.domain.algebra.associative.Polynomial;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.misha.domain.algebra.associative.impl.Monomial.monomial;

/**
 * author: misha
 * date: 11/6/17
 * time: 1:43 PM
 */
public class Reduction {
    private final Polynomial left;
    private final Polynomial right;

    Reduction(Polynomial left, Polynomial right) {
        this.left = left;
        this.right = right;
    }

    Set<Polynomial> reduceOnce() {
        if (left.compareTo(right) < 0) {
            return new TreeSet<Polynomial>() {{
                if (!left.isZero()) {
                    add(left);
                }
                if (!right.isZero()) {
                    add(right);
                }
            }};
        }
        final int leftLeadConst = left.elder().getConst();
        final int rightLeadConst = right.elder().getConst();
        final String le = left.elder().unify().toString().trim().replaceAll("[+-] ", EMPTY);
        final String re = right.elder().unify().toString().trim().replaceAll("[+-] ", EMPTY);
        final Pattern pattern = compile(re);
        final Matcher matcher = pattern.matcher(le);
        final Set<Polynomial> result = new TreeSet<Polynomial>() {

            @Override
            public boolean add(Polynomial monomials) {
                return !monomials.isZero() && super.add(monomials);
            }

            {
            }
        };
        if (matcher.find()) {
            Polynomial toAdd = left.times(rightLeadConst)
                                   .plus(monomial("+1" + le.substring(0, matcher.start())).times(right).times(monomial(
                                           "+1" + le.substring(matcher.end()))).times(leftLeadConst).times(-1));
            if (!toAdd.isZero()) {
                result.add(toAdd);
            }
        } else {
            result.add(left);
        }
        return result;
    }

    public Set<Polynomial> reduce() {
        Set<Polynomial> set = new TreeSet<Polynomial>() {

            @Override
            public boolean add(Polynomial monomials) {
                return !monomials.isZero() && super.add(monomials);
            }
        };
        set.add(left);
        set.add(right);
        int total;
        Set<Polynomial> result = set;
        do {
            total = total(result);
            result = reduce(result);
        }
        while (total(result) < total);
        return result;
    }

    static int total(Set<Polynomial> set) {
        int total = 0;
        for (Polynomial p : set) {
            total += p.deg();
        }
        return total;
    }

    Set<Polynomial> reduce(Set<Polynomial> input) {
        int total = total(input);
        int currentTotal = total;
        Polynomial by = null;
        Set<Polynomial> result = new TreeSet<Polynomial>() {

            @Override
            public boolean add(Polynomial monomials) {
                return !monomials.isZero() && super.add(monomials);
            }
        };
        do {
            Iterator<Polynomial> inputIterator = input.iterator();
            if (inputIterator.hasNext()) {
                by = inputIterator.next();
                if (!by.isZero()) {
                    result.add(by);
                }
            }
            while (inputIterator.hasNext()) {
                Polynomial p = inputIterator.next();
                inputIterator.remove();
                Set<Polynomial> once = new Reduction(p, by).reduceOnce();
                result.addAll(once);
            }
            total = currentTotal;
            currentTotal = total(result);
        }
        while (currentTotal < total);
        return result;
    }
}
