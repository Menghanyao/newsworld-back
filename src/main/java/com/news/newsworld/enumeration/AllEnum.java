package com.news.newsworld.enumeration;


public enum AllEnum {


    USER_LEVEL_NONE(1,"无部门"),
    USER_LEVEL_COMMON(2,"普通用户"),
    USER_LEVEL_OWNER(3,"群主"),
    USER_LEVEL_ADMIN(4,"管理员"),

    GROUP_STATE_NORMAL(10,"部门状态正常"),
    GROUP_STATE_DROP(11,"部门已解散"),


    NEWS_RANGE_ALL(20,"新闻范围为全部可见"),
    NEWS_RANGE_GROUP(21,"新闻范围为仅部门可见"),

    NEWS_RANGE_MY(22,"新闻范围为我发布的"),
    NEWS_RANGE_HOT(23,"新闻范围为最热门"),
    NEWS_RANGE_OTHER_PUBLIC(24,"新闻范围为他人发布的，公开的"),

    NEWS_SEARCH_NONE(25,"抱歉，未找到相关新闻"),
    NEWS_SEARCH(26,"已根据关键词返回新闻列表"),

    NOTICE_TYPE_REPORT(30,"消息类型为用户举报"),
    NOTICE_TYPE_PROCESS(31,"消息为对举报的处理"),
    NOTICE_TYPE_ENTER_GROUP(32,"消息为拉群通知"),
    NOTICE_TYPE_LIKE_COMMENT(33,"消息为正常的点赞、评论"),
    NOTICE_TYPE_CANCEL_COMMENT(34,"消息为删除评论"),

    COMMENT_TYPE_FIRST_LIKE(40,"一级点赞"),
    COMMENT_TYPE_SECOND_LIKE(41,"二级点赞"),
    COMMENT_TYPE_FIRST_COMMENT(42,"一级评论"),
    COMMENT_TYPE_SECOND_COMMENT(43,"二级评论"),

    REGISTRY_SUCCESS(100,"注册成功"),
    REGISTRY_FAIL(101, "注册失败，该用户已存在"),

    LOGIN_SUCCESS(200,"登录成功"),
    LOGIN_FAIL_EMPTY(201,"登录失败，账号不存在"),
    LOGIN_FAIL_ERROR(202,"登录失败，电话或密码错误"),
    LOGIN_FAIL_EXPIRE(203,"登录失败，登录状态已失效，请输入电话和密码进行登录"),


    GROUP_ADD_SUCCESS(300,"部门创建成功"),
    INVITE_SUCCESS(310,"邀请成功，对方已进入您的部门"),
    INVITE_FAIL_EMPTY(311,"邀请失败，您输入的号码不是注册用户"),
    INVITE_FAIL_NO_AUTHORITY(312,"邀请失败，您没有邀请权限"),
    INVITE_FAIL_DUPLICATE(313,"邀请失败，对方已加入部门，请先让对方退出既有部门"),
    GROUP_ADMIN_INVITE_SUCCESS(314,"已将目标用户加入目标部门"),
    EXIT_SUCCESS(320,"退出成功，您已不属于任何部门"),
    EXIT_FAIL_HAVE_MEMBER(321,"退出失败，请先确保部门内其他人全部退出，再尝试解散该群"),
    COLLEAGUE_GET(330,"已获取到同事列表"),
    GROUP_LIST_GET_SUCCESS(340,"已获取到部门列表"),
    GROUP_LIST_GET_FAIL(341,"获取部门列表失败，您没有权限"),

    NEWS_ADD_SUCCESS(400,"新闻发布成功"),
    FILE_UPLOAD_SUCCESS(401,"图片上传成功"),

    NOTICE_ADD_REPORT(500,"我们已经收到您的举报"),
    NOTICE_ADD_FEEDBACK(501,"根据您的举报，已将相关内容删除"),
    NOTICE_ADD_PROCESS(502,"您发布的内容存在违规，已进行删除处理:"),
    NOTICE_ADD_ENTER_GROUP(503,"您已加入部门："),

    NOTICE_PROCESS_SUCESS(504,"已成功处理"),
    NOTICE_PROCESS_FAIL_DELETE(505,"处理失败，该新闻已被删除"),

    NOTICE_LIST_REPORT(506,"已返回未被处理的举报信息"),
    NOTICE_LIST_MY(507,"已返回您的消息列表"),
    NOTICE_TOTAL_NEW(508,"已返回未读消息数量"),




    ;
    public int code;
    public String msg;


    AllEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
