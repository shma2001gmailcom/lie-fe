package org.misha.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: mshevelin
 * Date: 5/19/14
 * Time: 1:55 PM
 */

public final class PolynomialObject {
    private String value;

    public String getName() {
        return "polynomial";
    }

    public String getValue() {
        return value;
    }

    @SuppressWarnings("unused declaration")
    public void setValue(final String value) {
        this.value = value;
    }

    public void correctLeadingSign() {
        final Pattern pattern = Pattern.compile("([\\+\\-])+(.*)");
        final Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            value = "+" + value;
        }
    }
}
