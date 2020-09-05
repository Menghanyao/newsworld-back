package com.news.newsworld.mapper;

import com.news.newsworld.model.Notice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {

    @Insert("insert into notice" +
            "(news_id, notice_type, from_id, to_id, content, read, gmt_create, gmt_modified) " +
            "values " +
            "(#{newsId}, #{noticeType}, #{fromId}, #{toId}, #{content}, #{read}, #{gmtCreate}, #{gmtModified})")
    void addNotice(Notice notice);
}
