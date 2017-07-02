package sjs.fy.opt.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/12.
 * 处理数据库相关的事情
 */
@Component
public class DBService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 插入sql并且返回自动生成的ID
     * @param sql
     * @return
     */
    public int insertSql(String sql,String pk_id) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
                PreparedStatement ps = connection.prepareStatement(sql,new String[]{pk_id});
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * 执行单条Sql
     * @param sql
     */
    public void executeSql(String sql){
        jdbcTemplate.execute(sql);
    }

    /**
     * 通过sql获取数据信息
     * @param sql
     * @return
     */
    public List<Map<String,Object>> listInfos(String sql){
        return jdbcTemplate.queryForList(sql);
    }
}