package org.misha.utils;

import org.misha.domain.algebra.associative.Polynomial;
import org.misha.domain.algebra.associative.impl.Monomial;
import org.misha.domain.algebra.fox.Derivative;
import org.misha.views.JacobiMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Author: mshevelin
 * Date: 6/4/14
 * Time: 4:13 PM
 */

public final class MatrixFormatter {
    private static final int FAT = 3;
    private static final String LINE_BREAK = "\n";
    private static final String SPACE = " ";
    private static final String BORDER_FRAGMENT = "||";
    private final JacobiMatrix matrix;
    private final List<Integer> columnWidths;

    private MatrixFormatter(final JacobiMatrix m) {
        matrix = m;
        columnWidths = new ArrayList<Integer>();
    }

    public static MatrixFormatter get(final JacobiMatrix matrix) {
        final MatrixFormatter result = new MatrixFormatter(matrix);
        final List<Integer> columnWidths = result.columnWidths;
        initColumnWidths(matrix, columnWidths);
        columnWidths(matrix, columnWidths);
        return result;
    }

    private static void initColumnWidths(final Iterable<Derivative> matrix, List<Integer> columnWidths) {
        if (matrix.iterator().hasNext()) {
            for (final Entry<Monomial, Polynomial> ignored : matrix.iterator().next()) {
                columnWidths.add(2);
            }
        }
    }

    private static String blow(final int b, final String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b; ++i) {
            sb = sb.append(SPACE);
        }
        final String blow = sb.toString();
        return String.format("%s%s%s", blow, s, blow);
    }

    private static void columnWidths(final JacobiMatrix matrix, final List<Integer> columnWidths) {
        for (final Derivative derivative : matrix) {
            int j = 0;
            for (final Entry<Monomial, Polynomial> entry : derivative) {
                final int length = entry.getValue().toString().length();
                if (length > columnWidths.get(j)) {
                    columnWidths.set(j, length);
                }
                ++j;
            }
        }
    }

    public String toTxt() {
        StringBuilder sb = new StringBuilder(LINE_BREAK);
        final int maxRowWidth = findMaxRowWidth();
        for (final Derivative derivative : matrix) {
            StringBuilder sbRow = blowRow(derivative);
            if (sbRow.length() < maxRowWidth) {
                while (sbRow.length() < maxRowWidth) {
                    sbRow = sbRow.insert(sbRow.length() - 4, SPACE);
                }
            }
            sb = sb.append(sbRow).append(LINE_BREAK);
        }
        return sb.toString();
    }

    private int findMaxRowWidth() {
        int rowLength = 0;
        for (final Derivative derivative : matrix) {
            final int length = blowRow(derivative).length();
            if (length > rowLength) {
                rowLength = length;
            }
        }
        return rowLength;
    }

    private StringBuilder blowRow(final Iterable<Entry<Monomial, Polynomial>> derivative) {
        int j = 0;
        StringBuilder sbRow = new StringBuilder(BORDER_FRAGMENT);
        for (final Entry<Monomial, Polynomial> entry : derivative) {
            final String value = entry.getValue().toString();
            final int blowFactor = (columnWidths.get(j) - value.length()) / 2;
            sbRow = sbRow.append(blow(blowFactor + FAT, value));
            ++j;
        }
        sbRow = sbRow.append(BORDER_FRAGMENT);
        return sbRow;
    }

    public String toHtml() {
        StringBuilder sb = new StringBuilder(
                "<table align=\'center\' cellpadding=\'3\' frame='vsides'>\n" +
                        "<tr>\n" +
                        "<td>" +
                        "<table align=\'center\' cellpadding=\'12\' frame=\'vsides\'>"
        );
        for (final Derivative derivative : matrix) {
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
}
