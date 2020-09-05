package com.news.newsworld.model;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private Long groupId;   // 部门id
    private Integer userLevel;  // 用户身份：普通，群主，管理员
    private Long userPhone;     // 电话，登录主键，唯一
    private String userName;    // 姓名
    private String userPassword;
    private String userToken;
    private Long gmtCreate;
    private Long gmtModified;

}
