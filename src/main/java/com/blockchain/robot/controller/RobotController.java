package com.blockchain.robot.controller;

import com.blockchain.robot.service.BinanceAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RobotController {

    @Autowired
    private BinanceAPIService binanceAPIService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testAPI() {
        String response = binanceAPIService.testKline("NEOBTC", "15m", 10);
        return response;
    }
}
