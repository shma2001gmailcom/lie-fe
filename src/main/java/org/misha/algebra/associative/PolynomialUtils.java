package org.misha.algebra.associative;

import org.apache.commons.lang3.StringUtils;
import org.misha.algebra.associative.impl.Monomial;
import org.misha.algebra.parser.Summand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.misha.algebra.associative.impl.Monomial.monomial;

/**
 * Author: mshevelin
 * Date: 11/26/14
 * Time: 2:34 PM
 */

final class PolynomialUtils {
    private static final String SPACE = " ";
    private static final String PLUS = "+";

    private PolynomialUtils() {
    }

    private static Iterable<Summand> getSummands(final String expression) {
        String summands = StringUtils.remove(expression, " ");
        final Collection<Summand> result = new ArrayList<Summand>();
        String pattern = "(\\+|\\-)([0-9]*)([a-zA-Z]*)(-|\\+)";
        summands = findNextSummand(pattern, summands, result);
        pattern = "(-|\\+)([0-9 ]*)([a-zA-Z]+)";
        findNextSummand(pattern, summands, result);
        return result;
    }

    public static Polynomial mount(final String expression) {
        Polynomial result = new Polynomial();
        for (final Summand summand : getSummands(expression)) {
            if (isNotEmpty(summand.getCore())) {
                final Monomial m = monomial(summand.toString());
                result = result.plus(m);
            }
        }
        result.sort();
        return result;
    }

    private static String findNextSummand(
            final String pattern, String s, final Collection<Summand> result
    ) {
        Matcher matcher;
        String core;
        String constant;
        while (StringUtils.isNotEmpty(s)) {
            matcher = compile(pattern).matcher(s);
            if (matcher.find()) {
                core = matcher.group(3);
                constant = matcher.group(1) + matcher.group(2);
                final Summand summand = new Summand(constant(constant), core);
                result.add(summand);
                s = StringUtils.removeStart(s, constant + core);
            } else {
                break;
            }
        }
        return s;
    }

    private static int constant(String constant) {
        constant = StringUtils.remove(constant, SPACE);
        if ("-".equals(constant)) {
            return -1;
        }
        if (PLUS.equals(constant) || StringUtils.isBlank(constant)) {
            return 1;
        }
        constant = StringUtils.removeStart(constant, PLUS);
        return Integer.parseInt(constant);
    }
}
