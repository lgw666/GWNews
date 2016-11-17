package com.example.gwnews.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.gwnews.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //自动跳转
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                // 关闭自身
                finish();
                //设置Activity的跳转动画
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }
}
