package com.news.newsworld.mapper;

import com.news.newsworld.model.Group;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper
public interface GroupMapper {

    @Insert("insert into department" +
            "(user_id, group_name, group_state, group_scale, gmt_create, gmt_modified) " +
            "values " +
            "(#{userId}, #{groupName}, #{groupState}, #{groupScale}, #{gmtCreate}, #{gmtModified})")
    void addGroup(Group group);

    @Select("select group_id from department where user_id = #{userId}")
    Long getIdByUser(@Value("userId") Long userId);

    @Select("select * from department where group_id = #{groupId}")
    Group getGroupById(@Param("groupId") Long groupId);

    @Update("update department set " +
            "group_scale = #{groupScale}, group_state = #{groupState}, gmt_modified = #{gmtModified} " +
            "where " +
            "group_id = #{groupId}")
    void updateGroup(Group group);

    @Select("select group_scale from department where group_id = #{groupId}")
    Long getScaleById(@Value("groupId") Long groupId);

    @Select("select * from department " +
            "where group_scale > 0 " +
            "order by gmt_create desc " +
            "limit #{limit} offset #{offset}")
    List<Group> getGroupList(@Param("limit") Long limit, @Param("offset") Long offset);

    @Select("select count(*) from department where group_scale > 1")
    Long getGroupTotal();
}
