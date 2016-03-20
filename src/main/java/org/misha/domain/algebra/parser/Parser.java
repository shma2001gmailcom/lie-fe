/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.domain.algebra.parser;

import org.apache.commons.lang3.StringUtils;
import org.misha.domain.algebra.lie.endomorphism.Endo;
import org.misha.domain.algebra.lie.polynomial.Polynomial;
import org.misha.domain.algebra.lie.polynomial.monomial.Monomial;
import org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils;
import org.misha.service.validator.Validator;

import java.util.*;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.remove;

/**
 * author: misha
 * Date: 3/30/14
 * Time: 11:34 AM
 * <p/>
 * Given a String creates a Lie polynomial by this String
 */

public final class Parser {
    private static final String INCORRECT_BRACKETS = "can't parse expression having incorrect brackets.";
    private static final String SPACE = " ";
    private static final String CAN_T_OBTAIN_ROOT_FOR_NULL = "can't obtain root for null monomial.";
    private static final String PLUS = "+";
    private final String expression;
    private List<Summand> summands = new ArrayList<Summand>();
    private static final char lBracket = '[';
    private static final char rBracket = ']';

    public Parser(final String expression) {
        this.expression = expression;
        summands = getSummands();
    }

    public Parser() {
        expression = StringUtils.EMPTY;
    }

    public Polynomial parse() throws IllegalArgumentException {
        Polynomial result = new Polynomial();
        for (final Summand summand : summands) {
            if (isNotEmpty(summand.getCore())) {
                final Monomial m = parse(summand.getCore());
                m.setConst(summand.getConstant());
                result = result.plus(m);
            }
        }
        return result;
    }

    public boolean areBracketsIncorrect(final Iterable<Character> queue) {
        int level = 0;
        for (final Character c : queue) {
            if (c == lBracket) {
                level++;
            } else if (c == rBracket) {
                if (--level < 0) {
                    return true;
                }
            }
        }
        return level != 0;
    }

    public Monomial parse(final String summand) throws IllegalArgumentException {
        return obtainRoot(parseQueue(checkAndPrepare(summand)));
    }

    private Monomial obtainRoot(Monomial monomial) {
        if (monomial == null) {
            throw new IllegalArgumentException(CAN_T_OBTAIN_ROOT_FOR_NULL);
        }
        while (!monomial.isRoot()) {
            monomial = monomial.getParent();
        }
        return monomial;
    }

    private Monomial parseQueue(final Queue<Character> queue) {
        Monomial monomial = null;
        Monomial parent = null;
        while (!queue.isEmpty()) {
            final char c = queue.remove();
            switch (c) {
                case lBracket:
                    parent = onLeftBracket(parent);
                    break;
                case rBracket:
                    parent = onRightBracket(parent, monomial);
                    break;
                default:
                    monomial = onCharacter(parent, c);
                    break;
            }
        }
        return monomial;
    }

    private Queue<Character> checkAndPrepare(String summand) throws IllegalArgumentException {
        final Queue<Character> queue = new LinkedList<Character>();
        for (final Character c : summand.toCharArray()) {
            queue.add(c);
        }
        if (areBracketsIncorrect(queue)) {
            throw new IllegalArgumentException(INCORRECT_BRACKETS);
        }
        if (Validator.isLieMonomialValid(summand)) {
            summand = summand.replaceAll("[ ,]", StringUtils.EMPTY).trim();
            queue.clear();
            for (final Character c : summand.toCharArray()) {
                queue.add(c);
            }
            return queue;
        }
        throw new IllegalArgumentException("can't parse string '" + summand + "'.");
    }

    private Monomial onCharacter(final Monomial parent, final char c) {
        final Monomial monomial = MonomialUtils.monomial(Character.toString(c));
        if (parent != null) {
            parent.addChild(monomial);
        }
        return monomial;
    }

    private Monomial onRightBracket(Monomial parent, final Monomial monomial) {
        if (parent != null) {
            if (parent.isPending()) {
                parent.addChild(monomial);
            }
            if (parent.getParent() != null) {
                parent = parent.getParent();
            }
        }
        return parent;
    }

    private Monomial onLeftBracket(final Monomial parent) {
        final Monomial monomial = new Monomial();
        if (parent != null) {
            parent.addChild(monomial);
        }
        return monomial;
    }

    public static String getCore(final String s) {
        final Matcher matcher = compile("(-|\\+)([0-9 ]*)(.*)").matcher(s);
        return matcher.find() ? matcher.group(3) : compile("(-|\\+)([0-9 ]*)(a-zA-Z)").matcher(s).find() ?
                                                   matcher.group(3) : s;
    }

    private List<Summand> getSummands() {
        String summands = expression;
        final List<Summand> result = new ArrayList<Summand>();
        String pattern = "(-|\\+)([0-9 ]*)([a-zA-Z ,\\Q[\\E\\Q]\\E]+)(-|\\+)";
        summands = findNextSummand(pattern, summands, result);
        pattern = "(-|\\+)([0-9 ]*)([a-zA-Z ,\\Q[\\E\\Q]\\E]+)";
        findNextSummand(pattern, summands, result);
        return result;
    }

    private String findNextSummand(
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

    private int constant(String constant) {
        constant = remove(constant, SPACE);
        if ("-".equals(constant)) {
            return -1;
        }
        if (PLUS.equals(constant) || StringUtils.isBlank(constant)) {
            return 1;
        }
        constant = StringUtils.removeStart(constant, PLUS);
        return Integer.parseInt(constant);
    }

    public Endo parseEndo(String s) throws IllegalArgumentException {
        final Endo endo = new Endo();
        s = StringUtils.remove(s, ')');
        s = StringUtils.remove(s, '(');
        final String[] array = StringUtils.split(s, ";");
        char a = 'a';
        for (final String term : array) {
            final Polynomial polynomial = new Parser(term.trim()).parse();
            endo.mapTo(MonomialUtils.monomial(Character.toString(a++)), polynomial);
        }
        return endo;
    }
}
