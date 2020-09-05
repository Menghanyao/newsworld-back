package com.news.newsworld.service;

import com.news.newsworld.enumeration.AllEnum;
import com.news.newsworld.mapper.GroupMapper;
import com.news.newsworld.mapper.UserMapper;
import com.news.newsworld.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserService userService;


    /**
     *
     * @param group
     * @return 返回创建部门是否成功
     */
    public ReturnValue addGroup(Group group) {
        /**
         * 创建部门：直接创建，并更改user的group状态
         */
        group.setGroupScale(1L);
        group.setGroupState(AllEnum.GROUP_STATE_NORMAL.getCode());
        group.setGmtCreate(System.currentTimeMillis());
        group.setGmtModified(group.getGmtCreate());
//        System.out.println("已经添加 group = " + group);
        groupMapper.addGroup(group);
        group.setGroupId(groupMapper.getIdByUser(group.getUserId()));
        System.out.println("已经添加 group = " + group);
        //给更改user表中的userLevel和groupId
        userService.setGroupIdById(group.getUserId(), AllEnum.USER_LEVEL_OWNER.getCode(), group.getGroupId());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(AllEnum.GROUP_ADD_SUCCESS.getCode());
        returnValue.setMsg(AllEnum.GROUP_ADD_SUCCESS.getMsg());
        returnValue.setData(group);
        return returnValue;
    }

    /**
     * 通过主键查找部门信息
     * @param groupId，主键
     * @return Group，返回一条记录
     */
    public Group getGroupById(Long groupId) {
        return groupMapper.getGroupById(groupId);
    }

    /**
     * 给group加一个人
     * @param groupId
     */
    @Transactional
    public void addOnePerson(Long groupId) {
        Group group = getGroupById(groupId);
        group.setGroupScale(group.getGroupScale() + 1);
        group.setGmtModified(System.currentTimeMillis());
        groupMapper.updateGroup(group);
    }

    /**
     * 退群操作
     * @param user
     * @return
     */
    public ReturnValue exitGroup(User user) {
        /**
         * 退群逻辑：
         * 1 普通群员，可直接退，修改两处
         * 2 群主，判断群组人数，大于一人不能退，返回错误信息
         * 3 群主，只剩一人，可退。
         */
        ReturnValue returnValue = new ReturnValue();
        if (userService.checkAuthority(user.getUserId(), AllEnum.USER_LEVEL_OWNER.getCode())) {
            Long scale = groupMapper.getScaleById(user.getGroupId());
            if (scale > 1) {
                returnValue.setCode(AllEnum.EXIT_FAIL_HAVE_MEMBER.getCode());
                returnValue.setMsg(AllEnum.EXIT_FAIL_HAVE_MEMBER.getMsg());
                return returnValue;
            }
        }
        userService.setGroupIdById(user.getUserId(), AllEnum.USER_LEVEL_NONE.getCode(), 0L);
        minusOnePeople(user.getGroupId());
        returnValue.setCode(AllEnum.EXIT_SUCCESS.getCode());
        returnValue.setMsg(AllEnum.EXIT_SUCCESS.getMsg());
        return returnValue;
    }

    /**
     * 给group减一个人
     * @param groupId
     */
    private void minusOnePeople(Long groupId) {
        Group group = getGroupById(groupId);
        group.setGroupScale(group.getGroupScale() - 1);
        if (group.getGroupScale() == 0) {
            group.setGroupState(AllEnum.GROUP_STATE_DROP.getCode());
        }
        group.setGmtModified(System.currentTimeMillis());
        groupMapper.updateGroup(group);
    }


    /**
     * 管理员得到部门列表
     * @param pagination
     * @return
     */
    public ReturnValue groupList(Pagination pagination) {
        Long limit = pagination.getSize();
        Long offset = (pagination.getCurrent() - 1) * limit;
        ReturnValue returnValue = new ReturnValue();
        if (userService.checkAuthority(pagination.getUserId(), AllEnum.USER_LEVEL_ADMIN.getCode())) {
            Long total = groupMapper.getGroupTotal();
            List<Group> list = groupMapper.getGroupList(limit, offset);
            List<Group> listWithName = new ArrayList<>();
            for (Group item : list) {
                String name = userService.getUserNameById(item.getUserId());
                item.setUserName(name);
                listWithName.add(item);
            }
            ListDTO<Group> listDTO = new ListDTO<>();
            listDTO.setTotal(total);
            listDTO.setData(listWithName);
            returnValue.setCode(AllEnum.GROUP_LIST_GET_SUCCESS.getCode());
            returnValue.setMsg(AllEnum.GROUP_LIST_GET_SUCCESS.getMsg());
            returnValue.setData(listDTO);
            return returnValue;
        }
        returnValue.setCode(AllEnum.GROUP_LIST_GET_FAIL.getCode());
        returnValue.setMsg(AllEnum.GROUP_LIST_GET_FAIL.getMsg());
        return returnValue;
    }

    /**
     * 管理员拉人入群
     * @param user
     * @return
     */
    @Transactional
    public ReturnValue addMember(User user) {
        /**
         * 1 先判断操作者身份
         * 2 获取群主id，成员phone
         * 3 调用invite函数
         */
        ReturnValue returnValue = new ReturnValue();
        if (userService.checkAuthority(user.getUserId(), AllEnum.USER_LEVEL_ADMIN.getCode())) {
            Group group = groupMapper.getGroupById(user.getGroupId());
            Long ownerId = group.getUserId();
            Long servantPhone = user.getUserPhone();
            returnValue = userService.invite(ownerId, servantPhone);
            if (returnValue.getCode() == AllEnum.INVITE_SUCCESS.getCode()) {
                returnValue.setCode(AllEnum.GROUP_ADMIN_INVITE_SUCCESS.getCode());
                returnValue.setMsg(AllEnum.GROUP_ADMIN_INVITE_SUCCESS.getMsg());
                return returnValue;
            }
            return returnValue;
        }
        returnValue.setCode(AllEnum.INVITE_FAIL_NO_AUTHORITY.getCode());
        returnValue.setMsg(AllEnum.INVITE_FAIL_NO_AUTHORITY.getMsg());
        return returnValue;
    }

    /**
     * 获取部门信息
     * @param groupId
     * @return
     */
    public ReturnValue getGroup(Long groupId) {
        Group group = groupMapper.getGroupById(groupId);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setData(group);
        return returnValue;
    }
}
