package com.blockchain.robot.entity;

import java.util.List;

/**
 * 市场深度
 */
public class Depth {

    private String info;
    private List<MarketOrder> asks;//卖单数组
    private List<MarketOrder> bids;//买单数组

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<MarketOrder> getAsks() {
        return asks;
    }

    public void setAsks(List<MarketOrder> asks) {
        this.asks = asks;
    }

    public List<MarketOrder> getBids() {
        return bids;
    }

    public void setBids(List<MarketOrder> bids) {
        this.bids = bids;
    }
}
