package com.atguigu.gulimail.search.service;

import com.atguigu.gulimail.search.vo.SearchParam;
import com.atguigu.gulimail.search.vo.SearchResult;

/**
 * @author shkstart
 * @create 2021-06-12 21:23
 */
public interface MallSearchService {

    /**
     *
     * @param param 检索的所有的参数
     * @return 返回检索的结果
     */
    SearchResult search(SearchParam param);
}
