package cn.cntv.app.ipanda.ui.home.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.JMPType2Enum;
import cn.cntv.app.ipanda.constant.JMPTypeEnum;
import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.home.activity.InterDetailActivity;
import cn.cntv.app.ipanda.ui.home.activity.VoteActivity;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.BigImg;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterBigImg;
import cn.cntv.app.ipanda.ui.home.entity.Interaction;
import cn.cntv.app.ipanda.ui.home.entity.InteractionOne;
import cn.cntv.app.ipanda.ui.home.entity.InteractionTwo;
import cn.cntv.app.ipanda.ui.home.entity.Live;
import cn.cntv.app.ipanda.ui.home.entity.SSLiveVideoList;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList1;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList3;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList4;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectAdapterLVList;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectVote;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalLoginActivity;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.CustomDialog;
import cn.cntv.app.ipanda.utils.xinterface.DialogListener;

/**
 * @ClassName: HomeItemListener
 * @author Xiao JinLai
 * @Date Dec 31, 2015 4:33:30 PM
 * @Description：Home adapter item click listener
 */
public class SSubjectItemListener implements OnClickListener {

	private Context mContext;
	private ArrayList<AdapterData> mDatas;

	public SSubjectItemListener(Context context, ArrayList<AdapterData> datas) {

		this.mContext = context;
		this.mDatas = datas;
	}

	public void setData(ArrayList<AdapterData> datas) {

		this.mDatas = datas;
	}

	@Override
	public void onClick(View v) {

		if (v.getTag() == null) {

			return;
		}

		String[] tTags = v.getTag().toString().split(",");

		if (tTags == null || tTags.length == 0) {

			return;
		}

		int tPosition = Integer.parseInt(tTags[0]);
		int tIndex = Integer.parseInt(tTags[1]);

		if (mDatas == null || mDatas.isEmpty()) {
			return;
		}

		AdapterData tData = mDatas.get(tPosition);

		if (tData.getAdapterType() == SSubjectTypeEnum.BIGIMG_TYPE.value()) {

			// 轮播大图点击事件
			HomeAdapterBigImg tAdapterBigImg = (HomeAdapterBigImg) tData;

			if (tAdapterBigImg != null
					&& !tAdapterBigImg.getBigImgs().isEmpty()) {

				BigImg tBigImg = tAdapterBigImg.getBigImgs().get(tIndex);

				String tId = tBigImg.getId();
				String tStype = tBigImg.getStype();
				String tType = tBigImg.getType();
				String tTitle = tBigImg.getTitle();
				String tPid = tBigImg.getPid();
				String tVid = tBigImg.getVid();
				String tImg = tBigImg.getImage();
				String tUrl = tBigImg.getUrl();

				if (tStype.equals(JMPTypeEnum.JMP_CCTV.toString())) {

					// 央视名栏底层页
					setCCTV(tVid, tTitle, tImg);

				}
				if (tPid != null && !tPid.equals("") && tVid != null
						&& !tVid.equals("")) {

					// 点播视频集
					setVideoList(tPid, tVid, tTitle, tImg);
				} else if (tPid != null && !tPid.equals("")) {

					// 点播单视频
					setVodVideo(tPid, tTitle, null, tImg);
				} else if (tVid != null && !tVid.equals("")) {

					// 央视名栏底层页
					setCCTV(tVid, tTitle, tImg);
				} else if (tType.equals(JMPTypeEnum.VIDEO_LIVE.toString())) {

					int tPage = 0;

					if (tStype.equals("1") || tStype.equals("2")) {

						tPage = CollectPageSourceEnum.XMZB.value();
					} else if (tStype.equals("3") || tStype.equals("4")) {

						tPage = CollectPageSourceEnum.ZBZG.value();
					} else if (tStype.equals("5")) {

						tPage = CollectPageSourceEnum.PDZB.value();
					}

					// 直播视频
					setLiveVideo(tId, tTitle, tImg, tPage);

				} else if (tType.equals(JMPTypeEnum.VIDEO_BODY.toString())) {

					// 熊猫观察图文底层页
					setTeleText(tId, tTitle, tUrl, tImg, null);

				} else if (tBigImg.getType().equals(
						JMPTypeEnum.VIDEO_BODY6.toString())) {

					HashMap<String, String> tHashMap = new HashMap<String, String>();
					tHashMap.put("url", tUrl);
					tHashMap.put("title", tTitle);

					startIntent(InterDetailActivity.class, tHashMap);

				} else {

					dataError();
				}
			}
		} else if (tData.getAdapterType() == SSubjectTypeEnum.LIVE_TYPE.value()) {

			Live tLive = (Live) tData;

			if (tLive != null) {

				// 直播视频
				setLiveVideo(tLive.getId(), tLive.getTitle(), tLive.getImage(),
						CollectPageSourceEnum.PDZB.value());
			}

		} else if (tData.getAdapterType() == SSubjectTypeEnum.SSUBJECT_LIVE_LIST
				.value()) {

			SSubjectAdapterLVList tAdapterLive = (SSubjectAdapterLVList) tData;

			if (tAdapterLive != null
					&& !tAdapterLive.getLiveVideoLists().isEmpty()) {

				SSLiveVideoList tLive = tAdapterLive.getLiveVideoLists().get(
						tIndex);

				// 直播视频
				setLiveVideo(tLive.getId(), tLive.getTitle(), tLive.getImage(),
						CollectPageSourceEnum.ZBZG.value());

			}

		} else if (tData.getAdapterType() == SSubjectTypeEnum.SSUBJECT_LIST1
				.value()) {

			SSVideoList1 tVideoList1 = (SSVideoList1) tData;

			if (tVideoList1 != null && !tVideoList1.getVideoLists1().isEmpty()) {

				SSVideoList tVideoList = tVideoList1.getVideoLists1().get(
						tIndex);

				setVideoStyle(tVideoList);
			}

		} else if (tData.getAdapterType() == SSubjectTypeEnum.SSUBJECT_LIST3
				.value()) {

			SSVideoList3 tSSubject = (SSVideoList3) tData;

			if (tSSubject != null && tSSubject.getVideoList3() != null) {

				SSVideoList tVideoList = tSSubject.getVideoList3();
				setVideoStyle(tVideoList);
			}

		} else if (tData.getAdapterType() == SSubjectTypeEnum.SSUBJECT_LIST4
				.value()) {

			SSVideoList4 tVideoList4 = (SSVideoList4) tData;

			if (tVideoList4 != null && tVideoList4.getVideoList4() != null) {

				SSVideoList tVideoList = tVideoList4.getVideoList4();
				setVideoStyle(tVideoList);
			}

		} else if (tData.getAdapterType() == SSubjectTypeEnum.SSUBJECT_VOTEURL
				.value()) {

			SSubjectVote tVote = (SSubjectVote) tData;

			if (tVote == null || tVote.getVote() == null) {

				Toast.makeText(mContext, R.string.data_error_try_again_later, Toast.LENGTH_SHORT)
						.show();
			} else {

				String tUserId = UserManager.getInstance().getUserId();

				final String tVid = tVote.getVote().getId();

				if (tUserId == null || tUserId.equals("")) {

					CustomDialog.showDialogView(v, new DialogListener() {

						@Override
						public void confirm() {

							HashMap<String, String> tHashMap = new HashMap<String, String>();
							tHashMap.put(PersonalLoginActivity.FromWhere,
									PersonalLoginActivity.FromSSubjectPage);
							tHashMap.put("vid", tVid);
							startIntent(PersonalLoginActivity.class, tHashMap);
						}
					});
				} else {

					HashMap<String, String> tHashMap = new HashMap<String, String>();
					tHashMap.put("vid", tVid);

					startIntent(VoteActivity.class, tHashMap);
				}

			}
		} else if (tData.getAdapterType() == SSubjectTypeEnum.INTERACTIVEONE_TYPE
				.value()) {

			InteractionOne tHomeInteractionOne = (InteractionOne) tData;

			if (tHomeInteractionOne != null
					&& tHomeInteractionOne.getInteraction() != null) {

				Interaction tInteraction = tHomeInteractionOne.getInteraction();
				HashMap<String, String> tHashMap = new HashMap<String, String>();
				tHashMap.put("url", tInteraction.getUrl());
				tHashMap.put("title", tInteraction.getTitle());
				startIntent(InterDetailActivity.class, tHashMap);
			}

		} else if (tData.getAdapterType() == SSubjectTypeEnum.INTERACTIVETWO_TYPE
				.value()) {

			InteractionTwo tInteractionTwo = (InteractionTwo) tData;

			if (tInteractionTwo != null
					&& !tInteractionTwo.getInteraction().isEmpty()) {

				Interaction tInteraction = tInteractionTwo.getInteraction()
						.get(tIndex);
				HashMap<String, String> tHashMap = new HashMap<String, String>();
				tHashMap.put("url", tInteraction.getUrl());
				tHashMap.put("title", tInteraction.getTitle());
				startIntent(InterDetailActivity.class, tHashMap);
			}

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

		mContext.startActivity(tIntent);
	}

	private void setVideoStyle(SSVideoList videoList) {

		String tId = videoList.getId();
		String tVid = videoList.getVid();
		String tPid = videoList.getPid();
		String tTitle = videoList.getTitle();
		String tType = videoList.getId().substring(0, 4);
		String tUrl = videoList.getUrl();
		String tImg = videoList.getImage();
		String tVideoLength = videoList.getVideoLength();
		String tTimeval = null;

		if (tPid != null && !tPid.equals("") && tVid != null
				&& !tVid.equals("")) {

			// 点播视频集
			setVideoList(tPid, tVid, tTitle, tImg);
		} else if (tPid != null && !tPid.equals("")) {

			// 点播单视频
			setVodVideo(tPid, tTitle, tVideoLength, tImg);
		} else if (tVid != null && !tVid.equals("")) {

			// 央视名栏底层页
			setCCTV(tVid, tTitle, tImg);
		} else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {

			// 熊猫观察图文底层页
			setTeleText(tId, tTitle, tUrl, tImg, tTimeval);
		} else {

			dataError();
		}
	}

	/**
	 * 单视频跳转
	 * 
	 * @param pid
	 * @param title
	 * @param img
	 * @param timeLegth
	 */
	private void setVodVideo(String pid, String title, String timeLegth,
			String img) {

		if (pid == null || pid.equals("")) {

			dataError();
			return;
		}
		//统计
		MobileAppTracker.trackEvent(title, "专题推荐", "首页", 0, pid, "视频观看", mContext);
		MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
		Log.e("统计","事件名称:"+title+"***事件类别:"+"专题推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+pid);

		setVideo(pid, null, title, JMPTypeEnum.VIDEO_VOD.value(), timeLegth,
				img);
	}

	/**
	 * 视频集跳转
	 * 
	 * @param pid
	 * @param vid
	 * @param title
	 * @param img
	 */
	private void setVideoList(String pid, String vid, String title, String img) {

		if (vid == null || vid.equals("")) {

			dataError();
			return;
		}
		//统计
		MobileAppTracker.trackEvent(title, "专题推荐", "首页", 0, pid, "视频观看", mContext);
		MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
		Log.e("统计","事件名称:"+title+"***事件类别:"+"专题推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+pid);
		setVideo(pid, vid, title, JMPTypeEnum.VIDEO_LIST.value(), null, img);
	}

	/**
	 * 视频跳转
	 * 
	 * @param pid
	 * @param vid
	 * @param title
	 * @param img
	 * @param timeLegth
	 */
	private void setVideo(String pid, String vid, String title, int videoType,
			String timeLegth, String img) {

		PlayVodEntity tEntity = new PlayVodEntity();
		tEntity.setVid(pid);
		tEntity.setVideosId(vid);
		tEntity.setTitle(title);
		tEntity.setVideoType(videoType);
		tEntity.setTimeLegth(timeLegth);
		tEntity.setType(CollectTypeEnum.SP.value() + "");
		tEntity.setImg(img);

		Intent tIntent = new Intent(mContext, PlayVodFullScreeActivity.class);
		tIntent.putExtra("vid", tEntity);

		mContext.startActivity(tIntent);
	}

	private void setCCTV(String id, String title, String img) {

		if (id == null || id.equals("")) {

			dataError();
			return;
		}

		// 央视名栏底层页
		Intent tIntent = new Intent(mContext, CCTVDetailActivity.class);
		tIntent.putExtra("id", id);
		tIntent.putExtra("title", title);
		tIntent.putExtra("image", img);
		//统计
		MobileAppTracker.trackEvent(title, "专题推荐", "首页", 0, id, "视频观看", mContext);
		MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
		Log.e("统计","事件名称:"+title+"***事件类别:"+"专题推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+id);
		mContext.startActivity(tIntent);
	}

	private void setLiveVideo(String id, String title, String img, int page) {

		if (id == null || id.equals("")) {

			Toast.makeText(mContext, R.string.video_address_not_exist, Toast.LENGTH_SHORT).show();
			return;
		}

		PlayLiveEntity tEntity = new PlayLiveEntity();
		tEntity.setChanneId(id);
		tEntity.setTitle(title);
		tEntity.setType(CollectTypeEnum.PD.value() + "");
		tEntity.setImage(img);
		tEntity.setPageSource(page);
		//统计
		MobileAppTracker.trackEvent(title, "专题推荐", "首页", 0, id, "视频观看", mContext);
		MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
		Log.e("统计","事件名称:"+title+"***事件类别:"+"专题推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+id);
		Intent tIntent = new Intent(mContext, PlayLiveActivity.class);
		tIntent.putExtra("live", tEntity);

		mContext.startActivity(tIntent);
	}

	/**
	 * 数据为空时判断
	 */
	private void dataError() {

		Toast.makeText(mContext, R.string.video_address_not_exist,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 带参跳转
	 * 
	 * @param startActivity
	 *
	 */
	private void startIntent(Class<?> startActivity,
			HashMap<String, String> hashMap) {

		Intent tIntent = new Intent(mContext, startActivity);

		if (hashMap != null) {

			Iterator<String> tIterator = hashMap.keySet().iterator();

			while (tIterator.hasNext()) {

				String tKey = tIterator.next();
				tIntent.putExtra(tKey, hashMap.get(tKey));
			}
		}

		mContext.startActivity(tIntent);
	}
}
