package com.atguigu.gulimail.seckill.service;

import com.atguigu.gulimail.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-07-03 21:51
 */
public interface SeckillService {
    void uploadSeckillSkuLatest3Days();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
