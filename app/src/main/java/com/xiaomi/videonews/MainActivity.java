package com.xiaomi.videonews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.xiaomi.videonews.ui.LocalVideoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private Unbinder unbinder;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnLikes)
    Button btnLikes;
    @BindView(R.id.btnLocal)
    Button btnLocal;
    @BindView(R.id.btnNews)
    Button btnNews;

    private final FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            //目前都是本地视频
            switch (position){
                case 0:
                case 1:
                case 2:
                    return new LocalVideoFragment();
                default:
                    throw new RuntimeException("不存在的数据");
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("viewPager", "1");
        setContentView(R.layout.activity_main);
        Log.d("viewPager", "3");

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        unbinder =  ButterKnife.bind(this);
        Log.d("viewPager","2222");
        Log.i("viewPager",viewPager+","+adapter);
        viewPager.setAdapter(adapter);
        //viewpager的监听，主要是为了按钮
        viewPager.addOnPageChangeListener(this);
        //默认，显示的是在线视频
        btnNews.setSelected(true);
    }

    //按钮点击跳转事件
    @OnClick({R.id.btnNews,R.id.btnLocal,R.id.btnLikes})
    public void chooseFragment(Button button){
        switch (button.getId()){
            case R.id.btnNews:
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.btnLocal:
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.btnLikes:
                viewPager.setCurrentItem(2,false);
                break;
            default:
                // TODO: 16-9-14  
                throw new RuntimeException("未知的");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        btnNews.setSelected(position == 0);
        btnLocal.setSelected(position == 1);
        btnLikes.setSelected(position == 2);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
