package com.atguigu.gulimail.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-05 17:21
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catelog2VO {
    private String catelog1Id;//1级父分类id
    private List<Catelog2VO.Catelog3Vo> catelog3List;//三级子分类
    private String id;
    private String name;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catelog3Vo{
        private String catelog2id;//2级分类
        private String id;
        private String name;
    }
}
