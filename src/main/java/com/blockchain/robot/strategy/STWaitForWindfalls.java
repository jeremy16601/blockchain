package com.blockchain.robot.strategy;

import com.blockchain.robot.dao.OrderRecordDao;
import com.blockchain.robot.entity.DingMessage;
import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.db.OrderRecord;
import com.blockchain.robot.service.api.DingHttpClient;
import com.blockchain.robot.service.IExchangeAPIService;
import com.blockchain.robot.util.LoggerUtil;
import com.blockchain.robot.util.PriceFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 守株待兔2.0 策略说明
 * <p>
 * 一分钟内，涨幅达到x%则卖出
 * <p>
 * 然后以卖出价为基准，跌幅x%则买入
 */
@Component("waitForWindfalls")
public class STWaitForWindfalls implements IStrategy {

    private IExchangeAPIService exchange;//交易所

    @Autowired
    private OrderRecordDao recordDao;//数据库操作

    @Autowired
    private LoggerUtil logger;//通知和日志


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();
    private boolean tagBuy = false;//确保一个大周期内（一分钟）只能买一次

    //策略配置
    private int amount = 1;//默认 每次下单的数量
    private double rate = 0.020;//默认 2%的涨跌幅度


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
                //获取最近


            }

            List<Record> recordList = exchange.getRecords("1m", 3);
            Record record = recordList.get(2);
            double currentPrice = record.getClose();
            double openPrice = record.getOpen();

            //判断本地时间和交易所时间 不能有偏差
            Calendar serverTime = Calendar.getInstance();
            serverTime.setTime(new Date(record.getTime()));

            if (calendar.get(Calendar.MINUTE) != serverTime.get(Calendar.MINUTE)) {

                long diffTime = calendar.get(Calendar.SECOND);
                String message = "与API服务器时间不一致 \n当前服务器时间" + sdf.format(calendar.getTime()) + "\nAPI服务器时间:" + sdf.format(serverTime.getTime()) + "/n 相差" + diffTime + "s";
                logger.info(getClass(), message);

                return;
            }

            //读取数据库中所有的记录
            List<OrderRecord> records = recordDao.findOrderUnFinish(exchange.getSymbol(), getName());

            //如果未买入的订单 多余8个 则停止买入
            if (records.size() <= 8) {
                //买入操作
                double range = (currentPrice - openPrice) / openPrice;

                if (range >= rate) {

                    if (!tagBuy) {
                        tagBuy = true;

                        double sellPrice = openPrice * (1 + rate);
                        String orderId = null;

                        try {
                            orderId = exchange.sell(sellPrice, amount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (orderId != null) {

                                String _time = sdf.format(calendar.getTime());

                                OrderRecord orderRecord = new OrderRecord();
                                orderRecord.setTime(_time);
                                orderRecord.setExchange(exchange.getName());
                                orderRecord.setStrategy(getName());
                                orderRecord.setSymbol(exchange.getSymbol());
                                orderRecord.setSellOrderId(orderId);
                                orderRecord.setSellPrice(sellPrice);
                                orderRecord.setStatus(0);
                                recordDao.save(orderRecord);

                                String message = "时间" + _time + "卖出价格" + String.format("%.8f", sellPrice);
                                logger.info(getClass(), message);
                            } else {
                                logger.info(getClass(), "下单失败");
                            }
                        }


                    }
                }

            }


            //买出操作
            List<OrderRecord> records_sell = recordDao.findOrderUnFinish(exchange.getSymbol(), getName());

            for (OrderRecord orderRecord : records_sell) {

                double buyPrice = orderRecord.getSellPrice() / (1 + rate);//买入价格
                double spreadPrice = orderRecord.getSellPrice() - buyPrice;//差价

                if (currentPrice <= (buyPrice + spreadPrice * 0.3d)) {//提前下单
                    String orderId = null;

                    try {
                        orderId = exchange.buy(buyPrice, amount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (orderId != null) {
                            orderRecord.setBuyOrderId(orderId);
                            orderRecord.setBuyPrice(buyPrice);
                            orderRecord.setEarnings(amount * (orderRecord.getSellPrice() - buyPrice));
                            orderRecord.setStatus(1);
                            recordDao.save(orderRecord);

                            String message = "时间" + sdf.format(new Date(record.getTime())) + "买入价格" + String.format("%.8f", buyPrice);
                            logger.info(getClass(), message);
                        } else {
                            logger.info(getClass(), "下单失败");
                        }

                    }


                }
            }

        } else {
            logger.info(getClass(), "交易所，没有初始化");
        }
    }

    // //1.0版本
    //    @Override
//    public void onExec() {
//        if (exchange != null) {
//
//            calendar.setTime(new Date(System.currentTimeMillis()));
//            int second = calendar.get(Calendar.SECOND);
//            if (second == 0) {
//                tagBuy = false;
//            }
//
//            List<Record> recordList = exchange.getRecords("1m", 3);
//            Record record = recordList.get(2);
//            double currentPrice = record.getClose();
//            double openPrice = record.getOpen();
//
////            logger.info(sdf.format(new Date(record.getTime()))+"服务器时间");
//
//            //判断本地时间和交易所时间 不能有偏差
//            Calendar serverTime = Calendar.getInstance();
//            serverTime.setTime(new Date(record.getTime()));
//
//            if (calendar.get(Calendar.MINUTE) != serverTime.get(Calendar.MINUTE)) {
//
//                long diffTime = calendar.get(Calendar.SECOND);
//                String message = "与API服务器时间不一致 \n当前服务器时间" + sdf.format(calendar.getTime()) + "\nAPI服务器时间:" + sdf.format(serverTime.getTime()) + "/n 相差" + diffTime + "s";
//                if (diffTime >= 4) {
//                    logger.infoWithNotify(getClass(), message);
//                } else {
//                    logger.info(getClass(), message);
//                }
//
//                return;
//            }
//
//            //读取数据库中所有的记录
//            List<OrderRecord> records = recordDao.findOrderUnFinish(exchange.getSymbol(), getName());
//
//            //如果未卖出的订单 多余5个 则停止买入
//            if (records.size() <= 5) {
//                //买入操作
//                double range = (currentPrice - openPrice) / openPrice;
//
//                if (range <= -rate) {
//
//                    if (!tagBuy) {
//                        tagBuy = true;
//
//                        double joinPrice = openPrice * (1 - rate);
//                        String orderId = null;
//
//                        try {
//                            orderId = exchange.buy(joinPrice, amount);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            if (orderId != null) {
//
//                                String _time = sdf.format(calendar.getTime());
//
//                                OrderRecord orderRecord = new OrderRecord();
//                                orderRecord.setTime(_time);
//                                orderRecord.setExchange(exchange.getName());
//                                orderRecord.setStrategy(getName());
//                                orderRecord.setSymbol(exchange.getSymbol());
//                                orderRecord.setBuyOrderId(orderId);
//                                orderRecord.setBuyPrice(joinPrice);
//                                orderRecord.setStatus(0);
//                                recordDao.save(orderRecord);
//
//                                String message = "时间" + _time + "买入价格" + String.format("%.8f", joinPrice);
//                                logger.info(getClass(), message);
//                            } else {
//                                logger.info(getClass(), "下单失败");
//                            }
//                        }
//
//
//                    }
//                }
//
//            }
//
//
//            //卖出操作
//            List<OrderRecord> records_sell = recordDao.findOrderUnFinish(exchange.getSymbol(), getName());
//            Iterator<OrderRecord> iterator = records_sell.iterator();
//            while (iterator.hasNext()) {
//
//                OrderRecord orderRecord = iterator.next();
//
//                Double sellPrice = orderRecord.getBuyPrice() * (1 + rate);
//                if (currentPrice >= sellPrice) {
//                    String orderId = null;
//
//                    try {
//                        orderId = exchange.sell(sellPrice, amount);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        if (orderId != null) {
//                            orderRecord.setSellOrderId(orderId);
//                            orderRecord.setSellPrice(sellPrice);
//                            orderRecord.setEarnings(amount * (sellPrice - orderRecord.getBuyPrice()));
//                            orderRecord.setStatus(1);
//                            recordDao.save(orderRecord);
//
//                            iterator.remove();
//
//                            String message = "时间" + sdf.format(new Date(record.getTime())) + "卖出价格" + String.format("%.8f", sellPrice);
//                            logger.info(getClass(), message);
//                        } else {
//                            logger.info(getClass(), "下单失败");
//                        }
//
//                    }
//
//
//                }
//            }
//
//        } else {
//            logger.info(getClass(), "交易所，没有初始化");
//        }
//    }

}