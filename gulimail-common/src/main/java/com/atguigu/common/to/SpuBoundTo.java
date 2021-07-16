package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shkstart
 * @create 2021-05-29 15:39
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
