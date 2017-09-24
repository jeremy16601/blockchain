# blockchain

### 随想 核心算法（15分钟走势图）

1. 最新价位为一个点Point，当前Point的前96根K线（即前24小时）为分析点，计算出最高点HPoint，最低点LPoint。

2. 计算HPoint-LPoint 0.618的位置GPoint

3. 如果当前点位CPoint为GPoint的95%则买入。记为BuyPoint(1),买入会随身携带买入依据信息HPoint LPoint GPoint。
同时买入点BuyPoint(1) 加入到堆栈BuyStack中。

4. 随着时间的后移，继续执行1）2）操作

5. 如果当前点位CPoint为GPoint的90%。
先判断买入栈BuyStack是否已有买入点，
如果有则判断CPoint和栈顶BuyPoint（1）对应的HPoint*0.9对比，以Min(point,HPoint*0.9)作为买入点BuyPoint(2)
如果没有则执行3）操作

6. 卖出点的计算。以买入栈BuyStack的栈顶开始pull，CPoint*1.05f 开始卖出，直到栈清空。

7. 5%的跌幅买入，5%的涨幅卖出

### 技术解耦

1. 对接交易所API 获取各种K线信息，账户登录，订单交易等操作

2. 核心算法层，根据K线提供买点 卖点信息

3. 交互层，用户的密钥设置，买卖点日志，盈利日志，每次买入金额比例设置，终止条件的设定等
