package com.news.newsworld.service;

import com.news.newsworld.enumeration.AllEnum;
import com.news.newsworld.mapper.NewsMapper;
import com.news.newsworld.mapper.NoticeMapper;
import com.news.newsworld.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired UserService userService;

    /**
     * 添加一条信息，包括：举报信息，拉群通知，处理举报
     * @param notice
     * @return
     */
    public void addNotice(Notice notice) {
        notice.setRead(false);
        notice.setGmtCreate(System.currentTimeMillis());
        notice.setGmtModified(notice.getGmtCreate());
        noticeMapper.addNotice(notice);
    }


    /**
     * 处理违规广告
     * @param srcNotice
     */
    public ReturnValue addProcess(Notice srcNotice) {
        /**
         * srcNotice是举报后生成的消息，需要做的事：
         * 1 向违规者发送消息，说你的广告已被删除
         * 2 向举报者发送消息，说你的举报已受理
         * 3 源消息标记成已读
         * 4 删除news_id对应的广告
         */
        News news = newsMapper.getNewsById(srcNotice.getNewsId());
        ReturnValue returnValue = new ReturnValue();
        System.out.println("news = " + news);

        if (news == null) {
            System.out.println("这篇已经被删除了");
            newsMapper.readNews(srcNotice.getNewsId(), System.currentTimeMillis());
            returnValue.setCode(AllEnum.NOTICE_PROCESS_FAIL_DELETE.getCode());
            returnValue.setMsg(AllEnum.NOTICE_PROCESS_FAIL_DELETE.getMsg());
            return returnValue;
        }

        Notice sendToNewsOwner = new Notice();  // 1
        sendToNewsOwner.setNoticeType(AllEnum.NOTICE_TYPE_PROCESS.getCode());
        sendToNewsOwner.setFromId(0L);
        sendToNewsOwner.setToId(news.getUserId());
        sendToNewsOwner.setContent(AllEnum.NOTICE_ADD_PROCESS.getMsg() + news.getNewsTitle());
        addNotice(sendToNewsOwner);

        Notice sendToReporter = new Notice();   // 2
        sendToReporter.setNoticeType(AllEnum.NOTICE_TYPE_PROCESS.getCode());
        sendToReporter.setFromId(0L);
        sendToReporter.setToId(srcNotice.getFromId());
        sendToReporter.setContent(AllEnum.NOTICE_ADD_FEEDBACK.getMsg() +": "+ news.getNewsTitle());
        addNotice(sendToReporter);

        newsMapper.readNotice(srcNotice.getNoticeId(), System.currentTimeMillis()); // 3

        newsMapper.deleteNewsById(srcNotice.getNewsId());    // 4
        returnValue.setCode(AllEnum.NOTICE_PROCESS_SUCESS.getCode());
        returnValue.setMsg(AllEnum.NOTICE_PROCESS_SUCESS.getMsg());
        return returnValue;
    }

    /**
     * 获取消息列表
     * @param pagination
     * @return
     */
    public ReturnValue noticeList(Pagination pagination) {
        /**
         * 1 如果是管理员，就返回未被阅读的举报列表
         * 2 否则返回toId指向当前用户的消息。
         */
        Long limit = pagination.getSize();
        Long offset = (pagination.getCurrent() - 1) * limit;
        ReturnValue returnValue = new ReturnValue();
        ListDTO<Notice> listDTO = new ListDTO<>();
        if (userService.checkAuthority(pagination.getUserId(), AllEnum.USER_LEVEL_ADMIN.getCode())) {
            Long total = newsMapper.getUnreadReportTotal(AllEnum.NOTICE_TYPE_REPORT.getCode());
            List<Notice> dbList = newsMapper.getUnreadReportList(AllEnum.NOTICE_TYPE_REPORT.getCode(), offset, limit);
            List<Notice> list = new ArrayList<>();
            for (Notice item : dbList) {
                News news = newsMapper.getNewsById(item.getNewsId());
                User user = (User) userService.getUser(news.getUserId()).getData();
                item.setUser(user);
                item.setNews(news);
                list.add(item);
            }
            listDTO.setTotal(total);
            listDTO.setData(list);
            returnValue.setCode(AllEnum.NOTICE_LIST_REPORT.getCode());
            returnValue.setMsg(AllEnum.NOTICE_LIST_REPORT.getMsg());
            returnValue.setData(listDTO);
        } else {
            Long total = newsMapper.getMyNoticeTotal(pagination.getUserId());
            List<Notice> list = newsMapper.getMyNoticeList(pagination.getUserId(), offset, limit);
            listDTO.setTotal(total);
            listDTO.setData(list);
            returnValue.setCode(AllEnum.NOTICE_LIST_MY.getCode());
            returnValue.setMsg(AllEnum.NOTICE_LIST_MY.getMsg());
            returnValue.setData(listDTO);
        }
        return returnValue;
    }


    /**
     * 阅读一条消息
     * @param notice
     */
    public void readNotice(Notice notice) {
        newsMapper.readNotice(notice.getNoticeId(), System.currentTimeMillis());
    }


    /**
     * 返回未读消息数量
     * 1 判断身份
     * 2 管理员的未读数
     * 3 用户的未读数
     * @param user
     * @return
     */
    public ReturnValue noticeUnreadTotal(User user) {
        ReturnValue returnValue = new ReturnValue();
        Long total;
        if (userService.checkAuthority(user.getUserId(), AllEnum.USER_LEVEL_ADMIN.getCode())) {
            total = newsMapper.getUnreadReportTotal(AllEnum.NOTICE_TYPE_REPORT.getCode());
        } else {
            total = newsMapper.getMyNoticeUnreadTotal(user.getUserId());
        }
        returnValue.setCode(AllEnum.NOTICE_TOTAL_NEW.getCode());
        returnValue.setMsg(AllEnum.NOTICE_TOTAL_NEW.getMsg());
        returnValue.setData(total);
        return returnValue;
    }
}
