package com.atguigu.gulimail.auth.feign;

import com.atguigu.common.utils.R;
import com.atguigu.gulimail.auth.vo.SocialUser;
import com.atguigu.gulimail.auth.vo.UserLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author shkstart
 * @create 2021-06-20 21:03
 */
@FeignClient("gulimail-member")
public interface MemberFeignService {

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);
    @PostMapping("/member/member/oauth2/login")
    R oauthlogin(@RequestBody SocialUser socialUser)throws Exception;
}
