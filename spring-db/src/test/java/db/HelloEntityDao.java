package db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;


@Repository
public class HelloEntityDao extends JdbcDaoSupport {
    @Autowired
    public HelloEntityDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void persist(HelloEntity entity) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("HelloEntity");
        Map<String, Object> parameters = Collections.<String, Object>singletonMap("id", entity.getId());
        insert.execute(parameters);
    }

    public HelloEntity load(String id) {
        return getJdbcTemplate().queryForObject("select * from HelloEntity where id=?", new RowMapper<HelloEntity>() {
            @Override
            public HelloEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new HelloEntity().id(rs.getString("id"));
            }
        }, id);
    }
}
