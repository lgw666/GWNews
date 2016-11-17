package com.example.gwnews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.gwnews.R;

// 显示新闻内容的Activity
public class ShowNewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        initWebView();
    }

    // 初始化WebView
    private void initWebView() {
        // 创建Web引用
        WebView webViewShowNews = (WebView) findViewById(R.id.web_view_show_news);
        // 启动JavaScript
        WebSettings settings = webViewShowNews.getSettings();
        settings.setJavaScriptEnabled(true);
        // 获取网络访问地址
        String url = getIntent().getStringExtra("url");
        webViewShowNews.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webViewShowNews.setWebViewClient(new WebViewClient());
    }
}
