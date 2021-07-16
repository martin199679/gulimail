package com.atguigu.gulimail.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberRespVo;
import com.atguigu.gulimail.auth.feign.MemberFeignService;
import com.atguigu.gulimail.auth.feign.ThirdPartFeignService;
import com.atguigu.gulimail.auth.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author shkstart
 * @create 2021-06-19 21:15
 */
@Controller
public class LoginController {
//    @GetMapping("/login.html")
//    public String loginPage(){
//        return "login";
//    }
//
//    @GetMapping("/reg.html")
//    public String regPage(){
//        return "reg";
//    }
    @Autowired
    ThirdPartFeignService thirdPartFeignService;
    @Autowired
    MemberFeignService memberFeignService;
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone){
        String code = UUID.randomUUID().toString().substring(0, 5);
        thirdPartFeignService.sendCode(phone,code);
        return R.ok();
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute==null){
            //没登陆
            return "login";
        }else {
            return "redirect:http://gulimail.com";
        }
    }
    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes,
                        HttpSession session){
        R login = memberFeignService.login(vo);
        if (login.getCode()==0){
            MemberRespVo data = login.getData("data", new TypeReference<MemberRespVo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER,data);
            return "redirect:http://gulimail.com";
        }else {
            Map<String,String> errors = new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimail.com/login.html";
        }

    }
}
