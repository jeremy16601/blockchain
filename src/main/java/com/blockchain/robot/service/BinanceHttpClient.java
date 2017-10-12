package com.blockchain.robot.service;

import com.blockchain.robot.entity.binance.MarketDepth;
import com.blockchain.robot.entity.binance.NewOrder;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.util.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "binance", url = "https://www.binance.com", configuration = FeignConfiguration.class)
public interface BinanceHttpClient {

    /**
     * Test connectivity
     *
     * @return {}
     */
    @RequestMapping(value = "/api/v1/ping", method = RequestMethod.GET)
    String ping();

    /**
     * Check server time
     */
    @RequestMapping(value = "/api/v1/time", method = RequestMethod.GET)
    String server_time();


    //=========================↓↓↓交易数据↓↓↓============================

    /**
     * Order book
     *
     * @param symbol "NEOBTC"
     * @param limit  Default 100; max 100.
     * @return MarketDepth
     */
    @RequestMapping(value = "/api/v1/depth", method = RequestMethod.GET)
    MarketDepth market_depth(@RequestParam("symbol") String symbol, @RequestParam("limit") int limit);


    /**
     * Kline
     *
     * @param symbol    NEOBTC
     * @param interval  1m 3m 5m 15m 30m 1h 2h 4h 6h 8h 12h 1d 3d 1w 1M
     * @param limit     NO,Default 500; max 500
     * @param startTime NO,开始时间
     * @param endTime   NO,结束时间
     */
    @RequestMapping(value = "/api/v1/klines", method = RequestMethod.GET)
    List<List<String>> kline(@RequestParam("symbol") String symbol, @RequestParam("interval") String interval, @RequestParam("limit") int limit);


    /**
     * 24价格统计
     */
    @RequestMapping(value = "/api/v1/ticker/24hr", method = RequestMethod.GET)
    TwentyFourHoursPrice price_statistics_24hr(@RequestParam("symbol") String symbol);


    //=========================↓↓↓账户相关↓↓↓============================

    /**
     * 下单(BUY SELL)
     */
    @RequestMapping(value = "/api/v3/order", method = RequestMethod.POST, headers = {"Content-Type=application/x-www-form-urlencoded"})
    NewOrder new_order(@RequestHeader(value = "X-MBX-APIKEY") String api_key,
                       @RequestParam("symbol") String symbol,
                       @RequestParam("side") String side,
                       @RequestParam("type") String type,
                       @RequestParam("timeInForce") String timeInForce,
                       @RequestParam("quantity") double quantity,
                       @RequestParam("price") String price,
                       @RequestParam("recvWindow") long recvWindow,
                       @RequestParam("timestamp") long timestamp,
                       @RequestParam("signature") String signature);


}