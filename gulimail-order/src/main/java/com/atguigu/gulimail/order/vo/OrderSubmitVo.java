package com.atguigu.gulimail.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 封装订单提交的数据
 * @author shkstart
 * @create 2021-06-29 20:44
 */
@Data
public class OrderSubmitVo {
    private Long addrId;//收货地址的id
    private Integer payType;//支付方式
    private String orderToken;//防重令牌
    private BigDecimal payPrice;//应付价格
    private String note;//订单备注
    //用户相关信息，直接去session取出登陆的用户
}
