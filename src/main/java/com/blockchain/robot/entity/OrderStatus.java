package com.blockchain.robot.entity;

/**
 * 枚举 订单类型
 * <p>
 * ORDER_STATE_PENDING	:未完成
 * ORDER_STATE_FILLED	:已填满（完成）
 * ORDER_STATE_CANCELED	:已取消
 */
public enum OrderStatus {
    ORDER_STATE_PENDING, ORDER_STATE_FILLED, ORDER_STATE_CANCELED
}
