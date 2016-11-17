package com.example.gwnews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.gwnews.R;
import com.example.gwnews.adapter.NewsFragmentPagerAdapter;
import com.example.gwnews.adapter.StaggeredAdapter;
import com.example.gwnews.bean.NewsType;
import com.example.gwnews.fragment.NewsListFragment;
import com.example.gwnews.tool.AnimateFirstDisplayListener;
import com.example.gwnews.tool.BaseTools;
import com.example.gwnews.tool.NewsJsonUrl;
import com.example.gwnews.tool.SetNewsType;
import com.example.gwnews.view.CircleImageView;
import com.example.gwnews.view.ColumnHorizontalScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

// 主界面Activity
public class MainActivity extends AppCompatActivity {
    //自定义的视图
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private LinearLayout mRadioGroup_content;
    private LinearLayout ll_more_columns;
    private RelativeLayout rl_column;
    private ViewPager mViewPager;
    private DrawerLayout drawerLayoutLeft, drawerLayoutRight;
    private TextView textViewUserName;
    private CircleImageView circleImageViewUserIcon;
    // 登录和注销按钮
    private Button loginWeiBo, logout;
    // 当前选中的栏目
    private int columnSelectIndex;
    // 左阴影部分
    private ImageView shade_left;
    // 右阴影部分
    private ImageView shade_right;
    // 屏幕的宽度
    private int mScreenWidth;
    // item宽度
    private int mItemWidth;
    // 已添加的新闻类型栏目
    private RecyclerView mRecyclerViewAlreadyIn, mRecyclerViewNotYet;
    // StaggeredAdapter
    private StaggeredAdapter mStaggeredAdapterAlreadyIn, mStaggeredAdapterNotYet;

    // 新闻的分类列表
    private ArrayList<NewsType> alreadyInNewsTypes = new ArrayList<>();
    private ArrayList<Fragment> newsListFragments;

    // 新闻种类集合
    private List<String> alreadyInTypes = new ArrayList<>();
    private List<String> notYetTypes = new ArrayList<>();

    // 声明第三方平台引用
    private Platform weiBo;
    // 声明用户名和用户头像地址引用
    private String userName, userIconUrl;
    // Handler消息类型
    private static final int MSG_AUTH_CANCEL = 0;
    private static final int MSG_AUTH_ERROR = 1;
    private static final int MSG_AUTH_COMPLETE = 2;

    //声明Volley请求队列引用
    private RequestQueue rq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取屏幕宽度
        mScreenWidth = BaseTools.getWindowsWidth(this);
        //一个item宽度为屏幕的1/7
        mItemWidth = mScreenWidth / 7;
        rq = Volley.newRequestQueue(this);
        initShareSdk();
        initView();
    }

    // 初始化分享SDK
    private void initShareSdk() {
        ShareSDK.initSDK(this);
        weiBo = ShareSDK.getPlatform(SinaWeibo.NAME);
        // 如果已经授权则先取消授权
        if (weiBo.isAuthValid())
            weiBo.removeAccount(true);
    }

    /*初始化Layout控件*/
    private void initView() {
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_colums);
        rl_column = (RelativeLayout) findViewById(R.id.rl_colum);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);
        drawerLayoutLeft = (DrawerLayout) findViewById(R.id.drawer_layout_left);
        drawerLayoutRight = (DrawerLayout) findViewById(R.id.drawer_layout_right);
        mRecyclerViewAlreadyIn = (RecyclerView) findViewById(R.id.recycle_view_already_in);
        mRecyclerViewNotYet = (RecyclerView) findViewById(R.id.recycle_view_not_yet);
        textViewUserName = (TextView) findViewById(R.id.text_view_user_name);
        circleImageViewUserIcon = (CircleImageView) findViewById(R.id.circle_image_view_user_icon);
        loginWeiBo = (Button) findViewById(R.id.button_login_wei_bo);
        logout = (Button) findViewById(R.id.button_logout);

        mViewPager.setOnPageChangeListener(pageListener);
        // 初始界面
        setActionBarDrawerToggle();
        initColumnData();
        setNotYetTypes();
        setChangeView();
        setAlreadyInNews();
    }

    //ViewPager切换监听方法
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int i) {
            //设置选中的View
            mViewPager.setCurrentItem(i);
            selectTab(i);
        }
    };

    // 侧滑切换按钮
    private void setActionBarDrawerToggle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutLeft, toolbar, 0, 0);
        actionBarDrawerToggle.syncState();
        drawerLayoutLeft.setDrawerListener(actionBarDrawerToggle);
        drawerLayoutLeft.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.setClickable(true);
            }

        });
    }

    // 获取Column栏目数据
    private void initColumnData() {
        alreadyInNewsTypes = SetNewsType.getAlreadyInNewsType();
    }

    // 初始化未添加的新闻类型
    private void setNotYetTypes() {
        notYetTypes.add("体育");
        notYetTypes.add("汽车");
        notYetTypes.add("笑话");
        notYetTypes.add("时尚");
        notYetTypes.add("情感");
        notYetTypes.add("电台");
        notYetTypes.add("数码");
        notYetTypes.add("教育");
        notYetTypes.add("旅游");
        notYetTypes.add("游戏");
    }

    //当栏目发生变化时调用
    private void setChangeView() {
        initTabColumn();
        initFragment();
    }

    // 初始化栏目项
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = alreadyInNewsTypes.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content,
                shade_left, shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            //创建TextView对象，并对其进行属性设置
            TextView localTextView = new TextView(this);
            localTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);

            localTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setPadding(5, 0, 5, 0);
            localTextView.setId(i);

            // 获取新闻种类
            NewsType newsType = alreadyInNewsTypes.get(i);
            String type = newsType.getType();
            // 将新闻种类显示到标题栏
            localTextView.setText(type);

            localTextView.setTextColor(getResources().getColorStateList(
                    R.color.top_category_scroll_text_color_day
            ));

            //给TextView设置选中状态
            if (columnSelectIndex == i) {
                localTextView.setSelected(true);
            }

            //添加监听
            localTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v) {
                            localView.setSelected(false);
                        } else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            mRadioGroup_content.addView(localTextView, i, params);
        }
    }

    // 选择的Column里面的Tab
    private void selectTab(int tab_position) {
        columnSelectIndex = tab_position;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            //拿到被选中的视图
            View checkView = mRadioGroup_content.getChildAt(tab_position);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            //滚动View
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        //判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean isCheck;
            isCheck = j == tab_position;
            checkView.setSelected(isCheck);
        }
    }

    // 初始化Fragment
    private void initFragment() {
        if (newsListFragments == null) {
            newsListFragments = new ArrayList<>();
        } else {
            newsListFragments.clear();
        }
        for (NewsType newsType : alreadyInNewsTypes) {
            //创建Fragment对象
            NewsListFragment newsListFragment = new NewsListFragment();
            newsListFragment.setNewsType(newsType.getType());
            newsListFragment.setNewsUrl(newsType.getJsonUrl());
            newsListFragments.add(newsListFragment);
        }
        //创建Fragment适配器
        NewsFragmentPagerAdapter mAdapter = new NewsFragmentPagerAdapter(
                getSupportFragmentManager(), newsListFragments
        );
        mViewPager.setAdapter(mAdapter);
    }

    // 将集合里的新闻类型显示在已添加新闻类型栏目里
    private void setAlreadyInNews() {
        for (NewsType newsType : alreadyInNewsTypes) {
            alreadyInTypes.add(newsType.getType());
        }
        // 创建已添加新闻和待添加新闻的adapter
        mStaggeredAdapterAlreadyIn = new StaggeredAdapter(this, alreadyInTypes);
        mStaggeredAdapterNotYet = new StaggeredAdapter(this, notYetTypes);

        // 给已添加新闻和待添加新闻页面设置布局和动画
        mRecyclerViewAlreadyIn.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerViewAlreadyIn.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewNotYet.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerViewNotYet.setItemAnimator(new DefaultItemAnimator());

        // 给已添加新闻和待添加新闻页面设置Adapter
        mRecyclerViewAlreadyIn.setAdapter(mStaggeredAdapterAlreadyIn);
        mRecyclerViewNotYet.setAdapter(mStaggeredAdapterNotYet);

        // 已选择新闻种类的监听
        mStaggeredAdapterAlreadyIn.setOnItemClickLitener(new StaggeredAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mStaggeredAdapterNotYet.addData(alreadyInTypes.get(position));
                mStaggeredAdapterAlreadyIn.removeData(position);
                alreadyInNewsTypes.remove(position);
                setChangeView();
            }
        });

        // 待选择新闻种类的监听
        mStaggeredAdapterNotYet.setOnItemClickLitener(new StaggeredAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsType newsType = new NewsType();
                setNewsType(notYetTypes.get(position), newsType);
                alreadyInNewsTypes.add(newsType);
                mStaggeredAdapterAlreadyIn.addData(notYetTypes.get(position));
                mStaggeredAdapterNotYet.removeData(position);
                setChangeView();
            }
        });
    }

    // 给相应的NewsType设置种类和JsonUrl
    private void setNewsType(String type, NewsType newsType) {
        switch (type) {
            case "头条":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.TopId));
                newsType.setType("头条");
                break;
            case "娱乐":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.YuLeId));
                newsType.setType("娱乐");
                break;
            case "财经":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.CaiJingId));
                newsType.setType("财经");
                break;
            case "科技":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.KeJiId));
                newsType.setType("科技");
                break;
            case "电影":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.DianYingId));
                newsType.setType("电影");
                break;
            case "家居":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.JiaJuId));
                newsType.setType("家居");
                break;
            case "军事":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.JunShiId));
                newsType.setType("军事");
                break;
            case "社会":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.SheHuiId));
                newsType.setType("社会");
                break;
            case "体育":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.TiYuId));
                newsType.setType("体育");
                break;
            case "汽车":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.QiChiId));
                newsType.setType("汽车");
                break;
            case "笑话":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.XiaoHuaId));
                newsType.setType("笑话");
                break;
            case "时尚":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.ShiShangId));
                newsType.setType("时尚");
                break;
            case "情感":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.QingGanId));
                newsType.setType("情感");
                break;
            case "电台":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.DianTaiId));
                newsType.setType("电台");
                break;
            case "数码":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.ShuMaId));
                newsType.setType("数码");
                break;
            case "教育":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.JiaoYuId));
                newsType.setType("教育");
                break;
            case "旅游":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.LvYouId));
                newsType.setType("旅游");
                break;
            case "游戏":
                newsType.setJsonUrl(getJsonUrl(NewsJsonUrl.YouXiId));
                newsType.setType("游戏");
                break;
        }
    }

    // 获取JSONUrl
    private static String getJsonUrl(String newsId) {
        return NewsJsonUrl.CommonUrl + newsId + "/0" + NewsJsonUrl.endUrl;
    }


    // 加载更多频道按钮点击事件
    private boolean isClosed = true; // 判断右侧滑是否打开

    public void more(View v) {
        if (isClosed) {
            drawerLayoutRight.openDrawer(GravityCompat.END);   //打开右侧滑栏
            isClosed = false;
        } else {
            drawerLayoutRight.closeDrawer(GravityCompat.END);   //关闭右侧栏
            isClosed = true;
        }
    }

    // 登录按钮点击事件
    public void loginWeiBo(View view) {
        weiBo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                // 获取用户名和用户头像地址
                userName = platform.getDb().getUserName();
                userIconUrl = platform.getDb().getUserIcon();
                handler.sendEmptyMessage(MSG_AUTH_COMPLETE);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                handler.sendEmptyMessage(MSG_AUTH_ERROR);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                handler.sendEmptyMessage(MSG_AUTH_CANCEL);
            }
        });
        weiBo.SSOSetting(false);
        weiBo.authorize();
    }

    // 注销按钮点击事件
    public void logout(View view) {
        if (weiBo.isAuthValid()) {
            textViewUserName.setText("");
            textViewUserName.setVisibility(View.GONE);
            weiBo.removeAccount(true);
            // 显示登录按钮，隐藏注销按钮
            loginWeiBo.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
            // 将头像还原
            circleImageViewUserIcon.setImageResource(R.drawable.logout);
            Toast.makeText(this, "取消授权成功", Toast.LENGTH_SHORT).show();
        }
    }


    // 登陆后使用Handler更新用户头像和用户名，并更新UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTH_COMPLETE:
                    getUserIcon();
                    // 显示用户名
                    textViewUserName.setText(userName);
                    textViewUserName.setVisibility(View.VISIBLE);
                    // 隐藏登录按钮，显示注销按钮
                    loginWeiBo.setVisibility(View.GONE);
                    logout.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_LONG).show();
                    break;
                case MSG_AUTH_ERROR:
                    Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_LONG).show();
                    break;
                case MSG_AUTH_CANCEL:
                    Toast.makeText(MainActivity.this, "授权已经被取消", Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    // 获取头像
    private void getUserIcon() {
        ImageRequest imageRequest = new ImageRequest(userIconUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        circleImageViewUserIcon.setImageBitmap(bitmap);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.toString());
            }
        }
        );
        rq.add(imageRequest);
    }

    // 程序关闭时清理缓存
    @Override
    protected void onDestroy() {
        AnimateFirstDisplayListener.displayedImages.clear();
        super.onDestroy();
    }

    //使用协议点击事件
    public void usePermission(View view) {
        startActivity(new Intent(this, UsePermissionActivity.class));
    }

    //声明点击事件
    public void declare(View view) {
        startActivity(new Intent(this, Declare.class));
    }

}
