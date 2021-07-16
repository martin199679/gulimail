package com.atguigu.gulimail.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-18 22:33
 */
@Data
@ToString
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<Attr> attrs;
}
