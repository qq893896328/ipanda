package cn.cntv.app.ipanda.ui.personal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.api.PandaApi;
import cn.cntv.app.ipanda.bean.UserBean;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.Callback;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.utils.CacheUtil;

public class PersonalActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout person_no_login_layout;
    private RelativeLayout person_have_login_layout;
    private TextView person_nickname;
    private ImageView mPhoto;
    private String mNickname, mUserface;
    private UserManager mUserManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mUserManager.isUserLogged()) {

            person_have_login_layout.setVisibility(View.VISIBLE);
            person_no_login_layout.setVisibility(View.GONE);

            mNickname = mUserManager.getNickName();
            mUserface = mUserManager.getUserFace();

            displayUserInfo(mNickname, mUserface);

            if (mUserManager.isUserInfoRetrieved()) {
                return;
            }

            Call<UserBean> call = PandaApi.getNickNameAndFace(mUserManager.getUserId());
            call.enqueue(new Callback<UserBean>() {
                @Override
                public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                    UserBean bean =  response.body();

                    if (bean.code == Constants.CODE_SUCCEED) {
                        mNickname = bean.content.getNickname();
                        mUserface = bean.content.getUserface();

                        mUserManager.saveNickName(mNickname);
                        mUserManager.saveUserFace(mUserface);
                        mUserManager.setUserInfoRetrieved(true);

                        displayUserInfo(mNickname, mUserface);
                    }
                }

                @Override
                public void onFailure(Call<UserBean> call, Throwable t) {

                }

            });

        } else {
            person_have_login_layout.setVisibility(View.GONE);
            person_no_login_layout.setVisibility(View.VISIBLE);
        }
    }

    private void displayUserInfo(String nickName, String userface) {
        person_nickname.setText(nickName);

        Glide.with(this)
                .load(userface)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.personal_login_head)
                .into(new BitmapImageViewTarget(mPhoto){
                    @Override
                    protected void setResource(Bitmap resource) {
                      mPhoto.setImageBitmap(resource);
                    }
                });
    }

    private void initView() {
        mUserManager = UserManager.getInstance();
        mPhoto = (ImageView) this.findViewById(R.id.nicknm_img);

        TextView titleLeft = (TextView) this.findViewById(R.id.common_title_left);
        TextView titleCenter = (TextView) this.findViewById(R.id.common_title_center);
        RelativeLayout setLayout = (RelativeLayout) this
                .findViewById(R.id.personal_set_layout);
        RelativeLayout historyLayout = (RelativeLayout) this
                .findViewById(R.id.personal_history_layout);
        RelativeLayout shoucangLayout = (RelativeLayout) this
                .findViewById(R.id.personal_shoucang_layout);
        person_no_login_layout = (RelativeLayout) this
                .findViewById(R.id.person_no_login_layout);
        RelativeLayout callbackLayout = (RelativeLayout) this
                .findViewById(R.id.personal_callback_layout);
        titleCenter.setText(R.string.personal_page);

        titleLeft.setOnClickListener(this);
        setLayout.setOnClickListener(this);
        historyLayout.setOnClickListener(this);
        shoucangLayout.setOnClickListener(this);
        person_no_login_layout.setOnClickListener(this);
        callbackLayout.setOnClickListener(this);
        person_have_login_layout = (RelativeLayout) findViewById(R.id.person_have_login_layout);
        person_nickname = (TextView) findViewById(R.id.person_nickname);

        person_have_login_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.person_no_login_layout:
                Intent loginIntent = new Intent(PersonalActivity.this,
                        PersonalLoginActivity.class);
                startActivity(loginIntent);
//                finish();
                break;
            case R.id.personal_shoucang_layout:
                Intent shoucangIntent = new Intent(PersonalActivity.this,
                        PersonalShouCangActivity.class);
                startActivityForResult(shoucangIntent, 1001);
                break;
            case R.id.personal_history_layout:
                Intent historyIntent = new Intent(PersonalActivity.this,
                        PersonalHistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.personal_callback_layout:
                Intent inquirebackIntent = new Intent(PersonalActivity.this,
                        PersonalInquireActivity.class);
                startActivity(inquirebackIntent);
                break;

            case R.id.person_have_login_layout:
                Intent loginIntenthavalogin = new Intent(PersonalActivity.this,
                        PersonalLoginOutActivity.class);
                loginIntenthavalogin.putExtra("mNickname", mNickname);
                loginIntenthavalogin.putExtra("mUserface", mUserface);
                startActivity(loginIntenthavalogin);
                break;
            case R.id.common_title_left:
                finish();
                break;
            case R.id.personal_set_layout:
                Intent setIntent = new Intent(PersonalActivity.this,
                        PersonalSetActivity.class);
                startActivity(setIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        person_no_login_layout = null;
        person_have_login_layout = null;
        person_nickname = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1001 && resultCode == 1001) {
            int flag = data.getIntExtra("flag", -1);
            if (flag == 1 && CacheUtil.mRbLovePanda != null) {
                // 来源于熊猫直播的收藏，点击熊猫直播中后跳转到“熊猫直播直播频道页3.3.1”
                CacheUtil.mRbLovePanda.performClick();
                finish();
            } else if (flag == 2 && CacheUtil.mRbLiveChina != null) {
                // 来源于直播中国的收藏，点击熊猫直播中后跳转到“熊猫直播直播频道页3.3.1”
                CacheUtil.mRbLiveChina.performClick();
                finish();
            }
        }
    }



  
}
