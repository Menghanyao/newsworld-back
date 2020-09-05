package com.news.newsworld.controller;

import com.news.newsworld.enumeration.AllEnum;
import com.news.newsworld.mapper.NoticeMapper;
import com.news.newsworld.model.Notice;
import com.news.newsworld.model.Pagination;
import com.news.newsworld.model.ReturnValue;
import com.news.newsworld.model.User;
import com.news.newsworld.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeService noticeService;


    /**
     * 用户举报违规新闻
     * @param notice
     * @return
     */
    @PostMapping("/addReport")
    public ReturnValue addReport(@RequestBody() Notice notice) {
        notice.setNoticeType(AllEnum.NOTICE_TYPE_REPORT.getCode());
        noticeService.addNotice(notice);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(AllEnum.NOTICE_ADD_REPORT.getCode());
        returnValue.setMsg(AllEnum.NOTICE_ADD_REPORT.getMsg());
        return returnValue;
    }

    /**
     * 管理员处理违规内容，同时向两方生成消息
     * @param srcNotice
     * @return
     */
    @PostMapping("/addProcess")
    public ReturnValue addProcess(@RequestBody() Notice srcNotice) {
        ReturnValue returnValue = noticeService.addProcess(srcNotice);
        return returnValue;
    }

    /**
     * 获取消息列表
     * @param pagination
     * @return
     */
    @PostMapping("/noticeList")
    public ReturnValue noticeList(@RequestBody() Pagination pagination) {
        ReturnValue returnValue = noticeService.noticeList(pagination);
        return returnValue;
    }

    @PostMapping("/noticeUnreadTotal")
    public ReturnValue noticeUnreadTotal(@RequestBody() User user) {
        ReturnValue returnValue = noticeService.noticeUnreadTotal(user);
        return returnValue;
    }

    /**
     * 阅读一条消息
     * @param notice
     * @return
     */
    @PostMapping("/readNotice")
    public void readNotice(@RequestBody() Notice notice) {
        noticeService.readNotice(notice);
    }

}
