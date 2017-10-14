package com.blockchain.robot.strategy;

import com.blockchain.robot.service.IExchangeAPIService;

public interface IStrategy {

    String getName();

    void setExchange(IExchangeAPIService exchangeAPIService);

    void onExec();

}
