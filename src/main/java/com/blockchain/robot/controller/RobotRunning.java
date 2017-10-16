package com.blockchain.robot.controller;

import com.blockchain.robot.dao.OrderRecordDao;
import com.blockchain.robot.entity.DingMessage;
import com.blockchain.robot.entity.Order;
import com.blockchain.robot.entity.OrderStatus;
import com.blockchain.robot.entity.Ticker;
import com.blockchain.robot.entity.binance.BuyPoint;
import com.blockchain.robot.entity.db.OrderRecord;
import com.blockchain.robot.service.IExchangeAPIService;
import com.blockchain.robot.service.api.DingHttpClient;
import com.blockchain.robot.strategy.IStrategy;
import com.blockchain.robot.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RobotRunning {

    //1.定义交易所（可以多个）
    @Autowired
    @Qualifier("binanceExchangeService")
    private IExchangeAPIService binanceExchange;

    @Autowired
    @Qualifier("binanceExchangeService")
    private IExchangeAPIService binanceExchange2;


    //2.定义策略（可以多个，可重复定义）
    @Autowired
    @Qualifier("waitForWindfalls")
    private IStrategy waitForWindfalls;

    @Autowired
    @Qualifier("waitForWindfalls")
    private IStrategy waitForWindfalls2;

    //3.数据库查询提示
    @Autowired
    private OrderRecordDao recordDao;//数据库操作

    //4.日志及通知
    @Autowired
    private LoggerUtil logger;//通知和日志


//    private Stack<BuyPoint> buyPointStack = new Stack<>();
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private double btc_price;//BTC人民币价格

    //    @Scheduled(fixedRate = 5000)
//    private void BTCPrice() {
//        btc_price = binanceExchange.btc_cnyPrice();
//    }

    //    @Scheduled(fixedRate = 10000)
//    private void process() {
//
//        String out = "";//日志输出
//        double base_rate = 0.01;
//        double amount = 100;
//        String symbol = "NEOBTC";
//
//        Ticker ticker = binanceExchange.getTicker();
//
//        try {
//
//            double HPoint = ticker.getHigh();
//            double LPoint = ticker.getLow();
//            double GPoint = (HPoint - LPoint) * 0.618 + LPoint;
//            double CPoint = ticker.getLast();
//
//            if (buyPointStack.isEmpty()) {
//
//                if (CPoint <= GPoint * (1 - base_rate)) {//95%买入
//                    buyPointStack.push(new BuyPoint(HPoint, LPoint, CPoint, GPoint));
//                    binanceExchange.buy(CPoint, amount);
//
//                    out += "\n==买入操作C:" + String.format("%.8f", CPoint) + "G:" + String.format("%.8f", GPoint);
//                } else {
//                    out += "\n==不是买入点,买入点是：" + String.format("%.8f", GPoint * (1 - base_rate));
//                }
//
//            } else {
//                int count = buyPointStack.size();
//                BuyPoint preBuyPoint = buyPointStack.peek();
//                double rate = 1 - base_rate * (count + 1);
//
//                double point1 = preBuyPoint.gethPoint() * rate;
//                double point2 = GPoint * rate;
//                double point3 = preBuyPoint.getcPoint() * (1 - base_rate * count);
//
//                if (CPoint <= point2 && CPoint <= point1 && CPoint <= point3) {
//                    buyPointStack.push(new BuyPoint(HPoint, LPoint, CPoint, GPoint));
//                    binanceExchange.buy(CPoint, amount);
//                    out += "\n==第" + (count + 1) + "买入操作C:" + String.format("%.8f", CPoint) + "G:" + String.format("%.8f", GPoint);
//                } else {
//                    out += "\n**不是买入点,买入点是:" + String.format("%.8f", Math.min(Math.min(point1, point2), point3));
//                }
//
//            }
//
//
//            //卖出操作
//            if (!buyPointStack.isEmpty()) {
//                BuyPoint topBuyPoint = buyPointStack.peek();
//                if (CPoint >= topBuyPoint.getcPoint() * (1 + base_rate)) {
//                    out += "\n==卖出操作，买入点" + topBuyPoint.getcPoint() + "卖出点" + CPoint;
//                    binanceExchange.sell(CPoint, amount);
//                    buyPointStack.pop();
//                } else {
//                    out += "\n**不是卖出点,卖出点是：" + String.format("%.8f", topBuyPoint.getcPoint() * (1 + base_rate));
//                }
//            }
//
//
//            logger.info(String.format(symbol + "最新价格%.8f（¥%.2f）,最高价格%.8f,最低价格%.8f。", CPoint, CPoint * btc_price, HPoint, LPoint) + out);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    /**
     * 守株待兔测试
     */
//    @Scheduled(fixedRate = 360000)
//    private void waitForWindfallsTest() {
//
//        List<Record> recordList = binanceExchange.getRecords("1m", 500);
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
//
//
//    }


    /**
     * 守株待兔
     */
    @Scheduled(cron = "0/2 * * * * ?")
    private void waitForWindfalls() {
        binanceExchange.setSymbol("BNBBTC");
        waitForWindfalls.setExchange(binanceExchange);
        waitForWindfalls.config(50, 0.001);
        waitForWindfalls.onExec();
    }

    /**
     * 统一处理所有的订单，并通知给用户（暂时先给 守株待兔使用）
     */
    @Scheduled(fixedRate = 60000)
    private void orderHandle() {

        //1.查询所有'守株待兔'策略的订单
        List<OrderRecord> recordList = recordDao.findAllByStrategy(waitForWindfalls.getName());

        //2.查询每个订单的状态，如果买卖订单全部成交，则通知
        for (OrderRecord orderRecord : recordList) {

            Order buyOrder = binanceExchange.getOrder(orderRecord.getBuyOrderId());
            Order sellOrder = binanceExchange.getOrder(orderRecord.getSellOrderId());

            if (buyOrder != null && buyOrder.getStatus().equals(OrderStatus.ORDER_STATE_FILLED)) {
                orderRecord.setBuyStatus(1);
            }

            if (sellOrder != null && sellOrder.getStatus().equals(OrderStatus.ORDER_STATE_FILLED)) {
                orderRecord.setSellStatus(1);
            }
            recordDao.save(orderRecord);

            if (orderRecord.getBuyStatus() == 1 && orderRecord.getSellStatus() == 1) {
                logger.infoWithNotify(this.getClass(), orderRecord.getTime() + " " + orderRecord.getExchange() + " " + orderRecord.getSymbol() + "收益" + orderRecord.getEarnings());
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


//    @Scheduled(cron = "0/2 * * * * ?")
//    private void waitForWindfalls2() {
//        binanceExchange2.setSymbol("NEOBTC");
//        waitForWindfalls2.setExchange(binanceExchange2);
//        waitForWindfalls2.config(1, 0.011);
//        waitForWindfalls2.onExec();
//    }


}