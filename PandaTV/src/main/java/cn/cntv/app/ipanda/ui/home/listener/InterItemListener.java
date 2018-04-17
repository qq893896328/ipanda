package cn.cntv.app.ipanda.ui.home.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.List;

import cn.cntv.app.ipanda.ui.home.activity.InterDetailActivity;
import cn.cntv.app.ipanda.ui.home.entity.Interaction;

/**
 * @ClassName: HomeItemListener
 * @author Xiao JinLai
 * @Date Dec 31, 2015 4:33:30 PM
 * @Description：Home adapter item click listener
 */
public class InterItemListener implements OnClickListener {

	private Context mContext;
	private List<Interaction> mDatas;

	public InterItemListener(Context context, List<Interaction> datas) {

		this.mContext = context;
		this.mDatas = datas;
	}

	@Override
	public void onClick(View v) {

		if (v.getTag() == null) {

			return;
		}

		int tPosition = (Integer) v.getTag();

		String tUrl = mDatas.get(tPosition).getUrl();
		String tTitle = mDatas.get(tPosition).getTitle();
		String image = mDatas.get(tPosition).getImage();

        Intent tIntent = new Intent(mContext, InterDetailActivity.class);
		tIntent.putExtra("url", tUrl);
		tIntent.putExtra("title", tTitle);
		tIntent.putExtra("image", image);
		MobileAppTracker.trackEvent(tTitle, "", "互动", 0, tTitle, "图文浏览", mContext);
		MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
		Log.i("统计","事件名称:"+tTitle);

		mContext.startActivity(tIntent);

	}

	public void setData(List<Interaction> datas) {

		this.mDatas = datas;
	}
}
