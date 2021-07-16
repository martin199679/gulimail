package com.atguigu.gulimail.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-30 15:50
 */
@Data
public class WareSkuLockVo {

    private String orderSn;//订单号
    private List<OrderItemVo> locks;//需要锁住的所有库存信息
}
