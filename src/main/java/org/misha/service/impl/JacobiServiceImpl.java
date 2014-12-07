package org.misha.service.impl;

import org.apache.log4j.Logger;
import org.misha.algebra.lie.endomorphism.Endo;
import org.misha.algebra.parser.Parser;
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
    public String jacobi(final String s) {
        try {
            final Endo endo = new Parser("").parseEndo(s);
            return endo.fox().toHtml();
        } catch (final Exception e) {
            log.error(e.getMessage());
            return ErrorMessage.logErrorForUser(e.getMessage());
        }
    }

    @Override
    public String toTxt(final String s) {
        final Endo endo = new Parser("").parseEndo(s);
        return endo.fox().toTxt();
    }
}