package com.firespread.Impl;

import com.firespread.dao.FireBoundryDao;
import com.firespread.dao.WeatherPointDao;
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
public class WeatherPointDaoImpl implements WeatherPointDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String findweatherPoint(Double lon, Double lat){
        String sql="SELECT zh FROM (SELECT zh,ABS(lon-?)+ABS(lat-?) AS distance FROM fjqxz ORDER BY distance ASC LIMIT 1) t";
        String aa = jdbcTemplate.queryForObject(sql,String.class,new Object[]{lon,lat});
        return aa;
    }

}
