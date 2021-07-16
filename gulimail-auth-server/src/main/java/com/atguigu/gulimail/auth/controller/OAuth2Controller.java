package com.atguigu.gulimail.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.HttpUtils;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberRespVo;
import com.atguigu.gulimail.auth.feign.MemberFeignService;
import com.atguigu.gulimail.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理社交登录请求
 *
 * @author shkstart
 * @create 2021-06-21 10:55
 */
@Slf4j
@Controller
public class OAuth2Controller {
    @Autowired
    MemberFeignService memberFeignService;

    //    @GetMapping("oauth2.0/weibo/success")
//    public String weibo(@RequestParam("code") String code){
//        return ""
//    }
    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session, HttpServletResponse servletResponse, HttpServletRequest request) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("client_id", "1269377057");
        map.put("client_secret", "22b09809617c9190f5c36ef119a6d8f6");
        map.put("grant_type", "authorization_code");
        map.put("redirect", "http://gulimail.com/oauth2.0/weibo/success");
        map.put("code", code);

        //1.根据code换取accessToken
        HttpResponse response = HttpUtils.doPost("api.weibo.com", "/oauth2/access_token", "post", null, null, map);

        //2.处理
        if (response.getStatusLine().getStatusCode() == 200) {
            //获取到了accessToken
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

            //知道当前是哪个社交用户
            //1.当前用户如果是第一次进网站，自动注册进来（为当前社交用户生成一个会员信息账号，以后这个社交用户就对应此账号）
            R oauthlogin = memberFeignService.oauthlogin(socialUser);
            if (oauthlogin.getCode() == 0) {
                MemberRespVo data = oauthlogin.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("登陆成功：用户:{}",data.toString());
                session.setAttribute("loginUser",data);
                return "redirect:http://auth.gulimail.com/login.html";

            } else {
                return "redirect:http://gulimail.com";
            }


        }else {
            return "redirect:http://auth.gulimail.com/login.html";
        }
    }
}