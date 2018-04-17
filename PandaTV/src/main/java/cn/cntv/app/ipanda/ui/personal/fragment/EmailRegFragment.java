package cn.cntv.app.ipanda.ui.personal.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalAgreePostActivity;

public class EmailRegFragment extends BaseFragment implements View.OnFocusChangeListener {

    private TextView xieyi;
    public String JSESSIONID = null;
    private byte[] mCaptchaBytes;
    //private String erroType = "网络异常";
    private String erroType = "移动网络环境下无法进行注册,请开启WIFI后进行注册";
    private TextView mCaptchaImageView;


    //获取验证码图片成功
    private static final int IMG_GET_SUCCES = 1000;
    private static final int MSG_LOGIN_IN_ERROR = 1002;
    //获取邮箱注册成功
    private static final int MSG_LOGIN_IN_SUCCES = 1003;


    private TextView hint_emamil;
    private TextView hint_passwork;
    private TextView hint_again_passord;
    private TextView hint_yanzhengma;
    private TextView hint_xieyi;
    private TextView btn_register;
    private EditText edit_email;
    private EditText edit_password;
    private EditText edit_again_password;
    private EditText edit_yanzhengma;

    //输入框输入的验证码
    private String mCaptchaEditTextString;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_email_register, container, false);
        initView();
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                case MSG_LOGIN_IN_ERROR:
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
                case MSG_LOGIN_IN_SUCCES:
                    dismissLoadDialog();

                    toastRegisterSuccess();
                    MobileAppTracker.trackEvent("注册", null, "个人中心", 0, null, null, getActivity());
                    break;


            }
        }
    };

    private void toastRegisterSuccess() {
        if (getActivity().isFinishing()) {
            return;
        }

        final String key = edit_email.getText().toString().trim().split("@")[1].toLowerCase();
        if (mEmailAddress.getmEmailAddress().containsKey(key)) {
            View tview = View
                    .inflate(getActivity(), R.layout.dialog_internet_tishi, null);
            TextView tishiContent = (TextView) tview
                    .findViewById(R.id.play_continue_content);
            tishiContent.setText("请到您的邮箱激活账号后登录，是否现在去邮箱激活");
            TextView tishiCancel = (TextView) tview
                    .findViewById(R.id.play_continue_cancel);
            TextView tishiSure = (TextView) tview
                    .findViewById(R.id.play_continue_sure);
            final Dialog registerDialog = new Dialog(getActivity(), R.style.dialog);
            tishiSure.setText("确定");
            tishiCancel.setText("取消");

            registerDialog.setContentView(tview);
            registerDialog.setCanceledOnTouchOutside(true);

            tishiCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerDialog.dismiss();

                }
            });
            tishiSure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerDialog.dismiss();
                    String mEmailAdd = mEmailAddress.getmEmailAddress().get(key);
                    Uri uri = Uri.parse(mEmailAdd);
                    Intent it = new Intent();
                    it.setAction("android.intent.action.VIEW");
                    it.setData(uri);
                    startActivity(it);
                    getActivity().finish();
                }
            });
            registerDialog.show();

        } else {
            showTishiDialog("通行证需激活后才可登录");
        }


    }

    private static mEmailAddressMap mEmailAddress;

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edit_email:
                    hint_emamil.setText("");
                    break;
                case R.id.edit_passwrok:
                    if (!checkEmail())
                        return;
                    hint_passwork.setText("");
                    break;
                case R.id.edit_again_password:
                    if (!checkPasswork())
                        return;
                    hint_again_passord.setText("");
                    break;
                case R.id.edit_yanzhengma:
                    if (!checkAgainPasswork())
                        return;
                    hint_yanzhengma.setText("");
                    break;

            }
        }
    }

    public static class mEmailAddressMap implements Serializable {
        private Map<String, String> mEmailAddress;

        public Map<String, String> getmEmailAddress() {
            return mEmailAddress;
        }

        public void setmEmailAddress(Map<String, String> mEmailAddress) {
            this.mEmailAddress = mEmailAddress;
        }

    }

    static {
        mEmailAddress = new mEmailAddressMap();
        mEmailAddress.setmEmailAddress(new HashMap<String, String>());
        mEmailAddress.getmEmailAddress().put("qq.com", "http://mail.qq.com");
        mEmailAddress.getmEmailAddress().put("gmail.com", "http://mail.google.com");
        mEmailAddress.getmEmailAddress().put("sina.com", "http://mail.sina.com.cn");
        mEmailAddress.getmEmailAddress().put("163.com", "http://mail.163.com");
        mEmailAddress.getmEmailAddress().put("126.com", "http://mail.126.com");
        mEmailAddress.getmEmailAddress().put("yeah.net", "http://www.yeah.net/");
        mEmailAddress.getmEmailAddress().put("sohu.com", "http://mail.sohu.com/");
        mEmailAddress.getmEmailAddress().put("tom.com", "http://mail.tom.com/");
        mEmailAddress.getmEmailAddress().put("sogou.com", "http://mail.sogou.com/");
        mEmailAddress.getmEmailAddress().put("139.com", "http://mail.10086.cn/");
        mEmailAddress.getmEmailAddress().put("hotmail.com", "http://www.hotmail.com");
        mEmailAddress.getmEmailAddress().put("live.com", "http://login.live.com/");
        mEmailAddress.getmEmailAddress().put("live.cn", "http://login.live.cn/");
        mEmailAddress.getmEmailAddress().put("live.com.cn", "http://login.live.com.cn");
        mEmailAddress.getmEmailAddress().put("189.com", "http://webmail16.189.cn/webmail/");
        mEmailAddress.getmEmailAddress().put("yahoo.com.cn", "http://mail.cn.yahoo.com/");
        mEmailAddress.getmEmailAddress().put("yahoo.cn", "http://mail.cn.yahoo.com/");
        mEmailAddress.getmEmailAddress().put("eyou.com", "http://www.eyou.com/");
        mEmailAddress.getmEmailAddress().put("21cn.com", "http://mail.21cn.com/");
        mEmailAddress.getmEmailAddress().put("188.com", "http://www.188.com/");
        mEmailAddress.getmEmailAddress().put("foxmail.coom", "http://www.foxmail.com");
    }

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


        hint_emamil = (TextView) view.findViewById(R.id.hint_emamil);
        hint_passwork = (TextView) view.findViewById(R.id.hint_passwork);
        hint_again_passord = (TextView) view.findViewById(R.id.hint_again_passord);
        hint_yanzhengma = (TextView) view.findViewById(R.id.hint_yanzhengma);
        hint_xieyi = (TextView) view.findViewById(R.id.hint_xieyi);


        btn_register = (TextView) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new ViewClick());

        edit_email = (EditText) view.findViewById(R.id.edit_email);
        edit_password = (EditText) view.findViewById(R.id.edit_passwrok);
        edit_again_password = (EditText) view.findViewById(R.id.edit_again_password);
        edit_yanzhengma = (EditText) view.findViewById(R.id.edit_yanzhengma);

        edit_email.setOnFocusChangeListener(this);
        edit_password.setOnFocusChangeListener(this);
        edit_again_password.setOnFocusChangeListener(this);
        edit_yanzhengma.setOnFocusChangeListener(this);

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
                case R.id.btn_register:
                    edit_email.clearFocus();
                    edit_password.clearFocus();
                    edit_again_password.clearFocus();
                    edit_yanzhengma.clearFocus();
                    if (!isConnected()) {
                        showToast(R.string.network_invalid);
                        return;
                    }

                    //点击注册
                    if (!checkEmail()) {
                        return;
                    }
                    if (!checkPasswork()) {
                        return;
                    }
                    if (!checkAgainPasswork()) {
                        return;
                    }

                    if (!checkCaptcha()) {
                        return;
                    }

                    if (!checkXieyi()) {
                        return;
                    }

                    sendHttpMessageOfMail();

                    break;

            }
        }

    }

    //检查邮箱
    private boolean checkEmail() {
        String emailString = edit_email.getText().toString().trim();
        if (TextUtils.isEmpty(emailString)) {
            hint_emamil.setText("邮箱不能为空");
            // shakeViewToNotifyUser(edit_email);
            return false;
        }
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(emailString);
        if (matcher.matches()) {
            hint_emamil.setText("");
            return true;
        } else {
            hint_emamil.setText("邮箱格式不正确");
            return false;
        }
    }

    private boolean checkPasswork() {
        String editpasswordsString = edit_password.getText().toString();

        if (TextUtils.isEmpty(editpasswordsString)) {
            hint_passwork.setText("密码不能为空");
            // shakeViewToNotifyUser(edit_password);
            return false;
        } else if (editpasswordsString.length() < 6 || editpasswordsString.length() > 16) {
            hint_passwork.setText("密码仅限6-16个字符");
            return false;
        } else {
            hint_passwork.setText("");
            return true;
        }
    }

    private boolean checkAgainPasswork() {

        String editagainpasswordsString = edit_again_password.getText().toString();
        if (TextUtils.isEmpty(editagainpasswordsString)) {
            hint_again_passord.setText("确认密码不能为空");
            // shakeViewToNotifyUser(edit_again_password);
            return false;
        } else {
            hint_again_passord.setText("");
            String passwordsString = edit_password.getText().toString();
            if (!passwordsString.equals(editagainpasswordsString)) {
                hint_again_passord.setText("密码不一致");
                return false;
            } else {
                return true;
            }
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

        mCaptchaEditTextString = edit_yanzhengma.getText().toString().trim();
        if(mCaptchaEditTextString.contains(" ")){
            hint_yanzhengma.setText("验证码不正确");
            return false;
        }
        if (mCaptchaEditTextString == null || "".equals(mCaptchaEditTextString)) {
            // shakeViewToNotifyUser(edit_yanzhengma);
            hint_yanzhengma.setText("验证码不能为空");
            return false;
        } else {
            hint_yanzhengma.setText("");
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
        if (!isConnected()) {
            return;
        }

        showLoadingDialog();
        new Thread(new Runnable() {

            @Override
            public void run() {
                String url = "http://reg.cntv.cn/simple/verificationCode.action";
                HttpGet httpGet = new HttpGet(url);

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
                                .obtainMessage(MSG_LOGIN_IN_ERROR);
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
     * 发送邮箱
     *
     * @throws
     */
    private void sendHttpMessageOfMail() {
        if (!isConnected()) {
            showToast(R.string.network_invalid);
            return;
        }
        showLoadingDialog();

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    String emailString = edit_email.getText().toString().trim();
                    String passwordString = edit_password.getText().toString();
                    String from = "iPanda.Android";
                    String url = "https://reg.cntv.cn/api/register.action" + "?mailAdd="
                            + emailString + "&passWd=" + URLEncoder.encode(passwordString, "UTF-8")
                            + "&verificationCode=" + mCaptchaEditTextString + "&addons="
                            + URLEncoder.encode(from, "UTF-8");
                    HttpGet httpGet = new HttpGet(url);

                    httpGet.addHeader("Referer", URLEncoder.encode(from, "UTF-8"));
                    httpGet.addHeader("User-Agent",
                            URLEncoder.encode("CNTV_APP_CLIENT_CNTV_MOBILE", "UTF-8"));
                    httpGet.addHeader("Cookie", "JSESSIONID=" + JSESSIONID);

                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {// 读返回数据
                        String strResult = EntityUtils.toString(httpResponse
                                .getEntity());
                        JSONObject jsonObject = new JSONObject(strResult);
                        if (jsonObject.has("errtype")) {
                            String errtype = jsonObject.getString("errtype");
                            if ("0".equals(errtype)) { // success
                                Log.i("TAG",strResult+"-----"+URLEncoder.encode(passwordString, "UTF-8"));
                                Message msg = mHandler
                                        .obtainMessage(MSG_LOGIN_IN_SUCCES);
                                mHandler.sendMessage(msg);
                            } else { // failure
                                Message msg = mHandler
                                        .obtainMessage(MSG_LOGIN_IN_ERROR);
//								msg.obj = jsonObject.get("errtype");
                                msg.obj = jsonObject.get("msg");
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = mHandler
                                    .obtainMessage(MSG_LOGIN_IN_ERROR);
                            msg.obj = erroType;
                            mHandler.sendMessage(msg);
                        }
                    } else {
                        Message msg = mHandler
                                .obtainMessage(MSG_LOGIN_IN_ERROR);
                        msg.obj = erroType;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
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


}
