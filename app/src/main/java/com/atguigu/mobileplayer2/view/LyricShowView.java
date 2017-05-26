package com.atguigu.mobileplayer2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.atguigu.mobileplayer2.domain.Lyric;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/26.
 */

public class LyricShowView extends TextView {
    private Paint paint;
    private int width;
    private int height;
    private ArrayList<Lyric> lyrics;
    public LyricShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {
        paint = new Paint();
        //设置颜色
        paint.setColor(Color.GREEN);
        //设置抗锯齿
        paint.setAntiAlias(true);
        paint.setTextSize(16);
        //居中
        paint.setTextAlign(Paint.Align.CENTER);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("没有找到歌词...",width/2,height/2,paint);
    }
}
