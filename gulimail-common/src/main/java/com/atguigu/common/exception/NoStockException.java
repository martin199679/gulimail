package com.atguigu.common.exception;

/**
 * @author shkstart
 * @create 2021-06-30 17:02
 */
public class NoStockException extends RuntimeException{

    public Long skuId;

    public NoStockException(Long skuId){
        super("商品id:"+skuId+"没有足够的库存了");
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
