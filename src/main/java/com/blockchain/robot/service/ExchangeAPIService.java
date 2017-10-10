package com.blockchain.robot.service;

import com.blockchain.robot.entity.Account;
import com.blockchain.robot.entity.Order;
import com.blockchain.robot.entity.Ticker;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeAPIService implements IExchangeAPIService {

    @Autowired
    private BinanceAPIService binanceAPIService;
    //TODO 可以添加其他交易所的API


    @Override
    public String getName() {
        return "binance";
    }

    @Override
    public Ticker GetTicker() {

        TwentyFourHoursPrice hoursPrice = binanceAPIService.price_statistics_24hr("BNBBTC");

        Ticker ticker = new Ticker();
        try {
            double lastPrice = Double.parseDouble(hoursPrice.getLastPrice());
            ticker.setLast(lastPrice);





        } catch (Exception e) {
            e.printStackTrace();
        }


        return ticker;
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
}
