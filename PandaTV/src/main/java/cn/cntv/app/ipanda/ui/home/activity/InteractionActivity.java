package cn.cntv.app.ipanda.ui.home.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.home.adapter.InteractionAdapter;
import cn.cntv.app.ipanda.ui.home.entity.Interaction;
import cn.cntv.app.ipanda.ui.home.entity.InteractionInfo;
import cn.cntv.app.ipanda.xlistview.XListView;

/**
 * @ClassName: InteractionActivity
 * @author Xiao JinLai
 * @Date Jan 4, 2016 10:41:15 AM
 * @Description：Home interaction page（互动页面）
 */
public class InteractionActivity extends BaseActivity implements
		XListView.IXListViewListener, OnClickListener, OnItemClickListener {

	private static final int INTERACTION_DATA = 0;

	private XListView mListView;
	private ImageView mIvInterNoNet;

	private InteractionAdapter mAdapter;
	private List<Interaction> mDatas;

	private TextView titleLeft, titleCenter;

	private boolean mBolRefersh;

	private XjlHandler<Object> mHandler = new XjlHandler<Object>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {

					switch (msg.what) {
					case Integer.MAX_VALUE:

						dataError();
						break;

					case INTERACTION_DATA:

						InteractionInfo tDataInfo = (InteractionInfo) msg.obj;

						if (tDataInfo == null
								|| tDataInfo.getInteractive() == null
								|| tDataInfo.getInteractive().isEmpty()) {

							dataError();
						} else {

							mDatas = tDataInfo.getInteractive();

							mListView.stopRefresh();

							if (mBolRefersh) {

								mAdapter.updateData(mDatas);

								mBolRefersh = false;
							} else {

								mAdapter = new InteractionAdapter(
										InteractionActivity.this, mDatas);
								mListView.setAdapter(mAdapter);
							}
						}

						dismissLoadDialog();
						break;

					}
				}
			});


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		showLoadingDialog();
		initData();
	}

	private void initView() {

		setContentView(R.layout.activity_home_inter);

		titleLeft = (TextView) this.findViewById(R.id.common_title_left);
		titleCenter = (TextView) this.findViewById(R.id.common_title_center);
		titleCenter.setText(getString(R.string.hudong));

		titleLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				finish();
			}
		});

		mListView = (XListView) this.findViewById(R.id.xlvInter);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);

		mIvInterNoNet = (ImageView) this.findViewById(R.id.ivInterNoNet);
		mIvInterNoNet.setOnClickListener(this);
	}

	private void initData() {

		if (isConnected()) {

			mIvInterNoNet.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			mHandler.getHttpJson(WebAddressEnum.HOME_INTERACTION.toString(),
					InteractionInfo.class, INTERACTION_DATA);
		} else {

			dataError();
		}
	}

	/**
	 * 数据错误
	 */
	private void dataError() {

		mIvInterNoNet.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);

		mListView.stopRefresh();

		dismissLoadDialog();

		Toast.makeText(InteractionActivity.this, R.string.load_try_again,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {

		mBolRefersh = true;

		initData();
	}

	@Override
	public void onLoadMore() {

	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ivInterNoNet:

			initData();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
