package com.atguigu.gulimail.seckill.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author shkstart
 * @create 2021-07-03 22:23
 */
@FeignClient("gulimail-coupon")
public interface CouponFeignService {
    @GetMapping("/coupon/seckillsession/lates3DaySession")
    R getLates3DaySession();
}
