package com.xiaomi.videonews.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomi.videonews.R;
import com.xiaomi.videoplayer.full.VideoViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.provider.MediaStore;

/**
 * Created by Administrator on 16-9-14.
 */
public class LocalVideoItem extends FrameLayout {
    public LocalVideoItem(Context context) {
        this(context, null);
    }

    public LocalVideoItem(Context context, AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public LocalVideoItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_local_video,this,true);
        ButterKnife.bind(this);
    }

    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.tvVideoName)
    TextView tvVideoName;
    private String filePath;

    //将cursor中的内容绑定在固定控件上面
    public void bind(Cursor cursor) {
        filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        String videoName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
        tvVideoName.setText(videoName);
        ivPreview.setImageBitmap(null);
    }

    //点击之后全屏播放
    @OnClick
    public void click(){
        VideoViewActivity.open(getContext(),filePath);
    }
}
