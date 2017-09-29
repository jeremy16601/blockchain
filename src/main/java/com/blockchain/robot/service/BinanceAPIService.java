package com.blockchain.robot.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "binance", url = "https://www.binance.com")
public interface BinanceAPIService {

    @RequestMapping(value = "/api/v3/klines", method = RequestMethod.GET)
    public String testKline(@RequestParam("symbol") String symbol, @RequestParam("interval") String interval, @RequestParam("limit") int limit);
}
