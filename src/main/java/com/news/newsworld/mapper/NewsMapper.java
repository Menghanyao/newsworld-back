package com.news.newsworld.mapper;

import com.news.newsworld.model.News;
import com.news.newsworld.model.Notice;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper
public interface NewsMapper {

    @Insert("insert into news" +
            "(user_id, group_id, news_title, news_content, news_range, read_count, read_hot, gmt_create, gmt_modified) " +
            "values " +
            "(#{userId}, #{groupId}, #{newsTitle}, #{newsContent}, #{newsRange}, #{readCount}, #{readHot}, #{gmtCreate}, #{gmtModified})")
    void addNews(News news);

    @Update("update news set read_count = (read_count + 1) , read_hot = (read_hot + 1), gmt_modified = #{gmtModified} where news_id = #{newsId}")
    void readNews(@Param("newsId") Long newsId, @Param("gmtModified") Long gmtModified);

    @Select("select * from news " +
            "where news_range = #{newsRange} " +
            "order by gmt_create desc " +
            "limit #{limit} offset #{offset}")
    List<News> newsListByRange(@Param("newsRange") Integer newsRange, @Param("offset") Long offset, @Param("limit") Long limit);

    @Select("select * from news " +
            "where news_range = #{newsRange}" +
            "order by read_hot desc " +
            "limit 10")
    List<News> newsListByHot(Integer newsRange);

    @Select("select * from news " +
            "where user_id = #{userId} " +
            "order by gmt_create desc " +
            "limit #{limit} offset #{offset}")
    List<News> newsListByUserId(@Param("userId") Long userId, @Param("offset") Long offset, @Param("limit") Long limit);

    @Select("select * from news " +
            "where group_id = #{groupId} " +
            "order by gmt_create desc " +
            "limit #{limit} offset #{offset}")
    List<News> newsListByGroup(@Param("groupId") Long groupId, @Param("offset") Long offset, @Param("limit") Long limit);

    @Select("select * from news " +
            "where news_id = #{newsId}")
    News getNewsById(@Value("newsId") Long newsId);

    @Delete("delete from news where news_id = #{newsId}")
    void deleteNewsById(@Value("newsId") Long newsId);

    @Update("update notice set read = true, gmt_modified = #{gmtModified} where notice_id = #{noticeId}")
    void readNotice(@Param("noticeId") Long noticeId, @Param("gmtModified") Long currentTimeMillis);

    @Select("select count(*) from notice " +
            "where notice_type = #{noticeType} and read = false")
    Long getUnreadReportTotal(@Param("noticeType") int noticeType);

    @Select("select * from notice " +
            "where notice_type = #{noticeType} and read = false " +
            "order by gmt_create desc " +
            "limit #{limit} offset #{offset}")
    List<Notice> getUnreadReportList(@Param("noticeType") int noticeType, @Param("offset")  Long offset, @Param("limit")  Long limit);


    @Select("select count(*) from notice " +
            "where to_id = #{userId} and read = false")
    Long getMyNoticeUnreadTotal(@Param("userId") Long userId);

    @Select("select count(*) from notice " +
            "where to_id = #{userId}")
    Long getMyNoticeTotal(@Param("userId") Long userId);

    @Select("select * from notice " +
            "where to_id = #{userId}" +
            "order by gmt_create desc " +
            "limit #{limit} offset #{offset}")
    List<Notice> getMyNoticeList(@Param("userId") Long userId, @Param("offset")  Long offset, @Param("limit")  Long limit);

    @Select("select count(*) from news where " +
            "news_title regexp #{search} and news_range = #{newsRange}")
    Long getSearchTotal(@Param("search") String search, @Param("newsRange") int newsRange);

    @Select("select * from news where " +
            "news_title regexp #{search} and news_range = #{newsRange} " +
            "order by gmt_create desc " +
            "limit #{limit} offset #{offset}")
    List<News> getSearchList(@Param("search") String search, @Param("newsRange") int newsRange, @Param("limit") Long limit, @Param("offset")  Long offset);

    @Select("select count(*) from news where news_range = #{newsRange}")
    Long indexNewsListTotal(@Param("newsRange") int newsRange);

    @Select("select count(*) from news where user_id = #{userId}")
    Long myNewsListTotal(@Param("userId") Long userId);

    @Select("select count(*) from news where group_id = #{groupId}")
    Long groupNewsListTotal(@Param("groupId") Long groupId);

    @Select("select count(*) from news where user_id = #{userId} and news_range = #{newsRange}")
    Long otherPublicNewsListTotal(@Param("userId") Long userId, @Param("newsRange")  int newsRange);


    @Select("select * from news where " +
            "user_id = #{userId} and news_range = #{newsRange} " +
            "order by gmt_create desc " +
            "limit #{limit} offset #{offset}")
    List<News> otherPublicNewsList(@Param("userId") Long userId, @Param("newsRange") int newsRange, @Param("offset") Long offset, @Param("limit")  Long limit);


    @Update("update news set read_hot = 0")
    void autoUpdateReadHot();
}
