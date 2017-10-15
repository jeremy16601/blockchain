package com.blockchain.robot.entity;

/**
 * 订单结构
 */
public class Order {

    private String info;        //交易所返回的原始结构
    private String id;          //交易单唯一标识
    private double price;       //	下单价格
    private double amount;      //下单数量
    private double dealAmount;  //成交数量
    private OrderStatus status;         //订单状态, 参考常量里的订单状态
    private int type;           //订单类型, 参考常量里的订单类型

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public double getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(double dealAmount) {
        this.dealAmount = dealAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
