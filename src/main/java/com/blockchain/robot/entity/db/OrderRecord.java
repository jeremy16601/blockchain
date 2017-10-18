package com.blockchain.robot.entity.db;

import org.springframework.data.annotation.Id;

/**
 * 订单记录 存数据库
 */
public class OrderRecord {

    @Id
    private String id;
    private String time;
    private String exchange;
    private String strategy;
    private String symbol;

    private String buyOrderId;
    private double buyPrice;
    private int buyStatus;//0买单未成交 1买单全部成交

    private String sellOrderId;
    private double sellPrice;
    private int sellStatus;//0卖单未成交 1卖单全部成交

    private double earnings;

    private int status;//0 已下卖单，未下买单 1买单卖单都已下单 2卖买单都已成交（结束）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public void setBuyOrderId(String buyOrderId) {
        this.buyOrderId = buyOrderId;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(String sellOrderId) {
        this.sellOrderId = sellOrderId;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getBuyStatus() {
        return buyStatus;
    }

    public void setBuyStatus(int buyStatus) {
        this.buyStatus = buyStatus;
    }

    public int getSellStatus() {
        return sellStatus;
    }

    public void setSellStatus(int sellStatus) {
        this.sellStatus = sellStatus;
    }
}
