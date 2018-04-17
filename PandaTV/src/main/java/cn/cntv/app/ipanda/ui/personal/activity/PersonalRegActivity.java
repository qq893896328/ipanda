package cn.cntv.app.ipanda.ui.personal.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.personal.fragment.EmailRegFragment;
import cn.cntv.app.ipanda.ui.personal.fragment.PhoneRegFragment;

public class PersonalRegActivity extends BaseActivity implements OnClickListener{

	private TextView mTvPhonereg,mTvEmailreg;//手机 邮箱注册
	private TextView mTvPhoneLine,mTvEmailLine;//底部导航线

	private FragmentManager mFragManager;

	private TextView titleLeft, titleCenter,titleRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_reg);
		init();

	}
	private void init(){

		mTvPhonereg = (TextView) findViewById(R.id.tvphonereg);
		mTvEmailreg = (TextView) findViewById(R.id.tvemailreg);
		mTvPhoneLine = (TextView) findViewById(R.id.tvphonereg_line);
		mTvEmailLine = (TextView) findViewById(R.id.tvemailreg_line);

		titleLeft = (TextView) this.findViewById(R.id.common_title_left);
		titleCenter = (TextView) this.findViewById(R.id.common_title_center);
		titleRight = (TextView) this.findViewById(R.id.common_title_right);
		titleCenter.setText(getString(R.string.login_regist));

		PhoneRegFragment tphoneFrag = new PhoneRegFragment();
		mFragManager = getSupportFragmentManager();
		mFragManager.beginTransaction().replace(R.id.framelayout_register_content, tphoneFrag).commit();

		mTvEmailreg.setOnClickListener(this);
		mTvPhonereg.setOnClickListener(this);
		titleLeft.setOnClickListener(new OnClickListener() {

			@Override
 			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				if (imm.isActive(getCurrentFocus())) {

					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				}

				PersonalRegActivity.this.finish();


			}
		});

		mTvPhoneLine.setVisibility(View.VISIBLE);
		mTvEmailLine.setVisibility(View.GONE);
		mTvPhoneLine.setTextColor(Color.parseColor("#1f539e"));
		mTvEmailLine.setTextColor(Color.parseColor("#ffffff"));
		mTvPhonereg.setTextColor(Color.parseColor("#1f539e"));
		mTvEmailreg.setTextColor(Color.parseColor("#000000"));

	}
	@Override
	public void onClick(View v) {

		Fragment tFragment = null;

		switch (v.getId()) {
			case R.id.tvphonereg:

				mTvPhoneLine.setVisibility(View.VISIBLE);
				mTvEmailLine.setVisibility(View.GONE);
				mTvPhoneLine.setTextColor(Color.parseColor("#1f539e"));
				mTvEmailLine.setTextColor(Color.parseColor("#ffffff"));
				mTvPhonereg.setTextColor(Color.parseColor("#1f539e"));
				mTvEmailreg.setTextColor(Color.parseColor("#000000"));

				tFragment = new PhoneRegFragment();
				mFragManager.beginTransaction().replace(R.id.framelayout_register_content, tFragment).commit();
				break;
			case R.id.tvemailreg:

				mTvPhoneLine.setVisibility(View.GONE);
				mTvEmailLine.setVisibility(View.VISIBLE);
				mTvPhoneLine.setTextColor(Color.parseColor("#ffffff"));
				mTvEmailLine.setTextColor(Color.parseColor("#1f539e"));
				mTvPhonereg.setTextColor(Color.parseColor("#000000"));
				mTvEmailreg.setTextColor(Color.parseColor("#1f539e"));

				tFragment = new EmailRegFragment();
				mFragManager.beginTransaction().replace(R.id.framelayout_register_content, tFragment).commit();
				break;


		}

	}
}
