package org.misha.repository;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * author: misha
 * date: 12/27/15 1:05 AM.
 */
@Repository
@EnableTransactionManagement
class PolynomialDao {
    @Inject
    private SpringJdbcTemplates springJdbcTemplates;

    public void setSpringJdbcTemplates(final SpringJdbcTemplates springJdbcTemplates) {
        this.springJdbcTemplates = springJdbcTemplates;
    }

    void createPolynomial() {
        executeCall("new_polynomial");
    }

    @Transactional
    private void executeCall(final String callName) {
        SimpleJdbcCall call = springJdbcTemplates.getJdbcCall().withFunctionName("new_polynomial");
        //SqlParameterSource in = new MapSqlParameterSource().addValue("in_id", id);
        Long id = call.executeFunction(Long.class);
        System.out.println(id);
    }

    public static void main(final String... args) {
        final BeanFactory context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ((PolynomialDao) context.getBean("polynomialDao")).createPolynomial();
    }
}
