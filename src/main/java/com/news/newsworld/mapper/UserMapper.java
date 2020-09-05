package com.news.newsworld.mapper;

import com.news.newsworld.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user" +
            "(user_level, user_phone, user_name, user_password, user_token, gmt_create, gmt_modified)" +
            "values" +
            "(#{userLevel}, #{userPhone}, #{userName}, #{userPassword}, #{userToken}, #{gmtCreate}, #{gmtModified})")
    void addUser(User user);

    @Select("select count(*) from user where user_phone = #{userPhone}")
    int ifDuplicateRegistry(@Value("userPhone") Long userPhone);

    @Select("select * from user where user_id = #{userId}")
    User getUserById(@Value("userId") Long userId);

    @Select("select * from user where user_phone = #{userPhone}")
    User getUserByPhone(@Value("userPhone") Long userPhone);

    @Update("update user set user_token = #{userToken} , gmt_modified = #{gmtModified} where user_id = #{userId}")
    void updateUser(User user);

    @Update("update user set group_id = #{groupId}, user_Level = #{userLevel}, gmt_modified = #{gmtModified} where user_id = #{userId}")
    void setGroupIdById(@Param("groupId") Long groupId, @Param("userLevel") int userLevel, @Param("gmtModified") Long gmtModified, @Param("userId") Long userId);

    @Select("select user_id from user where user_phone = #{userPhone}")
    Long getIdByPhone(@Value("userPhone") Long userPhone);

    @Select("select user_id, group_id, user_level, user_phone, user_name from user where group_id = #{groupId}")
    List<User> colleagueList(@Value("groupId") Long groupId);

    @Select("select user_name from user where user_id = #{userId}")
    String getUserNameById(@Param("userId") Long userId);
}
