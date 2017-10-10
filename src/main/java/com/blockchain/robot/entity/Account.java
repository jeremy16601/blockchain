package com.blockchain.robot.entity;

import java.util.List;

/**
 * 交易所账户信息
 */
public class Account {


    private String Info	;//交易所返回的原始结构
    private double Balance;//	余额(人民币或者美元, 在Poloniex交易所里BTC_ETC这样的品种, Balance就指的是BTC的数量, Stocks指的是ETC数量, BTC38的ETC_BTC相当于Poloniex的BTC_ETC, 指的是以BTC计价)
    private double FrozenBalance	;//冻结的余额
    private List<Double> Stocks	:BTC/LTC数量, 现货为当前可操作币的余额(去掉冻结的币), 期货的话为合约当前可用保证金(传统期货为此属性)
    private List<Double> FrozenStocks	:冻结的BTC/LTC数量(传统期货无此属性)
}
