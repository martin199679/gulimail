package com.atguigu.gulimail.order.web;

import com.atguigu.gulimail.order.service.OrderService;
import com.atguigu.gulimail.order.vo.OrderConfirmVo;
import com.atguigu.gulimail.order.vo.OrderSubmitVo;
import com.atguigu.gulimail.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shkstart
 * @create 2021-06-28 17:22
 */
@Controller
public class OrderWebController {
    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request){
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData",confirmVo);
        //展示订单确认的数据
        return "confirm";
    }

    /**
     * 下单功能
     * @param vo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo,Model model){

        SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);


        //下单失败回到订单确认重新确认订单信息
        if (responseVo.getCode() == 0){
            //下单成功来到支付选择页
            model.addAttribute("submitOrderResp",responseVo);
            return "pay";
        }else {
            return "redirect:http://order.gulimail.com/toTrade";
        }


    }
}
