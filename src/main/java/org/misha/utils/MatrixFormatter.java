package org.misha.utils;

import org.misha.algebra.associative.Polynomial;
import org.misha.algebra.associative.impl.Monomial;
import org.misha.algebra.fox.Derivative;
import org.misha.domain.JacobiMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final List<Integer> columnWidths = new ArrayList<Integer>();

    public MatrixFormatter(final JacobiMatrix matrix) {
        this.matrix = matrix;
        initColumnWidths(matrix);
        getMaxWidths();
    }

    private void initColumnWidths(final Iterable<Derivative> matrix) {
        if (matrix.iterator().hasNext()) {
            for (final Map.Entry<Monomial, Polynomial> ignored : matrix.iterator().next()) {
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
        return blow + s + blow;
    }

    private void getMaxWidths() {
        for (final Derivative derivative : matrix) {
            int j = 0;
            for (final Map.Entry<Monomial, Polynomial> entry : derivative) {
                final int length = entry.getValue().toString().length();
                if (length > columnWidths.get(j)) {
                    columnWidths.set(j, length);
                }
                ++j;
            }
        }
    }

    public String format() {
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
            if (blowRow(derivative).length() > rowLength) {
                rowLength = blowRow(derivative).length();
            }
        }
        return rowLength;
    }

    private StringBuilder blowRow(final Iterable<Map.Entry<Monomial, Polynomial>> derivative) {
        int j = 0;
        StringBuilder sbRow = new StringBuilder(BORDER_FRAGMENT);
        for (final Map.Entry<Monomial, Polynomial> entry : derivative) {
            final String value = entry.getValue().toString();
            final int blowFactor = (columnWidths.get(j) - value.length()) / 2;
            sbRow = sbRow.append(blow(blowFactor + FAT, value));
            ++j;
        }
        sbRow = sbRow.append(BORDER_FRAGMENT);
        return sbRow;
    }
}
