package com.blockchain.robot;

import com.blockchain.robot.entity.Record;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.indicator.AR;
import com.blockchain.robot.service.BinanceExchangeService;
import com.blockchain.robot.service.api.BinanceHttpClient;
import com.blockchain.robot.util.PriceFormatUtil;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

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

        List<Record> allRecords = binanceExchange.getRecords("1h", 30);

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


        //macd
//        double[] close = new double[allRecords.size()];
//        for (int i = 0; i < allRecords.size(); i++) {
//            Record record = allRecords.get(i);
//            close[i] = record.getClose();
//        }
//
//        double[] macd = new double[close.length];
//        double[] signal = new double[close.length];
//        double[] hist = new double[close.length];
//        RetCode retCode = lib.macd(0, allRecords.size() - 1, close, 15, 26, 9, outBegIdx, outNbElement,
//                macd, signal, hist);
//
//        if (retCode.equals(RetCode.Success)) {
//            for (int i = 0; i < macd.length; i++) {
//                System.out.println(PriceFormatUtil.format(macd[i]) + ":" + PriceFormatUtil.format(signal[i]) + ":" + PriceFormatUtil.format(hist[i]));
//            }
//        }


        //ATR
//        Collections.reverse(allRecords);
//        double[] inHeight = new double[allRecords.size()];
//        double[] inLow = new double[allRecords.size()];
//        double[] inClose = new double[allRecords.size()];
//
//        for (int i = 0; i < allRecords.size(); i++) {
//            Record record = allRecords.get(i);
//            inHeight[i] = record.getHigh();
//            inLow[i] = record.getLow();
//            inClose[i] = record.getClose();
//        }
//        double[] real = new double[allRecords.size()];
//        RetCode retCode = lib.atr(0, allRecords.size() - 1, inHeight, inLow, inClose, 14, outBegIdx, outNbElement, real);
//
//        if (retCode.equals(RetCode.Success)) {
//            for (int i = 0; i < real.length; i++) {
//                calendar.setTime(new Date(allRecords.get(i).getTime()));
//                System.out.println("Time:" + sdf.format(calendar.getTime()) + " ATR:" + PriceFormatUtil.format(real[i]));
//            }
//        }

        //BOLL
        Collections.reverse(allRecords);
        double[] bollClose = new double[allRecords.size()];

        for (int i = 0; i < allRecords.size(); i++) {
            Record record = allRecords.get(i);
            bollClose[i] = record.getClose();
        }

        double[] realUp = new double[allRecords.size()];
        double[] realMid = new double[allRecords.size()];
        double[] realLower = new double[allRecords.size()];

        RetCode retCode = lib.bbands(0, allRecords.size() - 1, bollClose, 21, 2.0, 2.0, MAType.Sma, outBegIdx, outNbElement, realUp, realMid, realLower);

        if (retCode.equals(RetCode.Success)) {
            for (int i = 0; i < realUp.length; i++) {
                calendar.setTime(new Date(allRecords.get(i).getTime()));
                System.out.println("Time:" + sdf.format(calendar.getTime()) + " Close:" + PriceFormatUtil.format(allRecords.get(i).getClose()) + " UP:" + PriceFormatUtil.format(realUp[i]) + " Mid:" + PriceFormatUtil.format(realMid[i]) + " Lower:" + PriceFormatUtil.format(realLower[i]));
            }
        }

    }

}