package org.misha.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * Author: mshevelin
 * Date: 6/17/14
 * Time: 3:53 PM
 */

@Aspect
class MyLogger {
    private final Logger log = Logger.getLogger(getClass());

    @AfterReturning(value = "execution(* org.misha.domain.algebra.*.*(..))", returning = "retVal")
    public Object log(final JoinPoint point, final Object retVal) {
        log.info(point.getSignature().getName() + " called...");
        return retVal;
    }
}
