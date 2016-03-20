package org.misha.service.impl;

import org.apache.log4j.Logger;
import org.misha.domain.algebra.parser.Parser;
import org.misha.service.HallService;

import javax.inject.Named;

import static org.misha.utils.ErrorMessage.logErrorForUser;

/**
 * Author: mshevelin
 * Date: 5/19/14
 * Time: 1:31 PM
 */
@Named("hallService")
public final class HallServiceImpl implements HallService {
    private static final Logger log = Logger.getLogger(HallService.class);

    @Override
    public String getHall(final String data) {
        try {
            final Parser parser = new Parser(data);
            return parser.parse().hall().toString();
        } catch (final Exception e) {
            log.error(e.getMessage());
            return logErrorForUser(e.getMessage());
        }
    }
}

