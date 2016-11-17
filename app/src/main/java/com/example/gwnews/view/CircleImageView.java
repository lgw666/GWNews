package com.example.gwnews.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

// 圆形ImageView
public class CircleImageView extends ImageView {
    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获得当前View的Drawable对象
        Drawable drawable = getDrawable();
        // 判断此Drawable对象是否有效
        if (getWidth() == 0 || getHeight() == 0) {

        } else if (drawable == null) {

        }

        // 获得这个Drawable对象的绘制位图(可能为空)
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        /* 基于这个位图的尺寸来创建一个新的位图，将新的位图的配置设定为指定的样式，然后复制这个位图的元素
        *  变成新的位图，如果分配失败，则返回null
        *
        *  config：产生位图所需的配置
        *  true：得到的位图是可变的
        */
        if (b != null) {
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            //获得View 的宽高
            int w = getWidth(), h = getHeight();
          Bitmap roundBitmap =  getCroppedBitmap(bitmap,w);
            canvas.drawBitmap(roundBitmap,0,0,null);;
        }
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap sbmp;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            sbmp = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            sbmp = bitmap;
        }
        Bitmap outPut = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outPut);

        final int color = 0xffa1974;
        // 创建画笔
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        // 设置抗锯齿
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);
        // 设置模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return outPut;
    }
}
