package org.misha.domain.algebra.associative.reduction;

import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.associative.impl.Monomial;

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
        };
        if (matcher.find()) {
            Polynomial toAdd = left.times(rightLeadConst)
                    .plus(monomial("+1" + le.substring(0, matcher.start())).times(right).times(monomial(
                            "+1" + le.substring(matcher.end()))).times(leftLeadConst).times(-1));
            System.out.println(" reduction: {");
            System.out.println(left.times(rightLeadConst));
            System.out.println("[" + monomial("+1" + le.substring(0, matcher.start())) + "]" + "\u00B7" + "[" + right.times(-1) + "]" + "\u00B7" + "[" + monomial(
                    "+1" + le.substring(matcher.end())).times(leftLeadConst) + "]\n----------------\n" + toAdd);
            System.out.println("}");
            if (!toAdd.isZero()) {
                result.add(toAdd);
            }
        } else {
            System.out.println(" reduction:{}");
            result.add(left);
        }
        return result;
    }
    
    public static Monomial total(Set<Polynomial> set) {
        if (!set.isEmpty()) {
            Monomial result = set.iterator().next().elder();
            for (Polynomial p : set) {
                result = p.elder().compareTo(result) > 0 ? p.elder() : result;
            }
            return result;
        }
        return monomial("", 1);
    }
    
    public static Set<Polynomial> reduce(Set<Polynomial> input) {
        Monomial total = total(input);
        Monomial currentTotal = total;
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
                printSet(input);
                by = inputIterator.next();
                System.out.println("by = " + by);
                result.add(by);
            }
            while (inputIterator.hasNext()) {
                Polynomial p = inputIterator.next();
                inputIterator.remove();
                Set<Polynomial> once = new Reduction(p, by).reduceOnce();
                result.addAll(once);
            }
            total = currentTotal;
            currentTotal = total(result);
        } while (currentTotal.compareTo(total) < 0);
        return result;
    }
    
    private static void printSet(final Set<Polynomial> input) {
        System.out.println("set{");
        for (Polynomial r : input) {
            System.out.println("  " + r);
        }
        System.out.println("}");
    }
    
    public static Set<Polynomial> doReductions(Set<Polynomial> input) {
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
        return input;
    }
    
    private static Set<Polynomial> doReductions(
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
