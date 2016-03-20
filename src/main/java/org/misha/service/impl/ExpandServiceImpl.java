package org.misha.service.impl;

import org.apache.log4j.Logger;
import org.misha.domain.algebra.parser.Parser;
import org.misha.service.ExpandService;
import org.misha.utils.ErrorMessage;

import javax.inject.Named;

/**
 * author: misha
 * Date: 5/26/14
 * Time: 10:23 PM
 */
@Named("expandService")
public final class ExpandServiceImpl implements ExpandService {
    private static final Logger log = Logger.getLogger(ExpandServiceImpl.class);

    @Override
    public String expand(final String s) {
        try {
            return new Parser(s).parse().expand().toString();
        } catch (final Exception e) {
            log.error(e.getMessage());
            return ErrorMessage.logErrorForUser(e.getMessage());
        }
    }
}
