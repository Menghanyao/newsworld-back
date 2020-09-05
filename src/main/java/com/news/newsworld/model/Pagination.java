package com.news.newsworld.model;

import lombok.Data;

@Data
public class Pagination {
    private Long userId;
    private Long current;
    private Long size = 2L;
    private Integer type;
    private String search;
}
