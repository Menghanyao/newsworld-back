package com.news.newsworld.model;

import lombok.Data;

@Data
public class News {
    private Long newsId;
    private Long groupId;
    private Long userId;
    private String newsTitle;
    private String newsContent;
    private Integer newsRange;
    private Long readCount;
    private Long readHot;
    private Long gmtCreate;
    private Long gmtModified;

}
