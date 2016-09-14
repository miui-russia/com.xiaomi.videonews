package com.xiaomi.videonews.ui;



import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.xiaomi.videonews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 16-9-14.
 */
public class LocalVideoFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks <Cursor>{

    private Unbinder unbinder;
    private LocalVideoAdapter adapter;
    @BindView(R.id.gridView)
    GridView gridView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LocalVideoAdapter(getContext());
        //初始当前页面的loader，加载器，去loader视频加载器
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_video,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder =  ButterKnife.bind(this,view);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //加载视频的开始
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                MediaStore.Video.Media._ID,  //视频的ID
                MediaStore.Video.Media.DATA,  //视频的路径
                MediaStore.Video.Media.DISPLAY_NAME //视频的名称
        };
        return new CursorLoader(
                getContext(),
                //获取本地视频的uri
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            adapter.swapCursor(null);
    }
    //加载视频的结束
}
