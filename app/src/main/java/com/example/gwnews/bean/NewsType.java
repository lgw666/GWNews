package com.example.gwnews.bean;
// 新闻类型类
public class NewsType {
    //新闻种类
    private String mType;
    // 新闻Json数据地址
    private String jsonUrl;

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }


    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
