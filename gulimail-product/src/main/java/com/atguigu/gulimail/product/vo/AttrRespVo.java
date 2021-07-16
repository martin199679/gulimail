package com.atguigu.gulimail.product.vo;

import lombok.Data;

/**
 * @author shkstart
 * @create 2021-05-23 16:43
 */
@Data
public class AttrRespVo extends AttrVo{
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
