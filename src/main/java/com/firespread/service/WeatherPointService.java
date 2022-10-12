package com.firespread.service;

import com.firespread.dao.WeatherPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 服务类
 * @author: DelucaWu
 * @createDate: 2022/5/16 11:24
 */
@Service
public class WeatherPointService {

    @Autowired
    private WeatherPointDao WeatherPointDao;

    public String findweatherPoint(Double lon, Double lat){
        return WeatherPointDao.findweatherPoint(lon,lat);
    }
}
