package org.misha.jdbc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

/**
 * author: misha
 * date: 12/25/15 10:45 PM.
 */

public class MishaDao {
    private final JdbcDao jdbcDao;

    public MishaDao(final JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void insertIntoMisha() {
        final String sql = "INSERT INTO misha VALUES (NULL, :name, :value)";
        jdbcDao.getTemplate().update(sql, new HashMap<String, Object>() {{
                                         put("name", "misha");
                                         put("value", "misha shevelin");
                                     }}
        );
    }

    public static void main(final String... args) {
        final BeanFactory context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ((MishaDao) context.getBean("mishaDao")).insertIntoMisha();
    }
}
