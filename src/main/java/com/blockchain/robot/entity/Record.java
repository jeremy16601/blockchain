package com.blockchain.robot.entity;

/**
 * 标准OHLC结构, 用来画K线和指标分析用的
 */
public class Record {

    private double time;//一个时间戳, 精确到毫秒
    private double open;//开盘价
    private double high;//最高价
    private double low;//最低价
    private double close;//收盘价
    private double volume;//交易量

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
