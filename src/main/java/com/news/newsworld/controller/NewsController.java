package com.news.newsworld.controller;

import com.news.newsworld.enumeration.AllEnum;
import com.news.newsworld.model.News;
import com.news.newsworld.model.Pagination;
import com.news.newsworld.model.ReturnValue;
import com.news.newsworld.provider.UFileProvider;
import com.news.newsworld.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
@ResponseBody
@CrossOrigin
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 发布新闻
     * @param news
     * @return
     */
    @PostMapping("/addNews")
    public ReturnValue addNews(@RequestBody() News news) {
        return newsService.addNews(news);
    }

    /**
     * 获取新闻
     * @param newsId
     * @return
     */
    @PostMapping("/getNews")
    public ReturnValue getNews(@RequestBody() Long newsId) {
        return newsService.getNews(newsId);
    }

    /**
     * 阅读了news
     * @param news
     * @return
     */
    @PostMapping("/readNews")
    public void readNews(@RequestBody() News news) {
        newsService.readNews(news.getUserId(), news.getNewsId());
    }

    /**
     * 获取新闻列表
     * @param pagination
     * @return
     */
    @PostMapping("/newsList")
    public ReturnValue newsList(@RequestBody() Pagination pagination) {
        ReturnValue  returnValue =  newsService.newsList(pagination);
        return returnValue;
    }

    /**
     * 搜索新闻
     * @param pagination
     * @return
     */
    @PostMapping("/searchList")
    public ReturnValue searchList(@RequestBody() Pagination pagination) {
        ReturnValue returnValue =  newsService.searchList(pagination);
        return returnValue;
    }

    /**
     * imgUpload
     * @param file
     * @return
     */

    @Autowired
    private UFileProvider uFileProvider;

    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
    public ReturnValue imgUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String url = uFileProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(AllEnum.FILE_UPLOAD_SUCCESS.getCode());
        returnValue.setMsg(AllEnum.FILE_UPLOAD_SUCCESS.getMsg());
        returnValue.setData(url);
        return returnValue;
    }

}
