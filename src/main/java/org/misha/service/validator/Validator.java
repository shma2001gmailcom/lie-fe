package org.misha.service.validator;

import org.apache.log4j.Logger;
import org.misha.algebra.parser.Parser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: mshevelin
 * Date: 6/2/14
 * Time: 2:33 PM
 */

public final class Validator {
    private static final Logger log = Logger.getLogger(Validator.class);

    private Validator() {
    }

    public static boolean isLieMonomialValid(final String s) {
        final Pattern signsLettersCommasBrackets = Pattern.compile("([-\\+]*)([a-zA-Z0-9, \\Q[\\E\\Q]\\E])*");
        if (!signsLettersCommasBrackets.matcher(s).matches()) {
            log.error(String.format("invalid characters in the expression '%s'.", s));
            return false;
        }
        final Collection<Character> queue = new LinkedList<Character>();
        for (final Character c : s.toCharArray()) {
            queue.add(c);
        }
        if (!new Parser("").areBracketsCorrect(queue)) {
            log.error(String.format("the expression '%s' has unmatched brackets.", s));
            return false;
        }
        final Pattern twoLetters = Pattern.compile("(.*)([a-zA-Z]{2})(.*)");
        if (twoLetters.matcher(s).matches()) {
            log.error(String.format("two adjacent letters in the expression '%s' are inadmissible.", s));
            return false;
        }
        final Pattern emptyLeft = Pattern.compile("(.*)(\\[ *,(.*)])(.*)");
        final Pattern emptyRight = Pattern.compile("(.*)(\\[(.*), *])(.*)");
        if (emptyLeft.matcher(s).matches() || emptyRight.matcher(s).matches()) {
            log.error(String.format("all left and right multipliers in expression '%s' should be non-empty.", s));
            return false;
        }
        return true;
    }

    public static boolean isMonomial(final String s) {
        Pattern pattern = Pattern.compile("\\[(\\[.*\\]|[a-z]),(\\[.*\\]|[a-z])\\]");
        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            return s.matches("[a-z]");
        } else {
            String left = matcher.group(1);
            String right = matcher.group(2);
            return isMonomial(left) && isMonomial(right);
        }
    }
}
