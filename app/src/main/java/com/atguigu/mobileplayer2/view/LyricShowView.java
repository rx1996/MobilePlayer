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
    private Paint paintGreen;
    private int width;
    private int height;
    private ArrayList<Lyric> lyrics;
    private int index = 0;
    private Paint paintWhite;
    //歌词行间距
    private float textHeight = 80;
    private int currentPosition;

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
        paintGreen = new Paint();
        //设置颜色
        paintGreen.setColor(Color.GREEN);
        //设置抗锯齿
        paintGreen.setAntiAlias(true);
        paintGreen.setTextSize(60);
        //居中
        paintGreen.setTextAlign(Paint.Align.CENTER);


        paintWhite = new Paint();
        //设置颜色
        paintWhite.setColor(Color.WHITE);
        //设置抗锯齿
        paintWhite.setAntiAlias(true);
        paintWhite.setTextSize(60);
        //居中
        paintWhite.setTextAlign(Paint.Align.CENTER);

//        lyrics = new ArrayList<>();
//        Lyric lyric = new Lyric();
//        for (int i = 0; i < 10000; i++) {
//            //不同歌词
//            lyric.setContent("aaaaaaaaaaaa_" + i);
//            lyric.setSleepTime(2000);
//            lyric.setTimePoint(2000*i);
//            //添加到集合
//            lyrics.add(lyric);
//            //重新创建新对象
//            lyric = new Lyric();
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lyrics != null && lyrics.size() > 0) {
            //当前歌词
            String currentContent = lyrics.get(index).getContent();
            canvas.drawText(currentContent,width/2,height/2,paintGreen);
            //得到中间句的坐标
            float tempY = height / 2;
            //绘制前面部分
            for(int i = index - 1; i >= 0; i--) {
                //得到前一部分所有的歌词内容
                String preContent = lyrics.get(i).getContent();
                tempY = tempY - textHeight;
                if(tempY < 0) {
                    break;
                }
                canvas.drawText(preContent,width/2,tempY,paintWhite);

            }
            tempY = height / 2;
            //绘制后面部分
            for (int i = index + 1; i < lyrics.size(); i++) {
                //得到后一部分所有的歌词内容
                String nextContent = lyrics.get(i).getContent();

                tempY = tempY + textHeight;
                if (tempY > height) {
                    break;
                }

                //绘制内容
                canvas.drawText(nextContent, width / 2, tempY, paintWhite);
            }


        } else {
            canvas.drawText("没有找到歌词...", width / 2, height / 2, paintGreen);
        }
    }

    public void setNextShowLyric(int currentPosition) {
        this.currentPosition = currentPosition;
        if (lyrics == null || lyrics.size() == 0)
            return;

        for (int i = 1; i < lyrics.size(); i++) {

            if (currentPosition < lyrics.get(i).getTimePoint()) {
                int tempIndex = i - 1;
                if (currentPosition >= lyrics.get(tempIndex).getTimePoint()) {
                    //中间高亮显示的哪一句
                    index = tempIndex;
                }
            }

        }

        invalidate();
    }
    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;

    }
}
