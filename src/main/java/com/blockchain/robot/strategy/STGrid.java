package com.blockchain.robot.strategy;

import com.blockchain.robot.service.IExchangeAPIService;
import org.springframework.stereotype.Component;

@Component("grid")
public class STGrid implements IStrategy {

    @Override
    public String getName() {
        return "网格交易策略";
    }

    @Override
    public void setExchange(IExchangeAPIService exchangeAPIService) {

    }

    @Override
    public void config(double... params) {

    }

    @Override
    public void onExec() {

    }
}
