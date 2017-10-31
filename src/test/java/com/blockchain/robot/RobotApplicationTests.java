package com.blockchain.robot;

import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.indicator.AR;
import com.blockchain.robot.service.BinanceExchangeService;
import com.blockchain.robot.service.api.BinanceHttpClient;
import com.blockchain.robot.util.PriceFormatUtil;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RobotApplicationTests {

    @Autowired
    private BinanceExchangeService binanceExchange;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();
    private Core lib = new Core();

    private MInteger outBegIdx = new MInteger();
    private MInteger outNbElement = new MInteger();

    @Test
    public void contextLoads() {

        binanceExchange.setSymbol("NEOBTC");

        List<Record> allRecords = binanceExchange.getRecords("2h", 100);

//        for (int i = 26; i < allRecords.size(); i++) {
//            Record current = allRecords.get(i);
//
//            List<Record> testRecord = allRecords.subList(i - 26, i);
//            AR ar = new AR();
//            double result = ar.handle_data(testRecord);
//
//            calendar.setTime(new Date(current.getTime()));
//
//            System.out.println(sdf.format(calendar.getTime()) + "AR值:" + result);
//        }


        double[] close = new double[allRecords.size()];
        for (int i = 0; i < allRecords.size(); i++) {
            Record record = allRecords.get(i);
            close[i] = record.getClose();
        }

        double[] macd = new double[close.length];
        double[] signal = new double[close.length];
        double[] hist = new double[close.length];
        RetCode retCode = lib.macd(0, allRecords.size() - 1, close, 15, 26, 9, outBegIdx, outNbElement,
                macd, signal, hist);

        if (retCode.equals(RetCode.Success)) {
            for (int i = 0; i < macd.length; i++) {
                System.out.println(PriceFormatUtil.format(macd[i]) + ":" + PriceFormatUtil.format(signal[i]) + ":" + PriceFormatUtil.format(hist[i]));
            }
        }


    }

}