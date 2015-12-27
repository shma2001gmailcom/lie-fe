package org.misha.algebra.lie.polynomial.monomial;

import org.apache.log4j.Logger;
import org.misha.repository.SpringJdbcTemplates;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * author: misha
 * date: 12/27/15 1:38 PM.
 */
@Repository
public class MonomialService {
    private static final Logger log = Logger.getLogger(MonomialService.class);
    @Inject
    @Named("springJdbcTemplates")
    private final SpringJdbcTemplates templates;

    public MonomialService(final SpringJdbcTemplates springJdbcTemplates) {
        templates = springJdbcTemplates;
    }

    private static final String findById = "SELECT " +
            "n.node_id, " +
            "n.left_id, " +
            "n.right_id, " +
            "d.data_value " +
            "FROM NODES n " +
            "JOIN NODE_DATA d ON (n.node_id = d.node_id) " +
            "WHERE n.node_id = :nodeId";
    private static final String findBySmallId = "SELECT d.data_value FROM NODE_DATA d WHERE d.node_id = :nodeId";

    public Monomial findById(final Long id) {
        if (id < 26L) {
            return findBySmallId(id);
        }
        final MonomialData data = templates.getTemplate().query(findById, new HashMap<String, Long>() {{
                                                                    put("nodeId", id);
                                                                }}, new RowMapper<MonomialData>() {

                                                                    @Override
                                                                    public MonomialData mapRow(
                                                                            final ResultSet rs, final int rowNum
                                                                    ) throws SQLException {
                                                                        return new MonomialData(rs.getLong("left_id"),
                                                                                                rs.getLong("right_id")
                                                                        );
                                                                    }
                                                                }
        ).get(0);
        return MonomialUtils.monomial(findById(data.leftId), findById(data.rightId));
    }

    private static class MonomialData {
        private final Long leftId;
        private final Long rightId;

        private MonomialData(final Long left, final Long right) {
            leftId = left;
            rightId = right;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final MonomialData that = (MonomialData) o;
            return !(leftId != null ? !leftId.equals(that.leftId) : that.leftId != null) && !(rightId != null ? !rightId
                    .equals(that.rightId) : that.rightId != null);
        }

        @Override
        public int hashCode() {
            int result = leftId != null ? leftId.hashCode() : 0;
            result = 31 * result + (rightId != null ? rightId.hashCode() : 0);
            return result;
        }
    }

    private Monomial findBySmallId(final Long id) {
        return templates.getTemplate().query(findBySmallId, new HashMap<String, Long>() {{
                                                 put("nodeId", id);
                                             }}, new RowMapper<Monomial>() {

                                                 @Override
                                                 public Monomial mapRow(final ResultSet rs, final int rowNum) throws
                                                                                                              SQLException {
                                                     return MonomialUtils.monomial(rs.getString("data_value"));
                                                 }
                                             }
        ).get(0);
    }

    public static void main(final String... args) {
        final BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
        log.debug(((MonomialService) factory.getBean("monomialService")).findById(27L));
    }
}
