package com.atguigu.mobileplayer2.pager;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.mobileplayer2.fragment.BaseFragment;


public class LocalAudioPager extends BaseFragment {
    private TextView textView;


    //重写视图
    @Override
    public View initView() {
        Log.e("TAG","LocalAudioPager-initView");
        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","LocalAudioPager-initData");
        textView.setText("本地音乐的内容");
    }
}
