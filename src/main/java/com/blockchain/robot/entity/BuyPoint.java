package com.blockchain.robot.entity;

/**
 * 购买点位信息
 */
public class BuyPoint {

    private double hPoint;
    private double lPoint;
    private double cPoint;
    private double gPoint;

    public BuyPoint(double hPoint, double lPoint, double cPoint, double gPoint) {
        this.hPoint = hPoint;
        this.lPoint = lPoint;
        this.cPoint = cPoint;
        this.gPoint = gPoint;
    }

    public double gethPoint() {
        return hPoint;
    }

    public void sethPoint(double hPoint) {
        this.hPoint = hPoint;
    }

    public double getlPoint() {
        return lPoint;
    }

    public void setlPoint(double lPoint) {
        this.lPoint = lPoint;
    }

    public double getcPoint() {
        return cPoint;
    }

    public void setcPoint(double cPoint) {
        this.cPoint = cPoint;
    }

    public double getgPoint() {
        return gPoint;
    }

    public void setgPoint(double gPoint) {
        this.gPoint = gPoint;
    }
}
