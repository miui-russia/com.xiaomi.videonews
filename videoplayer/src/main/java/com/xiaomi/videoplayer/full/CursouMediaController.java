package com.xiaomi.videoplayer.full;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


import com.xiaomi.videoplayer.R;

import io.vov.vitamio.widget.MediaController;

/**
 * Created by Administrator on 16-9-13.
 */
public class CursouMediaController extends MediaController {

    private final int maxVolume;
    private int currentVolume;
    private float currentBrightness;

    //获得音量和亮度所用的manager
    private AudioManager audioManager;
    private Window window;
    public CursouMediaController(Context context) {
        super(context);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        window = ((Activity)context).getWindow();
    }

    @Override
    protected View makeControllerView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_video_controller, this, false);
        initView(view);
        return view;
    }

    private MediaPlayerControl mediaPlayerControl;
    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        mediaPlayerControl = player;
    }

    private void initView(View view) {
        //快进
        ImageButton btnFastForward = (ImageButton) findViewById(R.id.btnFastForward);
        btnFastForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentPosition = mediaPlayerControl.getCurrentPosition();
                currentPosition += 10000;
                if (currentPosition > mediaPlayerControl.getDuration()){
                    currentPosition = mediaPlayerControl.getDuration();
                }
                mediaPlayerControl.seekTo(currentPosition);
            }
        });
        //快退
        ImageButton btnFastRewind = (ImageButton) findViewById(R.id.btnFastRewind);
        btnFastRewind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentPosition = mediaPlayerControl.getCurrentPosition();
                currentPosition -= 10000;
                if (currentPosition <= 0) currentPosition = 0;
                mediaPlayerControl.seekTo(currentPosition);
            }
        });

        //调整(左边是亮度，右边是音量)
       final View adjustView = view.findViewById(R.id.adjustView);

        //触摸事件
        //利用gesture，不需要我们做什么，gesture替我们做这些动作
        final GestureDetector gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                //判断是左边还是右边
                float width = adjustView.getWidth();
                float height = adjustView.getHeight();
                float startX = e1.getX();
                float startY = e1.getY();
                float endX = e2.getX();
                float endY = e2.getY();

                //所占的比重
                float percentage = (startY - endY)/height;
                //左侧是亮度
                if (startX < width / 5){
                    adjustBrightness(percentage);
                }else if (startX > width * 4 / 5){
                    //右侧是音量
                    adjustVolume(percentage);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        adjustView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //处理之前获得当前的音量和亮度
                //只有在按下的时候才获得当前的音量和亮度
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
                    currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    currentBrightness = window.getAttributes().screenBrightness;
                }
                gestureDetector.onTouchEvent(event);
                //在调整的过程中，一直显示
                show();
                return true;
            }
        });
    }

    private void adjustVolume(float percentage) {
        int volume = (int) (currentVolume + (percentage * maxVolume));
        volume = volume > maxVolume ? maxVolume:volume;
        volume = volume < 0 ? 0:volume;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,AudioManager.FLAG_SHOW_UI);
    }

    private void adjustBrightness(float percentage) {
        float brightness = percentage + currentBrightness;
        brightness = brightness > 1.0f ? 1.0f : brightness;
        brightness = brightness < 0 ? 0 : brightness;

        //设置亮度
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = brightness;
        window.setAttributes(layoutParams);
    }
}
