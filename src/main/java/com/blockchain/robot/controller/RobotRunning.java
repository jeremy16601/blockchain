package com.blockchain.robot.controller;

import com.blockchain.robot.dao.OrderRecordDao;
import com.blockchain.robot.entity.*;
import com.blockchain.robot.entity.binance.BuyPoint;
import com.blockchain.robot.entity.db.OrderRecord;
import com.blockchain.robot.service.IExchangeAPIService;
import com.blockchain.robot.service.api.DingHttpClient;
import com.blockchain.robot.strategy.IStrategy;
import com.blockchain.robot.strategy.STGrid;
import com.blockchain.robot.util.LoggerUtil;
import com.blockchain.robot.util.PriceFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
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
    @Qualifier("grid")
    private IStrategy gridStrategy;


    //3.数据库查询提示
    @Autowired
    private OrderRecordDao recordDao;//数据库操作

    //4.日志及通知
    @Autowired
    private LoggerUtil logger;//通知和日志


    /**
     * 测试
     */
    @Scheduled(fixedRate = 360000)
    private void strategyTest() {

        List<Record> recordList = binanceExchange.getRecords("1h", 500);

        for (Record record : recordList) {

            double currentPrice = record.getClose();

            binanceExchange2.setSymbol("NEOBTC");
            gridStrategy.setExchange(binanceExchange2);
            gridStrategy.config(0.003, 0.01, 28, 0.03, 0.004750);

            ((STGrid) gridStrategy).setTestPrice(currentPrice);
            gridStrategy.onExec();

        }

    }


    /**
     * 守株待兔
     */
//    @Scheduled(cron = "0/2 * * * * ?")
    private void waitForWindfalls() {
        binanceExchange.setSymbol("BNBBTC");
        waitForWindfalls.setExchange(binanceExchange);
        waitForWindfalls.config(50, 0.011);
        waitForWindfalls.onExec();
    }

    /**
     * 统一处理所有的订单，并通知给用户（暂时先给 守株待兔使用）
     */
//    @Scheduled(fixedRate = 60000)
    private void orderHandle() {

        //1.查询所有'守株待兔'策略的订单
        List<OrderRecord> recordList = recordDao.findAllByStrategy(waitForWindfalls.getName());

        //2.查询每个订单的状态，如果买卖订单全部成交，则通知
        for (OrderRecord orderRecord : recordList) {


            boolean _isSave = false;

            if (orderRecord.getBuyStatus() == 0) {
                Order buyOrder = binanceExchange.getOrder(orderRecord.getBuyOrderId());
                if (buyOrder != null && buyOrder.getStatus().equals(OrderStatus.ORDER_STATE_FILLED)) {
                    orderRecord.setBuyStatus(1);
                    _isSave = true;
                }
            }

            if (orderRecord.getSellStatus() == 0) {
                Order sellOrder = binanceExchange.getOrder(orderRecord.getSellOrderId());

                if (sellOrder != null && sellOrder.getStatus().equals(OrderStatus.ORDER_STATE_FILLED)) {
                    orderRecord.setSellStatus(1);
                    _isSave = true;
                }
            }

            if (_isSave) {

                if (orderRecord.getBuyStatus() == 1 && orderRecord.getSellStatus() == 1) {
                    orderRecord.setStatus(2);//订单结束

                    logger.infoWithNotify(this.getClass(), orderRecord.getTime() + "\n"
                            + orderRecord.getExchange() + " " + orderRecord.getSymbol() + "\n"
                            + "买入价格" + PriceFormatUtil.format(orderRecord.getBuyPrice()) + "\n"
                            + "卖出价格" + PriceFormatUtil.format(orderRecord.getSellPrice()) + "\n"
                            + "收益   " + PriceFormatUtil.format(orderRecord.getEarnings()));
                }


                recordDao.save(orderRecord);
            }


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    //    @Scheduled(cron = "0/30 * * * * ?")
    private void gridStrategyTask() {
        binanceExchange2.setSymbol("NEOBTC");
        gridStrategy.setExchange(binanceExchange2);
        gridStrategy.config(0.003, 0.01, 28, 0.03, 0.004750);
        gridStrategy.onExec();
    }


}