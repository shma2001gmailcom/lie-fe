package org.misha.utils;

/**
 * author: misha
 * Date: 4/20/14
 * Time: 2:32 PM
 * <p/>
 * Left hand side of the implication
 */

public final class Premise {
    private final boolean premise;

    private Premise(final boolean premise) {
        this.premise = premise;
    }

    public static Premise premise(final boolean premise) {
        return new Premise(premise);
    }

    boolean implies(final boolean conclusion) {
        return !premise || conclusion;
    }

    public boolean iff(final boolean conclusion) {
        return implies(conclusion) && new Premise(conclusion).implies(premise);
    }
}




