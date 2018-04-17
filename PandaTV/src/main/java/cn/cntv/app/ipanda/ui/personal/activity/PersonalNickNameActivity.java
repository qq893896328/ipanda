package cn.cntv.app.ipanda.ui.personal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.api.PandaApi;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.Callback;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.personal.entity.NickName;

public class PersonalNickNameActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextView mLeftTitle, mRigthTitle, mCenterTitle;
    private TextView mTvNickName;
    private UserManager mUserManager;
    private PopupWindow mTipPw;
    private String mOldName, mNewName;

    private int flag; //标识修改头像成功的提示框的显示样式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_change_nickname);
        init();
    }

    private void init() {

        mUserManager = UserManager.getInstance();
        mLeftTitle = (TextView) findViewById(R.id.common_title_left);
        mCenterTitle = (TextView) findViewById(R.id.common_title_center);
        mRigthTitle = (TextView) findViewById(R.id.common_title_right);
        mTvNickName = (TextView) findViewById(R.id.edit_nickmane);
        mLeftTitle.setOnClickListener(this);

        mCenterTitle.setText(getString(R.string.change_nickname));
        mRigthTitle.setTextColor(Color.parseColor("#7c7c7c"));
        mRigthTitle.setText(getString(R.string.save));
        mTvNickName.addTextChangedListener(this);

        setNickName();

    }

    /**
     * 设置要修改的昵称 并且初始化光标位置
     */
    private void setNickName() {

        mOldName = getIntent().getStringExtra("nickname");
        mTvNickName.setText(mOldName);
        CharSequence text = mTvNickName.getText();

        if (text instanceof Spannable) {
            Spannable text1 = (Spannable) text;
            Selection.setSelection(text1, text.length());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.common_title_left:
                hideKeyBoard();
                finish();
                break;

            case R.id.common_title_right:

                showLoadingDialog();
                changeNickName();

                break;

        }
    }


    /**
     * 修改用户昵称
     */
    private void changeNickName() {
        final String userId = mUserManager.getUserId();

        Call<NickName> call = PandaApi.alterNickName(userId,  mNewName);
        call.enqueue(new Callback<NickName>() {
            @Override
            public void onResponse(Call<NickName> call, Response<NickName> response) {
                NickName nickName = response.body();
                int code = nickName.getCode();
                if (code == Constants.CODE_SUCCEED) {
                    flag = 1;//上传成功 显示“我知道啦”提示框
                    showTipPop(findViewById(R.id.common_title_right), R.string.change_nickname_tips
                            , R.string.change_nickname_sure, R.string.tip_null);
                } else {
                    //根据返回errcode 判断cookie失效 提示用户重新登录
                    if (code == Constants.CODE_USER_NOT_LOGGED && userId != null) {
                        showTipPop(findViewById(R.id.common_title_right), R.string.login_faile, R.string.sure,
                                R.string.cancel);

                    } else {
                        //提示服务器返回的错误信息
                        Toast toast = Toast.makeText(PersonalNickNameActivity.this, nickName.getError(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
                dismissLoadDialog();

            }

            @Override
            public void onFailure(Call<NickName> call, Throwable t) {
                dismissLoadDialog();

            }

        });

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        mNewName = mTvNickName.getText().toString().trim();

        if (mTvNickName != null && !mNewName.equals(mOldName)) {
            mRigthTitle.setTextColor(Color.WHITE);
            mRigthTitle.setOnClickListener(this);
        }

    }


    /**
     * 显示提示框
     *
     * @param parent
     * @param strcontentId 提示框的内容文本
     * @param strokbtn  提示框的确定按钮文本
     * @param strcancelbtn 提示框的取消按钮文本
     */
    public void showTipPop(final View parent, int strcontentId, int strokbtn, int strcancelbtn) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.player_tip, null, false);
        // 提示信息
        final TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip);
        tipTv.setText(strcontentId);
        // 创建弹出对话框，设置弹出对话框的背景为圆角
        mTipPw = new PopupWindow(dialogView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        // 响应返回键
        mTipPw.setFocusable(true);
        // Cancel按钮及其处理事件
        final TextView okbtn = (TextView) dialogView.findViewById(R.id.btn_ok);

        okbtn.setTextColor(Color.parseColor("#1f539e"));
        okbtn.setText(strokbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mTipPw != null && mTipPw.isShowing()) {
                    mTipPw.dismiss();// 关闭
                }
                if (flag == 1) {//上传成功 “我知道啦” 提示框

                    PersonalNickNameActivity.this.finish();
                } else {
                    //用户登录过期 “确定” 提示框

                    mUserManager.saveNickName(null);
                    mUserManager.saveUserId(null);
                    startActivity(new Intent(PersonalNickNameActivity.this, PersonalActivity.class));
                    finish();

                }

            }
        });

        final TextView cancelbtn = (TextView) dialogView.findViewById(R.id.btn_cancel);
            LinearLayout tipsLine = (LinearLayout) dialogView.findViewById(R.id.ll_tips_line);

        if (flag == 1) {
                //上传成功 提示框 隐藏 “取消”按钮
            cancelbtn.setVisibility(View.GONE);
            tipsLine.setVisibility(View.GONE);
            cancelbtn.setText(strcancelbtn);

        } else {
            //登录过期  “取消” 按钮
            cancelbtn.setVisibility(View.VISIBLE);
            cancelbtn.setText(strcancelbtn);
            cancelbtn.setTextColor(Color.parseColor("#1f539e"));
            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mTipPw != null && mTipPw.isShowing()) {
                        mTipPw.dismiss();// 关闭
                    }
                    PersonalNickNameActivity.this.finish();

                }
            });
        }

        // 显示RoundCorner对话框
        mTipPw.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);
    }


    private void hideKeyBoard() {

        View tView = getWindow().peekDecorView();

        if (tView != null) {
            // 获取输入法接口
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // 强制隐藏键盘
            imm.hideSoftInputFromWindow(tView.getWindowToken(), 0);
        }
    }

}
