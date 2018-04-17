package cn.cntv.app.ipanda.ui.personal.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.personal.adapter.PersonalCommonQuestionAdapter;
import cn.cntv.app.ipanda.ui.personal.entity.CommonQuestionModle;
import cn.cntv.app.ipanda.utils.ToastUtil;

public class CommonQuestionFragment extends BaseFragment {

	ListView mListView;
	List<CommonQuestionModle> mListData;
	PersonalCommonQuestionAdapter mAdapter;
	XjlHandler<ToastUtil> mHander = new XjlHandler<ToastUtil>(new HandlerListener() {

		@Override
		public void handlerMessage(HandlerMessage msg) {
			// TODO Auto-generated method stub
			dismissLoadDialog();

			switch (msg.what) {
			case Integer.MAX_VALUE:
				// 错误处理
				Log.e("wang", msg.obj.toString());
				break;
			case 1:
//				CommonQuestionData data1 = (CommonQuestionData) msg.obj;
//				Log.e("test", "history=" + data1.toString());
//
//				if (null != data1) {
//					mListData = data1.getList();
//					mAdapter = new PersonalCommonQuestionAdapter(getActivity(),
//							mListData);
//					mListView.setAdapter(mAdapter);
//				}
				break;
			}

		}
	});

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = View.inflate(getActivity(),
				R.layout.activity_personal_feedback_commonquestion, null);
		mListView = (ListView) v.findViewById(R.id.common_listview);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mListData = new ArrayList<CommonQuestionModle>();
		requestHisData();
	}

	private void requestHisData() {

//		if (isConnected()) 
		{
			showLoadingDialog();
			final String json = "{'1.观看熊猫频道视频对手机网络有什么要求':'建议您在有WiFi的情况下观看视频，不建议您在2G网络下观看视频熊猫频道可以对您的联网类型进行自动识别，当您在3G、4G情况下观看视频时，手机会自动提示您在使用数据流量环境下观看视频，可能会产生高额费用，是否继续观看。',"
					+ "'2.如何清除缓存':'在“个人中心”页面里点击“设置”，在设置页面里选择“清除缓存”。',"
					+ "'3.如何进行版本更新':'新版本更新以后，用户首次启动客户端，会自动接收到升级提示。点击“升级”即可。IOS用户手机自动跳转至AppStore下载页面，Android用户手机自动后台下载更新。',"
					+ "'4.启动或使用时闪退该怎么办？':'登录或使用熊猫频道时出现闪退，可能是手机运行空间不足或软件数据包丢失导致，请您尝试重启手机或卸载软件后，下载安装最新版本使用。',"
					+ "'5.我有问题，如何反馈？':'在“个人中心”页面里点击 “设置”，在设置页面里选择“用户反馈及帮助”。根据页面提示提交反馈，我们收到后将及时处理。'}";
//			mHander.getHttpJson(
//					PandaEyeAddressEnum.PE_FEEDBACK_COMMONQUESTION.toString(),
//					CommonQuestionData.class, 1);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						JSONObject job = new JSONObject(json);
						Iterator it = job.keys();
						TreeMap<Integer, CommonQuestionModle> tmap = new TreeMap<Integer, CommonQuestionModle>();
						while(it.hasNext()){
							String key = it.next().toString();
							CommonQuestionModle cqm = new CommonQuestionModle();
							cqm.setTitle(key);
							cqm.setConten("		"+job.getString(key));
							String skey = key.substring(0, 1);
							int pos = Integer.parseInt(skey)-1;
							tmap.put(pos, cqm);
						}
						mListData.addAll(tmap.values());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}finally{
						mListView.post(new Runnable() {
							
							@Override
							public void run() {
								dismissLoadDialog();
								
								// TODO Auto-generated method stub
								mAdapter = new PersonalCommonQuestionAdapter(getActivity(),
										mListData);
								mListView.setAdapter(mAdapter);
							}
						});
					}		
				}
			}).start();
			
			
			
//		} else {
//			Toast.makeText(getActivity(), R.string.network_invalid, Toast.LENGTH_SHORT)
//					.show();
		}
	}

//	public static class CommonQuestionData {
//		List<CommonQuestionModle> list;
//
//		public List<CommonQuestionModle> getList() {
//			return list;
//		}
//
//		public void setList(List<CommonQuestionModle> list) {
//			this.list = list;
//		}
//
//		public static class CommonQuestionModle {
//			String title;
//			String conten;
//
//			public String getTitle() {
//				return title;
//			}
//
//			public void setTitle(String title) {
//				this.title = title;
//			}
//
//			public String getConten() {
//				return conten;
//			}
//
//			public void setConten(String conten) {
//				this.conten = conten;
//			}
//		}
//	}

}
