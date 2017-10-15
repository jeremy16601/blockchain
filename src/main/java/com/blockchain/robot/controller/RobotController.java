package com.blockchain.robot.controller;

import com.blockchain.robot.entity.DingMessage;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.service.api.BinanceHttpClient;
import com.blockchain.robot.service.api.DingHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RobotController {

    @Autowired
    private BinanceHttpClient binanceAPIService;

    @Autowired
    private DingHttpClient dingHttpClient;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testAPI() {

        TwentyFourHoursPrice response = binanceAPIService.price_statistics_24hr("NEOBTC");

        return "NEO最新价格" + response.getLastPrice() + "最高价格" + response.getHighPrice() + "最低价格" + response.getLowPrice()
                + "跌幅" + response.getPriceChangePercent() + "平均" + response.getWeightedAvgPrice();
    }


    @RequestMapping(value = "/ding", method = RequestMethod.GET)
    public String dingAPI() {

        DingMessage message = DingMessage.newInstance("我是丁丁");

        String response = dingHttpClient.ding("4bc7f090cbae8f97ebe4cc9d5007b5cadbac4c3c3ad7c1ff19b9dd91c4617f06", message);

        return response;
    }

}