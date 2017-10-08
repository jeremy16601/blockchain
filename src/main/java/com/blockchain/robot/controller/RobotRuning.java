package com.blockchain.robot.controller;

import com.blockchain.robot.entity.BuyPoint;
import com.blockchain.robot.entity.ParamOrder;
import com.blockchain.robot.entity.TwentyFourHoursPrice;
import com.blockchain.robot.service.BinanceAPIService;
import com.blockchain.robot.util.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class RobotRuning {

    @Autowired
    private BinanceAPIService binanceAPIService;

    private Stack<BuyPoint> buyPointStack = new Stack<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private double btc_price;//BTC人民币价格

    @Scheduled(fixedRate = 5000)
    private void BTCPrice() {
        TwentyFourHoursPrice response = binanceAPIService.price_statistics_24hr("BTCUSDT");

        try {
            double lastPrice = Double.parseDouble(response.getLastPrice());
            btc_price = lastPrice * 6.65;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 10000)
    private void process() {

        String out = "";//日志输出
        double base_rate = 0.01;
        String symbol = "NEOBTC";

        TwentyFourHoursPrice response = binanceAPIService.price_statistics_24hr(symbol);

        try {

            double HPoint = Double.parseDouble(response.getHighPrice());
            double LPoint = Double.parseDouble(response.getLowPrice());
            double GPoint = (HPoint - LPoint) * 0.618 + LPoint;
            double CPoint = Double.parseDouble(response.getLastPrice());

            if (buyPointStack.isEmpty()) {

                if (CPoint <= GPoint * (1 - base_rate)) {//95%买入
                    buyPointStack.push(new BuyPoint(HPoint, LPoint, CPoint, GPoint));
                    buyOrder(symbol, CPoint);
                    out += "\n==买入操作C:" + String.format("%.8f", CPoint) + "G:" + String.format("%.8f", GPoint);
                } else {
                    out += "\n==不是买入点,买入点是：" + String.format("%.8f", GPoint * (1 - base_rate));
                }

            } else {
                int count = buyPointStack.size();
                BuyPoint preBuyPoint = buyPointStack.peek();
                double rate = 1 - base_rate * (count + 1);

                double point1 = preBuyPoint.gethPoint() * rate;
                double point2 = GPoint * rate;
                double point3 = preBuyPoint.getcPoint() * (1 - base_rate * count);

                if (CPoint <= point2 && CPoint <= point1 && CPoint <= point3) {
                    buyPointStack.push(new BuyPoint(HPoint, LPoint, CPoint, GPoint));
                    buyOrder(symbol, CPoint);
                    out += "\n==第" + (count + 1) + "买入操作C:" + String.format("%.8f", CPoint) + "G:" + String.format("%.8f", GPoint);
                } else {
                    out += "\n**不是买入点,买入点是:" + String.format("%.8f", Math.min(Math.min(point1, point2), point3));
                }

            }


            //卖出操作
            if (!buyPointStack.isEmpty()) {
                BuyPoint topBuyPoint = buyPointStack.peek();
                if (CPoint >= topBuyPoint.getcPoint() * (1 + base_rate)) {
                    out += "\n==卖出操作，买入点" + topBuyPoint.getcPoint() + "卖出点" + CPoint;
                    sellOrder(symbol, CPoint);
                    buyPointStack.pop();
                } else {
                    out += "\n**不是卖出点,卖出点是：" + String.format("%.8f", topBuyPoint.getcPoint() * (1 + base_rate));
                }
            }


            logger.info(String.format(symbol + "最新价格%.8f（¥%.2f）,最高价格%.8f,最低价格%.8f。", CPoint, CPoint * btc_price, HPoint, LPoint) + out);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void buyOrder(String symbol, double price) {
        long time = System.currentTimeMillis();

        ParamOrder param = new ParamOrder();
        param.setSymbol(symbol);
        param.setSide("BUY");
        param.setType("LIMIT");
        param.setTimeInForce("GTC");
        param.setQuantity(100);
        param.setPrice(String.format("%.8f", price));
        param.setRecvWindow(10000);
        param.setTimestamp(time);

        String secret = "";
        String hash = SHA256.signature(secret, param.toString());
        param.setSignature(hash);

        binanceAPIService.new_order("",
                param.getSymbol(),
                param.getSide(),
                param.getType(),
                param.getTimeInForce(),
                param.getQuantity(),
                param.getPrice(),
                param.getRecvWindow(),
                param.getTimestamp(),
                param.getSignature());
    }


    private void sellOrder(String symbol, double price) {

        long time = System.currentTimeMillis();
        ParamOrder param = new ParamOrder();
        param.setSymbol(symbol);
        param.setSide("SELL");
        param.setType("LIMIT");
        param.setTimeInForce("GTC");
        param.setQuantity(100);
        param.setPrice(String.format("%.8f", price));
        param.setRecvWindow(10000);
        param.setTimestamp(time);

        String secret = "";
        String hash = SHA256.signature(secret, param.toString());
        param.setSignature(hash);

        binanceAPIService.new_order("",
                param.getSymbol(),
                param.getSide(),
                param.getType(),
                param.getTimeInForce(),
                param.getQuantity(),
                param.getPrice(),
                param.getRecvWindow(),
                param.getTimestamp(),
                param.getSignature());
    }

}
