package com.atguigu.gulimail.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shkstart
 * @create 2021-06-30 7:42
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
