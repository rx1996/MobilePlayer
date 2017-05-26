package com.atguigu.mobileplayer2.domain;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Lyric {
    //歌词内容
    private String content;
    //设置时间戳
    private long timePoint;
    //高亮持续时间
    private long sleepTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(long timePoint) {
        this.timePoint = timePoint;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "content='" + content + '\'' +
                ", timePoint=" + timePoint +
                ", sleepTime=" + sleepTime +
                '}';
    }
}
