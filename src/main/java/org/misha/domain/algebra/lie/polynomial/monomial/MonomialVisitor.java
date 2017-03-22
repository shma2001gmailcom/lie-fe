package org.misha.domain.algebra.lie.polynomial.monomial;

import java.util.LinkedList;

/**
 * Author: mshevelin
 * Date: 3/28/14
 * Time: 3:21 PM
 */

abstract class MonomialVisitor {
    private final LinkedList<Monomial> queue = new LinkedList<Monomial>();

    void visit(final Monomial m) {
        queue.add(m);
        while(!queue.isEmpty()) {
            final Monomial pop = queue.pop();
            if(!pop.hasNoChildren()) {
                queue.add(pop.left());
                queue.add(pop.right());
            }
            doSomethingWith(pop);
        }
    }

    protected abstract void doSomethingWith(Monomial m);
}
