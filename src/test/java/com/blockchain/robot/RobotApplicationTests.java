package com.blockchain.robot;

import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.service.BinanceExchangeService;
import com.blockchain.robot.service.api.BinanceHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RobotApplicationTests {

    @Autowired
    private BinanceExchangeService binanceExchange;

    @Test
    public void contextLoads() {

        List<Record> allRecords = binanceExchange.getRecords("1d", 500);

        for (int i = 26; i < allRecords.size(); i++) {

        }
    }

}