package org.misha.domain.algebra.associative.diamond;

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

    public Reduction(Polynomial left, Polynomial right) {
        this.left = left;
        this.right = right;
    }

    public Set<Polynomial> reduceOnce() {
        final String le = left.elder().unify().toString().trim().replaceAll("[+-] ", EMPTY);
        final String re = right.elder().unify().toString().trim().replaceAll("[+-] ", EMPTY);
        final Pattern pattern = compile(re);
        final Matcher matcher = pattern.matcher(le);
        final Set<Polynomial> result = new TreeSet<Polynomial>();
        while (matcher.find()) {
            result.add(left.plus(monomial("+1" + le.substring(0, matcher.start())).times(right).times(monomial(
                    "+1" + le.substring(matcher.end()))).times(-1)));
        }
        return result;
    }

    public Set<Polynomial> reduce(Set<Polynomial> input, Polynomial by) {
        Set<Polynomial> result = new TreeSet<Polynomial>();
        Iterator<Polynomial> it = input.iterator();
        Polynomial current = null;
        if (it.hasNext()) {
            current = it.next();
            it.remove();
        }
        if (current != null) {
            result.addAll(new Reduction(current, by).reduceOnce());
        }
        return result;
    }

    int total(Set<Polynomial> set) {
        int total = 0;
        for (Polynomial p : set) {
            total = total < p.deg() ? p.deg() : total;
        }
        return total;
    }

    public Set<Polynomial> reduce(Set<Polynomial> input) {
        int total = total(input);
        int currentTotal = total;
        Polynomial by = null;
        Set<Polynomial> result = new TreeSet<Polynomial>();
        do {
            Iterator<Polynomial> iterator = input.iterator();
            if (iterator.hasNext()) {
                by = iterator.next();
                result.add(by);
            }
            while (iterator.hasNext()) {
                Polynomial p = iterator.next();
                iterator.remove();
                Set<Polynomial> once = new Reduction(p, by).reduceOnce();
                result.addAll(once);
                result.addAll(input);
            }
            total = currentTotal;
            currentTotal = total(result);
            while (iterator.hasNext()) iterator.next();
        }
        while (currentTotal < total);
        return result;
    }
}
