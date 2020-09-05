package com.news.newsworld.controller;

import com.news.newsworld.enumeration.AllEnum;
import com.news.newsworld.model.Comment;
import com.news.newsworld.model.News;
import com.news.newsworld.model.ReturnValue;
import com.news.newsworld.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment")
    public ReturnValue addComment(@RequestBody() Comment comment) {
        ReturnValue returnValue = commentService.addComment(comment);
        return returnValue;
    }

    @PostMapping("/cancelLike")
    public ReturnValue cancelLike(@RequestBody() Comment comment) {
        ReturnValue returnValue = commentService.cancelLike(comment);
        return returnValue;
    }

    @PostMapping("/deleteSecondComment")
    public ReturnValue deleteSecondComment(@RequestBody() Comment comment) {
        ReturnValue returnValue = commentService.deleteSecondComment(comment);
        return returnValue;
    }

    @PostMapping("/deleteFirstComment")
    public ReturnValue deleteFirstComment(@RequestBody() Comment comment) {
        ReturnValue returnValue = commentService.deleteFirstComment(comment);
        return returnValue;
    }

    @PostMapping("/getNewsLikeCount")
    public ReturnValue getNewsLikeCount(@RequestBody() News news) {
        ReturnValue returnValue = commentService.getNewsLikeCount(news);
        return returnValue;
    }

    @PostMapping("/getFirstLikeList")
    public ReturnValue getFirstLikeList(@RequestBody() News news) {
        ReturnValue returnValue = commentService.getFirstLikeList(news);
        return returnValue;
    }

    @PostMapping("/getFirstCommentList")
    public ReturnValue getFirstCommentList(@RequestBody() News news) {
        ReturnValue returnValue = commentService.getFirstCommentList(news);
        return returnValue;
    }

    @PostMapping("/getSecondLikeCount")
    public ReturnValue getSecondLikeCount(@RequestBody() Comment comment) {
        ReturnValue returnValue = commentService.getSecondLikeCount(comment);
        return returnValue;
    }

    @PostMapping("/getSecondLikeList")
    public ReturnValue getSecondLikeList(@RequestBody() Comment comment) {
        ReturnValue returnValue = commentService.getSecondLikeList(comment);
        return returnValue;
    }


    @PostMapping("/getSecondCommentCount")
    public ReturnValue getSecondCommentCount(@RequestBody() Comment comment) {
        ReturnValue returnValue = commentService.getSecondCommentCount(comment);
        return returnValue;
    }


    @PostMapping("/getSecondCommentList")
    public ReturnValue getSecondCommentList(@RequestBody() Comment comment) {
        ReturnValue returnValue = commentService.getSecondCommentList(comment);
        return returnValue;
    }
}
