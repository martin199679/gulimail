package com.atguigu.thirdparty.controller;

import com.atguigu.common.utils.R;
import com.atguigu.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shkstart
 * @create 2021-06-20 17:20
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {
    @Autowired
    SmsComponent smsComponent;
    @GetMapping("/sendcode")
    public R sendCode(@PathVariable("phone") String phone,@PathVariable("code") String code){
        smsComponent.sendSmsCode(phone,code);
        return R.ok();
    }
}
