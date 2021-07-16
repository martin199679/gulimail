package com.atguigu.gulimail.product.feign.fallback;

import com.atguigu.common.utils.R;
import com.atguigu.gulimail.product.feign.SeckillFeignService;
import org.springframework.stereotype.Component;

/**
 * @author shkstart
 * @create 2021-07-06 10:17
 */
@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSeckillInfo(Long skuId) {
        return R.error(56666,"请求太多");
    }
}
