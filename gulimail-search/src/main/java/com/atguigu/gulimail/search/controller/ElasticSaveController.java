package com.atguigu.gulimail.search.controller;

import com.atguigu.common.to.es.SkuEsModel;
import com.atguigu.common.utils.R;
import com.atguigu.gulimail.search.service.ProductSaveService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-02 21:33
 */
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {
    @Resource
    ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) throws IOException {

        productSaveService.productStatusUp(skuEsModels);
        return R.ok();

    }
}
