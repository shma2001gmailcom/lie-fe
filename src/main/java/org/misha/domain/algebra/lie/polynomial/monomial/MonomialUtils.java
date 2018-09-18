package org.misha.domain.algebra.lie.polynomial.monomial;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: mshevelin
 * Date: 11/26/14
 * Time: 2:41 PM
 */

public final class MonomialUtils {
    private static final String CAN_T_RECOGNIZE_LETTER = "can't recognize valid letter for the String '%s'.";
    private static final Pattern PATTERN =Pattern.compile("([+\\-])*([0-9 ])*([\\u0001-\\u2000])");

    private MonomialUtils() {
    }

    /**
     * Static generation method
     *
     * @param left  left multiplier
     * @param right right multiplier
     * @param times the count of times right multiplier will applied
     * @return product
     */
    public static Monomial monomial(final Monomial left, final Monomial right, final int times) {
        Monomial result = left.copy();
        for (int i = 0; i < times; ++i) {
            result = monomial(result, right.copy());
        }
        return result;
    }

    /**
     * Static generation method
     *
     * @param l the letter
     * @return the monomial which is a letter
     */
    public static Monomial monomial(final String l) {
        return new Monomial(l);
    }

    /**
     * Static generation method
     *
     * @param left  left multiplier
     * @param right right multiplier
     * @return product
     */
    public static Monomial monomial(final Monomial left, final Monomial right) {
        final Monomial lCopy = left != null ? left.copy() : null;
        final Monomial rCopy = right != null ? right.copy() : null;
        final int cL = lCopy != null ? left.getConst() : 1;
        final int cR = rCopy != null ? right.getConst() : 1;
        if (isSimilar(lCopy, rCopy)) {
            return new Monomial(lCopy, rCopy, 0);
        }
        final Monomial result = new Monomial(lCopy, rCopy, cL * cR);
        if (right != null) {
            right.setParent(result);
        }
        if (left != null) {
            left.setParent(result);
        }
        return result;
    }

    private static boolean isSimilar(final Monomial left, final Monomial right) {
        if (right != null && left != null) {
            right.setConst(1);
            left.setConst(1);
            return right.equals(left);
        }
        return false;
    }

    static MatchResult getMatcher(final String s) throws IllegalArgumentException {
        final Matcher matcher = PATTERN.matcher(s);
        if (!matcher.find()) {
            throw new IllegalArgumentException(String.format(CAN_T_RECOGNIZE_LETTER, s));
        } else {
            return matcher;
        }
    }
}
