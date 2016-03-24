package org.misha.views;

import org.misha.domain.algebra.fox.Derivative;
import org.misha.utils.MatrixFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * author: misha
 * Date: 5/27/14
 * Time: 9:36 PM
 */

public final class JacobiMatrix implements Iterable<Derivative> {
    private final Collection<Derivative> derivatives = new ArrayList<Derivative>();

    @SuppressWarnings("unused declaration")
    public String getName() {
        return "jacobiMatrix";
    }

    public void add(final Derivative derivative) {
        derivatives.add(derivative);
    }

    @Override
    public Iterator<Derivative> iterator() {
        return derivatives.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final Derivative derivative : this) {
            sb = sb.append(derivative).append('\n');
        }
        return sb.toString();
    }

    public String toHtml() {
        return MatrixFormatter.get(this).toHtml();
    }

    public String toTxt() {
        return MatrixFormatter.get(this).toTxt();
    }

    public String getValue() {
        return toString();
    }
}
