package com.blockchain.robot.controller;

import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.service.BinanceHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RobotController {

    @Autowired
    private BinanceHttpClient binanceAPIService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testAPI() {

        TwentyFourHoursPrice response = binanceAPIService.price_statistics_24hr("NEOBTC");

        return "NEO最新价格" + response.getLastPrice() + "最高价格" + response.getHighPrice() + "最低价格" + response.getLowPrice()
                + "跌幅" + response.getPriceChangePercent() + "平均" + response.getWeightedAvgPrice();
    }


}