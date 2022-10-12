package com.firespread.dao;

import com.firespread.entity.FireBoundry;

import java.util.List;

/**
 * @description: DAO类
 * @author: DelucaWu
 * @createDate: 2022/5/16 11:21
 */
public interface WeatherPointDao {

    public String findweatherPoint(Double lon,Double lat);
}
