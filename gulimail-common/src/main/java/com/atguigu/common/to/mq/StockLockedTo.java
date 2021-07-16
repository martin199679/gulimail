package com.atguigu.common.to.mq;

import lombok.Data;

/**
 * @author shkstart
 * @create 2021-07-02 16:29
 */
@Data
public class StockLockedTo {
    private Long id;//库存工作单id
    private StockDetailTo detailTo;//工作详情的所有id
}
