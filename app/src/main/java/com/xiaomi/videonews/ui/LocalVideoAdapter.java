package com.xiaomi.videonews.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 16-9-14.
 */
public class LocalVideoAdapter extends CursorAdapter {

    //用来加载视频预览图的线程池
    private final ExecutorService mExecutorService = Executors.newFixedThreadPool(3);

    //图片的缓存处理
    private LruCache<String,Bitmap> mLruCache = new LruCache<String,Bitmap>(5 * 1024 * 1024){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };
    public LocalVideoAdapter(Context context) {
        super(context, null,true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new LocalVideoItem(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final LocalVideoItem item = (LocalVideoItem) view;
        item.bind(cursor);

       final String filePath = item.getFilePath();
        Bitmap bitmap = mLruCache.get(filePath);
        if (bitmap != null){
            item.setIvPreview(bitmap);
            return;
        }

        //后台线程获得视频的预览图
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //加载视频的预览图
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                mLruCache.put(filePath,bitmap);
                item.setIvPreview(filePath,bitmap);
            }
        });
    }
    public void release(){
        mExecutorService.shutdown();
    }
}
