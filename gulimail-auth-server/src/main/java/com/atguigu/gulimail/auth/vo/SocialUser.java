package com.atguigu.gulimail.auth.vo;

import lombok.Data;

/**
 * @author shkstart
 * @create 2021-06-21 15:54
 */
@Data
public class SocialUser {

    private String access_token;
    private String remind_in;
    private long expires_in;
    private String uid;
    private String isRealName;
}
