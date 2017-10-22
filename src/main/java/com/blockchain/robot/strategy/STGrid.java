package com.blockchain.robot.strategy;

import com.blockchain.robot.dao.OrderRecordGridDao;
import com.blockchain.robot.entity.Order;
import com.blockchain.robot.entity.OrderStatus;
import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.db.OrderRecordGrid;
import com.blockchain.robot.service.IExchangeAPIService;
import com.blockchain.robot.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component("grid")
public class STGrid implements IStrategy {

    private IExchangeAPIService exchange;//交易所

    @Autowired
    private OrderRecordGridDao recordDao;//数据库操作

    @Autowired
    private LoggerUtil logger;//通知和日志


    private double mMaxPrice;
    private double mMinPrice;
    private double mSteps;//一共分多少档位
    private double mBtcAmount;//每次买入btc数量
    private double mInitPrice;//初始价格，用户第一次启动

    private double mStepPrice;//每个档位的价差

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();


    @Override
    public String getName() {
        return "网格交易策略";
    }

    @Override
    public void setExchange(IExchangeAPIService exchangeAPIService) {
        exchange = exchangeAPIService;
    }

    @Override
    public void config(double... params) {
        if (params != null && params.length == 5) {
            mMinPrice = params[0];
            mMaxPrice = params[1];
            mSteps = params[2];
            mBtcAmount = params[3];
            mInitPrice = params[4];

            mStepPrice = (mMaxPrice - mMinPrice) / mSteps;
        }
    }

    @Override
    public void onExec() {
        if (exchange != null) {

            calendar.setTime(new Date(System.currentTimeMillis()));

            //1.去读数据库是否有记录
            List<OrderRecordGrid> orderRecordGridList = recordDao.findOrderUnFinish(exchange.getSymbol(), getName());
            if (orderRecordGridList == null || orderRecordGridList.size() == 0) {
                //没有数据根据初始值，创建一个

                String _time = sdf.format(calendar.getTime());

                OrderRecordGrid orderRecord = new OrderRecordGrid();
                orderRecord.setTime(_time);
                orderRecord.setExchange(exchange.getName());
                orderRecord.setStrategy(getName());
                orderRecord.setSymbol(exchange.getSymbol());
                orderRecord.setGradePrice(mInitPrice);
                orderRecord.setBuyPrice(mInitPrice - mStepPrice);
                orderRecord.setBuyStatus(-1);
                orderRecord.setSellPrice(mInitPrice + mStepPrice);
                orderRecord.setSellStatus(-1);
                orderRecord.setStatus(0);
                recordDao.save(orderRecord);

            } else {
                //如果有记录，取最后一个
                OrderRecordGrid orderRecord = orderRecordGridList.get(orderRecordGridList.size() - 1);

                //如果买单还未下单，下买单
                if (orderRecord.getBuyStatus() == -1) {

                    String orderId = null;
                    try {
                        orderId = exchange.buy(orderRecord.getBuyPrice(), mBtcAmount / orderRecord.getBuyPrice());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        orderId = "123";
                        if (orderId != null) {
                            orderRecord.setBuyOrderId(orderId);
                            orderRecord.setBuyStatus(0);
                            recordDao.save(orderRecord);
                        }
                    }

                }

                //如果卖单还没下单，下卖单
                if (orderRecord.getSellStatus() == -1) {
                    String orderId = null;
                    try {
                        orderId = exchange.sell(orderRecord.getSellPrice(), mBtcAmount / orderRecord.getGradePrice());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        orderId = "123";
                        if (orderId != null) {
                            orderRecord.setSellOrderId(orderId);
                            orderRecord.setSellStatus(0);
                            recordDao.save(orderRecord);
                        }
                    }
                }

                boolean _isBought = false;
                boolean _isSold = false;

                //检测买单是否成交
                if (orderRecord.getBuyOrderId() != null) {
                    if(orderRecord.getBuyPrice()>=currentPrice){
                        orderRecord.setBuyStatus(1);
                        recordDao.save(orderRecord);
                        _isBought = true;
                    }

//                    Order buyOrder = exchange.getOrder(orderRecord.getBuyOrderId());
//                    if (buyOrder != null && buyOrder.getStatus().equals(OrderStatus.ORDER_STATE_FILLED)) {
//                        orderRecord.setBuyStatus(1);
//                        recordDao.save(orderRecord);
//                        _isBought = true;
//                    }
                }

                //检测卖单是否成交
                if (orderRecord.getSellOrderId() != null) {

                    if(orderRecord.getSellPrice()<=currentPrice){
                        orderRecord.setSellStatus(1);
                        recordDao.save(orderRecord);
                        _isSold = true;

                        logger.infoWithNotify(STGrid.class, "卖单成交，预计收益" +
                                (orderRecord.getSellPrice() - orderRecord.getGradePrice()) * (mBtcAmount / orderRecord.getGradePrice())
                        );
                    }

//                    Order sellOrder = exchange.getOrder(orderRecord.getSellOrderId());
//                    if (sellOrder != null && sellOrder.getStatus().equals(OrderStatus.ORDER_STATE_FILLED)) {
//                        orderRecord.setSellStatus(1);
//                        recordDao.save(orderRecord);
//                        _isSold = true;
//
//                        logger.infoWithNotify(STGrid.class, "卖单成交，预计收益" +
//                                (orderRecord.getSellPrice() - orderRecord.getGradePrice()) * (mBtcAmount / orderRecord.getGradePrice())
//                        );
//                    }
                }

                if (_isSold || _isBought) {
                    //当前档位结束
                    orderRecord.setStatus(1);
                    recordDao.save(orderRecord);

                    //进行下一个档位
                    String _time = sdf.format(calendar.getTime());

                    OrderRecordGrid _order = new OrderRecordGrid();
                    _order.setTime(_time);
                    _order.setExchange(exchange.getName());
                    _order.setStrategy(getName());
                    _order.setSymbol(exchange.getSymbol());

                    if (_isBought) {
                        _order.setGradePrice(orderRecord.getBuyPrice());
                        _order.setBuyPrice(orderRecord.getBuyPrice() - mStepPrice);
                        _order.setBuyStatus(-1);
                        _order.setSellPrice(orderRecord.getBuyPrice() + mStepPrice);
                        _order.setSellStatus(-1);
                    }
                    if (_isSold) {
                        _order.setGradePrice(orderRecord.getSellPrice());
                        _order.setBuyPrice(orderRecord.getSellPrice() - mStepPrice);
                        _order.setBuyStatus(-1);
                        _order.setSellPrice(orderRecord.getSellPrice() + mStepPrice);
                        _order.setSellStatus(-1);
                    }

                    _order.setStatus(0);
                    recordDao.save(_order);

                    logger.infoWithNotify(STGrid.class, "执行一下个档位");
                }

            }

        }

    }


    private double currentPrice;

    public void setTestPrice(double price) {
        currentPrice = price;
    }
}
