package com.news.newsworld.service;

import com.news.newsworld.enumeration.AllEnum;
import com.news.newsworld.mapper.NewsMapper;
import com.news.newsworld.mapper.UserMapper;
import com.news.newsworld.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 发布新闻
     * @param news
     * @return
     */
    public ReturnValue addNews(News news) {
        if (news.getNewsRange().equals(AllEnum.NEWS_RANGE_GROUP)) {
            User user = userMapper.getUserById(news.getUserId());
            news.setGroupId(user.getGroupId());
        }
        news.setReadCount(0L);
        news.setReadHot(0L);
        news.setGmtCreate(System.currentTimeMillis());
        news.setGmtModified(news.getGmtCreate());
        newsMapper.addNews(news);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(AllEnum.NEWS_ADD_SUCCESS.getCode());
        returnValue.setMsg(AllEnum.NEWS_ADD_SUCCESS.getMsg());
        return returnValue;
    }

    /**
     * 阅读了一条新闻，给readCount++
     * @param readerId，读者
     * @param newsId，目标新闻
     * @return
     */
    @Transactional
    public void readNews(Long readerId, Long newsId) {
        newsMapper.readNews(newsId, System.currentTimeMillis());
        System.out.println("已阅读一条新闻");
    }


    /**
     * 返回新闻列表，一共四种：首页，最热，我的，内部
     * @param pagination，含有userId, current, size, type
     * @return
     */
    public ReturnValue newsList(Pagination pagination) {
        Long limit = pagination.getSize();
        Long offset = (pagination.getCurrent() - 1) * limit;
        ListDTO<News> listDTO = new ListDTO<>();
        ReturnValue returnValue = new ReturnValue();
        if (pagination.getType().equals(AllEnum.NEWS_RANGE_ALL.getCode())) {
            Long total = newsMapper.indexNewsListTotal(pagination.getType());
            List<News> list = newsMapper.newsListByRange(pagination.getType(), offset, limit);
            listDTO.setData(list);
            listDTO.setTotal(total);
            returnValue.setCode(AllEnum.NEWS_RANGE_ALL.getCode());
            returnValue.setMsg(AllEnum.NEWS_RANGE_ALL.getMsg());
            returnValue.setData(listDTO);
            return returnValue;
        } else if (pagination.getType().equals(AllEnum.NEWS_RANGE_HOT.getCode())) {
            List<News> list = newsMapper.newsListByHot(AllEnum.NEWS_RANGE_ALL.getCode());
            returnValue.setCode(AllEnum.NEWS_RANGE_HOT.getCode());
            returnValue.setMsg(AllEnum.NEWS_RANGE_HOT.getMsg());
            returnValue.setData(list);
            return returnValue;
        } else if (pagination.getType().equals(AllEnum.NEWS_RANGE_MY.getCode())) {
            Long total = newsMapper.myNewsListTotal(pagination.getUserId());
            List<News> list = newsMapper.newsListByUserId(pagination.getUserId(), offset, limit);
            listDTO.setData(list);
            listDTO.setTotal(total);
            returnValue.setCode(AllEnum.NEWS_RANGE_MY.getCode());
            returnValue.setMsg(AllEnum.NEWS_RANGE_MY.getMsg());
            returnValue.setData(listDTO);
            return returnValue;
        } else if (pagination.getType().equals(AllEnum.NEWS_RANGE_GROUP.getCode())) {
            User user = userMapper.getUserById(pagination.getUserId());
            Long groupId = user.getGroupId();
            Long total = newsMapper.groupNewsListTotal(groupId);
            List<News> list = newsMapper.newsListByGroup(groupId, offset, limit);
            listDTO.setData(list);
            listDTO.setTotal(total);
            returnValue.setCode(AllEnum.NEWS_RANGE_GROUP.getCode());
            returnValue.setMsg(AllEnum.NEWS_RANGE_GROUP.getMsg());
            returnValue.setData(listDTO);
            return returnValue;
        } else if (pagination.getType().equals(AllEnum.NEWS_RANGE_OTHER_PUBLIC.getCode())) {
            int newsRange = AllEnum.NEWS_RANGE_ALL.getCode();
            Long userId = pagination.getUserId();
            Long total = newsMapper.otherPublicNewsListTotal(userId, newsRange);
            List<News> list = newsMapper.otherPublicNewsList(userId, newsRange, offset, limit);
            listDTO.setData(list);
            listDTO.setTotal(total);
            returnValue.setCode(AllEnum.NEWS_RANGE_OTHER_PUBLIC.getCode());
            returnValue.setMsg(AllEnum.NEWS_RANGE_OTHER_PUBLIC.getMsg());
            returnValue.setData(listDTO);
            return returnValue;
        }
        return null;
    }

    /**
     * 获取搜索列表
     * @param pagination
     * @return
     */
    public ReturnValue searchList(Pagination pagination) {
        /**
         * 1 拆分合并关键词
         * 2 从数据库中查找公开的新闻
         */
        Long limit = pagination.getSize();
        Long offset = (pagination.getCurrent() - 1) * limit;
        String[] tags = pagination.getSearch().split(" ");
        String search = Arrays.stream(tags).collect(Collectors.joining("|"));
        Long total = newsMapper.getSearchTotal(search, AllEnum.NEWS_RANGE_ALL.getCode());
        List<News> searchList = newsMapper.getSearchList(search, AllEnum.NEWS_RANGE_ALL.getCode(), limit, offset);
        if (total == 0L || searchList.size() == 0) {
            ReturnValue returnValue = new ReturnValue();
            returnValue.setCode(AllEnum.NEWS_SEARCH_NONE.getCode());
            returnValue.setMsg(AllEnum.NEWS_SEARCH_NONE.getMsg());
            returnValue.setData(searchList);
            return returnValue;
        }
        ReturnValue returnValue = new ReturnValue();
        ListDTO<News> listDTO = new ListDTO<>();
        listDTO.setTotal(total);
        listDTO.setData(searchList);
        returnValue.setCode(AllEnum.NEWS_SEARCH.getCode());
        returnValue.setMsg(AllEnum.NEWS_SEARCH.getMsg());
        returnValue.setData(listDTO);
        return returnValue;
    }

    /**
     * 获取新闻详情
     * @param newsId
     * @return
     */
    public ReturnValue getNews(Long newsId) {
        News news = newsMapper.getNewsById(newsId);
        ReturnValue returnValue = new ReturnValue();
        returnValue.setData(news);
        return returnValue;
    }

    @Scheduled(cron = "0 0,30 * * * ?")
    public void autoUpdateReadHot() {
        newsMapper.autoUpdateReadHot();
        System.out.println("自动执行了重置readHot方法");
    }
}
