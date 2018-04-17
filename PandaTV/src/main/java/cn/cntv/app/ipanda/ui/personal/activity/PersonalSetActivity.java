package cn.cntv.app.ipanda.ui.personal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gridsum.mobiledissector.util.StringUtil;
import com.umeng.message.PushAgent;

import java.io.File;
import java.util.HashMap;

import cn.cntv.app.ipanda.AppConfig;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.PandaEyeAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.personal.entity.PIUpdateData;
import cn.cntv.app.ipanda.ui.personal.entity.PIUpdateModel;
import cn.cntv.app.ipanda.utils.DataCleanManager;
import cn.cntv.app.ipanda.utils.GetFileSizeUtil;
import cn.cntv.app.ipanda.utils.UpgradeTask;

public class PersonalSetActivity extends BaseActivity {

	private TextView titleLeft, titleCenter, cacheTv;
	private RelativeLayout aboutLayout, fanKuiLayout, udpateLayout, delLayout,
			pingLayout;
	private ImageView pushImg, playImg;
	private String pushSign, playSign;
	private SharedPreferences pSp;
    private static final int LIST_DATA1 = 1;
    private static final int UPGRADE_APK_CODE = 1;
    private static final int CLEAN_CACHE_CODE = 2;
    private PIUpdateModel mPIUpdateModel = null;

	private XjlHandler mHandler1 = new XjlHandler(
			new HandlerListener() {
				@Override
				public void handlerMessage(HandlerMessage msg) {
					dismissLoadDialog();
					switch (msg.what) {
					case Integer.MAX_VALUE:
						break;
					case LIST_DATA1:
						PIUpdateData data1 = (PIUpdateData) msg.obj;
						//Log.e("test", "update="+data1.toString());
						
						if(null != data1 && null != data1.getData()){
							mPIUpdateModel = data1.getData();
							int versioncode;
							try {
								versioncode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
								if(Integer.parseInt(mPIUpdateModel.getVersionsNum()) > versioncode)
									showDialog(UPGRADE_APK_CODE);
								else
									showToast("已经是最新版本了！");
							} catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

				}
				break;
			}
		}
	});

	protected void onResume() {
		super.onResume();
	};

	private void requestHisData() {
		if (isConnected()) {
			showLoadingDialog();
			// JSONObject object = new JSONObject();
			HashMap<String, String> map = new HashMap<String, String>();
			// try {
			// object.put("uid", "f4f8041441404e7cb1253465f37db3d4");
			// object.put("pagesize", "50");
			// object.put("pageno", "1");
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			map.put("applyName", "1426217325");
			// map.put("method", "videoformobile.getUserVideoRecordList");
			// map.put("client", "9");

			mHandler1.getHttpJson(
					mHandler1.appendParameter(
							PandaEyeAddressEnum.PE_UPDATEURL.toString(), map),
					PIUpdateData.class, LIST_DATA1);

			
			// mHandler1.getHttpPostJson(PandaEyeAddressEnum.PE_UPDATEURL.toString(),
			// map,
			// Object.class, LIST_DATA1);

		} else {
			Toast.makeText(PersonalSetActivity.this, R.string.check_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_set);

		initView();
		initCache();
	}

	private void initView() {
		titleLeft = (TextView) this.findViewById(R.id.common_title_left);
		titleCenter = (TextView) this.findViewById(R.id.common_title_center);
		aboutLayout = (RelativeLayout) this
				.findViewById(R.id.personal_set_about_layout);
		fanKuiLayout = (RelativeLayout) this
				.findViewById(R.id.personal_set_fankui_layout);
		udpateLayout = (RelativeLayout) this
				.findViewById(R.id.personal_set_udpate_layout);
		delLayout = (RelativeLayout) this
				.findViewById(R.id.personal_set_delete_cache_layout);
		pingLayout = (RelativeLayout) this
				.findViewById(R.id.personal_set_ping_layout);
		cacheTv = (TextView) this.findViewById(R.id.set_cache_size_tv);

		pushImg = (ImageView) this.findViewById(R.id.pe_set_push_view);
		playImg = (ImageView) this.findViewById(R.id.pe_set_play_view);

	
		
		
		pSp = getSharedPreferences("set", Activity.MODE_PRIVATE);
		pushSign = pSp.getString("push", "open");
		playSign = pSp.getString("play", "open");

		if ("close".equals(pushSign)) {
			pushImg.setImageResource(R.drawable.switch_close);
			PushAgent.getInstance(PersonalSetActivity.this).disable();
		} else {
			pushImg.setImageResource(R.drawable.switch_open);
			PushAgent.getInstance(PersonalSetActivity.this).enable();
		}

		if ("close".equals(playSign)) {
			playImg.setImageResource(R.drawable.switch_close);
		} else {
			playImg.setImageResource(R.drawable.switch_open);
		}

		titleCenter.setText(getString(R.string.personal_setting));
		titleLeft.setOnClickListener(new ViewClick());
		aboutLayout.setOnClickListener(new ViewClick());
		fanKuiLayout.setOnClickListener(new ViewClick());
		udpateLayout.setOnClickListener(new ViewClick());
		pushImg.setOnClickListener(new ViewClick());
		playImg.setOnClickListener(new ViewClick());
		delLayout.setOnClickListener(new ViewClick());
		pingLayout.setOnClickListener(new ViewClick());
	}

	class ViewClick implements OnClickListener {
		@Override
		public void onClick(final View view) {
			switch (view.getId()) {
			case R.id.personal_set_ping_layout:
				// Uri uri = Uri.parse("market://details?id=cn.cntv");
				Uri uri = Uri.parse("market://details?id=cn.cntv.app.ipanda");
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(goToMarket);
				} catch (Exception e) {
					Toast.makeText(PersonalSetActivity.this,R.string.no_find_store,
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.personal_set_delete_cache_layout:

				// showDialog();
				showDialog(CLEAN_CACHE_CODE);

				break;
			case R.id.pe_set_push_view:
				pSp = getSharedPreferences("set", Activity.MODE_PRIVATE);
				pushSign = pSp.getString("push", "open");
				
				if ("close".equals(pushSign)) {
//					pushImg.setImageResource(R.drawable.switch_open);
//					pushSign = "open";
//					PushAgent.getInstance(PersonalSetActivity.this).enable();
					
//					showDialog(-1);
					pushImg.setImageResource(R.drawable.switch_open);
					pushSign = "open";
					PushAgent.getInstance(PersonalSetActivity.this).enable();
					Editor pued = pSp.edit();
					pued.putString("push", pushSign);
					pued.commit();
					
				} else {
					pushImg.setImageResource(R.drawable.switch_close);
					pushSign = "close";
					PushAgent.getInstance(PersonalSetActivity.this).disable();
					Editor pued = pSp.edit();
					pued.putString("push", pushSign);
					pued.commit();
				}
				break;
			case R.id.pe_set_play_view:
				if ("close".equals(playSign)) {
					playImg.setImageResource(R.drawable.switch_open);
					playSign = "open";
				} else {
					playImg.setImageResource(R.drawable.switch_close);
					playSign = "close";
				}

				Editor played = pSp.edit();
				played.putString("play", playSign);
				played.commit();
				break;
			case R.id.personal_set_udpate_layout:
				requestHisData();
				break;
			case R.id.personal_set_fankui_layout:
				Intent fankuiIntent = new Intent(PersonalSetActivity.this,
						PersonalFeedBackActivity.class);
				startActivity(fankuiIntent);
				break;
			case R.id.personal_set_about_layout:
				Intent aboutIntent = new Intent(PersonalSetActivity.this,
						PersonalAboutUsActivity.class);
				startActivity(aboutIntent);
				break;
			case R.id.common_title_left:
				finish();
				break;
			}
		}

	}

	protected Dialog onCreateDialog(int id) {
		View viewDialog = (View) LayoutInflater.from(this).inflate(
				R.layout.delete_dialog, null);
		final Dialog dialog = new Dialog(PersonalSetActivity.this,
				R.style.loginDialogTheme);
		dialog.setCancelable(false);
		TextView dialogCancel, dialogRightsure, dialog_title,dialog_conTextView;
		dialogRightsure = (TextView) viewDialog
				.findViewById(R.id.login_right_sure);
		dialogCancel = (TextView) viewDialog
				.findViewById(R.id.login_cancel_but);
		dialog_title = (TextView) viewDialog.findViewById(R.id.del_title_tv);
		dialog_conTextView = (TextView) viewDialog.findViewById(R.id.del_content_tv);		
		dialog.setContentView(viewDialog);
		dialogCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		switch (id) {
		case UPGRADE_APK_CODE: {
			dialog_title.setText(getString(R.string.set_check_newversion));
			dialogRightsure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View arg0) {
					dialog.dismiss();
					new UpgradeTask(PersonalSetActivity.this, mPIUpdateModel)
							.execute();
				}
			});

			return dialog;
		}
		case CLEAN_CACHE_CODE: {

			dialogRightsure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View arg0) {
					DataCleanManager.deleteDirectory(getCacheDir());
					DataCleanManager.deleteDirectory(getFilesDir());
					DataCleanManager.deleteDirectory(new File(AppConfig.APP_PATH));
					initCache();
					dialog.dismiss();
				}
			});
			dialog_title.setText(getString(R.string.set_confirm_clear_cache));
			dialogRightsure.setText(getString(R.string.sure));
			dialogCancel.setText(getString(R.string.cancel));

			return dialog;
		}
		default: {
			dialog_title.setText(getString(R.string.set_push_tips));
			dialog_conTextView.setText(getString(R.string.set_is_open_push));
			dialog_conTextView.setVisibility(View.VISIBLE);
			dialogRightsure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pushImg.setImageResource(R.drawable.switch_open);
					pushSign = "open";
					PushAgent.getInstance(PersonalSetActivity.this).enable();
					Editor pued = pSp.edit();
					pued.putString("push", pushSign);
					pued.commit();
					dialog.dismiss();
				}
			});
			return dialog;
		}
		}
	}

	void initCache() {
		try {
			long innerCacheSize = 0, innerFileSize = 0;
			if (getCacheDir().exists()) {
				innerCacheSize = GetFileSizeUtil.getInstance().getFileSize(
						getCacheDir());
			}
			if (getFilesDir().exists()) {
				innerFileSize = GetFileSizeUtil.getInstance().getFileSize(
						getFilesDir());
			}
			// SD card
			File appFile = new File(AppConfig.APP_PATH);
			long outerDirSize = 0;
			if (appFile.exists()) {
				outerDirSize = GetFileSizeUtil.getInstance().getFileSize(
						appFile);
			}
			String strInnerSize = GetFileSizeUtil.getInstance().FormetFileSize(
					innerCacheSize + innerFileSize + outerDirSize);
			if (StringUtil.isNullOrEmpty(strInnerSize)) {
				strInnerSize = "0KB";
			}
			cacheTv.setText(strInnerSize);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
