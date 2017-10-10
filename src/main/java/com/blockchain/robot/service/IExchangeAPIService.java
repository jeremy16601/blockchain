package com.blockchain.robot.service;


import com.blockchain.robot.entity.Account;
import com.blockchain.robot.entity.Order;
import com.blockchain.robot.entity.Ticker;

import java.util.List;

public interface IExchangeAPIService {

    /**
     * 返回交易所名称
     *
     * @return String
     */
    String getName();

    /**
     * 返回一个Ticker结构
     *
     * @return Ticker
     */
    Ticker GetTicker();


    /**
     * 返回一个Account结构
     */
    Account getAccount();

    /**
     * 下买单, Price为买单价格,Amount为数量, 返回一个订单ID
     *
     * @param price  价格
     * @param amount 数量
     * @return 订单ID
     */
    String buy(double price, double amount);

    /**
     * 下卖单，返回一个订单ID
     *
     * @param price  价格
     * @param amount 数量
     * @return 订单ID
     */
    String sell(double price, double amount);


    /**
     * 获取所有未完成的订单
     *
     * @return Order数组结构
     */
    List<Order> getOrders();

    /**
     * 根据订单号获取订单详情
     *
     * @param orderId 订单ID
     * @return Order结构
     */
    Order getOrder(String orderId);

    /**
     * 根据订单号取消一个订单
     *
     * @param orderId 订单ID
     * @return 返回true或者false
     */
    boolean cancelOrder(String orderId);

}
