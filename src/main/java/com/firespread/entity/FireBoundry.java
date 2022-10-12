package com.firespread.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description: 实体类
 * @author: DelucaWu
 * @createDate: 2022/5/16 11:16
 */
@Component
@ApiModel("蔓延边界实体类")
public class FireBoundry {

    @ApiModelProperty(value = "lon", dataType = "double")
    private Double lon;
    @ApiModelProperty(value = "lat", dataType = "double")
    private Double lat;
    @ApiModelProperty(value = "info", dataType = "string")
    private String info;
    @ApiModelProperty(value = "inserttime", dataType = "date")
    private Date inserttime;
    @ApiModelProperty(value = "userid", dataType = "int")
    private Integer userid;


    public FireBoundry() {
    }

    public FireBoundry(Double lon, Double lat, String info, Date inserttime) {
        this.lon = lon;
        this.lat = lat;
        this.info = info;
        this.inserttime = inserttime;
    }

    public FireBoundry(Double lon, Double lat, String info, Integer userid) {
        this.lon = lon;
        this.lat = lat;
        this.info = info;
        this.userid = userid;
    }

    public FireBoundry(Double lon, Double lat, String info, Date inserttime, Integer userid) {
        this.lon = lon;
        this.lat = lat;
        this.info = info;
        this.inserttime = inserttime;
        this.userid = userid;
    }

    public FireBoundry(Double lon, Double lat, String info) {
        this.lon = lon;
        this.lat = lat;
        this.info = info;
    }

    public FireBoundry(Double lon, Double lat, Date inserttime) {
        this.lon = lon;
        this.lat = lat;
        this.inserttime = inserttime;
    }

    public FireBoundry(String info) {
        this.info = info;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "FireBoundry{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", info='" + info + '\'' +
                ", inserttime=" + inserttime +
                ", userid=" + userid +
                '}';
    }
}
