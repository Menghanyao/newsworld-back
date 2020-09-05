package com.news.newsworld.model;

import lombok.Data;

import java.util.List;

@Data
public class ListDTO<T> {
    private List<T> data;
    private Long total;
}
