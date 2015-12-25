package org.misha.jdbc;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 * author: misha
 * date: 12/19/15 2:31 PM.
 */
public class JdbcDao {

    private NamedParameterJdbcTemplate template;
    @Inject
    @Named("dataSource")
    private DataSource dataSource;

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        template = new NamedParameterJdbcTemplate(dataSource);
    }

    public NamedParameterJdbcTemplate getTemplate() {
        return template;
    }
}
