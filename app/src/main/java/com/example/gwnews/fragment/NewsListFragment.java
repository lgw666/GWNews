package com.example.gwnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gwnews.activity.ShowNewsActivity;
import com.example.gwnews.R;
import com.example.gwnews.adapter.NewsInfo;
import com.example.gwnews.bean.News;
import com.example.gwnews.tool.AnimateFirstDisplayListener;
import com.example.gwnews.type.CaiJIngNews;
import com.example.gwnews.type.DianTaiNews;
import com.example.gwnews.type.JiaJuNews;
import com.example.gwnews.type.DianYingNews;
import com.example.gwnews.type.JiaoYuNews;
import com.example.gwnews.type.LvYouNews;
import com.example.gwnews.type.QiCheNews;
import com.example.gwnews.type.QingGanNews;
import com.example.gwnews.type.ShiShangNews;
import com.example.gwnews.type.ShuMaNews;
import com.example.gwnews.type.TiYuNews;
import com.example.gwnews.type.XiaoHuaNews;
import com.example.gwnews.type.JunShiNews;
import com.example.gwnews.type.SheHuiNews;
import com.example.gwnews.type.KeJiNews;
import com.example.gwnews.type.TouTiaoNews;
import com.example.gwnews.type.YouXiNews;
import com.example.gwnews.type.YuLeNews;
import com.google.gson.Gson;

import java.util.Iterator;
import java.util.List;

// 显示新闻列表内容的Fragment
public class NewsListFragment extends Fragment {
    // 显示新闻的ListView
    private ListView listViewNews;
    // 存放新闻数据的集合
    private List<News> newses;
    // 请求我网络的响应队列
    private RequestQueue rq;
    // 新闻的种类
    private String mType;
    // 新闻的Json数据地址
    private String newsUrl;
    // 刷新组件
    private SwipeRefreshLayout refreshLayout;

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public void setNewsType(String type) {
        mType = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rq = Volley.newRequestQueue(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news, null);
        initView(view);
        return view;
    }

    //初始化控件
    private void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.update_comment_digg_text_highlight);
        listViewNews = (ListView) view.findViewById(R.id.list_view_news);
        getNews();
        // 下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews();
            }
        });
    }

    // 获取新闻列表
    private void getNews() {
        // 获取Json地址里的Json数据
        StringRequest sr = new StringRequest(newsUrl
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonData) {
                parse(jsonData);
                listViewNews.setAdapter(new NewsInfo(getActivity(), newses));
                listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 将相应的新闻页面网址发送到显示页面，并跳转
                        Intent intent = new Intent(getActivity(), ShowNewsActivity.class);
                        intent.putExtra("url", newses.get(position).getUrl_3w());
                        startActivity(intent);
                    }
                });
                refreshLayout.setRefreshing(false);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.toString());
            }

        });

        rq.add(sr);
    }


    // 判断新闻是否为空
    private void judge() {
        Iterator<News> iterator = newses.iterator();
        while (iterator.hasNext()) {
            News news = iterator.next();
            if (null == news.getUrl_3w())
                iterator.remove();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        AnimateFirstDisplayListener.displayedImages.clear();
    }

    // 解析新闻数据
    private void parse(String jsonData) {
        Gson gson = new Gson();
        switch (mType) {
            case "头条":
                TouTiaoNews touTiaoNews = gson.fromJson(jsonData, TouTiaoNews.class);
                newses = touTiaoNews.getT1348647909107();
                judge();
                break;
            case "娱乐":
                YuLeNews yuLeNews = gson.fromJson(jsonData, YuLeNews.class);
                newses = yuLeNews.getT1348648517839();
                judge();
                break;
            case "财经":
                CaiJIngNews caiJIngNews = gson.fromJson(jsonData, CaiJIngNews.class);
                newses = caiJIngNews.getT1348648756099();
                judge();
                break;
            case "科技":
                KeJiNews keJiNews = gson.fromJson(jsonData, KeJiNews.class);
                newses = keJiNews.getT1348649580692();
                judge();
                break;
            case "电影":
                DianYingNews dianYingNews = gson.fromJson(jsonData, DianYingNews.class);
                newses = dianYingNews.getT1348648650048();
                judge();
                break;
            case "家居":
                JiaJuNews jiaJuNews = gson.fromJson(jsonData, JiaJuNews.class);
                newses = jiaJuNews.getT1348654105308();
                judge();
                break;
            case "军事":
                JunShiNews junShiNews = gson.fromJson(jsonData, JunShiNews.class);
                newses = junShiNews.getT1348648141035();
                judge();
                break;
            case "社会":
                SheHuiNews sheHuiNews = gson.fromJson(jsonData, SheHuiNews.class);
                newses = sheHuiNews.getT1348648037603();
                judge();
                break;
            case "体育":
                TiYuNews tiYuNews = gson.fromJson(jsonData, TiYuNews.class);
                newses = tiYuNews.getT1348649079062();
                judge();
                break;
            case "汽车":
                QiCheNews qiCheNews = gson.fromJson(jsonData, QiCheNews.class);
                newses = qiCheNews.getT1348654060988();
                judge();
                break;
            case "笑话":
                XiaoHuaNews xiaoHuaNews = gson.fromJson(jsonData, XiaoHuaNews.class);
                newses = xiaoHuaNews.getT1350383429665();
                judge();
                break;
            case "时尚":
                ShiShangNews shiShangNews = gson.fromJson(jsonData, ShiShangNews.class);
                newses = shiShangNews.getT1348650593803();
                judge();
                break;
            case "情感":
                QingGanNews qingGanNews = gson.fromJson(jsonData, QingGanNews.class);
                newses = qingGanNews.getT1348650839000();
                judge();
                break;
            case "电台":
                DianTaiNews dianTaiNews = gson.fromJson(jsonData, DianTaiNews.class);
                newses = dianTaiNews.getT1379038288239();
                judge();
                break;
            case "数码":
                ShuMaNews shuMaNews = gson.fromJson(jsonData, ShuMaNews.class);
                newses = shuMaNews.getT1348649776727();
                judge();
                break;
            case "教育":
                JiaoYuNews jiaoYuNews = gson.fromJson(jsonData, JiaoYuNews.class);
                newses = jiaoYuNews.getT1348654225495();
                judge();
                break;
            case "旅游":
                LvYouNews lvYouNews = gson.fromJson(jsonData, LvYouNews.class);
                newses = lvYouNews.getT1348654204705();
                judge();
                break;
            case "游戏":
                YouXiNews youXiNews = gson.fromJson(jsonData, YouXiNews.class);
                newses = youXiNews.getT1348654151579();
                judge();
                break;
        }
    }


}
