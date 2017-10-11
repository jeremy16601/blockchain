package com.blockchain.robot.service;

import com.blockchain.robot.entity.Account;
import com.blockchain.robot.entity.Order;
import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.Ticker;
import com.blockchain.robot.entity.binance.NewOrder;
import com.blockchain.robot.entity.binance.ParamOrder;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.util.SHA256;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:auth.properties")
public class BinanceExchangeService implements IExchangeAPIService {

    @Autowired
    private BinanceHttpClient binanceAPIService;

    @Value("${binance.api_key}")
    private String api_key;

    @Value("${binance.api_secrect}")
    private String api_secret;

    //TODO 后期提出来
    private String symbol = "SNMBTC";

    @Override
    public String getName() {
        return "币安";
    }

    @Override
    public Ticker getTicker() {

        TwentyFourHoursPrice hoursPrice = binanceAPIService.price_statistics_24hr(symbol);

        Ticker ticker = new Ticker();
        try {

            ticker.setLast(Double.parseDouble(hoursPrice.getLastPrice()));
            ticker.setHigh(Double.parseDouble(hoursPrice.getHighPrice()));
            ticker.setLow(Double.parseDouble(hoursPrice.getLowPrice()));

            ticker.setBuy(Double.parseDouble(hoursPrice.getBidPrice()));
            ticker.setSell(Double.parseDouble(hoursPrice.getAskPrice()));
            ticker.setVolume(Double.parseDouble(hoursPrice.getVolume()));


        } catch (Exception e) {
            e.printStackTrace();
        }


        return ticker;
    }

    @Override
    public List<Record> getRecords(String period, int limit) {
        List<List<String>> kLines = binanceAPIService.kline(symbol, period, limit);
        List<Record> recordList = new ArrayList<>();

        for (List<String> kline : kLines) {
            Record record = new Record();
            record.setTime(Long.parseLong(kline.get(0)));
            record.setOpen(Double.parseDouble(kline.get(1)));
            record.setHigh(Double.parseDouble(kline.get(2)));
            record.setLow(Double.parseDouble(kline.get(3)));
            record.setClose(Double.parseDouble(kline.get(4)));
            record.setVolume(Double.parseDouble(kline.get(5)));
            recordList.add(record);
        }

        return recordList;
    }

    @Override
    public Account getAccount() {
        return null;
    }

    @Override
    public String buy(double price, double amount) {

        long time = System.currentTimeMillis();

        ParamOrder param = new ParamOrder();
        param.setSymbol(symbol);
        param.setSide("BUY");
        param.setType("LIMIT");
        param.setTimeInForce("GTC");
        param.setQuantity(amount);
        param.setPrice(String.format("%.8f", price));
        param.setRecvWindow(5000);
        param.setTimestamp(time);

        String hash = SHA256.signature(api_secret, param.toString());
        param.setSignature(hash);

        NewOrder order = binanceAPIService.new_order(api_key,
                param.getSymbol(),
                param.getSide(),
                param.getType(),
                param.getTimeInForce(),
                param.getQuantity(),
                param.getPrice(),
                param.getRecvWindow(),
                param.getTimestamp(),
                param.getSignature());


        return order.getOrderId() + "";
    }

    @Override
    public String sell(double price, double amount) {

        long time = System.currentTimeMillis();
        ParamOrder param = new ParamOrder();
        param.setSymbol(symbol);
        param.setSide("SELL");
        param.setType("LIMIT");
        param.setTimeInForce("GTC");
        param.setQuantity(amount);
        param.setPrice(String.format("%.8f", price));
        param.setRecvWindow(5000);
        param.setTimestamp(time);

        String hash = SHA256.signature(api_secret, param.toString());
        param.setSignature(hash);

        NewOrder order = binanceAPIService.new_order(api_key,
                param.getSymbol(),
                param.getSide(),
                param.getType(),
                param.getTimeInForce(),
                param.getQuantity(),
                param.getPrice(),
                param.getRecvWindow(),
                param.getTimestamp(),
                param.getSignature());

        return order.getOrderId() + "";
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

        TwentyFourHoursPrice response = binanceAPIService.price_statistics_24hr("BTCUSDT");
        double btc_price = 0;
        try {
            double lastPrice = Double.parseDouble(response.getLastPrice());
            btc_price = lastPrice * 6.65;//6.65美元人民币汇率
        } catch (Exception e) {
            e.printStackTrace();
        }

        return btc_price;
    }
}