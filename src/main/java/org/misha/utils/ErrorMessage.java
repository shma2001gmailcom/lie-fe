package org.misha.utils;

/**
 * Author: mshevelin
 * Date: 5/30/14
 * Time: 5:03 PM
 */
public final class ErrorMessage {
    private ErrorMessage() {
    }

    public static String logErrorForUser(final String message) {
        return String.format("<span class='error-message'> ERROR: %s</span>", message);
    }
}
