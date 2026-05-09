package com.ai.code.common;

import java.util.List;

public record Order (
        String orderId,           // 订单ID
        List<OrderItem> items,    // 订单项列表（嵌套Record）
        Double totalAmount,       // 订单总金额
        String status            // 订单状态
) {}
