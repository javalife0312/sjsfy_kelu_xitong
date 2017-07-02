package sjs.fy.opt.api.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Created by jinguowei on 16/10/26.
 */
@Component
public class DBService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 执行单条Sql
     * @param sql
     */
    public void executeSql(String sql){
        jdbcTemplate.execute(sql);
    }

    /**
     * 同时支持 批处理
     * @param sqls
     */
    public void executeSqls(List<String> sqls){
        jdbcTemplate.batchUpdate((String[])sqls.toArray());
    }

    /**
     * 通过sql获取数据信息
     * @param sql
     * @return
     */
    public List<Map<String,Object>> listInfos(String sql){
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 执行插入操作，返回刚插入生成的ID
     * @param sql
     * @return
     */
    public  Long insertAndGetKey(final String sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);
        Long generatedId = keyHolder.getKey().longValue();
        return generatedId;
    }


}
