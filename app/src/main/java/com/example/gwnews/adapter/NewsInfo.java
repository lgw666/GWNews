package com.example.gwnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gwnews.R;
import com.example.gwnews.bean.News;
import com.example.gwnews.tool.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

// 新闻的Adapter
public class NewsInfo extends BaseAdapter {
    // 存放新闻的集合
    private List<News> newses;

    // 显示新闻的列表布局
    private LayoutInflater layoutInflater;

    // Activity的Context
    private Context mContext;

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public NewsInfo(Context context, List<News> newses) {
        this.newses = newses;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//是否緩存到內存中
                .cacheOnDisc(true)//是否緩存到sd卡上
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public int getCount() {
        return newses.size();
    }

    @Override
    public Object getItem(int position) {
        return newses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 内部类ViewHolder缓存控件以便复用
    private static class ViewHolder {
        private TextView textViewTitle, textViewDigest;
        private ImageView imageViewShow;
        private Button buttonShare;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.news_info, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.text_view_title);
            viewHolder.textViewDigest = (TextView) convertView.findViewById(R.id.text_view_digest);
            viewHolder.imageViewShow = (ImageView) convertView.findViewById(R.id.image_view_show);
            viewHolder.buttonShare = (Button) convertView.findViewById(R.id.button_share);

            // 视图绑定viewHolder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        News news = newses.get(position);
        // 获取标题、新闻地址、图片地址
        final String title = news.getLtitle();
        final String url = news.getUrl_3w();
        final String imgSrc = news.getImgsrc();

        // 给控件设置相应数据
        viewHolder.textViewTitle.setText(title);
        viewHolder.textViewDigest.setText(news.getDigest());
        // ImageLoader
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgSrc, viewHolder.imageViewShow, options, animateFirstListener);
        viewHolder.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(url, imgSrc);
            }
        });
        return convertView;
    }



    private void showShare(String url, String imgSrc) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setText("我发现了一个好的新闻，快来看看吧!!!" + "\n-->点击查看：" + url);
        oks.setTitleUrl(url);
        oks.setImageUrl(imgSrc);
        //启动分享GUI
        oks.show(mContext);
    }

}
