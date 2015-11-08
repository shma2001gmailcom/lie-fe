/*
 * Copyright (c) 2014. Misha's property, all rights reserved.
 */

package org.misha.service;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

/**
 * Author: mshevelin
 * Date: 8/19/14
 * Time: 2:58 PM
 */

public final class MailSender {

    public static final int A_PORT_NUMBER = 587;

    private MailSender() {
    }

    public static void main(final String[] args) throws Exception {
        final Email email = new SimpleEmail();
        email.setSmtpPort(A_PORT_NUMBER);
        email.setAuthenticator(
                new DefaultAuthenticator(
                        "shma2001@gmail.com", "Misha~9999"
                )
        );
        email.setDebug(false);
        email.setHostName("smtp.gmail.com");
        email.setFrom("me@gmail.com");
        email.setSubject("Hi");
        email.setMsg("This is a test mail.");
        email.addTo("shma2001@gmail.com");
        email.setStartTLSEnabled(true);
        email.send();
        //System.out.println("Mail has been sent!");
    }
}


