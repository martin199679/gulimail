package com.atguigu.gulimail.seckill.controller;

import com.atguigu.common.utils.R;
import com.atguigu.gulimail.seckill.service.SeckillService;
import com.atguigu.gulimail.seckill.to.SeckillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-07-04 14:28
 */
@Controller
public class SeckillController {
    @Autowired
    SeckillService seckillService;

    /**
     * 返回当前时间可以参与秒杀商品信息
     */
    @ResponseBody
    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus(){
        List<SeckillSkuRedisTo> vos = seckillService.getCurrentSeckillSkus();
        return R.ok().setData(vos);
    }

    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId){

        SeckillSkuRedisTo to = seckillService.getSkuSeckillInfo(skuId);
        return R.ok().setData(to);
    }

    @GetMapping("/kill")
    public String secKill(@RequestParam("killId") String killId, // session_skuID
                          @RequestParam("key") String key,
                          @RequestParam("num") Integer num, Model model){
        String orderSn = seckillService.kill(killId,key,num);
        // 1.判断是否登录
        model.addAttribute("orderSn", orderSn);
        return "success";
    }
}
