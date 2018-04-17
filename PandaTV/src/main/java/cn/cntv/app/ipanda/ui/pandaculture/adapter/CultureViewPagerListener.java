package cn.cntv.app.ipanda.ui.pandaculture.adapter;

import android.support.v4.view.ViewPager;

import cn.cntv.app.ipanda.view.PointView;

/**
 * Created by maqingwei on 2016/5/19.
 */
public class CultureViewPagerListener  implements ViewPager.OnPageChangeListener {

    private PointView mPointView;

    public CultureViewPagerListener(PointView pointView) {

        this.mPointView = pointView;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        mPointView.setCurrentIndex(position);
    }

}

