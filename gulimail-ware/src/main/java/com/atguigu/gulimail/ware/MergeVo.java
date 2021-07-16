package com.atguigu.gulimail.ware;

import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-05-30 17:17
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
