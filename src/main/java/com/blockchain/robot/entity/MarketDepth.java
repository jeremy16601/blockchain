package com.blockchain.robot.entity;

import java.util.List;

/**
 * 市场订单深度
 */
public class MarketDepth {

    /**
     * {
     * "lastUpdateId": 1027024,
     * "bids": [
     * [
     * "4.00000000",     // PRICE
     * "431.00000000",   // QTY
     * []                // Can be ignored
     * ]
     * ],
     * "asks": [
     * [
     * "4.00000200",
     * "12.00000000",
     * []
     * ]
     * ]
     * }
     */

    private int lastUpdateId;
    private List<List<String>> bids;//卖单
    private List<List<String>> asks;//买单

    public int getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(int lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public List<List<String>> getBids() {
        return bids;
    }

    public void setBids(List<List<String>> bids) {
        this.bids = bids;
    }

    public List<List<String>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<String>> asks) {
        this.asks = asks;
    }
}
