package com.xiaomi.videoplayer.part;

import android.content.Context;
import android.graphics.PixelFormat;


import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.xiaomi.videoplayer.R;
import com.xiaomi.videoplayer.full.VideoViewActivity;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;


/**
 * Created by Administrator on 16-9-13.
 */
public class SimpleVideoView extends FrameLayout {

    private String videoPath;   //视频播放的URL
    private MediaPlayer mediaPlayer;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private ImageButton btnToggle;
    private ImageView ivPreview;
    private ImageButton btnFullScreen;
    private ProgressBar progressBar;

    private boolean isPresent;
    private boolean isPlaying;
    private static final int PROGRESS_MAX = 1000;



    public SimpleVideoView(Context context) {
        this(context, null);
    }

    public SimpleVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private Handler handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isPlaying) {
                // 每200毫秒更新一次播放进度
                int progress = (int) (mediaPlayer.getCurrentPosition() * PROGRESS_MAX / mediaPlayer.getDuration());
                progressBar.setProgress(progress);
                handler.sendEmptyMessageDelayed(0, 200);
            }
        }
    };

    private void initView() {
        Vitamio.isInitialized(getContext());
        LayoutInflater.from(getContext()).inflate(R.layout.view_simple_video_player,this,true);
        initSurfaceView(); // 初始化SurfaceView
        initControllerViews(); // 初始化视频播放控制视图

    }



    private void initSurfaceView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        //vitamio使用surfaceview的时候，要设置pixelFormat
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
    }

    private void initControllerViews() {
        //预览图
        //多个模块使用ButterKnife就不适合
        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        //播放暂停
        btnToggle = (ImageButton) findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    pauseMediaPlayer();
                } else if (isPresent) {
                    startMediaPlayer();
                } else {
                    Toast.makeText(getContext(), "不能播放", Toast.LENGTH_LONG).show();
                }
            }
        });
        //设置进度条
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(PROGRESS_MAX );
        btnFullScreen = (ImageButton) findViewById(R.id.btnFullScreen);
        //设置全屏
        btnFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16-9-13 点击跳转到全屏
//                Intent intent = new Intent(getContext(), VideoViewActivity.class);
//                getContext().startActivity(intent);
                VideoViewActivity.open(getContext(), videoPath);
            }
        });
    }

    //对外提供三个方法，用来对MediaPlayer进行生命周期及状态控制
    public void onResume() {
        //用来初始状态
        initMediaPlayer(); // 初始化MediaPlayer，设置一系列监听器
        prepareMediaPlayer(); // 准备MediaPlayer，同时更新UI状态
    }

    private void initMediaPlayer() {
        //使用vitamio首先要初始化一下
      //  Vitamio.isInitialized(getContext());
        mediaPlayer = new MediaPlayer(getContext());
        mediaPlayer.setDisplay(surfaceHolder);
        //监听
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPresent = true;
                startMediaPlayer();
            }
        });

        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                //vitamio5.0之后，要对audio进行初始化要不然就不能播放在线视频
                if (what == MediaPlayer.MEDIA_INFO_FILE_OPEN_OK) {
                    mediaPlayer.audioInitedOk(mediaPlayer.audioTrackInit());
                    return true;
                }
                return false;
            }
        });

        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                int layoutWidth = surfaceView.getWidth();
                int layoutHeight = layoutWidth * height /width;

                ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
                layoutParams.width = layoutWidth ;
                layoutParams.height = layoutHeight;
                surfaceView.setLayoutParams(layoutParams);

            }
        });
    }
    //播放
    private void startMediaPlayer() {
        ivPreview.setVisibility(View.INVISIBLE);
        mediaPlayer.start();
        isPlaying = true;
        handler.sendEmptyMessage(0);
        btnToggle.setImageResource(R.drawable.ic_pause);
    }

    private void prepareMediaPlayer() {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.d("simplevideo", "prepare MediaPlayer" + e.getMessage());
        }
    }

    public void onPause() {
        //用来释放状态
        pauseMediaPlayer(); // 暂停播放，同时更新UI状态
        releaseMediaPlayer(); // 释放MediaPlayer，同时更新UI状态
    }

    private void pauseMediaPlayer() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        isPlaying = false;
        btnToggle.setImageResource(R.drawable.ic_play_arrow);
        handler.removeMessages(0);
    }

    private void releaseMediaPlayer() {
        //mediaPlayer的释放
        mediaPlayer.release();
        mediaPlayer = null;
        isPresent = false;
        progressBar.setProgress(0);
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
