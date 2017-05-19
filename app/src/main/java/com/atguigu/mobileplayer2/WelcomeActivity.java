package com.atguigu.mobileplayer2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        },2000);
    }

    private void startMainActivity() {
        startActivity(new Intent(this,MainActivity.class));
        //关闭当前页面
        finish();
    }

    //设置触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除消息
        handler.removeCallbacksAndMessages(null);
    }
}
