package com.xiaomi.videoplayer.full;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.xiaomi.videonesws.R;

import io.vov.vitamio.widget.MediaController;

/**
 * Created by Administrator on 16-9-13.
 */
public class CursouMediaController extends MediaController {
    public CursouMediaController(Context context) {
        super(context);
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
    }
}
