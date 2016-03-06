package org.misha.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 * author: misha
 * date: 12/19/15 2:31 PM.
 */
@Repository
class SpringJdbcTemplates {
    private NamedParameterJdbcTemplate template;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall jdbcCall;
    @Inject
    @Named("dataSource")
    private DataSource dataSource;

    @Autowired
    public void setDataSource(final DataSource ds) {
        dataSource = ds;
        template = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcCall = new SimpleJdbcCall(dataSource);
    }

    public NamedParameterJdbcTemplate getTemplate() {
        return template;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public SimpleJdbcCall getJdbcCall() {
        return jdbcCall;
    }
}
