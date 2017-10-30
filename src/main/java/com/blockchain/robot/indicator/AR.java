package com.blockchain.robot.indicator;

import com.blockchain.robot.entity.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * AR 人气指标
 * <p>
 * AR（人气指标）策略简介
 * 人气指标通过一定时期内开盘价、最高价、以及最低价之间的关系，
 * 来分析多空力量的对比，反映市场买卖人气，分析价格波动，达到追踪价格未来动向的目的。
 * <p>
 * 计算公式（以日为单位举例）
 * AR = [N天所有（High-Open）的和/ N天所有（Open—Low）的和] * 100
 * 解释：
 * N： 回看的时间窗口，一般设为26天
 * High: 每天的最高价
 * Open: 每天的开盘价
 * Low： 每天的最低价
 * <p>
 * https://zhuanlan.zhihu.com/p/24659914
 */
public class AR {

    // 设定回看时间窗口为26天
    private int period = 26;

    Logger logger = LoggerFactory.getLogger(AR.class);

    public double handle_data(List<Record> recordList) {
        if (recordList.size() == period) {

            double sumHigh = 0, sumLow = 0;
            for (Record record : recordList) {
                sumHigh += record.getHigh() - record.getOpen();
                sumLow += record.getOpen() - record.getLow();
            }

            // 计算AR值
            return sumHigh / sumLow * 100;

        } else {
            logger.error("AR策略，record数量不正确");
            return 0;
        }
    }


    public void setPeriod(int period) {
        this.period = period;
    }
}