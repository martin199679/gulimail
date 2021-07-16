package com.atguigu.gulimail.order.vo;

import com.atguigu.gulimail.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author shkstart
 * @create 2021-06-29 21:59
 */
@Data
public class SubmitOrderResponseVo {
    private OrderEntity order;
    private Integer code;
}
