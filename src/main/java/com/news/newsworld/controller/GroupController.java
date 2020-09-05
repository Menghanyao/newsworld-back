package com.news.newsworld.controller;

import com.news.newsworld.model.Group;
import com.news.newsworld.model.Pagination;
import com.news.newsworld.model.ReturnValue;
import com.news.newsworld.model.User;
import com.news.newsworld.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@CrossOrigin
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 创建group
     * @param group
     * @return
     */
    @PostMapping("/addGroup")
    public ReturnValue addGroup(@RequestBody() Group group) {
        return groupService.addGroup(group);
    }

    /**
     * 自己退群
     * @param user
     * @return
     */
    @PostMapping("/exitGroup")
    public ReturnValue exitGroup(@RequestBody() User user) {
        return groupService.exitGroup(user);
    }


    /**
     * 管理员获取部门列表
     * @param pagination
     * @return
     */
    @PostMapping("/groupList")
    public ReturnValue groupList(@RequestBody() Pagination pagination) {
        return groupService.groupList(pagination);
    }

    /**
     * 管理员获往群里加人
     * @param user
     * @return
     */
    @PostMapping("/addMember")
    public ReturnValue addMember(@RequestBody() User user) {
        return groupService.addMember(user);
    }

    /**
     * 获取部门信息
     * @param groupId
     * @return
     */
    @PostMapping("/getGroup")
    public ReturnValue getGroup(@RequestBody() Long groupId) {
        return groupService.getGroup(groupId);
    }

}
