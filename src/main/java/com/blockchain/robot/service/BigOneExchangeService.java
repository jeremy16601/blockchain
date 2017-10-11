package com.blockchain.robot.service;

import com.blockchain.robot.entity.Account;
import com.blockchain.robot.entity.Order;
import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.Ticker;

import java.util.List;

public class BigOneExchangeService implements IExchangeAPIService{
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Ticker getTicker() {
        return null;
    }

    @Override
    public List<Record> getRecords(String period, int limit) {
        return null;
    }

    @Override
    public Account getAccount() {
        return null;
    }

    @Override
    public String buy(double price, double amount) {
        return null;
    }

    @Override
    public String sell(double price, double amount) {
        return null;
    }

    @Override
    public List<Order> getOrders() {
        return null;
    }

    @Override
    public Order getOrder(String orderId) {
        return null;
    }

    @Override
    public boolean cancelOrder(String orderId) {
        return false;
    }

    @Override
    public double btc_cnyPrice() {
        return 0;
    }
}
