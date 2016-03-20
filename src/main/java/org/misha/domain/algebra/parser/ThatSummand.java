package org.misha.domain.algebra.parser;

import org.misha.domain.algebra.scalars.Scalar;

/**
 * Author: mshevelin
 * Date: 8/26/14
 * Time: 11:48 AM
 */

public final class ThatSummand<T extends Scalar> {
    private final T constant;
    private final String core;

    public ThatSummand(final T constant, final String core) {
        this.constant = constant;
        this.core = core;
    }

    public String getCore() {
        return core;
    }

    public T getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return (constant.isPositive() ? "+ " + constant : constant.toString()) + core;
    }
}


