package com.atguigu.gulimail.seckill.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.to.mq.SeckillOrderTo;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberRespVo;
import com.atguigu.gulimail.seckill.feign.CouponFeignService;
import com.atguigu.gulimail.seckill.feign.ProductFeignService;
import com.atguigu.gulimail.seckill.interceptor.LoginUserInterceptor;
import com.atguigu.gulimail.seckill.service.SeckillService;
import com.atguigu.gulimail.seckill.to.SeckillSkuRedisTo;
import com.atguigu.gulimail.seckill.vo.SeckillSessionsWithSkus;
import com.atguigu.gulimail.seckill.vo.SkuInfoVo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2021-07-03 21:51
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    RabbitTemplate rabbitTemplate;


    private final String SESSIONS_CACHE_PREFIX = "seckill:sessions";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus";

    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    @Override
    public void uploadSeckillSkuLatest3Days() {
        //扫描最近三天需要参与秒杀的商品
        R session = couponFeignService.getLates3DaySession();
        if (session.getCode() == 0) {
            //上架商品
            List<SeckillSessionsWithSkus> sessionData = session.getData(new TypeReference<List<SeckillSessionsWithSkus>>() {
            });
            //缓存到redis
            //1、缓存活动信息
            saveSessionInfos(sessionData);
            //2.缓存活动的关联商品信息
            saveSessionSkuInfos(sessionData);

        }


    }

    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        // 1.确定当前时间属于那个秒杀场次
        long time = new Date().getTime();
        // 定义一段受保护的资源
        try (Entry entry = SphU.entry("seckillSkus")){
            Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
            for (String key : keys) {
                // seckill:sessions:1593993600000_1593995400000
                String replace = key.replace("seckill:sessions:", "");
                String[] split = replace.split("_");
                long start = Long.parseLong(split[0]);
                long end = Long.parseLong(split[1]);
                if(time >= start && time <= end){
                    // 2.获取这个秒杀场次的所有商品信息
                    List<String> range = redisTemplate.opsForList().range(key, 0, 100);
                    BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                    List<String> list = hashOps.multiGet(range);
                    if(list != null){
                        return list.stream().map(item -> {
                            SeckillSkuRedisTo redisTo = JSON.parseObject(item, SeckillSkuRedisTo.class);
                            //						redisTo.setRandomCode(null);
                            return redisTo;
                        }).collect(Collectors.toList());
                    }
                    break;
                }
            }
        }catch (BlockException e){
            log.warn("资源被限流：" + e.getMessage());
        }
        return null;
    }

    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        //获取所有商品的hash key
        Set<String> keys = ops.keys();
        for (String key : keys) {
            //通过正则表达式匹配 数字-当前skuid的商品
            if (Pattern.matches("\\d-" + skuId,key)) {
                String v = ops.get(key);
                SeckillSkuRedisTo redisTo = JSON.parseObject(v, SeckillSkuRedisTo.class);
                //当前商品参与秒杀活动
                if (redisTo!=null){
                    long current = System.currentTimeMillis();
                    //当前活动在有效期，暴露商品随机码返回
                    if (redisTo.getStartTime() < current && redisTo.getEndTime() > current) {
                        return redisTo;
                    }
                    //当前商品不再秒杀有效期，则隐藏秒杀所需的商品随机码
                    redisTo.setRandomCode(null);
                    return redisTo;
                }
            }
        }
        return null;
    }

    @Override
    public String kill(String killId, String key, Integer num) {
        MemberRespVo MemberRespVo = LoginUserInterceptor.loginUser.get();

        // 1.获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String json = hashOps.get(killId);
        if(StringUtils.isEmpty(json)){
            return null;
        }else{
            SeckillSkuRedisTo redisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
            // 校验合法性
            long time = new Date().getTime();
            if(time >= redisTo.getStartTime() && time <= redisTo.getEndTime()){
                // 1.校验随机码跟商品id是否匹配
                String randomCode = redisTo.getRandomCode();
                String skuId = redisTo.getPromotionSessionId() + "-" + redisTo.getSkuId();

                if(randomCode.equals(key) && killId.equals(skuId)){
                    // 2.说明数据合法
                    BigDecimal limit = redisTo.getSeckillLimit();
                    if(num <= limit.intValue()){
                        // 3.验证这个人是否已经购买过了
                        String redisKey = MemberRespVo.getId() + "-" + skuId;
                        // 让数据自动过期
                        long ttl = redisTo.getEndTime() - redisTo.getStartTime();

                        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl<0?0:ttl, TimeUnit.MILLISECONDS);
                        if(aBoolean){
                            // 占位成功 说明从来没买过// 前面的逻辑是acquireSemaphore(S
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            boolean acquire = semaphore.tryAcquire(num);// acquire阻塞  tryAcquire非阻塞
                            if(acquire){
                                // 秒杀成功
                                // 快速下单 发送MQ
                                String orderSn = IdWorker.getTimeId() + UUID.randomUUID().toString().replace("-","").substring(7,8);
                                SeckillOrderTo orderTo = new SeckillOrderTo();
                                orderTo.setOrderSn(orderSn);
                                orderTo.setMemberId(MemberRespVo.getId());
                                orderTo.setNum(num);
                                orderTo.setSkuId(redisTo.getSkuId());
                                orderTo.setSeckillPrice(redisTo.getSeckillPrice());
                                orderTo.setPromotionSessionId(redisTo.getPromotionSessionId());
                                rabbitTemplate.convertAndSend("order-event-exchange","order.seckill.order", orderTo);
                                return orderSn;
                            }
                        }else {
                            return null;
                        }
                    }
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }
        return null;
    }

    private void saveSessionInfos(List<SeckillSessionsWithSkus> sessions) {
        if (sessions != null) {
            sessions.stream().forEach(session -> {
                long startTime = session.getStartTime().getTime();

                long endTime = session.getEndTime().getTime();
                String key = SESSIONS_CACHE_PREFIX + startTime + "_" + endTime; // "seckill:sessions:";
                Boolean hasKey = redisTemplate.hasKey(key);
                // 防止重复添加活动到redis中
                if (!hasKey) {
                    // 获取所有商品id // 格式：活动id-skuId
                    List<String> skus = session.getRelationSkus().stream().map(item -> item.getPromotionSessionId() + "-" + item.getSkuId()).collect(Collectors.toList());
                    // 缓存活动信息
                    redisTemplate.opsForList().leftPushAll(key, skus);
                }
            });
        }
    }

    private void saveSessionSkuInfos(List<SeckillSessionsWithSkus> sessions) {
        if (sessions != null) {
            // 遍历session
            sessions.stream().forEach(session -> {
                BoundHashOperations<String, Object, Object> ops =
                        redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX); // "seckill:skus:";
                // 遍历sku
                session.getRelationSkus().stream().forEach(seckillSkuVo -> {
                    // 1.商品的随机码
                    String randomCode = UUID.randomUUID().toString().replace("-", "");
                    // 缓存中没有再添加
                    if (!ops.hasKey(seckillSkuVo.getPromotionSessionId() + "-" + seckillSkuVo.getSkuId())) {
                        // 2.缓存商品
                        SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                        BeanUtils.copyProperties(seckillSkuVo, redisTo);
                        // 3.sku的基本数据 sku的秒杀信息
                        R info = productFeignService.getSkuInfo(seckillSkuVo.getSkuId());
                        if (info.getCode() == 0) {
                            SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                            });
                            redisTo.setSkuInfo(skuInfo);
                        }
                        // 4.设置当前商品的秒杀信息
                        redisTo.setStartTime(session.getStartTime().getTime());
                        redisTo.setEndTime(session.getEndTime().getTime());
                        // 设置随机码
                        redisTo.setRandomCode(randomCode);
                        // 活动id-skuID   秒杀sku信息
                        ops.put(seckillSkuVo.getPromotionSessionId() + "-" + seckillSkuVo.getSkuId(), JSON.toJSONString(redisTo));
                        // 5.使用库存作为分布式信号量  限流
                        RSemaphore semaphore =
                                redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);//"seckill:stock:";
                        // 在信号量中设置秒杀数量
                        semaphore.trySetPermits(seckillSkuVo.getSeckillCount().intValue());
                    }
                });
            });
        }
    }
}
