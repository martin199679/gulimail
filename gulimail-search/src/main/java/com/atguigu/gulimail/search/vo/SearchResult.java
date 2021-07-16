package com.atguigu.gulimail.search.vo;

import com.atguigu.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-12 22:31
 */
@Data
public class SearchResult {
    //查询到所有商品信息
    private List<SkuEsModel> products;
    /**
     * 以下是分页信息
     */
    private Integer pageNum;//当前页码
    private Long total;//总记录数
    private Integer totalPages;//总记录数
    private List<Integer> pageNavs;

    private List<AttrVo> attrs;//当前查询到的结果，所有涉及到的所有属性
    private List<CatelogVo> catelogs;//当前查询到的结果，所有涉及到的分类
    private List<BrandVo> brands;//当前查询到的结果，所有涉及到的品牌

    //面包屑导航数据
    private List<NavVo> navs = new ArrayList<>();
    private List<Long> attrIds = new ArrayList<>();
    @Data
    public static class NavVo{
        private String navName;
        private String navValue;
        private String link;
    }
    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class CatelogVo{
        private Long catelogId;
        private String catelogName;
    }

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
