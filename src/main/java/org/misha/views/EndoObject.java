package org.misha.views;

/**
 * author: misha
 * Date: 5/22/14
 * Time: 9:07 PM
 */

public final class  EndoObject {
    private String value;

    @SuppressWarnings("unused declaration")
    public String getName() {
        return "endoObject";
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
