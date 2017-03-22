package org.misha.domain.algebra.lie.polynomial.monomial;

/**
 * author: misha
 * date: 3/15/17.
 */
public class Occurrence {
    private final Monomial m;
    private final Monomial parent;

    private Occurrence(Monomial m, Monomial parent) {
        this.parent = parent;
        this.m = m;
    }

    static Occurrence occurrence(final Monomial m) {
        if(m == null) throw  new IllegalArgumentException("unable create occurrence");
        return new Occurrence(m, m.getParent());
    }

    public Monomial getMonomial() {
        return m;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Occurrence that = (Occurrence) o;
        return m.equals(that.m)  && (parent != null ? parent.equals(that.parent) : that.parent == null);
    }

    @Override
    public int hashCode() {
        int result = m != null ? m.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
