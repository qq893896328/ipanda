package cn.cntv.app.ipanda.ui.personal.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;
import com.umeng.socialize.utils.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalActivity;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalAgreePostActivity;
import cn.cntv.app.ipanda.utils.ToastUtil;

public class PhoneRegFragment extends BaseFragment implements View.OnFocusChangeListener {

    private TextView xieyi;
    public String JSESSIONID = null;
    public String verifycode = null;
    public String uct = null;
    private byte[] mCaptchaBytes;
    //private String erroType = "网络异常";
    private String erroType = "移动网络环境下无法进行注册,请开启WIFI后进行注册";
    private TextView mCaptchaImageView;

    private String mUserSeqId;
    private String mUserId;
    private String mNickName;
    //获取验证码图片成功、失败
    private static final int IMG_GET_SUCCES = 101;
    private static final int IMG_GET_ERROR = 102;

    //获取手机验证码成功、失败
    private static final int MSG_GETTING_SUCCESS = 103;
    private static final int MSG_GETTING_ERROR = 104;
    //手机号注册成功、失败
    private static final int MSG_LOGIN_SUCCESS = 105;
    private static final int MSG_LOGIN_ERROR = 106;
    //获取用户ID、昵称
    private static final int MSG_LGOIN_IN_GET_NICKNAME = 107;
    private static final int MSG_LOGIN_IN_ERROR = 108;
    private static final int MSG_GET_NICKNAME_SUCCESS = 109;

    private TextView hint_phone;
    private TextView hint_imagecheck;
    private TextView hint_phonecheck;
    private TextView hint_surepawd;
    private TextView hint_xieyi;
    private TextView btn_register;
    private TextView personal_reg_phonecheck;

    private EditText edit_phone;
    private EditText edit_imgyanzhengma;
    private EditText edit_phoneyanzhengma;
    private EditText edit_password;

    private TimeCount mTime;
    private LinearLayout email;

    private TextView phone;
    //输入框输入的验证码
    private String mCaptchaEditTextString;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_phone_register, container, false);
        initView();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        sendCaptchaHttpMessage();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IMG_GET_SUCCES:
                    dismissLoadDialog();
                    Drawable captchaImage = byteToDrawable(mCaptchaBytes);
                    mCaptchaImageView.setBackgroundDrawable(captchaImage);
                    mCaptchaImageView.setText("");
                    break;
                case IMG_GET_ERROR:
                    dismissLoadDialog();
                    mCaptchaImageView.setText("图形验证码");
                    sendCaptchaHttpMessage();
                    mCaptchaImageView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    if (msg.obj != null) {
                        if (msg.obj instanceof String) {
                            showTishiDialog((String) msg.obj);
                        }
                    }

                    break;
                case MSG_LOGIN_SUCCESS:
                    dismissLoadDialog();
                    if (msg.obj != null) {

                        Toast toast = Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        MobileAppTracker.trackEvent("注册", null, "个人中心", 0, null, null, getActivity());
                        goLogin();
                    }
                    break;
                case MSG_LOGIN_ERROR:
                    dismissLoadDialog();
                    if (msg.obj != null)

                        ToastUtil.showShort(getActivity(), (String) msg.obj);
                    break;
                case MSG_GETTING_SUCCESS:
                    mTime.start();
                    dismissLoadDialog();
                    if (msg.obj != null)

                        ToastUtil.showShort(getActivity(), (String) msg.obj);
                    break;
                case MSG_GETTING_ERROR:
                    dismissLoadDialog();
                    if (msg.obj != null)

                        ToastUtil.showShort(getActivity(), (String) msg.obj);
                    break;
                case MSG_LGOIN_IN_GET_NICKNAME:
                    getUserTicket();
                    break;
                case MSG_LOGIN_IN_ERROR:
                    dismissLoadDialog();
                    if (msg.obj != null) {
                        if (msg.obj instanceof String) {
                            String err = (String) msg.obj;
                            // Toast.makeText(PersonalLoginActivity.this,err,
                            /// 0).show();
                            showTishiDialog(err);
                        }
                    }
                    break;
                case MSG_GET_NICKNAME_SUCCESS:
                    dismissLoadDialog();
                    UserManager.getInstance().saveNickName(mNickName);
                    UserManager.getInstance().saveUserId(mUserSeqId);

                    AppContext.getInstance().getSpUtil()
                            .setIsRegistSuccess(true);
                    Intent intent = new Intent(getActivity(),PersonalActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
            }
        }
    };


    private void showTishiDialog(String tishiString) {
        try {
            if (getActivity().isFinishing()) {
                return;
            }

            View tview = View.inflate(
                    getActivity(),
                    R.layout.person_register_tishi_dialog, null);
            TextView tishiView = (TextView) tview.findViewById(R.id.register_tishi_txt);
            tishiView.setText(tishiString);
            TextView tishiSure = (TextView) tview.findViewById(R.id.register_tishi_btn_sure);
            final Dialog dialog = new Dialog(getActivity(), R.style.dialog);

            dialog.setContentView(tview);
            dialog.setCanceledOnTouchOutside(true);
            tishiSure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {

        }
    }

    private void initView() {

        xieyi = (TextView) view.findViewById(R.id.personal_reg_xieyi_view);
        xieyi.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        //xieyi.setText(Html.fromHtml("我已阅读并同意《"+"<a href=\"# \">央视网网络服务使用协议</a>"+"》"));

        xieyi.setOnClickListener(new ViewClick());

        mCaptchaImageView = (TextView) view.findViewById(R.id.personal_reg_imgcheck);
        mCaptchaImageView.setOnClickListener(new ViewClick());


        hint_phone = (TextView) view.findViewById(R.id.hint_phone);
        hint_imagecheck = (TextView) view.findViewById(R.id.hint_imagecheck);
        hint_phonecheck = (TextView) view.findViewById(R.id.hint_phonecheck);
        hint_surepawd = (TextView) view.findViewById(R.id.hint_password);
        hint_xieyi = (TextView) view.findViewById(R.id.hint_xieyi);
        personal_reg_phonecheck = (TextView) view.findViewById(R.id.personal_reg_phonecheck);

        btn_register = (TextView) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new ViewClick());
        personal_reg_phonecheck.setOnClickListener(new ViewClick());
        mTime = new TimeCount(180000, 1000);//构造CountDownTimer对象

        edit_phone = (EditText) view.findViewById(R.id.edit_phone);
        edit_imgyanzhengma = (EditText) view.findViewById(R.id.edit_imgyanzhengma);
        edit_phoneyanzhengma = (EditText) view.findViewById(R.id.edit_phoneyanzhengma);
        edit_password = (EditText) view.findViewById(R.id.edit_inputpasswrod);

        edit_phone.setOnFocusChangeListener(this);
        edit_imgyanzhengma.setOnFocusChangeListener(this);
        edit_phoneyanzhengma.setOnFocusChangeListener(this);
        edit_password.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edit_phone:
                    hint_phone.setText("");
                    break;
                case R.id.edit_imgyanzhengma:
                    if (!checkPhone())
                        return;
                    hint_imagecheck.setText("");
                    break;
                case R.id.edit_phoneyanzhengma:
                    if (!checkCaptcha())
                        return;
                    hint_phonecheck.setText("");
                    break;
                case R.id.edit_inputpasswrod:
                    if (!checkPhoneCheck())
                        return;
                    hint_surepawd.setText("");
                    break;
            }
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            personal_reg_phonecheck.setText("获取验证码");
            personal_reg_phonecheck.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            personal_reg_phonecheck.setClickable(false);
            personal_reg_phonecheck.setText("重新获取"+"("+millisUntilFinished / 1000 +")" );
        }
    }

    class ViewClick implements OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.personal_reg_xieyi_view:
                    Intent xieIntent = new Intent(getActivity(), PersonalAgreePostActivity.class);
                    startActivity(xieIntent);
                    break;

                case R.id.personal_reg_imgcheck:
                    if (!isConnected()) {
                        showToast(R.string.network_invalid);
                        return;
                    }

                    //获取验证码
                    sendCaptchaHttpMessage();
                    break;
                case R.id.personal_reg_phonecheck:
                    edit_phone.clearFocus();
                    edit_imgyanzhengma.clearFocus();
                    edit_phoneyanzhengma.clearFocus();

                    String phone = edit_phone.getText().toString().trim();
                    String tImageChcek = edit_imgyanzhengma.getText().toString().trim();

                    if (TextUtils.isEmpty(tImageChcek)) {
                        hint_imagecheck.setText("图片验证码输入有误");
                    }
                    if(!checkPhone()){
                        return;
                    }
                    if(!checkCaptcha()){
                        return;
                    }else {

                        hint_phone.setText("");
                        hint_imagecheck.setText("");

                        sendCaptchaSmsHttpMessage();
                    }

                    break;

                case R.id.btn_register:
                    edit_phone.clearFocus();
                    edit_imgyanzhengma.clearFocus();
                    edit_phoneyanzhengma.clearFocus();
                    edit_password.clearFocus();
                    if (!isConnected()) {
                        showToast(R.string.network_invalid);
                        return;
                    }

                    //点击注册
                    if (!checkPhone()) {
                        return;
                    }


                    if (!checkCaptcha()) {
                        return;
                    }

                    if (!checkPhoneCheck()) {

                        return;
                    }
                    if (!checkPasswork()) {
                        return;
                    }


                    if (!checkXieyi()) {
                        return;
                    }

                    sendPhoneRegistHttp();
                    break;

            }
        }

    }

    //检查手机号
    private boolean checkPhone() {
        String phoneString = edit_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneString)) {
            hint_phone.setText("手机号码不能为空");
            return false;
        }
        Pattern pattern = Pattern.compile("^1[3578]\\d{9}$");
        Matcher matcher = pattern.matcher(phoneString);
        if (matcher.matches()) {
            hint_phone.setText("");
            return true;
        } else {
            hint_phone.setText("手机格式不正确");
            return false;
        }
    }

    /**
     * 检查密码
     *
     * @return
     */
    private boolean checkPasswork() {
        String editpasswordsString = edit_password.getText().toString();

        if (TextUtils.isEmpty(editpasswordsString)) {
            hint_surepawd.setText("密码不能为空");
            return false;
        } else if (editpasswordsString.length() < 6 || editpasswordsString.length() > 16) {
            hint_surepawd.setText("密码仅限6-16个字符");
            return false;
        } else {
            hint_surepawd.setText("");
            return true;
        }
    }

    /**
     * 检查手机验证码
     */

    private boolean checkPhoneCheck() {
        String phonecheck = edit_phoneyanzhengma.getText().toString().trim();

        if (TextUtils.isEmpty(phonecheck)) {
            hint_phonecheck.setText("验证码不能为空");
            return false;
        } else {
            hint_phonecheck.setText(" ");
            return true;
        }
    }

    /**
     * 检查验证码
     *
     * @return
     */
    private boolean checkCaptcha() {
        if (mCaptchaBytes == null) {
            Toast.makeText(getActivity(), "未获取验证码", Toast.LENGTH_SHORT).show();
            return false;
        }

        mCaptchaEditTextString = edit_imgyanzhengma.getText().toString().trim();
        if(mCaptchaEditTextString.contains(" ")){
            hint_imagecheck.setText("验证码不正确");
            return false;
        }
        if (mCaptchaEditTextString == null || "".equals(mCaptchaEditTextString)) {
            hint_imagecheck.setText("验证码不能为空");
            return false;
        } else {
            hint_imagecheck.setText("");
            return true;
        }

    }

    /**
     * 检查协议
     *
     * @return
     */
    private boolean checkXieyi() {


        CheckBox checkBox = (CheckBox) view.findViewById(R.id.xieyi_check);
        if (!checkBox.isChecked()) {
            hint_xieyi.setText("未勾选《央视网网络服务使用协议》");
            return false;
        } else {
            hint_xieyi.setText("");
            return true;
        }

    }


    /**
     * 获取图片验证码
     */
    private void sendCaptchaHttpMessage() {
        if(!isConnected()){
            return;
        }
        showLoadingDialog();
        new Thread(new Runnable() {

            @Override
            public void run() {
                String from = "http://reg.cntv.cn/simple/verificationCode.action";
                HttpGet httpGet = new HttpGet(from);

                try {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {// 读返回数据

                        CookieStore cookieStore = httpClient.getCookieStore();
                        List<Cookie> cookies = cookieStore.getCookies();
                        for (int index = 0, count = cookies.size(); index < count; index++) {
                            Cookie cookie = cookies.get(index);
                            if ("JSESSIONID".equals(cookie.getName())) {
                                JSESSIONID = cookie.getValue();

                                break;
                            }
                        }
                        mCaptchaBytes = EntityUtils.toByteArray(httpResponse
                                .getEntity());
                        mHandler.sendEmptyMessage(IMG_GET_SUCCES);

                    } else {
                        Message msg = mHandler
                                .obtainMessage(IMG_GET_ERROR);
                        msg.obj = erroType;
                        mHandler.sendMessage(msg);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 短信验证码的请求
     */
    private void sendCaptchaSmsHttpMessage() {
        if (!isConnected()) {
            showToast("请连接网络...");
            return;
        }
        showLoadingDialog();
        XjlHandler<String> tHandler = new XjlHandler<String>(new HandlerListener() {

            @Override
            public void handlerMessage(HandlerMessage msg) {

                String string = (String) msg.obj;

                if (string.endsWith("success")) {
                    Message tmsg = mHandler.obtainMessage(MSG_GETTING_SUCCESS);
                    tmsg.obj = "发送成功";
                    mHandler.sendMessage(tmsg);
                } else if (string.endsWith("registered")) {
                    Message tmsg = mHandler.obtainMessage(MSG_GETTING_ERROR);
                    tmsg.obj = "您的手机号已注册";
                    mHandler.sendMessage(tmsg);
                } else if (string.endsWith("sendfailure")) {
                    Message tmsg = mHandler.obtainMessage(MSG_GETTING_ERROR);
                    tmsg.obj = "验证码发送失败";
                    mHandler.sendMessage(tmsg);
                } else if (string.endsWith("sendagain")) {
                    Message tmsg = mHandler.obtainMessage(MSG_GETTING_ERROR);
                    tmsg.obj = "三分钟内只能获取一次";
                    mHandler.sendMessage(tmsg);
                } else if (string.endsWith("ipsendagain")) {
                    Message tmsg = mHandler.obtainMessage(MSG_GETTING_ERROR);
                    tmsg.obj = "同一IP用户请求校验码超过5次";
                    mHandler.sendMessage(tmsg);
                } else if (string.endsWith("mobileoften")) {
                    Message tmsg = mHandler.obtainMessage(MSG_GETTING_ERROR);
                    tmsg.obj = "同一手机号用户请求校验码超过3次";
                    mHandler.sendMessage(tmsg);
                } else if (string.endsWith("mobilecodeerror")) {
                    Message tmsg = mHandler.obtainMessage(MSG_GETTING_ERROR);
                    tmsg.obj = "验证码不正确";
                    mHandler.sendMessage(tmsg);
                }

            }
        });

        String phoneString = edit_phone.getText().toString().trim();
        String phoneyanzhengma = edit_imgyanzhengma.getText().toString().trim();
        String url = "http://reg.cntv.cn/regist/getVerifiCode.action";
        String from = "http://cbox_mobile.regclientuser.cntv.cn";
        HashMap<String, String> tHeaders = new HashMap<String, String>();
        try {

            tHeaders.put("Referer", URLEncoder.encode(from, "UTF-8"));
            tHeaders.put("User-Agent", URLEncoder.encode("CNTV_APP_CLIENT_CBOX_MOBILE", "UTF-8"));
            tHeaders.put("Cookie", "JSESSIONID=" + JSESSIONID);
        } catch (Exception e) {

        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "getRequestVerifiCodeM");
        params.put("mobile", phoneString);
        params.put("verfiCodeType", "1");
        params.put("verificationCode", phoneyanzhengma);
        tHandler.postHeaderJson(url, tHeaders, params, 0);

    }

    /***
     * 手机号注册的请求
     *
     * @param
     * @return
     */
    private void sendPhoneRegistHttp() {
        if (!isConnected()) {
            showToast("请连接网络...");
            return;
        }
        showLoadingDialog();
        XjlHandler<String> tHandler = new XjlHandler<String>(new HandlerListener() {
            @Override
            public void handlerMessage(HandlerMessage msg) {
                String tString = (String) msg.obj;
                Log.i("aaaa", tString);
                if (tString.endsWith("success")) {
                    Message tmsg = mHandler.obtainMessage(MSG_LOGIN_SUCCESS);
                    tmsg.obj = "手机号注册成功";
                    mHandler.sendMessage(tmsg);
                } else if (tString.endsWith("registered")) {
                    Message tmsg = mHandler.obtainMessage(MSG_LOGIN_ERROR);
                    tmsg.obj = "该手机号已注册";
                    mHandler.sendMessage(tmsg);
                } else if (tString.endsWith("error")) {
                    Message tmsg = mHandler.obtainMessage(MSG_LOGIN_ERROR);
                    tmsg.obj = "验证码输入有误";
                    mHandler.sendMessage(tmsg);
                } else if (tString.endsWith("mobilenull")) {
                    Message tmsg = mHandler.obtainMessage(MSG_LOGIN_ERROR);
                    tmsg.obj = "手机号为空";
                    mHandler.sendMessage(tmsg);
                } else if (tString.endsWith("timeout")) {
                    Message tmsg = mHandler.obtainMessage(MSG_LOGIN_ERROR);
                    tmsg.obj = "校验码已超过有效时间";
                    mHandler.sendMessage(tmsg);
                } else if (tString.endsWith("passwordnull")) {
                    Message tmsg = mHandler.obtainMessage(MSG_LOGIN_ERROR);
                    tmsg.obj = "密码为空";
                    mHandler.sendMessage(tmsg);
                }

            }
        });
        String url = "https://reg.cntv.cn/regist/mobileRegist.do";
        String tPhoneNumber = edit_phone.getText().toString().trim();
        String tCheckPhone = edit_phoneyanzhengma.getText().toString().trim();
        String tPassWord = edit_password.getText().toString();

        HashMap<String, String> tHeader = new HashMap<String, String>();
        try {

            tHeader.put("Referer", URLEncoder.encode("http://cbox_mobile.regclientuser.cntv.cn", "UTF-8"));
            tHeader.put("User-Agent", URLEncoder.encode("CNTV_APP_CLIENT_CBOX_MOBILE", "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HashMap<String, String> tParams = new HashMap<String, String>();
        tParams.put("method", "saveMobileRegisterM");
        tParams.put("mobile", tPhoneNumber);
        tParams.put("verfiCode", tCheckPhone);
        try {
            tParams.put("passWd", URLEncoder.encode(tPassWord, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tParams.put("verfiCodeType", "1");
        try {
            tParams.put("addons", URLEncoder.encode("http://cbox_mobile.regclientuser.cntv.cn", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tHandler.postHeaderJson(url, tHeader, tParams, 1);
    }


    public static Drawable byteToDrawable(byte[] byteArray) {
        try {
            String string = new String(byteArray, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
        return Drawable.createFromStream(ins, null);
    }

    /**
     * 登录
     */
    private void goLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mUserNameString = edit_phone.getText().toString()
                        .trim();
                String mPassWordString = edit_password.getText().toString()
                        .trim();
                try {
                    String from = "https://reg.cntv.cn/login/login.action";
                    String url = from + "?username="
                            + URLEncoder.encode(mUserNameString, "UTF-8")
                            + "&password=" + mPassWordString
                            + "&service=client_transaction" + "&from="
                            + URLEncoder.encode(from, "UTF-8");
                    HttpGet httpRequest = new HttpGet(url);

                    httpRequest.addHeader("Referer",
                            URLEncoder.encode(from, "UTF-8"));
                    httpRequest.addHeader("User-Agent", URLEncoder.encode(
                            "CNTV_APP_CLIENT_CYNTV_MOBILE", "UTF-8"));

                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpClient.execute(httpRequest);

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {// 读返回数据
                        String strResult = EntityUtils.toString(httpResponse
                                .getEntity());
                        CookieStore cookieStore = httpClient.getCookieStore();
                        List<Cookie> cookies = cookieStore.getCookies();
                        for (int index = 0, count = cookies.size(); index < count; index++) {
                            Cookie cookie = cookies.get(index);
                            if ("JSESSIONID".equals(cookie.getName())) {
                                JSESSIONID = cookie.getValue();
                            } else if ("verifycode".equals(cookie.getName())) {
                                verifycode = cookie.getValue();
                            } else if ("uct".equals(cookie.getName())) {
                                uct = cookie.getValue();
                            }
                        }

                        JSONObject jo = new JSONObject(strResult);
                        if (jo.getString("errType").equals("0")) {
                            if (jo.has("user_seq_id")) {
                                mUserSeqId = jo.getString("user_seq_id");
                            }
                            if (jo.has("usrid")) {
                                mUserId = jo.getString("usrid");
                            }
                            mHandler.sendEmptyMessage(MSG_LGOIN_IN_GET_NICKNAME);
                        } else {
                            // String codeString = jo.getString("errType");
                            String errMsg = jo.getString("errMsg");

                            Message msg = mHandler
                                    .obtainMessage(MSG_LOGIN_IN_ERROR);
                            msg.obj = errMsg;
                            mHandler.sendMessage(msg);
                        }
                    } else {
                        Message msg = mHandler
                                .obtainMessage(MSG_LOGIN_IN_ERROR);
                        //msg.obj = "网络异常";
                        msg.obj = "移动网络环境下无法进行注册,请开启WIFI后进行注册";
                        mHandler.sendMessage(msg);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 获取昵称
     */
    private void getUserTicket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String client = "http://cbox_mobile.regclientuser.cntv.cn";
                String url = "http://my.cntv.cn/intf/napi/api.php" + "?client="
                        + "cbox_mobile" + "&method=" + "user.getNickName"
                        + "&userid=" + mUserSeqId;
                HttpGet httpGet = new HttpGet(url);
                try {
                    httpGet.addHeader("Referer",
                            URLEncoder.encode(client, "UTF-8"));
                    httpGet.addHeader("User-Agent", URLEncoder.encode(
                            "CNTV_APP_CLIENT_CBOX_MOBILE", "UTF-8"));
                    httpGet.addHeader("Cookie", "verifycode=" + verifycode);

                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {// 读返回数据
                        String strResult = EntityUtils.toString(httpResponse
                                .getEntity());
                        JSONObject jo = new JSONObject(strResult);
                        if (jo.getString("code").equals("0")) {
                            if (jo.has("content")) {
                                JSONObject contentJSONObject = jo
                                        .getJSONObject("content");
                                if (contentJSONObject.has("nickname")) {
                                    mNickName = contentJSONObject
                                            .getString("nickname");
                                } else {
                                    mNickName = "default";
                                }
                            }
                            mHandler.sendEmptyMessage(MSG_GET_NICKNAME_SUCCESS);
                        } else {
                            String codeErrorString = jo.getString("error");
                            Message msg = mHandler
                                    .obtainMessage(MSG_LOGIN_IN_ERROR);
                            msg.obj = codeErrorString;
                            mHandler.sendMessage(msg);
                        }
                    } else {
                        Message msg = mHandler
                                .obtainMessage(MSG_LOGIN_IN_ERROR);
                        //msg.obj = "网络错误";
                        msg.obj = "移动网络环境下无法进行注册,请开启WIFI后进行注册";
                        mHandler.sendMessage(msg);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
