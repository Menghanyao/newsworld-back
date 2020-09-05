package com.news.newsworld.mapper;

import com.news.newsworld.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {


    @Insert("insert into comment" +
            "(news_id, comment_type, content, from_id, to_id, parent_id, gmt_create, gmt_modified) " +
            "values " +
            "(#{newsId}, #{commentType}, #{content}, #{fromId}, #{toId}, #{parentId}, #{gmtCreate}, #{gmtModified})")
    void addComment(Comment comment);

    @Delete("delete from comment where comment_id = #{commentId}")
    void cancelLike(@Param("commentId") Long commentId);

    @Delete("delete from comment where comment_id = #{commentId}")
    void deleteSecondComment(@Param("commentId") Long commentId);

    @Update("update comment set " +
            "content = #{content}, gmt_modified = #{gmtModified} " +
            "where " +
            "comment_id = #{commentId}")
    void deleteFirstComment(@Param("commentId") Long commentId, @Param("content") String content,  @Param("gmtModified") Long gmtModified);



    @Select("select count(*) from comment where news_id = #{newsId} and comment_type = 40")
    Long getNewsLikeCount(@Param("newsId") Long newsId);

    @Select("select * from comment where news_id = #{newsId} and comment_type = 40")
    List<Comment> getFirstLikeList(@Param("newsId") Long newsId);

    @Select("select * from comment where news_id = #{newsId} and comment_type = 42")
    List<Comment> getFirstCommentList(@Param("newsId") Long newsId);

    @Select("select count(*) from comment where parent_id = #{commentId} and comment_type = 41 and to_id = #{fromId}")
    Long getSecondLikeCount(@Param("commentId") Long commentId, @Param("fromId") Long fromId);

    @Select("select * from comment where parent_id = #{commentId} and comment_type = 41 and to_id = #{fromId}")
    List<Comment> getSecondLikeList(@Param("commentId") Long commentId, @Param("fromId") Long fromId);


    @Select("select count(*) from comment where parent_id = #{commentId} and comment_type = 43 and to_id = #{toId}")
    Long getSecondCommentCount(@Param("commentId") Long commentId, @Param("toId") Long toId);

    @Select("select * from comment where parent_id = #{commentId} and comment_type = 43 and to_id = #{fromId}")
    List<Comment> getSecondCommentList(@Param("commentId") Long commentId, @Param("fromId") Long fromId);

}
