package org.misha.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.misha.algebra.lie.endomorphism.Endo;
import org.misha.algebra.parser.Parser;
import org.misha.service.EndoService;
import org.misha.utils.ErrorMessage;

import javax.inject.Named;

/**
 * author: misha
 * Date: 5/22/14
 * Time: 9:58 PM
 */

@Named("endoService")
public final class EndoServiceImpl implements EndoService {
    private static final Logger log = Logger.getLogger(EndoServiceImpl.class);

    @Override
    public String getProductOf(final String s) {
        final String[] factors = StringUtils.split(s, "*");
        final int factorsCount = factors.length;
        if (factorsCount <= 1) {
            return "Error: the factors count should be more then one.";
        }
        try {
            Endo leftFactor = new Parser("").parseEndo(factors[0]);
            for (int i = 1; i < factorsCount; ++i) {
                final Endo rightFactor = new Parser("").parseEndo(factors[i]);
                leftFactor = leftFactor.times(rightFactor);
            }
            return leftFactor.toString();
        } catch (final IllegalArgumentException e) {
            log.error(e.getMessage());
            return ErrorMessage.logErrorForUser(e.getMessage());
        }
    }
}
