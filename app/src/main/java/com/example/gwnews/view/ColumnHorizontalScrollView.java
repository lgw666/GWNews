package com.example.gwnews.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

// 新闻种类水平滑动视图
public class ColumnHorizontalScrollView extends HorizontalScrollView {
    //传入整体布局
    private View ll_content;
    //传入更多栏目选择布局
    private View ll_more;
    //传入拖动栏布局
    private View rl_colum;
    //左侧阴影图片
    private ImageView leftImage;
    //右侧阴影图片
    private ImageView rightImage;
    //屏幕宽度
    private int mScreenWidth = 0;
    //父类活动
    private Activity activity;

    public ColumnHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 在拖动时执行
     */
    @Override
    protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        shade_ShowOrHide();
        if (!activity.isFinishing() && ll_content != null && leftImage != null && rightImage != null
                && ll_more != null && rl_colum != null) {
            if (ll_content.getWidth() <= mScreenWidth) {
                //当标题部分不在中央时，隐藏左右两边的渐变
                leftImage.setVisibility(GONE);
                rightImage.setVisibility(GONE);
            }
        } else {
            return;
        }
        //在左侧
        if (paramInt1 == 0) {
            leftImage.setVisibility(GONE);
            rightImage.setVisibility(VISIBLE);
            return;
        }
        //在右侧
        if (ll_content.getWidth() - paramInt1 + ll_more.getWidth() +
                rl_colum.getLeft() == mScreenWidth) {
            leftImage.setVisibility(VISIBLE);
            rightImage.setVisibility(GONE);
            return;
        }
        leftImage.setVisibility(VISIBLE);
        rightImage.setVisibility(VISIBLE);
    }

    /**
     * 传入父类布局中的资源文件
     */
    public void setParam(Activity activity, int mScreenWidth,
                         View paraView1, ImageView paraView2, ImageView paraView3,
                         View paraView4, View paraView5) {
        this.activity = activity;
        this.mScreenWidth = mScreenWidth;
        ll_content = paraView1;
        leftImage = paraView2;
        rightImage = paraView3;
        ll_more = paraView4;
        rl_colum = paraView5;
    }

    /**
     * 判断左右阴影显示隐藏效果
     */
    public void shade_ShowOrHide() {
        if (!activity.isFinishing() && ll_content != null) {
            measure(0, 0);
            //如果整体部分宽度小于屏幕宽度的话，那左右阴影都隐藏
            if (mScreenWidth >= getMeasuredWidth()) {
                leftImage.setVisibility(GONE);
                rightImage.setVisibility(GONE);
            }
        } else {
            return;
        }
        //如果滑动在最左边时，左边隐藏，右边显示
        if (getLeft() == 0) {
            leftImage.setVisibility(GONE);
            rightImage.setVisibility(VISIBLE);
            return;
        }
    }
}
