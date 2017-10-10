package com.blockchain.robot.entity;

/**
 * 订单结构
 */
public class Order {

    private String Info;        //交易所返回的原始结构
    private String Id;          //交易单唯一标识
    private double Price;       //	下单价格
    private double Amount;      //下单数量
    private double DealAmount;  //成交数量
    private int Status;         //订单状态, 参考常量里的订单状态
    private int Type;           //订单类型, 参考常量里的订单类型

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public double getDealAmount() {
        return DealAmount;
    }

    public void setDealAmount(double dealAmount) {
        DealAmount = dealAmount;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
