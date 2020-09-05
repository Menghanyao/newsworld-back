package com.news.newsworld.service;

import com.news.newsworld.dto.CommentDTO;
import com.news.newsworld.enumeration.AllEnum;
import com.news.newsworld.mapper.CommentMapper;
import com.news.newsworld.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;

    /**
     * 通用方法，往数据库新增一条comment记录，包括了：一二级的评论、点赞
     * @param comment
     * @return
     */
    public ReturnValue databaseAddComment(Comment comment) {
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        commentMapper.addComment(comment);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(comment.getCommentType());
        return returnValue;
    }

    public void sendNotice(Comment comment, Integer type, String content) {
        Notice notice = new Notice();
        notice.setNewsId(comment.getNewsId());
        notice.setFromId(comment.getFromId());
        notice.setToId(comment.getToId());
        notice.setNoticeType(type);
        notice.setContent(content);
        noticeService.addNotice(notice);
    }

    /**
     * 添加点赞或者评论
     * @param comment
     * @return
     */
    public ReturnValue addComment(Comment comment) {
        /**
         * 1 先把comment记录放进数据库中
         * 2 设置消息
         * 3 发送消息
         */
        databaseAddComment(comment);

        ReturnValue returnValue = new ReturnValue();
        String noticeContent = "";
        String fromUserName = userService.getUserNameById(comment.getFromId());
        if (comment.getCommentType() == AllEnum.COMMENT_TYPE_FIRST_LIKE.getCode()
                || comment.getCommentType() == AllEnum.COMMENT_TYPE_SECOND_LIKE.getCode()) {
            noticeContent = fromUserName + "点赞了您";
            returnValue.setMsg("已点赞");
        } else if (comment.getCommentType() == AllEnum.COMMENT_TYPE_FIRST_COMMENT.getCode()
                || comment.getCommentType() == AllEnum.COMMENT_TYPE_SECOND_COMMENT.getCode()) {
            noticeContent = fromUserName + "评论了您";
            returnValue.setMsg("已评论");
        }
        sendNotice(comment, AllEnum.NOTICE_TYPE_LIKE_COMMENT.getCode(), noticeContent);
        returnValue.setCode(comment.getCommentType());
        return returnValue;
    }

    /**
     * 取消一二级点赞
     * @param comment
     * @return
     */
    public ReturnValue cancelLike(Comment comment) {
        commentMapper.cancelLike(comment.getCommentId());
        return null;
    }

    /**
     * 删除二级评论，直接删
     * @param comment
     * @return
     */
    public ReturnValue deleteSecondComment(Comment comment) {
        commentMapper.deleteSecondComment(comment.getCommentId());
        return null;
    }

    /**
     * 删除一级评论，逻辑删除
     * @param comment
     * @return
     */
    public ReturnValue deleteFirstComment(Comment comment) {
        commentMapper.deleteFirstComment(comment.getCommentId(), "该评论已被删除", System.currentTimeMillis());
        return null;
    }

    public ReturnValue getNewsLikeCount(News news) {
        Long newsLikeCount =  commentMapper.getNewsLikeCount(news.getNewsId());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setMsg("已返回新闻的点赞总数");
        returnValue.setData(newsLikeCount);
        return returnValue;

    }

    public ReturnValue getFirstLikeList(News news) {
        List<Comment> firstLikeList =  commentMapper.getFirstLikeList(news.getNewsId());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setMsg("已返回一级点赞列表");
        returnValue.setData(firstLikeList);
        return returnValue;
    }

    public ReturnValue getFirstCommentList(News news) {
        List<Comment> list = commentMapper.getFirstCommentList(news.getNewsId());
        List<CommentDTO> commentDTOList = setUserForComment(list);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setMsg("已返回新闻的一级评论列表");
        returnValue.setData(commentDTOList);
        return returnValue;
    }


    public ReturnValue getSecondLikeCount(Comment comment) {
        Long secondLikeCount = commentMapper.getSecondLikeCount(comment.getCommentId(), comment.getFromId());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setMsg("已返回该评论的点赞数");
        returnValue.setData(secondLikeCount);
        return returnValue;
    }

    public ReturnValue getSecondLikeList(Comment comment) {
        List<Comment> secondLikeCount = commentMapper.getSecondLikeList(comment.getCommentId(), comment.getFromId());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setMsg("已返回二级点赞列表");
        returnValue.setData(secondLikeCount);
        return returnValue;
    }


    public ReturnValue getSecondCommentCount(Comment comment) {
        Long secondCommentCount = commentMapper.getSecondCommentCount(comment.getCommentId(), comment.getToId());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setMsg("已返回该评论的二级评论数");
        returnValue.setData(secondCommentCount);
        return returnValue;
    }

    public ReturnValue getSecondCommentList(Comment comment) {
        System.out.println("parent comment = " + comment);
        List<Comment> list = commentMapper.getSecondCommentList(comment.getCommentId(), comment.getFromId());
        System.out.println("child comment = " + list);
        List<CommentDTO> commentDTOList = setUserForComment(list);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setMsg("已返回该评论的的二级评论列表");
        returnValue.setData(commentDTOList);
        return returnValue;
    }

    /**
     * 为comment赋予作者信息
     * @param list
     * @return
     */
    public List<CommentDTO> setUserForComment(List<Comment> list) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment item: list) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setComment(item);
            commentDTO.setFromUser((User) userService.getUser(item.getFromId()).getData());
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
