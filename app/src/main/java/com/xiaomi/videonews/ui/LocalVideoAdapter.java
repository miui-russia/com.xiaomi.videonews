package com.xiaomi.videonews.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Administrator on 16-9-14.
 */
public class LocalVideoAdapter extends CursorAdapter {
    public LocalVideoAdapter(Context context) {
        super(context, null,true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new LocalVideoItem(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LocalVideoItem item = (LocalVideoItem) view;
        item.bind(cursor);
    }
}
