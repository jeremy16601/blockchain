package com.blockchain.robot.controller;

import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.Ticker;
import com.blockchain.robot.entity.binance.BuyPoint;
import com.blockchain.robot.entity.binance.ParamOrder;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.service.IExchangeAPIService;
import com.blockchain.robot.util.PriceFormatUtil;
import com.blockchain.robot.util.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RobotRunning {

    @Autowired
    @Qualifier("binanceExchangeService")
    private IExchangeAPIService exchange;

    private Stack<BuyPoint> buyPointStack = new Stack<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private double btc_price;//BTC人民币价格

    //    @Scheduled(fixedRate = 5000)
    private void BTCPrice() {
        btc_price = exchange.btc_cnyPrice();
    }

    //    @Scheduled(fixedRate = 10000)
    private void process() {

        String out = "";//日志输出
        double base_rate = 0.01;
        double amount = 100;
        String symbol = "NEOBTC";

        Ticker ticker = exchange.getTicker();

        try {

            double HPoint = ticker.getHigh();
            double LPoint = ticker.getLow();
            double GPoint = (HPoint - LPoint) * 0.618 + LPoint;
            double CPoint = ticker.getLast();

            if (buyPointStack.isEmpty()) {

                if (CPoint <= GPoint * (1 - base_rate)) {//95%买入
                    buyPointStack.push(new BuyPoint(HPoint, LPoint, CPoint, GPoint));
                    exchange.buy(CPoint, amount);

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
                    exchange.buy(CPoint, amount);
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
                    exchange.sell(CPoint, amount);
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


    /**
     * 守株待兔测试
     */
//    @Scheduled(fixedRate = 360000)
//    private void waitForWindfallsTest() {
//
//        List<Record> recordList = exchange.getRecords("1m", 500);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        int amount = 1000;
//        double earnings = 0;
//
//        for (Record record : recordList) {
//
//            double currentPrice = record.getClose();
//            double openPrice = record.getOpen();
//
//
//            //买入操作
//            double rate = currentPrice / openPrice;
//            if (rate < 1) {
//                double range = (1 - rate) * 100;
//                if (range >= 1.3) {
//                    buyPrice.add(currentPrice);
//                    logger.info("时间" + sdf.format(new Date(record.getTime())) + "买入价格" + String.format("%.8f", currentPrice) + "交易量" + record.getVolume());
//                }
//            }
//
//            //卖出操作
//            Iterator<Double> iterator = buyPrice.iterator();
//            while (iterator.hasNext()) {
//                Double price = iterator.next();
//                if (currentPrice >= price * 1.013) {
//                    iterator.remove();
//
//
//                    earnings += (record.getVolume() > amount ? amount : record.getVolume()) * price * 0.013;
//
//                    logger.info("时间" + sdf.format(new Date(record.getTime())) + "卖出价格" + String.format("%.8f", currentPrice) + "交易量" + record.getVolume() + "总收益" + String.format("%.8f", earnings));
//                }
//            }
//
//        }
//
//
//        if (buyPrice.size() > 0) {
//            logger.info("还要" + buyPrice.size() + "个订单未卖出，价格");
//            for (Double aDouble : buyPrice) {
//                logger.info("未卖出价格"+String.format("%.8f", aDouble));
//            }
//        }
////        logger.info("开盘价格：" + String.format("%.8f", openPrice) + "最新价格：" + String.format("%.8f", currentPrice)
////                + (rate >= 1 ? "涨幅" : "跌幅") + Math.abs((1 - rate) * 100)
////        );
//
//    }

    List<Double> buyPrice = new ArrayList<>();
    int amount = 50;
    double earnings = 0;

    Calendar calendar = Calendar.getInstance();
    boolean tagBuy = false;
    boolean tagSell = false;

    /**
     * 守株待兔
     */
    @Scheduled(cron = "0/2 * * * * ?")
    private void waitForWindfalls() {

        calendar.setTime(new Date(System.currentTimeMillis()));
        int second = calendar.get(Calendar.SECOND);
        if (second == 0) {
            tagBuy = false;
            tagSell = false;
        }

        List<Record> recordList = exchange.getRecords("1m", 3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Record record = recordList.get(2);
        double currentPrice = record.getClose();
        double openPrice = record.getOpen();

        //买入操作
        double rate = currentPrice / openPrice;
        if (rate < 1) {
            double range = (1 - rate) * 100;
            if (range >= 1.3) {

                if (!tagBuy) {
                    tagBuy = true;

                    exchange.buy(currentPrice, amount);

                    buyPrice.add(currentPrice);
                    logger.info("时间" + sdf.format(new Date(record.getTime())) + "买入价格" + String.format("%.8f", currentPrice) + "交易量" + record.getVolume());
                }
            }
        }

        //卖出操作
        Iterator<Double> iterator = buyPrice.iterator();
        while (iterator.hasNext()) {
            Double price = iterator.next();
            if (currentPrice >= price * 1.013) {

                if (!tagSell) {
                    tagSell = true;
                    iterator.remove();

                    exchange.sell(currentPrice, amount);

                    earnings += (record.getVolume() > amount ? amount : record.getVolume()) * price * 0.013;
                    logger.info("时间" + sdf.format(new Date(record.getTime())) + "卖出价格" + String.format("%.8f", currentPrice) + "交易量" + record.getVolume() + "总收益" + String.format("%.8f", earnings));
                }
            }
        }

    }

//        logger.info("开盘价格：" + String.format("%.8f", openPrice) + "最新价格：" + String.format("%.8f", currentPrice)
//                + (rate >= 1 ? "涨幅" : "跌幅") + Math.abs((1 - rate) * 100)
//        );


}