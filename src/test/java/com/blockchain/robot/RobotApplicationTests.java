package com.blockchain.robot;

import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.service.api.BinanceHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RobotApplicationTests {

    @Autowired
    private BinanceHttpClient binanceAPIService;

    @Test
    public void contextLoads() {

        TwentyFourHoursPrice response = binanceAPIService.price_statistics_24hr("NEOBTC");

        String result = "NEO最新价格" + response.getLastPrice() + "最高价格" + response.getHighPrice() + "最低价格" + response.getLowPrice()
                + "跌幅" + response.getPriceChangePercent() + "平均" + response.getWeightedAvgPrice();

        System.out.println(result);

    }

}