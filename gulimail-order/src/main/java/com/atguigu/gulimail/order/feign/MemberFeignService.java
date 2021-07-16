package com.atguigu.gulimail.order.feign;

import com.atguigu.gulimail.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-28 18:28
 */
@FeignClient("gulimail-member")
public interface MemberFeignService {
    @GetMapping("member/memberreceiveaddress/{memberId}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);
}
