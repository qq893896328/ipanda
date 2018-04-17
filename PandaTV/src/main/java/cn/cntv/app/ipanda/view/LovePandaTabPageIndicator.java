package cn.cntv.app.ipanda.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gridsum.mobiledissector.MobileAppTracker;

/**
 * Created by ASUS on 2016/7/5.
 */
public class LovePandaTabPageIndicator extends LiveChinaTabPageIndicator {
    public LovePandaTabPageIndicator(Context context) {
        super(context);
    }

    public LovePandaTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        TabView tabView = (TabView)view;
        //统计
        MobileAppTracker.trackEvent(String.valueOf(tabView.getText()), "", "熊猫直播", 0, null, "",getContext());
        MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
        Log.e("统计","事件名称:"+String.valueOf(tabView.getText())+"***事件类别:"+"直播中国导航栏"+"***事件标签:"+"直播中国"+"***类型:"+"null");
    }
}
