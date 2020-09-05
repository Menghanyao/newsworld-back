package com.news.newsworld.service;

import com.news.newsworld.enumeration.AllEnum;
import com.news.newsworld.mapper.UserMapper;
import com.news.newsworld.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Utilities;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupService groupService;

    @Autowired
    private NoticeService noticeService;


    /** 注册功能
     * @param user
     * @return 是否注册成功，是一个ReturnValue对象
     */
    public Object addUser(User user) {
        /**
         * 注册逻辑：
         * 1 从数据库中根据电话查找用户
         * 2 如果存在，注册失败，返回电话已被注册
         * 3 否则 登录成功
         */
        int flag = userMapper.ifDuplicateRegistry(user.getUserPhone());
        ReturnValue returnValue = new ReturnValue();
        if (flag > 0) {
            returnValue.setCode(AllEnum.REGISTRY_FAIL.getCode());
            returnValue.setMsg(AllEnum.REGISTRY_FAIL.getMsg());
            return returnValue;
        }
        user.setUserLevel(AllEnum.USER_LEVEL_NONE.getCode());
        user.setUserToken(UUID.randomUUID().toString());
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        userMapper.addUser(user);
        user.setUserId(userMapper.getIdByPhone(user.getUserPhone()));
        returnValue.setCode(AllEnum.REGISTRY_SUCCESS.getCode());
        returnValue.setMsg(AllEnum.REGISTRY_SUCCESS.getMsg());
        System.out.println("已成功注册，user = " + user);
        return returnValue;
    }


    /** 登录功能
     * @param user
     * @return 是否登录成功，是一个ReturnValue对象
     */
    public ReturnValue login(User user) {
        /**
         * 登录逻辑：
         * 1 看是不是空账号
         * 2 看是手动登录还是自动登录
         * 3 手动登录校验账密，失败返回账密错误
         * 4 自动登录校验id和token，失败返回登录已过期
         * 5 登录成功的处理，改token和gmtModified
         */
        int flag = userMapper.ifDuplicateRegistry(user.getUserPhone());
        ReturnValue returnValue = new ReturnValue();
        if (flag == 0) {
            returnValue.setCode(AllEnum.LOGIN_FAIL_EMPTY.getCode());
            returnValue.setMsg(AllEnum.LOGIN_FAIL_EMPTY.getMsg());
            return returnValue;
        }
        User dbUser = new User();
        if (user.getUserId() != null) {
            // 存在id，说明是自动登录
            dbUser = userMapper.getUserById(user.getUserId());
            if (!dbUser.getUserToken().equals(user.getUserToken())) {
                returnValue.setCode(AllEnum.LOGIN_FAIL_EXPIRE.getCode());
                returnValue.setMsg(AllEnum.LOGIN_FAIL_EXPIRE.getMsg());
                return returnValue;
            } else {
                BeanUtils.copyProperties(dbUser, user);
            }
        } else{
            // 不存在id，说明是手动登录，需校验密码
            dbUser = userMapper.getUserByPhone(user.getUserPhone());
            if (!dbUser.getUserPassword().equals(user.getUserPassword())) {
                returnValue.setCode(AllEnum.LOGIN_FAIL_ERROR.getCode());
                returnValue.setMsg(AllEnum.LOGIN_FAIL_ERROR.getMsg());
                return returnValue;
            } else {
                BeanUtils.copyProperties(dbUser, user);
            }
        }
        user.setUserToken(UUID.randomUUID().toString());
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateUser(user);
        System.out.println("已登录user = " + user);
        // 扰乱密码
        user.setUserPassword(user.getUserToken());
        returnValue.setCode(AllEnum.LOGIN_SUCCESS.getCode());
        returnValue.setMsg(AllEnum.LOGIN_SUCCESS.getMsg());
        returnValue.setData(user);
        return returnValue;
    }


    /**
     * 给user 设置上groupId
     * @param userId
     * @param groupId
     */
    public void setGroupIdById(Long userId, int userLevel, Long groupId) {
        /**
         * 绑定逻辑：直接绑定
         * 应用场合：
         *  1 当群主创建部门时，给自己绑上
         *  2 群主邀请别人时，给别人绑上
         *  3 管理员给某人拉入某群
         */
        userMapper.setGroupIdById(groupId, userLevel, System.currentTimeMillis(), userId);
        System.out.println("已更新用户的部门信息：userId = " + userId + ", userType = " + userLevel + ", groupId = " + groupId);
    }

    /**
     * 检查用户有没有相应的权限（群主权限，管理员权限）
     * @param userId
     * @param targetLevel
     * @return true or false
     */
    public boolean checkAuthority(Long userId, int targetLevel) {
        /**
         * 检查逻辑，根据userId从数据库中找权限和第二个参数比较
         */
        User user = userMapper.getUserById(userId);
        if (user.getUserLevel() >= targetLevel) {
            return true;
        }
        return false;
    }


    /**
     * 邀请用户加入群组
     * @param ownerId 群主id
     * @param servantPhone 被邀请人的电话
     * @return
     */
    @Transactional
    public ReturnValue invite(Long ownerId, Long servantPhone) {
        /**
         * 邀请逻辑：
         * 1 检查被邀请人是否存在，不存在就返回错误信息
         * 2 检查操作者是否有owner权限，没有就返回错误信息
         * 3 邀请成功，修改被邀请人的groupId和群组的groupScale
         * 4 同时向被邀请者发送通知
         * 5 返回成功信息
         */
        int flag = userMapper.ifDuplicateRegistry(servantPhone);
        ReturnValue returnValue = new ReturnValue();
        if (flag == 0) {
            returnValue.setCode(AllEnum.INVITE_FAIL_EMPTY.getCode());
            returnValue.setMsg(AllEnum.INVITE_FAIL_EMPTY.getMsg());
            return returnValue;
        }
        if (!checkAuthority(ownerId, AllEnum.USER_LEVEL_OWNER.getCode())) {
            returnValue.setCode(AllEnum.INVITE_FAIL_NO_AUTHORITY.getCode());
            returnValue.setMsg(AllEnum.INVITE_FAIL_NO_AUTHORITY.getMsg());
            return returnValue;
        }

        User owner = userMapper.getUserById(ownerId);
        User servant = userMapper.getUserByPhone(servantPhone);
        /**
         * 如果对方已有group
         */
        System.out.println("owner = " + owner);
        System.out.println("servant= " + servant);
        System.out.println(servant.getGroupId() != null);
        if (servant.getGroupId() != null && servant.getGroupId() != 0L) {
            returnValue.setCode(AllEnum.INVITE_FAIL_DUPLICATE.getCode());
            returnValue.setMsg(AllEnum.INVITE_FAIL_DUPLICATE.getMsg());
            return returnValue;
        }

        /**
         * 3 设置groupId和groupScale
         */
        setGroupIdById(servant.getUserId(), AllEnum.USER_LEVEL_COMMON.getCode(), owner.getGroupId());
        groupService.addOnePerson(owner.getGroupId());

        /**
         * 发送通知
         */
        Notice inviteNotice = new Notice();
        inviteNotice.setNoticeType(AllEnum.NOTICE_TYPE_ENTER_GROUP.getCode());
        inviteNotice.setFromId(owner.getUserId());
        inviteNotice.setToId(servant.getUserId());
        Group group = groupService.getGroupById(owner.getGroupId());
        inviteNotice.setContent(AllEnum.NOTICE_ADD_ENTER_GROUP.getMsg() + group.getGroupName());
        noticeService.addNotice(inviteNotice);

        returnValue.setCode(AllEnum.INVITE_SUCCESS.getCode());
        returnValue.setMsg(AllEnum.INVITE_SUCCESS.getMsg());
        return returnValue;
    }

    /**
     * 获取同事列表
     * @param user
     * @return
     */
    public ReturnValue colleagueList(User user) {
        List<User> colleagueList = userMapper.colleagueList(user.getGroupId());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(AllEnum.COLLEAGUE_GET.getCode());
        returnValue.setMsg(AllEnum.COLLEAGUE_GET.getMsg());
        returnValue.setData(colleagueList);
        return returnValue;
    }

    /**
     * 获取作者
     * @param userId
     * @return
     */
    public ReturnValue getUser(Long userId) {
        User user = userMapper.getUserById(userId);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setData(user);
        return returnValue;
    }

    /**
     * 通过userId返回userName
     * @param userId
     * @return
     */
    public String getUserNameById(Long userId) {
        return userMapper.getUserNameById(userId);
    }


    public Object addAdmin(User user) {
        AdminAccess adminAccess = AdminAccess.getInstance();
        ReturnValue returnValue = new ReturnValue();
        if (adminAccess == null) {
            returnValue.setMsg("注册失败，已有管理员");
        } else {
            user.setUserLevel(AllEnum.USER_LEVEL_ADMIN.getCode());
            user.setUserToken(UUID.randomUUID().toString());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.addUser(user);
            user.setUserId(userMapper.getIdByPhone(user.getUserPhone()));
            user.setUserPassword("");
            returnValue.setMsg("注册管理员成功!");
            returnValue.setData(user);
        }
        return returnValue;
    }
}
