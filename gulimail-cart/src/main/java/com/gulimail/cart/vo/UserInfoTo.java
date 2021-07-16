package com.gulimail.cart.vo;

import lombok.Data;

/**
 * @author shkstart
 * @create 2021-06-22 16:39
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private boolean tempUser = false;
}
