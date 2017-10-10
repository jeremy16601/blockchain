package com.blockchain.robot.entity.binance;

/**
 * 下订单 返回的信息
 */
public class NewOrder {

    /**
     * symbol : LTCBTC
     * orderId : 1
     * clientOrderId : myOrder1
     * transactTime : 1499827319559
     */

    private String symbol;
    private int orderId;
    private String clientOrderId;
    private long transactTime;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public long getTransactTime() {
        return transactTime;
    }

    public void setTransactTime(long transactTime) {
        this.transactTime = transactTime;
    }
}
