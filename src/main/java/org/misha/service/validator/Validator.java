package org.misha.service.validator;

import org.apache.log4j.Logger;
import org.misha.algebra.parser.Parser;

import java.util.Collection;
import java.util.LinkedList;
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
            log.error("invalid characters in the expression '" + s + "'.");
            return false;
        }
        final Collection<Character> queue = new LinkedList<Character>();
        for (final Character c : s.toCharArray()) {
            queue.add(c);
        }
        if (!new Parser("").areBracketsCorrect(queue)) {
            log.error("the expression '" + s + "' has unmatched brackets.");
            return false;
        }
        final Pattern twoLetters = Pattern.compile("(.*)([a-zA-Z]{2})(.*)");
        if (twoLetters.matcher(s).matches()) {
            log.error("two adjacent letters in the expression '" + s + "' are inadmissible.");
            return false;
        }
        final Pattern emptyLeft = Pattern.compile("(.*)(\\[ *,(.*)])(.*)");
        final Pattern emptyRight = Pattern.compile("(.*)(\\[(.*), *])(.*)");
        if (emptyLeft.matcher(s).matches() || emptyRight.matcher(s).matches()) {
            log.error("all left and right multipliers in expression '" + s + "' should be non-empty.");
            return false;
        }
        return true;
    }
}
