package com.atguigu.gulimail.auth.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author shkstart
 * @create 2021-06-20 17:25
 */
@FeignClient("gulimail-third-party")
public interface ThirdPartFeignService {

    @GetMapping("/sms/sendcode")
    public R sendCode(@PathVariable("phone") String phone, @PathVariable("code") String code);

}
