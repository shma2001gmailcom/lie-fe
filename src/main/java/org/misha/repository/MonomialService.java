package org.misha.repository;

import org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils;
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
    @Inject
    @Named("springJdbcTemplates")
    private SpringJdbcTemplates springJdbctemplates;

    public void setSpringJdbcTemplates(final SpringJdbcTemplates springJdbcTemplates) {
        this.springJdbctemplates = springJdbcTemplates;
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

    public org.misha.domain.algebra.lie.polynomial.monomial.Monomial findById(final Long id) {
        if (id < 26L) {
            return findBySmallId(id);
        }
        final MonomialData data = springJdbctemplates.getTemplate().query(findById,
                new HashMap<String, Long>() {{
                    put("nodeId", id);
                }},
                new RowMapper<MonomialData>() {

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
        org.misha.domain.algebra.lie.polynomial.monomial.Monomial
                result = MonomialUtils.monomial(findById(data.leftId), findById(data.rightId));
        result.setId(id);
        return result;
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

    public org.misha.domain.algebra.lie.polynomial.monomial.Monomial findBySmallId(final Long id) {
        return springJdbctemplates.getTemplate().query(findBySmallId,
                new HashMap<String, Long>() {{
                    put("nodeId", id);
                }},
                new RowMapper<org.misha.domain.algebra.lie.polynomial.monomial.Monomial>() {

                    @Override
                    public org.misha.domain.algebra.lie.polynomial.monomial.Monomial mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                        org.misha.domain.algebra.lie.polynomial.monomial.Monomial result = MonomialUtils.monomial(rs.getString("data_value"));
                        result.setId(id);
                        return result;
                    }
                }
        ).get(0);
    }



    private static final String writeMonomial = "select new_node(?, ?)";

    public Long write(final org.misha.domain.algebra.lie.polynomial.monomial.Monomial monomial) {
        return springJdbctemplates.getJdbcTemplate().queryForLong(writeMonomial, monomial.left().getId(), monomial.right().getId());
    }
}
