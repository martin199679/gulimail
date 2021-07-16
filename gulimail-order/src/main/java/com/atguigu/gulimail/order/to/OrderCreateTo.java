package com.atguigu.gulimail.order.to;

import com.atguigu.gulimail.order.entity.OrderEntity;
import com.atguigu.gulimail.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-30 7:25
 */
@Data
public class OrderCreateTo {
    private OrderEntity order;
    private List<OrderItemEntity> orderItems;
    private BigDecimal payPrice;//订单计算的应付价格
    private BigDecimal fare;//运费
}
