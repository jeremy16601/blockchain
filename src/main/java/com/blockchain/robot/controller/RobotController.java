package com.blockchain.robot.controller;

import com.blockchain.robot.entity.DingMessage;
import com.blockchain.robot.entity.binance.OrderDetail;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.service.api.BinanceHttpClient;
import com.blockchain.robot.service.api.DingHttpClient;
import com.blockchain.robot.util.SHA256;
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


    @RequestMapping(value = "/orderInfo", method = RequestMethod.GET)
    public String testOrderApi() {

        String symbol = "BNBBTC";
        String orderId = "7469934";
        long timestamp = System.currentTimeMillis();
        int recvWindow = 5000;
        String parmas = "symbol=" + symbol + "&orderId=" + orderId + "&recvWindow=" + recvWindow + "&timestamp=" + timestamp;
        String hash = SHA256.signature("", parmas);

        OrderDetail orderDetail = binanceAPIService.order_info("", symbol, orderId, recvWindow, timestamp, hash);


        return orderDetail.getStatus();
    }

    @RequestMapping(value = "/ding", method = RequestMethod.GET)
    public String dingAPI() {

        DingMessage message = DingMessage.newInstance("我是丁丁");

        String response = dingHttpClient.ding("", message);

        return response;
    }

}