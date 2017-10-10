package com.blockchain.robot.controller;

import com.blockchain.robot.entity.binance.BuyPoint;
import com.blockchain.robot.entity.binance.ParamOrder;
import com.blockchain.robot.entity.binance.TwentyFourHoursPrice;
import com.blockchain.robot.service.BinanceAPIService;
import com.blockchain.robot.util.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Stack;


@RestController
public class RobotController {

    @Autowired
    private BinanceAPIService binanceAPIService;

    private Stack<BuyPoint> buyPointStack = new Stack<>();

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testAPI() {

        String out = "";
        double base_rate = 0.05;

        TwentyFourHoursPrice response = binanceAPIService.price_statistics_24hr("NEOBTC");

//        try {
//
//            double HPoint = Double.parseDouble(response.getHighPrice());
//            double LPoint = Double.parseDouble(response.getLastPrice());
//            double GPoint = (HPoint - LPoint) * 0.618 + LPoint;
//            double CPoint = Double.parseDouble(response.getLastPrice());
//
//            if (buyPointStack.isEmpty()) {
//
//                if (CPoint <= GPoint * (1 - base_rate)) {//95%买入
//                    buyPointStack.push(new BuyPoint(HPoint, LPoint, GPoint, CPoint));
//                    out = "买入操作C:" + String.format("%.8f", CPoint) + "G:" + String.format("%.8f", GPoint);
//                } else {
//                    out = "不是买入点";
//                }
//
//            } else {
//                int count = buyPointStack.size();
//                BuyPoint preBuyPoint = buyPointStack.peek();
//                double rate = 1 - base_rate * (count + 1);
//
//                double point1 = preBuyPoint.gethPoint() * rate;
//                double point2 = GPoint * rate;
//
//                if (CPoint <= point2 && CPoint <= point1) {
//                    buyPointStack.push(new BuyPoint(HPoint, LPoint, GPoint, CPoint));
//                } else {
//                    out = "不是买入点";
//                }
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return "NEO最新价格" + response.getLastPrice() + "最高价格" + response.getHighPrice() + "最低价格" + response.getLowPrice()
                + "跌幅" + response.getPriceChangePercent() + "平均" + response.getWeightedAvgPrice()
                + out;
    }


    @RequestMapping(value = "/testOrder", method = RequestMethod.GET)
    public String testOrderAPI() {

        long time = System.currentTimeMillis();

        ParamOrder param = new ParamOrder();
        param.setSymbol("NEOBTC");
        param.setSide("BUY");
        param.setType("LIMIT");
        param.setTimeInForce("GTC");
        param.setQuantity(1);
        param.setPrice("0.0071");
        param.setRecvWindow(10000);
        param.setTimestamp(time);

        //TODO 去掉自己的密钥
        String secret = "";
        String hash = SHA256.signature(secret, param.toString());
        param.setSignature(hash);

        String dddd = binanceAPIService.new_order("",
                param.getSymbol(),
                param.getSide(),
                param.getType(),
                param.getTimeInForce(),
                param.getQuantity(),
                param.getPrice(),
                param.getRecvWindow(),
                param.getTimestamp(),
                param.getSignature());
        return dddd;
    }


}