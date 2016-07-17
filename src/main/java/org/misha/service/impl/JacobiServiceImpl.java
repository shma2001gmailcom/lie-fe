/*
 * Copyright (c) 2014. Misha's property, all rights reserved.
 */

package org.misha.service.impl;

import org.apache.log4j.Logger;
import org.misha.domain.algebra.lie.endomorphism.Endo;
import org.misha.domain.algebra.parser.Parser;
import org.misha.service.JacobiService;
import org.misha.utils.ErrorMessage;

import javax.inject.Named;

/**
 * Author: mshevelin
 * Date: 5/30/14
 * Time: 5:03 PM
 */

@Named("jacobiService")
public final class JacobiServiceImpl implements JacobiService {
    private static final Logger log = Logger.getLogger(JacobiServiceImpl.class);

    @Override
    public String foxToHtml(final String s) {
        try {
            final Endo endo = new Parser().parseEndo(s);
            return endo.fox().toHtml();
        } catch (final Exception e) {
            log.error(e.getMessage());
            return ErrorMessage.logErrorForUser(e.getMessage());
        }
    }

    @Override
    public String foxToTxt(final String s) throws IllegalArgumentException {
        final Endo endo = new Parser().parseEndo(s);
        return endo.fox().toTxt();
    }
}
