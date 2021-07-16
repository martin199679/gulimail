package com.atguigu.gulimail.product.feign;

import com.atguigu.common.utils.R;
import com.atguigu.gulimail.product.feign.fallback.SeckillFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author shkstart
 * @create 2021-07-04 15:20
 */
@FeignClient(value = "gulimail-seckill",fallback = SeckillFeignServiceFallBack.class)
public interface SeckillFeignService {
    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId") Long skuId);
}
