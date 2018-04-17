package cn.cntv.app.ipanda.ui.personal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.util.Date;

import cn.cntv.app.ipanda.AppConfig;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.api.PandaApi;
import cn.cntv.app.ipanda.bean.Entity;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.Callback;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.dialog.ChoosePictureDialog;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseActivity;

public class PersonalLoginOutActivity extends BaseActivity implements OnClickListener {
    private TextView titleLeft, titleCenter;

    private TextView nickNameTxt;
    private TextView loginOutButton;
    private RelativeLayout loginOutLayout;
    private PopupWindow mTipPw;
    private String mNickName;
    private ImageView mHeadImage;
    private static final String FOR_LOGIN_IN = "FOR_LOGIN_IN";
    private String mFilename;
    private Boolean isClickLoginOut;
    //裁剪后图片的宽高
    private static int output_X = 300;
    private static int output_Y = 300;

    private Bitmap mHeadBitmap;
    private UserManager mUserManager = UserManager.getInstance();
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0;
    private static final int CODE_CAMERA_REQUEST = 1;
    private static final int CODE_RESULT_REQUEST = 2;
    private static final int CODE_CLIP_REQUEST = 3;

    private String mNickname, mUserface;//用户昵称和头像

    private int flag;//打开相机和相册的标记 1相机、2相册3.登录过期的提示框
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_loginout);
        Intent intent = getIntent();
        mNickname = intent.getStringExtra("mNickname");//获取个人中心的昵称和头像
        mUserface = intent.getStringExtra("mUserface");
        initView();
    }

    private void initView() {
        RelativeLayout mHeadPhoto = (RelativeLayout) findViewById(R.id.person_have_login_layout);
        titleLeft = (TextView) this.findViewById(R.id.common_title_left);
        titleCenter = (TextView) this.findViewById(R.id.common_title_center);
        mHeadImage = (ImageView) this.findViewById(R.id.iv_headicon);
        Glide.with(this)
                .load(mUserface)
                .asBitmap()
                .placeholder(R.drawable.personal_login_head)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new BitmapImageViewTarget(mHeadImage){
                    @Override
                    protected void setResource(Bitmap resource) {

                        mHeadImage.setImageBitmap(resource);
                    }
                });

        titleCenter.setText(getString(R.string.personal_info));

        titleLeft.setOnClickListener(this);

        nickNameTxt = (TextView) findViewById(R.id.nick_name);
        loginOutButton = (TextView) findViewById(R.id.btn_login_out);

        loginOutLayout = (RelativeLayout) findViewById(R.id.login_out_layout);
        RelativeLayout tnicknamejiantou = (RelativeLayout) findViewById(R.id.personal_nickname_layout);

        loginOutButton.setOnClickListener(this);
        tnicknamejiantou.setOnClickListener(this);
        mHeadPhoto.setOnClickListener(this);

        String nickName = mUserManager.getNickName();

        if (nickName != null) {
            mNickName = nickName;
            nickNameTxt.setText(mNickname);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;

            case R.id.btn_login_out:

                mUserManager.clearUser();

                finish();
                break;

            case R.id.person_have_login_layout:
                showTipPop();
                break;

            case R.id.personal_nickname_layout:
                Intent tintent = new Intent(this, PersonalNickNameActivity.class);
                tintent.putExtra("nickname", mNickname);
                startActivity(tintent);
                break;
        }
    }

    /**
     * 显示选择照片提示框
     */
    public void showTipPop() {

        ChoosePictureDialog dialog = new ChoosePictureDialog(this, new ChoosePictureDialog.Listener() {
            @Override
            public void choosePic() {
                //从手机相册选择
                choseHeadImageFromGallery();
            }

            @Override
            public void chooseCamera() {
                //拍一张
                choseHeadImageFromCameraCapture();
            }
        });

        dialog.show();

    }


    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
        // 设置文件类型
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
//        finish();
    }


    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        mFilename = AppConfig.DEFAULT_IMAGE_PATH + android.text.format.DateFormat
                .format("yyyyMMddkkmmss",
                        new Date()).toString() + ".jpg";

        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri turi = Uri.fromFile(new File(mFilename));
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, turi);
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
//        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
//	if(intent ==null){
//		return;
//	}
        if (resultCode == 123) {
            showLoadingDialog();
            String filepath = intent.getStringExtra("file");
            //上传头像
            uploadAvatar(filepath);
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                //cropRawPhoto(intent.getData());
                Intent clipintent = new Intent(this, PersonalClipPictureActivity.class);
                if (resultCode == Activity.RESULT_OK) {
                    Uri data = intent.getData();
                    clipintent.putExtra("uri", data);
                    startActivityForResult(clipintent, CODE_CLIP_REQUEST);
                }
                break;
            case CODE_CAMERA_REQUEST:
                //cropRawPhoto(intent.getData());
                if (resultCode == Activity.RESULT_OK) {
                    Intent clipcameraintent = new Intent(this, PersonalClipPictureActivity.class);
                    Uri data2 = Uri.fromFile(new File(mFilename));
                    clipcameraintent.putExtra("uri", data2);
                    startActivityForResult(clipcameraintent, CODE_CLIP_REQUEST);
                }
                break;
            case CODE_RESULT_REQUEST:
//					setImageToHeadView(intent);
                break;
        }

    }

    private void uploadAvatar(String path) {
        userId = mUserManager.getUserId();
        PandaApi.uploadProfile(userId, new File(path)).enqueue(new Callback<Entity>() {
            @Override
            public void onResponse(Call<Entity> call, Response<Entity> response) {
                dismissLoadDialog();
                Entity result = response.body();
                if (result.code == Constants.CODE_SUCCEED) {
                    changePhotoShowTipPop(findViewById(R.id.common_title_right), "您的头像已提交审核\n\n请稍后回来确认吧");

                } else if (result.code == Constants.CODE_USER_NOT_LOGGED && userId != null) {//返回码为-100登录失效
                    LonginShowTipPop(findViewById(R.id.common_title_right), R.string.login_cookies);

                } else {
                    changePhotoShowTipPop(findViewById(R.id.common_title_right), result.error);
                }
            }

            @Override
            public void onFailure(Call<Entity> call, Throwable t) {
                Toast.makeText(PersonalLoginOutActivity.this, getString(R.string.request_faile_tips), Toast.LENGTH_SHORT).show();
                dismissLoadDialog();

            }
        });
    }


    /*登录失效提示*/
    public void LonginShowTipPop(final View parent, int strId) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.player_tip, null, false);
        // 提示信息
        final TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip);
        tipTv.setText(strId);
        // 创建弹出对话框，设置弹出对话框的背景为圆角
        mTipPw = new PopupWindow(dialogView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        // 响应返回键
        mTipPw.setFocusable(true);
        // Cancel按钮及其处理事件
        final TextView btnknow = (TextView) dialogView.findViewById(R.id.btn_cancel);

        btnknow.setTextColor(Color.parseColor("#1f539e"));
        btnknow.setText(getString(R.string.cancel));
        btnknow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mTipPw != null && mTipPw.isShowing()) {
                    mTipPw.dismiss();// 关闭
                }
            }
        });
        final TextView btnOk = (TextView) dialogView.findViewById(R.id.btn_ok);
        btnOk.setTextColor(Color.parseColor("#1f539e"));
        btnOk.setText(getString(R.string.sure));
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTipPw != null && mTipPw.isShowing()) {
                    mTipPw.dismiss();// 关闭
                    mUserManager.saveUserId(null);
                    Intent intent = new Intent(PersonalLoginOutActivity.this, PersonalActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
        // 显示RoundCorner对话框
        mTipPw.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);

    }


    public void changePhotoShowTipPop(final View parent, String strId) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.player_tip, null, false);
        // 提示信息
        final TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip);
        tipTv.setText(strId);
        // 创建弹出对话框，设置弹出对话框的背景为圆角
        mTipPw = new PopupWindow(dialogView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        // 响应返回键
        mTipPw.setFocusable(true);
        // Cancel按钮及其处理事件
        final TextView btnknow = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnknow.setTextColor(Color.parseColor("#1f539e"));
        btnknow.setText(this.getResources().getString(R.string.change_nickname_sure));
        btnknow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mTipPw != null && mTipPw.isShowing()) {
                    mTipPw.dismiss();// 关闭
                }


//                PersonalLoginOutActivity.this.finish();
            }
        });
        final TextView btnOk = (TextView) dialogView.findViewById(R.id.btn_ok);
        LinearLayout tipsLine = (LinearLayout) dialogView.findViewById(R.id.ll_tips_line);

        tipsLine.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
        // 显示RoundCorner对话框
        mTipPw.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);

    }

}
