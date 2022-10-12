package com.firespread.entity;

import org.springframework.stereotype.Component;

/**
 * @description: 实体类
 * @author: DelucaWu
 * @createDate: 2022/5/10 20:19
 */
@Component
public class LatAndLogEntity {
    private double lat;
    private double log;
    private double latMedium;
    private double logMedium;

    private double x;
    private double y;
    private double xMedium;
    private double yMedium;

    public LatAndLogEntity() {
    }

    public LatAndLogEntity(double lat, double log, double latMedium, double logMedium, double x, double y, double xMedium, double yMedium) {
        this.lat = lat;
        this.log = log;
        this.latMedium = latMedium;
        this.logMedium = logMedium;
        this.x = x;
        this.y = y;
        this.xMedium = xMedium;
        this.yMedium = yMedium;
    }

    @Override
    public String toString() {
        return "LatAndLogEntity{" +
                "lat=" + lat +
                ", log=" + log +
                ", latMedium=" + latMedium +
                ", logMedium=" + logMedium +
                ", x=" + x +
                ", y=" + y +
                ", xMedium=" + xMedium +
                ", yMedium=" + yMedium +
                '}';
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public double getLatMedium() {
        return latMedium;
    }

    public void setLatMedium(double latMedium) {
        this.latMedium = latMedium;
    }

    public double getLogMedium() {
        return logMedium;
    }

    public void setLogMedium(double logMedium) {
        this.logMedium = logMedium;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getxMedium() {
        return xMedium;
    }

    public void setxMedium(double xMedium) {
        this.xMedium = xMedium;
    }

    public double getyMedium() {
        return yMedium;
    }

    public void setyMedium(double yMedium) {
        this.yMedium = yMedium;
    }
}
