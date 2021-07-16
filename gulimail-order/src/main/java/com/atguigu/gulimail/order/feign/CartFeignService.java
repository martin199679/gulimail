package com.atguigu.gulimail.order.feign;

import com.atguigu.gulimail.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-28 19:00
 */
@FeignClient("gulimail-cart")
public interface CartFeignService {
    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();
}
