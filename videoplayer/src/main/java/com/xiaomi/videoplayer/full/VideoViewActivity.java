package com.xiaomi.videoplayer.full;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.xiaomi.videoplayer.R;

import java.util.Locale;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Administrator on 16-9-13.
 */
public class VideoViewActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageView ivLoading;
    private TextView tvBufferInfo;

    private MediaPlayer mediaPlayer;
    private int downLoadSpeed;
    private int bufferPrecent;

    private final static String VIDEOPATH = "video_path";
    public static void open(Context context,String videoPath){
        Intent intent = new Intent(context,VideoViewActivity.class);
        intent.putExtra(VIDEOPATH,videoPath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置窗口的背景颜色
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        setContentView(R.layout.activity_video_view);

        initBufferViews();  // 缓冲相关控件的初始化
        initMediaController();// 视频控制器的初始化
        initVideoView(); // VideoView的初始化
    }

    private void initVideoView() {
        Vitamio.isInitialized(this);
        videoView = (VideoView) findViewById(R.id.videoView);
        //使用系统自带的MediaController
      //  videoView.setMediaController(new MediaController(this));
        videoView.setMediaController(new CursouMediaController(this));
        videoView.setKeepScreenOn(true);
        videoView.requestFocus();
        //资源准备的监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                mediaPlayer.setBufferSize(512 * 1024);
            }
        });
        //缓冲更新的监听(得到缓冲percent)
        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                bufferPrecent = percent;
                updateBufferView();
            }
        });

        //播放信息的监听
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        showBufferView();
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }
                        return true;
                    //结束缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        hideBufferView();
                        videoView.start();
                        return true;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        downLoadSpeed = extra;
                        updateBufferView();
                        return true;
                }
                return false;
            }
        });
    }
    //隐藏缓冲视图
    private void hideBufferView() {
        tvBufferInfo.setVisibility(View.INVISIBLE);
        ivLoading.setVisibility(View.INVISIBLE);
        downLoadSpeed = 0;
        bufferPrecent = 0;
    }
    //显示缓冲视图
    private void showBufferView() {
        tvBufferInfo.setVisibility(View.VISIBLE);
        ivLoading.setVisibility(View.VISIBLE);
    }

    //更新缓冲视图UI
    private void updateBufferView() {
        String info = String.format(Locale.CHINA,"%d%%dkb/s",bufferPrecent,downLoadSpeed);
        tvBufferInfo.setText(info);
    }

    private void initMediaController() {

    }

    private void initBufferViews() {
        ivLoading = (ImageView) findViewById(R.id.ivLoading);
        tvBufferInfo = (TextView) findViewById(R.id.tvBufferInfo);
        tvBufferInfo.setVisibility(View.INVISIBLE);
        ivLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.setVideoPath(getIntent().getStringExtra(VIDEOPATH));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //自身所带的停止
        videoView.stopPlayback();
    }
}
