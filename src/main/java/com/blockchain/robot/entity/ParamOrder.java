package com.blockchain.robot.entity;

/**
 * 创建订单 参数
 */
public class ParamOrder {

    private String symbol;//LTCBTC
    private String side;//BUY
    private String type;//LIMIT
    private String timeInForce;//GTC
    private double quantity;//1
    private String price;//0.1
    private long recvWindow;//本次请求多少时间内有效，为了方式多重攻击，设置5-10秒左右
    private long timestamp;//1499827319559
    private String signature;//签名

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getRecvWindow() {
        return recvWindow;
    }

    public void setRecvWindow(long recvWindow) {
        this.recvWindow = recvWindow;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "symbol=" + symbol + "&side=" + side + "&type=" + type + "&timeInForce=" + timeInForce + "&quantity=" + quantity + "&price=" + price + "&recvWindow=" + recvWindow + "&timestamp=" + timestamp;
    }
}
