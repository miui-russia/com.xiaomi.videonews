package com.xiaomi.videonews.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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
    private String filePath;  //文件的途径

    public String getFilePath() {
        return filePath;
    }

    public void setIvPreview(Bitmap bitmap) {
        ivPreview.setImageBitmap(bitmap);
    }

    public void setIvPreview(final String filePath, final Bitmap bitmap) {
        if (!filePath.equals(this.filePath)) return;
        post(new Runnable() {
            @Override
            public void run() {
                if (!filePath.equals(LocalVideoItem.this.filePath)) return;
                ivPreview.setImageBitmap(bitmap);
            }
        });
    }

    //将cursor中的内容绑定在固定控件上面
    public void bind(Cursor cursor) {
        filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        String videoName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
        tvVideoName.setText(videoName);
        ivPreview.setImageBitmap(null);

        //获取所有视频的预览图，这是一个很耗时的操作,到后台去操作
        //用线程池去处理获取多张预览图
        //如果已经获取图片就缓存起来，LruCache
    }

    //点击之后全屏播放
    @OnClick
    public void click(){
        VideoViewActivity.open(getContext(),filePath);
    }
}
