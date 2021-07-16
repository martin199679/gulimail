package com.atguigu.gulimail.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-18 22:37
 */
@Data
@ToString
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}
