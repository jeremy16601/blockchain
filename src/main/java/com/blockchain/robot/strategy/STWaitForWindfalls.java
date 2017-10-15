package com.blockchain.robot.strategy;

import com.blockchain.robot.dao.OrderRecordDao;
import com.blockchain.robot.entity.DingMessage;
import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.db.OrderRecord;
import com.blockchain.robot.service.api.DingHttpClient;
import com.blockchain.robot.service.IExchangeAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component("waitForWindfalls")
public class STWaitForWindfalls implements IStrategy {

    private IExchangeAPIService exchange;//交易所

    @Autowired
    private OrderRecordDao recordDao;//数据库操作

    @Autowired
    private DingHttpClient dingLogger;//通知和日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    //    List<Double> buyPrice = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar calendar = Calendar.getInstance();
    boolean tagBuy = false;//确保一个大周期内（一分钟）只能买一次

    //策略配置
    int amount = 50;//每次下单的数量
    double rate = 0.011;//1.1%的涨跌幅度


    @Override
    public String getName() {
        return "守株待兔";
    }

    @Override
    public void setExchange(IExchangeAPIService exchangeAPIService) {
        exchange = exchangeAPIService;
    }

    @Override
    public void config(double... params) {
        if (params.length == 2) {
            amount = (int) params[0];
            rate = params[1];
        }
    }

    @Override
    public void onExec() {
        if (exchange != null) {

            calendar.setTime(new Date(System.currentTimeMillis()));
            int second = calendar.get(Calendar.SECOND);
            if (second == 0) {
                tagBuy = false;
            }

            List<Record> recordList = exchange.getRecords("1m", 3);
            Record record = recordList.get(2);
            double currentPrice = record.getClose();
            double openPrice = record.getOpen();

            //判断本地时间和交易所时间 不能有偏差
            Calendar serverTime = Calendar.getInstance();
            serverTime.setTime(new Date(record.getTime()));

            if (calendar.get(Calendar.MINUTE) != serverTime.get(Calendar.MINUTE)) {
                String message = "与服务器时间不一致";
                log(message);
                return;
            }

            //读取数据库中所有的记录
            List<OrderRecord> records = recordDao.findOrderUnFinish(exchange.getSymbol(), getName());

            //如果未卖出的订单 多余5个 则停止买入
            if (records.size() <= 5) {
                //买入操作
                double range = (currentPrice - openPrice) / openPrice;
                if (range <= -rate) {

                    if (!tagBuy) {
                        tagBuy = true;

                        double joinPrice = openPrice * (1 - rate);
                        String orderId = null;

                        try {
                            orderId = exchange.buy(joinPrice, amount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (orderId != null) {

                                String _time = sdf.format(new Date(record.getTime()));

                                OrderRecord orderRecord = new OrderRecord();
                                orderRecord.setTime(_time);
                                orderRecord.setExchange(exchange.getName());
                                orderRecord.setStrategy(getName());
                                orderRecord.setSymbol(exchange.getSymbol());
                                orderRecord.setBuyOrderId(orderId);
                                orderRecord.setBuyPrice(joinPrice);
                                orderRecord.setStatus(0);
                                recordDao.save(orderRecord);

                                String message = "时间" + _time + "买入价格" + String.format("%.8f", joinPrice);
                                log(message);
                            } else {
                                logger.error("下单失败");
                            }
                        }


                    }
                }

            }


            //卖出操作
            List<OrderRecord> records_sell = recordDao.findOrderUnFinish(exchange.getSymbol(), getName());
            Iterator<OrderRecord> iterator = records_sell.iterator();
            while (iterator.hasNext()) {

                OrderRecord orderRecord = iterator.next();

                Double sellPrice = orderRecord.getBuyPrice() * (1 + rate);
                if (currentPrice >= sellPrice) {
                    String orderId = null;

                    try {
                        orderId = exchange.sell(sellPrice, amount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (orderId != null) {
                            orderRecord.setSellOrderId(orderId);
                            orderRecord.setSellPrice(sellPrice);
                            orderRecord.setEarnings(amount * (sellPrice - orderRecord.getBuyPrice()));
                            orderRecord.setStatus(1);
                            recordDao.save(orderRecord);

                            iterator.remove();

                            String message = "时间" + sdf.format(new Date(record.getTime())) + "卖出价格" + String.format("%.8f", sellPrice);
                            log(message);
                        } else {
                            logger.error("下单失败");
                        }

                    }


                }
            }

        } else {
            logger.error("交易所，没有初始化");
        }
    }


    private void log(String message) {
        dingLogger.ding("4bc7f090cbae8f97ebe4cc9d5007b5cadbac4c3c3ad7c1ff19b9dd91c4617f06", DingMessage.newInstance(message));
        logger.info(message);
    }
}