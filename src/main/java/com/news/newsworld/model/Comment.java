package com.news.newsworld.model;

import lombok.Data;

@Data
public class Comment {
    private Long commentId;
    private Long newsId;
    private Integer commentType;
    private String content;
    private Long fromId;
    private Long toId;
    private Long gmtCreate;
    private Long gmtModified;

    private Long parentId;
}
