package com.firespread.Impl;

import com.firespread.dao.FireBoundryDao;
import com.firespread.entity.FireBoundry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @description: 实现类
 * @author: DelucaWu
 * @createDate: 2022/5/16 11:23
 */
@Repository
public class FireBoundryDaoImpl implements FireBoundryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insertinfo(FireBoundry fireBoundry){
        String sql = "INSERT INTO fireboundry(lon,lat,info,inserttime,userid) VALUES(?,?,?,NOW(),?)";
        return jdbcTemplate.update(sql,new Object[]{
                fireBoundry.getLon(),
                fireBoundry.getLat(),
                fireBoundry.getInfo(),
                fireBoundry.getUserid(),
        });
    }

    @Override
    public String findBoundry(Double lon, Double lat, Integer userid){
        String sql="SELECT info FROM fireboundry WHERE lon=? AND lat=? AND userid=? ORDER BY inserttime DESC LIMIT 1";
        String aa = jdbcTemplate.queryForObject(sql,String.class,new Object[]{lon,lat,userid});
        return aa;
    }

    @Override
    public List<FireBoundry> findAllBoundry(Integer userid){
        String sql="SELECT * FROM fireboundry WHERE userid=?";
        return jdbcTemplate.query(sql, new Object[]{userid}, new RowMapper<FireBoundry>(){
            @Nullable
            @Override
            public FireBoundry mapRow(ResultSet resultSet, int i) throws SQLException {
                return new FireBoundry(
                        resultSet.getDouble("lon"),
                        resultSet.getDouble("lat"),
                        resultSet.getString("info"),
                        resultSet.getDate("inserttime"),
                        resultSet.getInt("userid")
                );
            }
        });
    }

}
