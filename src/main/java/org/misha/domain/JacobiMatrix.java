package org.misha.domain;

import org.misha.algebra.associative.Polynomial;
import org.misha.algebra.associative.impl.Monomial;
import org.misha.algebra.fox.Derivative;
import org.misha.utils.MatrixFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

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
        StringBuilder sb = new StringBuilder(
                "<table align=\'center\' cellpadding=\'3\' frame='vsides'>\n" +
                        "<tr>\n" +
                        "<td>" +
                        "<table align=\'center\' cellpadding=\'12\' frame=\'vsides\'>"
        );
        for (final Derivative derivative : this) {
            sb = sb.append('\n').append("<tr align=\'center\'>\n");
            for (final Entry<Monomial, Polynomial> entry : derivative) {
                sb = sb.append("<td>\n");
                sb.append(entry.getValue().toString());
                sb = sb.append("</td>\n");
            }
            sb = sb.append('\n').append("</tr>");
        }
        sb = sb.append("</table></tr>\n</td></table>");
        return sb.toString();
    }

    public String toTxt() {
        return new MatrixFormatter(this).format();
    }

    public String getValue() {
        return toString();
    }
}
