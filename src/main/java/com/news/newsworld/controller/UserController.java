package com.news.newsworld.controller;

import com.news.newsworld.model.ReturnValue;
import com.news.newsworld.model.User;
import com.news.newsworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public Object addUser(@RequestBody User user) {
        /**
         * 添加普通用户，交给UserService实现
         */
        ReturnValue returnValue = (ReturnValue) userService.addUser(user);
        return returnValue;
    }

    @PostMapping("/addAdmin")
    public Object addAdmin(@RequestBody User user) {
        /**
         * 添加admin用户，交给UserService实现
         */
        ReturnValue returnValue = (ReturnValue) userService.addAdmin(user);
        return returnValue;
    }

    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        /**
         * 注册功能
         */
        ReturnValue returnValue = userService.login(user);
        return returnValue;
    }

    @PostMapping("/invite")
    public ReturnValue invite(@RequestBody User user) {
        /**
         * 这里会拿到两个输入，分别是群主的id和被邀请者的phone
         */
        ReturnValue returnValue = userService.invite(user.getUserId(), user.getUserPhone());
        return  returnValue;
    }

    /**
     * 获取同事列表
     * @param user
     * @return
     */
    @PostMapping("/colleagueList")
    public ReturnValue colleagueList(@RequestBody() User user) {
        return userService.colleagueList(user);
    }

    /**
     * 获取作者
     * @param userId
     * @return
     */
    @PostMapping("/getUser")
    public ReturnValue getUser(@RequestBody() Long userId) {
        return userService.getUser(userId);
    }
}
