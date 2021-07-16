package com.atguigu.gulimail.ware.vo;

import lombok.Data;

/**
 * @author shkstart
 * @create 2021-06-30 15:54
 */
@Data
public class LockStockResult {
    private Long skuId;
    private Integer num;
    private Boolean locked;
}
