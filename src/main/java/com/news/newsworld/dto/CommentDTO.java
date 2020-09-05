package com.news.newsworld.dto;

import com.news.newsworld.model.Comment;
import com.news.newsworld.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Comment comment;
    private User fromUser;
}
