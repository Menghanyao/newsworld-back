package com.news.newsworld.model;

import lombok.Data;

@Data
public class ReturnValue {
//    返回体，通用格式，包含了状态码，信息，数据
    private int code;
    private String msg;
    private Object data;

}
