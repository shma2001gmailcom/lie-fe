package org.misha.domain.algebra.associative.diamond;

import org.misha.domain.algebra.associative.impl.Monomial;

import static org.misha.domain.algebra.associative.impl.Monomial.monomial;

/**
 * author: misha
 * date: 11/4/17
 * time: 8:07 PM
 * <p>
 *         hmt
 *          ^
 *         / \
 *        /   \
 *    hm <     > mt
 *        \   /
 *         \ /
 *          v
 *          m
 */
public class Diamond {
    private final Monomial left;
    private final Monomial right;

    public Diamond(final Monomial left, final Monomial right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("both monomials should be nonnull");
        }
        this.left = left;
        this.right = right;
    }

    public Top find() {
        final int leftDeg = left.deg();
        final int rightDeg = right.deg();
        if (leftDeg == 0 || rightDeg == 0) {
            return Top.NONE;
        }
        int i = 0, leftBound, rightBound;
        Monomial leftTail, rightHead;
        final int minDeg = leftDeg <= rightDeg ? leftDeg : rightDeg;
        while (i < minDeg) {
            leftBound = minDeg == leftDeg ? i : leftDeg - i - 1;
            rightBound = minDeg == leftDeg ? leftDeg - i : i + 1;
            leftTail = left.tail(leftBound);
            rightHead = right.head(rightBound);
            if (leftTail.equals(rightHead)) {
                return new Top(left.head(leftBound), leftTail, right.tail(rightBound));
            }
            ++i;
        }
        return Top.NONE;
    }

    public static class Top {
        private final Monomial head;
        private final Monomial middle;
        private final Monomial tail;
        private static final Top NONE = new Top(monomial(""), monomial(""), monomial(""));

        private Top(final Monomial head,
                    final Monomial middle,
                    final Monomial tail
        ) {
            this.head = head;
            this.tail = tail;
            this.middle = middle;
        }

        public Monomial head() {
            return head.copy();
        }

        public Monomial middle() {
            return middle.copy();
        }

        public Monomial tail() {
            return tail.copy();
        }
    }
}
