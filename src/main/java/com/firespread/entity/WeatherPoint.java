package com.firespread.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

/**
 * @description: 实体类
 * @author: DelucaWu
 * @createDate: 2022/5/16 11:16
 */
@Component
@ApiModel("气象站点实体类")
public class WeatherPoint {

    @ApiModelProperty(value = "lon", dataType = "double")
    private Double lon;
    @ApiModelProperty(value = "lat", dataType = "double")
    private Double lat;
    @ApiModelProperty(value = "zh", dataType = "string")
    private String zh;


    public WeatherPoint() {
    }

    public WeatherPoint(Double lon, Double lat, String zh) {
        this.lon = lon;
        this.lat = lat;
        this.zh = zh;
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

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    @Override
    public String toString() {
        return "WeatherPoint{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", zh='" + zh + '\'' +
                '}';
    }
}
