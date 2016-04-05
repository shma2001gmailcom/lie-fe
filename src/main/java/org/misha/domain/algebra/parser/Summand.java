package org.misha.domain.algebra.parser;

/**
 * Author: mshevelin
 * Date: 8/26/14
 * Time: 11:48 AM
 * For spelling questions see
 * http://www.dictionary.com/browse/summand
 */

public final class Summand {
    private final int constant;
    private final String core;

    public Summand(final int constant, final String core) {
        this.constant = constant;
        this.core = core;
    }

    public String getCore() {
        return core;
    }

    public int getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return (constant > 0 ? "+ " + constant : constant) + core;
    }
}


