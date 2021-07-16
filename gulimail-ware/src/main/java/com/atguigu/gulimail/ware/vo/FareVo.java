package com.atguigu.gulimail.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shkstart
 * @create 2021-06-29 18:49
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
