package org.misha.algebra.lie.polynomial.monomial;

/**
 * Author: mshevelin
 * Date: 3/28/14
 * Time: 3:21 PM
 */

abstract class MonomialVisitor {

    public void visit(final Monomial m) {
        if (m.isLetter()) {
            doSomethingWith(m);
            return;
        }
        final Monomial left = m.left();
        final Monomial right = m.right();
        doSomethingWith(left);
        doSomethingWith(right);
        visit(left);
        visit(right);
    }

    protected abstract void doSomethingWith(Monomial m);
}
