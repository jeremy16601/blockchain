package com.blockchain.robot.strategy;

import com.blockchain.robot.entity.Record;
import com.blockchain.robot.service.IExchangeAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component("waitForWindfalls")
public class STWaitForWindfalls implements IStrategy {

    private IExchangeAPIService exchange;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    List<Double> buyPrice = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    boolean tagBuy = false;//确保一个大周期内（一分钟）只能买一次
    double earnings = 0;//显示总收益

    //策略配置
    int amount = 10;//每次下单的数量


    @Override
    public String getName() {
        return "守株待兔";
    }

    @Override
    public void setExchange(IExchangeAPIService exchangeAPIService) {
        exchange = exchangeAPIService;
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Record record = recordList.get(2);
            double currentPrice = record.getClose();
            double openPrice = record.getOpen();

            //logger.info("时间" + sdf.format(new Date(record.getTime())) + "最新价格" + PriceFormatUtil.format(currentPrice) + "开盘价" + PriceFormatUtil.format(openPrice));


            //如果未卖出的订单 多余5个 则停止买入
            //TODO 检测为成交的订单，如果太多 也不能买入
            if (buyPrice.size() <= 5) {
                //买入操作
                double rate = currentPrice / openPrice;
                if (rate < 1) {
                    double range = (1 - rate) * 100;
                    if (range >= 1.3) {

                        if (!tagBuy) {
                            tagBuy = true;
                            buyPrice.add(currentPrice);
                            exchange.buy(currentPrice, amount);
                            logger.info("时间" + sdf.format(new Date(record.getTime())) + "买入价格" + String.format("%.8f", currentPrice) + "交易量" + record.getVolume());
                        }
                    }
                }
            }


            //卖出操作
            Iterator<Double> iterator = buyPrice.iterator();
            while (iterator.hasNext()) {
                Double price = iterator.next();

                if (currentPrice >= price * 1.013) {

                    iterator.remove();
                    exchange.sell(currentPrice, amount);
                    earnings += (record.getVolume() > amount ? amount : record.getVolume()) * price * 0.013;
                    logger.info("时间" + sdf.format(new Date(record.getTime())) + "卖出价格" + String.format("%.8f", currentPrice) + "交易量" + record.getVolume() + "总收益" + String.format("%.8f", earnings));

                }
            }

        } else {
            logger.error("交易所，没有初始化");
        }
    }
}