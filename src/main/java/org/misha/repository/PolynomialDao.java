package org.misha.repository;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * author: misha
 * date: 12/27/15 1:05 AM.
 */
@Repository
public class PolynomialDao {
    @Inject
    private SpringJdbcTemplates springJdbcTemplates;

    public void  setSpringJdbcTemplates(final SpringJdbcTemplates springJdbcTemplates) {
        this.springJdbcTemplates = springJdbcTemplates;
    }

    public void createPolynomial() {
        executeCall("new_polynomial");
    }

    private void executeCall(final String callName) {
        springJdbcTemplates.getJdbcTemplate().update("call " + callName);
    }

    public static void main(final String... args) {
        final BeanFactory context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ((PolynomialDao) context.getBean("monomialDao")).createPolynomial();
    }
}
