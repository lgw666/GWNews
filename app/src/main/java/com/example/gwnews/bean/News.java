package com.example.gwnews.bean;

import android.graphics.Bitmap;
// 新闻类
public class News {
    // 声明新闻标题，地址，图片
    private String ltitle, imgsrc, url_3w, digest;
    private Bitmap img;

    public void setLtitle(String ltitle) {
        this.ltitle = ltitle;
    }

    public void setUrl_3w(String url_3w) {
        this.url_3w = url_3w;
    }

    void setImg(Bitmap img) {
        this.img = img;
    }

    public String getLtitle() {
        return ltitle;
    }

    public String getUrl_3w() {
        return url_3w;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public String getDigest() {
        return digest;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public Bitmap getImg() {
        return img;
    }

    @Override
    public String toString() {
        return "litle:" + ltitle + ",url_3w" + url_3w + ", imgsrc:" + imgsrc;
    }
}