package cn.cntv.app.ipanda.ui.home.listener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.JMPType2Enum;
import cn.cntv.app.ipanda.constant.JMPTypeEnum;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;

public class SSubjectItem2Listener implements OnItemClickListener {

	private List<SSVideoList> mDatas;
	private Context mContext;
	SharedPreferences mSharedPreferences;
	private String groupTitle = "";

	public SSubjectItem2Listener(Context context) {

		this.mContext = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		SSVideoList tVideoList = mDatas.get(position);

		String tId = tVideoList.getId();
		String tVid = tVideoList.getVid();
		String tPid = tVideoList.getPid();
		String tTitle = tVideoList.getTitle();
		String tType = tVideoList.getId().substring(0, 4);
		String tUrl = tVideoList.getUrl();
		String tImg = tVideoList.getImage();
		String tVideoLength = tVideoList.getVideoLength();
		String tTimeval = null;


		if (tPid != null && !tPid.equals("") && tVid != null
				&& !tVid.equals("")) {

			// 点播视频集
			PlayVodEntity tEntity = new PlayVodEntity();
			tEntity.setVid(tPid);
			tEntity.setVideosId(tVid);
			tEntity.setTitle(tTitle);
			tEntity.setImg(tImg);
			tEntity.setVideoType(JMPTypeEnum.VIDEO_LIST.value());
			tEntity.setTimeLegth(tVideoLength);
			tEntity.setType(CollectTypeEnum.SP.value() + "");
			//统计
			MobileAppTracker.trackEvent(tTitle, "专题推荐", "首页", 0, tPid, "视频观看", mContext);
			MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
			Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"专题推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tPid);
			Intent tIntent = new Intent(mContext,
					PlayVodFullScreeActivity.class);
			tIntent.putExtra("vid", tEntity);

			mContext.startActivity(tIntent);
		} else if (tPid != null && !tPid.equals("")) {

			// 点播单视频
			PlayVodEntity tEntity = new PlayVodEntity();
			tEntity.setVid(tPid);
			tEntity.setTitle(tTitle);
			tEntity.setImg(tImg);
			tEntity.setVideoType(JMPTypeEnum.VIDEO_VOD.value());
			tEntity.setTimeLegth(tVideoLength);
			tEntity.setType(CollectTypeEnum.SP.value() + "");

			Intent tIntent = new Intent(mContext,
					PlayVodFullScreeActivity.class);
			tIntent.putExtra("vid", tEntity);
			//统计
			MobileAppTracker.trackEvent(tTitle, "专题推荐", "首页", 0, tPid, "视频观看", mContext);
			MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
			Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"专题推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tPid);
			mContext.startActivity(tIntent);

		} else if (tVid != null && !tVid.equals("")) {

			// 央视名栏底层页
			Intent tIntent = new Intent(mContext, CCTVDetailActivity.class);
			tIntent.putExtra("id", tVid);
			tIntent.putExtra("title", tTitle);
			tIntent.putExtra("image", tImg);
			tIntent.putExtra("videoLength", tVideoLength);
			//统计
			MobileAppTracker.trackEvent(tTitle, "专题推荐", "首页", 0, tVid, "视频观看", mContext);
			MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
			Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"专题推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tVid);
			mContext.startActivity(tIntent);
		} else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {

			// 熊猫观察图文底层页
			//setTeleText(tTitle, tUrl, tImg, tTimeval);
			
			// 熊猫观察图文底层页
						setTeleText(tId, tTitle, tUrl, tImg, tTimeval);
			
		} else {

			Toast.makeText(mContext, R.string.video_address_not_exist,
					Toast.LENGTH_SHORT).show();
		}
	}






	/**
	 * 设置熊猫观察图文跳转
	 * 
	 * @param title
	 * @param url
	 * @param img
	 * @param timeval
	 */
	private void setTeleText(String id, String title, String url, String img,
			String timeval) {

		Intent tIntent = new Intent(mContext, PandaEyeDetailActivity.class);
		tIntent.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);

		if (id != null && !id.equals("")) {

			tIntent.putExtra("id", id);
		}

		if (title != null && !title.equals("")) {

			tIntent.putExtra("title", title);
		}

		if (url != null && !url.equals("")) {

			tIntent.putExtra("url", url);
		}

		if (img != null && !img.equals("")) {

			tIntent.putExtra("pic", img);
		}

		if (timeval != null && !timeval.equals("")) {

			tIntent.putExtra("timeval", timeval);
		}
		//统计
		MobileAppTracker.trackEvent(title, "专题推荐", "首页", 0, id, "图文浏览", mContext);
		MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
		Log.e("统计","事件名称:"+title+"***事件类别:"+"专题推荐"+"***事件标签:"+"首页"+"***类型:"+"图文浏览"+"*****ID="+id);
		mContext.startActivity(tIntent);
	}

	
	
	
	

	/**
	 * 设置熊猫观察图文跳转
	 *
	 */
//	private void setTeleText(String title, String url, String img,
//			String timeval) {
//
//		Intent tIntent = new Intent(mContext, PandaEyeDetailActivity.class);
//
//		if (title != null && !title.equals("")) {
//
//			tIntent.putExtra("title", title);
//		}
//
//		if (url != null && !url.equals("")) {
//
//			tIntent.putExtra("url", url);
//		}
//
//		if (img != null && !img.equals("")) {
//
//			tIntent.putExtra("pic", img);
//		}
//
//		if (timeval != null && !timeval.equals("")) {
//
//			tIntent.putExtra("timeval", timeval);
//		}
//
//		mContext.startActivity(tIntent);
//	}

	public void setData(List<SSVideoList> datas) {

		this.mDatas = datas;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
}
