package com.atguigu.gulimail.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-18 22:34
 */
@Data
@ToString
public class SpuBaseAttrVo {
    private String attrName;
    private List<String> attrValues;
}
