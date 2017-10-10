package com.blockchain.robot.service;


import com.blockchain.robot.entity.Ticker;

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


    getAccount();

}
