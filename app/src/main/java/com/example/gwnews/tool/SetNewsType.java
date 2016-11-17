package com.example.gwnews.tool;

import com.example.gwnews.bean.NewsType;

import java.util.ArrayList;

// 设置需要显示的新闻种类
public class SetNewsType {
    public static ArrayList<NewsType> getAlreadyInNewsType() {
        ArrayList<NewsType> alreadyInNewsType = new ArrayList<NewsType>();

        NewsType newsType = new NewsType();
        newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.TopId));
        newsType.setType("头条");
        alreadyInNewsType.add(newsType);

        newsType = new NewsType();
        newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.YuLeId));
        newsType.setType("娱乐");
        alreadyInNewsType.add(newsType);

        newsType = new NewsType();
        newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.CaiJingId));
        newsType.setType("财经");
        alreadyInNewsType.add(newsType);

        newsType = new NewsType();
        newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.KeJiId));
        newsType.setType("科技");
        alreadyInNewsType.add(newsType);

        newsType = new NewsType();
        newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.DianYingId));
        newsType.setType("电影");
        alreadyInNewsType.add(newsType);

        newsType = new NewsType();
        newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.JiaJuId));
        newsType.setType("家居");
        alreadyInNewsType.add(newsType);

        newsType = new NewsType();
        newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.JunShiId));
        newsType.setType("军事");
        alreadyInNewsType.add(newsType);

        newsType = new NewsType();
        newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.SheHuiId));
        newsType.setType("社会");
        alreadyInNewsType.add(newsType);

        return alreadyInNewsType;
    }
    
    // 获取JSONUrl
    private static String getJsonUrl(String newsId) {
        return NewsJsonUrl.CommonUrl + newsId + "/0" + NewsJsonUrl.endUrl;
    }
}
