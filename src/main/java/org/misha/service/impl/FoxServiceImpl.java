package org.misha.service.impl;

import org.apache.log4j.Logger;
import org.misha.service.FoxService;
import org.misha.utils.ErrorMessage;

import javax.inject.Named;

import static org.misha.algebra.lie.polynomial.Polynomial.mount;

/**
 * author: misha
 * Date: 5/26/14
 * Time: 10:56 PM
 */
@Named("foxService")
public final class FoxServiceImpl implements FoxService {
    private static final Logger log = Logger.getLogger(FoxServiceImpl.class);

    @Override
    public String fox(final String s) {
        try {
            return mount(s).fox().toString();
        } catch (final Exception e) {
            log.error(e.getMessage());
            return ErrorMessage.logErrorForUser(e.getMessage());
        }
    }
}
