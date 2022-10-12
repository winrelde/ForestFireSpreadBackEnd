package com.firespread.service;

import com.firespread.dao.FireBoundryDao;
import com.firespread.entity.FireBoundry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 服务类
 * @author: DelucaWu
 * @createDate: 2022/5/16 11:24
 */
@Service
public class FireBoundryService {

    @Autowired
    private FireBoundryDao fireBoundryDao;

    public int insertinfo(FireBoundry fireBoundry){
        return fireBoundryDao.insertinfo(fireBoundry);
    }

    public String findBoundry(Double lon, Double lat, Integer userid){
        return fireBoundryDao.findBoundry(lon,lat,userid);
    }

    public List<FireBoundry> findAllBoundry(Integer userid){
        return fireBoundryDao.findAllBoundry(userid);
    }
}
