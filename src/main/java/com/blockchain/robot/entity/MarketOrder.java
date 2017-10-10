package com.blockchain.robot.entity;

/**
 * 市场深度单
 */
public class MarketOrder {

    private double price;
    private double amount;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
