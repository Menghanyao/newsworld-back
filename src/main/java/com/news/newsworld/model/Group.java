package com.news.newsworld.model;

import lombok.Data;

@Data
public class Group {
    private Long groupId;
    private Long userId;
    private String groupName;
    private Integer groupState;
    private Long groupScale;
    private Long gmtCreate;
    private Long gmtModified;

    private String userName;

}
