package cn.cntv.app.ipanda.ui.home.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.ErrorEnum;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.home.adapter.VoteAdapter;
import cn.cntv.app.ipanda.ui.home.adapter.VoteDetailAdapter;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.GroupData;
import cn.cntv.app.ipanda.ui.home.entity.VoteBody;
import cn.cntv.app.ipanda.ui.home.entity.VoteCode;
import cn.cntv.app.ipanda.ui.home.entity.VoteDataInfo;
import cn.cntv.app.ipanda.ui.home.entity.VoteFoot;
import cn.cntv.app.ipanda.ui.home.entity.VoteHead;
import cn.cntv.app.ipanda.ui.home.entity.VoteOption;
import cn.cntv.app.ipanda.ui.home.entity.VoteOptionValue;
import cn.cntv.app.ipanda.ui.home.entity.VoteSingleGroup;
import cn.cntv.app.ipanda.ui.home.listener.VoteListener;
import cn.cntv.app.ipanda.ui.play.ShareController;
import cn.cntv.app.ipanda.utils.PopWindowUtils;
import cn.cntv.app.ipanda.view.VoteDialog;
import cn.cntv.app.ipanda.xlistview.XListView;

/**
 * @ClassName: VoteActivity
 * @author Xiao JinLai
 * @Date Jan 18, 2016 10:36:50 AM
 * @Description：
 */
public class VoteActivity extends BaseActivity implements OnClickListener,
		VoteListener {

	private static final String DIALOG_TAG = "voteDialog";

	private static final int VOTE_DATA = 0;
	private static final int VOTE_CODE = 1;
	private static final int VOTE_CODES = 2;
	private static final int VOTE_NUM = 3;
	private static final int VOTE_DETAIL = 4;
	private static final int VOTE_GROUP = 5;

	private UserManager mUserManager = UserManager.getInstance();
	private XListView mListView;
	private ImageView mIvVoteNoNet;

	private String mVid;
	private String mTitle;

	private int mOptionSize = 0;

	private boolean mIsGroup = false; // 单组，多组标记，false 为单组，true 为多组

	private List<String> mStageIds;

	private XjlHandler<Object> mHandler = new XjlHandler<Object>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {

					switch (msg.what) {
					case Integer.MAX_VALUE:

						dataError();

						break;

					case VOTE_DATA: // 投票数据

						VoteDataInfo tDataInfo = (VoteDataInfo) msg.obj;

						if (tDataInfo == null || tDataInfo.getData() == null
								|| tDataInfo.getData().getBody() == null) {

							dataError();
						} else {

							handlerData(tDataInfo.getData().getBody());
							dismissLoadDialog();
						}

						break;

					case VOTE_CODE: // 单组投票返回的成功与失败状态

						VoteCode tVoteCode = (VoteCode) msg.obj;
						if (tVoteCode == null) {

							dataError();
						} else {

							if (tVoteCode.getCode() == 0) {

								PopWindowUtils.getInstance()
										.showPopWindowCenter(VoteActivity.this,
												ErrorEnum.VOTE_OK.toString());

								// code 值为 0 说明成功，去投票详情页面
								getVoteData(VOTE_DETAIL);
							} else {

								// 投票失败，显示错误信息
								showDialog(tVoteCode.getMsg());
							}
						}
						break;
					case VOTE_CODES: // 多组投票返回的成功与失败状态

						String tCodeMsg = (String) msg.obj;

						if (tCodeMsg == null || tCodeMsg.equals("")) {

							dataError();
						} else {

							boolean tIsOk = true;
							try {

								JSONObject tJsonObject = new JSONObject(
										tCodeMsg);
								JSONObject tDataObject = tJsonObject
										.getJSONObject("data");

								if (tDataObject == null
										|| tDataObject.equals("")) {

									dataError();

									return;
								}

								for (int i = 0; i < mStageIds.size(); i++) {

									JSONObject tIdObject = tDataObject
											.getJSONObject(mStageIds.get(i));

									if (tIdObject == null
											|| tIdObject.equals("")) {

										continue;
									}

									int tCode = tIdObject.getInt("code");

									if (tCode != 0) {

										String tErrorMsg = tIdObject
												.getString("msg");
										showDialog(tErrorMsg);
										tIsOk = false;
										break;
									}
								}
							} catch (Exception e) {

								dataError();
								tIsOk = false;
							}

							if (tIsOk) {

								PopWindowUtils.getInstance()
										.showPopWindowCenter(VoteActivity.this,
												ErrorEnum.VOTE_OK.toString());
								// 去投票详情页面
								getVoteData(VOTE_DETAIL);
							}
						}
						break;

					case VOTE_NUM: // 投票剩余次数获取

						String tMsg = (String) msg.obj;

						if (tMsg == null || tMsg.equals("")) {

							dataError();
						} else {

							handlerVoteNum(tMsg);
						}
						break;
					case VOTE_DETAIL: // 投票详情数据

						VoteDataInfo tDetailData = (VoteDataInfo) msg.obj;

						if (tDetailData == null
								|| tDetailData.getData() == null
								|| tDetailData.getData().getBody() == null) {

							dataError();
						} else {

							handlerDetailData(tDetailData.getData().getBody());
							dismissLoadDialog();
						}
						break;
					case VOTE_GROUP: // 首先区分单组，多组投票

						VoteDataInfo tDataGroup = (VoteDataInfo) msg.obj;

						if (tDataGroup == null || tDataGroup.getData() == null
								|| tDataGroup.getData().getBody() == null) {

							dataError();
						} else {

							handlerGroupData(tDataGroup.getData().getBody());
							dismissLoadDialog();
						}
						break;

					}
				}
			});
	private ShareController shareController = new ShareController();

	/**
	 * 处理组数据请求
	 * 
	 * @param body
	 */
	private void handlerGroupData(VoteBody body) {

		String tUserId = mUserManager.getUserId();

		IdentityHashMap<String, String> tHashMap = new IdentityHashMap<String, String>();
		tHashMap.put("uid", tUserId);
		tHashMap.put("vid", mVid);

		if (body.getStages() != null && !body.getStages().isEmpty()) {

			int tSize = body.getStages().size();

			mStageIds = new ArrayList<String>();

			for (int i = 0; i < tSize; i++) {

				String tString = new String("stageid[]");
				String tStageId = body.getStages().get(i).getId();
				mStageIds.add(tStageId);
				tHashMap.put(tString, tStageId);
			}

			mIsGroup = true;
		}

		String tGetUrl = mHandler.appendParameter(
				WebAddressEnum.HOME_VOTE_GET.toString(), tHashMap);
		mHandler.getHttpString(tGetUrl, VOTE_NUM);
	}

	/**
	 * 处理投票剩余次数数据
	 * 
	 * @param
	 */
	private void handlerVoteNum(String json) {

		int tLeftNum = 0;

		if (mIsGroup) {

			try {

				JSONObject tJsonObject = new JSONObject(json);
				int tCode = tJsonObject.getInt("code");
				if (tCode == 0) {

					JSONObject tDataObject = tJsonObject.getJSONObject("data");

					if (tDataObject == null || tDataObject.equals("")) {

						dataError();

						return;
					}

					for (int i = 0; i < mStageIds.size(); i++) {

						JSONObject tIdObject = tDataObject
								.getJSONObject(mStageIds.get(i));

						if (tIdObject == null || tIdObject.equals("")) {

							continue;
						}

						int tIdLeftNum = tIdObject.getInt("left_num");

						if (tIdLeftNum <= 0) {

							tLeftNum = 0;
							break;
						}

						tLeftNum = tIdLeftNum;

					}
				} else {

					dataError();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				dataError();

				return;
			}

		} else {

			Gson tGson = new Gson();
			VoteSingleGroup tData = tGson.fromJson(json, VoteSingleGroup.class);

			if (tData == null || tData.getData() == null
					|| tData.getData().isEmpty()) {

				dataError();
				return;
			}

			for (int i = 0; i < tData.getData().size(); i++) {

				int tIdLeftNum = tData.getData().get(i).getLeft_num();

				if (tIdLeftNum <= 0) {

					tLeftNum = 0;
					break;
				}

				tLeftNum = tIdLeftNum;
			}
		}

		if (tLeftNum < 1) {

			// 投票剩余次数小于1，直接显示投票详情数据
			getVoteData(VOTE_DETAIL);
		} else {

			// 投票剩余次数大于1，获取投票数据
			getVoteData(VOTE_DATA);
		}
	}

	/**
	 * 获取投票数据
	 */
	private void getVoteData(int voteWhat) {

		String tWebAddess = WebAddressEnum.HOME_VOTE.toString() + mVid;
		mHandler.getHttpJson(tWebAddess, VoteDataInfo.class, voteWhat);
	}

	/**
	 * 处理投票数据
	 * 
	 * @param body
	 */
	private void handlerData(VoteBody body) {

		List<AdapterData> tDatas = new ArrayList<AdapterData>();

		mTitle = body.getSubject();

		VoteHead tVoteHead = new VoteHead(body.getSubject(),
				body.getEnd_time(), body.getInfo_img(), body.getDesc());

		tDatas.add(tVoteHead);

		if (body.getStages() == null || body.getStages().isEmpty()) {

			mOptionSize = 1;
			tDatas.add(new GroupData(body.getSubject()));
			tDatas.add(new VoteOption(0, body.getId(), body.getItems()));
		} else {

			mOptionSize = body.getStages().size();

			for (int i = 0; i < mOptionSize; i++) {

				tDatas.add(new GroupData(body.getStages().get(i)
						.getStage_desc()));
				tDatas.add(new VoteOption(i, body.getStages().get(i).getId(),
						body.getStages().get(i).getItems()));
			}
		}

		tDatas.add(new GroupData());
		tDatas.add(new VoteFoot());

		mListView.setAdapter(new VoteAdapter(VoteActivity.this, tDatas));
	}

	/**
	 * 处理详情数据
	 * 
	 * @param body
	 */
	private void handlerDetailData(VoteBody body) {

		List<AdapterData> tDatas = new ArrayList<AdapterData>();

		mTitle = body.getSubject();

		VoteHead tVoteHead = new VoteHead(body.getSubject(),
				body.getEnd_time(), body.getInfo_img(), body.getDesc());

		tDatas.add(tVoteHead);

		if (body.getStages().isEmpty()) {

			tDatas.add(new GroupData(body.getSubject()));

			int tCount = 0;
			int tSize = body.getItems().size();

			for (int i = 0; i < tSize; i++) {

				tCount += body.getItems().get(i).getVoted_cnt();
			}

			for (int i = 0; i < tSize; i++) {

				String title = body.getItems().get(i).getTitle();
				double cnt = body.getItems().get(i).getVoted_cnt();
				double total = Math.round((cnt / tCount) * 10000);
				double width = total / 100;
				String pct = width + "%";
				String tik = "(" + ((int) cnt) + "票)";

				tDatas.add(new VoteOptionValue(i, title, width, pct, tik));
			}
		} else {

			int tOptionSize = body.getStages().size();

			for (int i = 0; i < tOptionSize; i++) {

				tDatas.add(new GroupData(body.getStages().get(i)
						.getStage_desc()));

				int tCount = 0;
				int tSize = body.getStages().get(i).getItems().size();

				for (int j = 0; j < tSize; j++) {

					tCount += body.getStages().get(i).getItems().get(j)
							.getVoted_cnt();
				}

				for (int j = 0; j < tSize; j++) {

					String title = body.getStages().get(i).getItems().get(j)
							.getTitle();
					double cnt = body.getStages().get(i).getItems().get(j)
							.getVoted_cnt();
					double width = (Math.round((cnt / tCount) * 10000)) / 100;
					String pct = width + "%";
					String tik = "(" + ((int) cnt) + "票)";

					tDatas.add(new VoteOptionValue(j, title, width, pct, tik));
				}
			}
		}

		mListView.setAdapter(new VoteDetailAdapter(VoteActivity.this, tDatas));
	}

	/**
	 * 数据错误提示
	 */
	private void dataError() {

		dataError(null);
	}

	/**
	 * 数据错误提示
	 * 
	 * @param toastValue
	 */
	private void dataError(String toastValue) {

		mListView.stopRefresh();

		mListView.setVisibility(View.GONE);
		mIvVoteNoNet.setVisibility(View.VISIBLE);

		dismissLoadDialog();

		String tValue = "数据加载失败,请重试!";

		if (toastValue != null && !toastValue.equals("")) {

			tValue = toastValue;
		}

		PopWindowUtils.getInstance().showPopWindowCenter(this, tValue);
	}

	@SuppressLint("NewApi")
	private void showDialog(String title) {

		dismissLoadDialog();

		FragmentTransaction tTransaction = getFragmentManager()
				.beginTransaction();
		Fragment tFragment = getFragmentManager().findFragmentByTag(DIALOG_TAG);
		if (tFragment != null) {

			tTransaction.remove(tFragment);
		}

		VoteDialog tDialog = new VoteDialog(title);
		tDialog.show(tTransaction, DIALOG_TAG);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {

		setContentView(R.layout.activity_home_vote);

		this.findViewById(R.id.tvVoteBack).setOnClickListener(this);
		this.findViewById(R.id.tvVoteShare).setOnClickListener(this);

		mListView = (XListView) this.findViewById(R.id.xlvVote);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);

		mIvVoteNoNet = (ImageView) this.findViewById(R.id.ivVoteNoNet);
		mIvVoteNoNet.setOnClickListener(this);
	}

	/**
	 * 加载数据
	 */
	private void initData() {

		showLoadingDialog();

		if (isConnected()) {

			mIvVoteNoNet.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			mVid = getIntent().getStringExtra("vid");


			if (mVid != null && !mVid.equals("")) {

				getVoteData(VOTE_GROUP);
				return;
			}
		}

		dataError();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ivVoteNoNet:

			initData();
			break;
		case R.id.tvVoteBack:

			VoteActivity.this.finish();
			break;

		case R.id.tvVoteShare:

			shareController.showPopWindow(v, mTitle,null,null,null,null,null);
			break;
		}
	}

	/**
	 * 提交投票方法
	 */
	@Override
	public void comitVote(HashMap<String, String> comitValue) {

		if (comitValue.size() >= mOptionSize) {

			String tUserId = mUserManager.getUserId();

			comitValue.put("vid", mVid);
			comitValue.put("uid", tUserId);

			showLoadingDialog();

			if (mIsGroup) {

				mHandler.getHttpPostJson(
						WebAddressEnum.HOME_COMIT_VOTE_STAGE.toString(),
						comitValue, VOTE_CODES);
			} else {

				mHandler.getHttpPostJson(
						WebAddressEnum.HOME_COMIT_VOTE.toString(), comitValue,
						VoteCode.class, VOTE_CODE);
			}
		} else {

			if (comitValue.size() == 0) {

				PopWindowUtils.getInstance().showPopWindowCenter(this,
						ErrorEnum.VOTE_VALUE_NULL.toString());
			} else {

				PopWindowUtils.getInstance().showPopWindowCenter(this,
						ErrorEnum.VOTE_VALUE_DELETION.toString());
			}
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	

}
