package com.news.newsworld.model;

import lombok.Data;

@Data
public class Notice {
    private Long noticeId;
    private Long newsId;
    private Integer noticeType;
    private Long fromId;
    private Long toId;
    private String content;
    private Boolean read;
    private Long gmtCreate;
    private Long gmtModified;

    private News news;
    private User user;
}
