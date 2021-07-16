package com.atguigu.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shkstart
 * @create 2021-07-05 16:52
 */
@Data
public class SeckillOrderTo {

    private String orderSn;
    private Long promotionSessionId;//场次ID
    private Long skuId;//商品id
    private BigDecimal seckillPrice;
    private Integer num;
    private Long memberId;
}
