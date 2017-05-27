package com.atguigu.mobileplayer2.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.mobileplayer2.IMusicPlayService;
import com.atguigu.mobileplayer2.R;
import com.atguigu.mobileplayer2.domain.Lyric;
import com.atguigu.mobileplayer2.domain.MediaItem;
import com.atguigu.mobileplayer2.service.MusicPlayService;
import com.atguigu.mobileplayer2.utils.LyricsUtils;
import com.atguigu.mobileplayer2.utils.Utils;
import com.atguigu.mobileplayer2.view.BaseVisualizerView;
import com.atguigu.mobileplayer2.view.LyricShowView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout rlTop;
    private ImageView ivIcon;
    private TextView tvAudioname;
    private TextView tvArtist;
    private LinearLayout llBottom;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnPlaymode;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnLyric;
    private IMusicPlayService service;
    private int position;
    private MyReceiver receiver;
    private Utils utils;
    private final  static  int PROGRESS = 0;
    private boolean notification;
    private static final int SHOW_LYRIC = 1;
    private LyricShowView lyric_show_view;

    private BaseVisualizerView visualizerview;
    private Visualizer mVisualizer;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LYRIC:
                    try {
                        int currentPosition = service.getCurrentPosition();

                        //调用歌词显示控件的setNextShowLyric
                        lyric_show_view.setNextShowLyric(currentPosition);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    removeMessages(SHOW_LYRIC);
                    sendEmptyMessage(SHOW_LYRIC);
                    break;
                case PROGRESS :
                    try {
                        int currentPosition = service.getCurrentPosition();
                        seekbarAudio.setProgress(currentPosition);
                        //设置更新时间
                        tvTime.setText(utils.stringForTime(currentPosition)+ "/" + utils.stringForTime(service.getDuration()) );

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    //每秒中更新一次
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
            }
        }
    };
    //连接好服务后回调
    private ServiceConnection conon = new ServiceConnection() {
        //当绑定服务成功后回调
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMusicPlayService.Stub.asInterface(iBinder);
            if(service != null) {
                try {
                    if(notification) {
                        //重新从Service获取数据
                        setViewData(null);
                    }else {
                        service.openAudio(position);

                    }
                    service.openAudio(position);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        //当断开链接时回调
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-25 09:50:02 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_audio_player);
        ivIcon = (ImageView)findViewById(R.id.iv_icon);
        ivIcon.setBackgroundResource(R.drawable.animation_bg);
        AnimationDrawable background = (AnimationDrawable) ivIcon.getBackground();
        background.start();
        rlTop = (RelativeLayout)findViewById( R.id.rl_top );
        ivIcon = (ImageView)findViewById( R.id.iv_icon );
        tvAudioname = (TextView)findViewById( R.id.tv_audioname );
        tvArtist = (TextView)findViewById( R.id.tv_artist );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvTime = (TextView)findViewById( R.id.tv_time );
        seekbarAudio = (SeekBar)findViewById( R.id.seekbar_audio );
        btnPlaymode = (Button)findViewById( R.id.btn_playmode );
        btnPre = (Button)findViewById( R.id.btn_pre );
        btnStartPause = (Button)findViewById( R.id.btn_start_pause );
        btnNext = (Button)findViewById( R.id.btn_next );
        btnLyric = (Button)findViewById( R.id.btn_lyric );
        lyric_show_view = (LyricShowView)findViewById(R.id.lyric_show_view);
        visualizerview = (BaseVisualizerView) findViewById(R.id.visualizerview);

        btnPlaymode.setOnClickListener( this );
        btnPre.setOnClickListener( this );
        btnStartPause.setOnClickListener( this );
        btnNext.setOnClickListener( this );
        btnLyric.setOnClickListener( this );

        //设置监听拖动音乐
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }
    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-25 09:50:02 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnPlaymode ) {
            // Handle clicks for btnPlaymode
            setPlayMode();
        } else if ( v == btnPre ) {
            // Handle clicks for btnPre
            try {
                service.pre();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ( v == btnStartPause ) {
            // Handle clicks for btnStartPause
            try {
                if(service.isPlaying()) {
                    //暂停
                    service.pause();
                    //按钮状态--播放
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                }else {
                    //播放
                    service.start();
                    //按钮状态 -- 暂停
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ( v == btnNext ) {
            // Handle clicks for bt nNext
            try {
                service.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ( v == btnLyric ) {
            // Handle clicks for btnLyric
        }
    }

    private void setPlayMode() {
        try {
            int playmode = service.getPlaymode();
            if(playmode == MusicPlayService.REPEAT_NORMAL) {
                playmode = MusicPlayService.REPEAT_SINGLE;
            }else if(playmode == MusicPlayService.REPEAT_SINGLE) {
                playmode = MusicPlayService.REPEAT_ALL;
            }else if(playmode == MusicPlayService.REPEAT_ALL) {
                playmode = MusicPlayService.REPEAT_NORMAL;
            }
            //保存到服务里
            service.setPlaymode(playmode);
            setButtonImage();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setButtonImage() {
        try {
            int playmode = service.getPlaymode();
            if(playmode == MusicPlayService.REPEAT_NORMAL) {
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_normal_selector);
            }else if(playmode == MusicPlayService.REPEAT_SINGLE) {
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_single_selector);
            }else if(playmode == MusicPlayService.REPEAT_ALL) {
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_all_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();
        startAndBindService();
    }

    private void initData() {

        //注册广播
//        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayService.OPEN_COMPLETE);
//        registerReceiver(receiver,intentFilter);
        utils = new Utils();
        //注册-->使用EventBus
        EventBus.getDefault().register(this);
    }
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            setViewData(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setViewData(MediaItem mediaItem) {

        try {
            tvArtist.setText(service.getArtistName());
            tvAudioname.setText(service.getAudioName());
            setButtonImage();
            int duration = service.getDuration();
            seekbarAudio.setMax(duration);
            //解析歌词
            //1.得到歌词所在路径
            String audioPath = service.getAudioPath();//mnt/sdcard/audio/beijingbeijing.mp3

            String lyricPath = audioPath.substring(0,audioPath.lastIndexOf("."));//mnt/sdcard/audio/beijingbeijing
            File file = new File(lyricPath+".lrc");
            if(!file.exists()){
                file = new File(lyricPath+".txt");
            }
            LyricsUtils lyricsUtils = new LyricsUtils();
            lyricsUtils.readFile(file);

            //2.传入解析歌词的工具类
            ArrayList<Lyric> lyrics = lyricsUtils.getLyrics();
            lyric_show_view.setLyrics(lyrics);

            //3.如果有歌词，就歌词同步

            if(lyricsUtils.isLyric()){
                handler.sendEmptyMessage(SHOW_LYRIC);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(PROGRESS);
//        handler.sendEmptyMessage(SHOW_LYRIC);
        //显示音乐频谱
        setupVisualizerFxAndUi();
    }
    /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi() {

        int audioSessionid = 0;
        try {
            audioSessionid = service.getAudioSessionId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("audioSessionid==" + audioSessionid);
        mVisualizer = new Visualizer(audioSessionid);
        // 参数内必须是2的位数
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // 设置允许波形表示，并且捕获它
        visualizerview.setVisualizer(mVisualizer);
        mVisualizer.setEnabled(true);
    }

    private void getData() {
        notification = getIntent().getBooleanExtra("notification",false);
        if(!notification) {
            position = getIntent().getIntExtra("position",0);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            mVisualizer.release();
        }
    }

    @Override
    protected void onDestroy() {

//        if(conon != null) {
//            unbindService(conon);
//            conon = null;
//        }
//        if(receiver != null) {
//            unregisterReceiver(receiver);
//            receiver = null;
//        }
        //使用EventBus解注册
        EventBus.getDefault().unregister(this);
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    //启动服务
    private void startAndBindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
//        intent.setAction("com.atguigu.mobileplayer2.service.MUSICPLAYSERVICE");
        bindService(intent,conon, Context.BIND_AUTO_CREATE);
        //防止多次实例化Service
        startService(intent);
    }
}
