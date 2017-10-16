package com.blockchain.robot.strategy;

import com.blockchain.robot.service.IExchangeAPIService;

public interface IStrategy {

    String getName();

    void setExchange(IExchangeAPIService exchangeAPIService);

    /**
     * 策略参数设置
     *
     * @param params 守株待兔 数量，涨幅
     *               网格交易
     */
    void config(double... params);

    void onExec();

}
