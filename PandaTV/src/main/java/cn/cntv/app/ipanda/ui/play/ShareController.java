package cn.cntv.app.ipanda.ui.play;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gridsum.mobiledissector.util.StringUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.facebook.controller.UMFacebookHandler;
import com.umeng.socialize.facebook.media.FaceBookShareContent;
import com.umeng.socialize.media.TwitterShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.List;

import cn.cntv.app.ipanda.AppConfig;
import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.UMengEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.manager.WeiboShareManager;
import cn.cntv.app.ipanda.utils.JsonUtils;
import cn.cntv.app.ipanda.utils.PicUtil;
import cn.cntv.app.ipanda.xlistview.MyShare;

/**
 * @author： pj @Date： 2016年1月4日 上午9:48:17
 * @Description:分享的工具类
 */
@SuppressWarnings("WrongConstant")
public class ShareController implements OnClickListener {

    private static final int SORT_URL = 1;

    private LayoutInflater mInflater;
    private Context mContext;
    //实例化分享对象
    private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private UMImage umImage;
    private ProgressDialog mDlg;

    private String mShareTitle = "", mShareText, mShareUrl = "", mShareInitUrl = "", mId = "", mVideoLength = "",
            mShareImgPath = AppConfig.DEFAULT_IMAGE_PATH + "share_logo_ipnda.png", mH5ShareText = "";

    private boolean mIsNetPic;// 分享时显示的图片是否为网络图片

    private PopupWindow tipPw;

    private Dialog shareDialog;

    private String url_short = "";
    private String url_long = "";

    private String sinaurl = "";
    private String mUrl = "";
    private String mURI;
    private String testUrl1;
    private String testUrl2;
    private String testUrl3;
    private String testUrl4;
    private String shareid;
    private String h5image;
    private int mLayoutId;
    private boolean mIsFullScreen;

    /** 是否跳向分享 */
    private boolean isGoShare;

    private XjlHandler<Object> mXjlHandler = new XjlHandler<>(new HandlerListener() {
        @Override
        public void handlerMessage(HandlerMessage msg) {

            switch (msg.what) {
                case Integer.MAX_VALUE:

                    break;
                case SORT_URL:

                    String json = (String) msg.obj;

                    List<MyShare> list = JsonUtils.getList(MyShare[].class, json);
                    for (MyShare myShare : list) {
                        mShareInitUrl = myShare.getUrl_short();
                        url_long = myShare.getUrl_long();
                        Log.i("TAG", "mShareInitUrl:" + mShareInitUrl);
                        Log.i("TAG", "url_long:" + url_long);
                        break;
                    }
                    mShareText = mShareTitle + ",熊猫频道邀请您一同观看！" + mShareInitUrl;
                    mH5ShareText = mShareTitle + ",熊猫频道邀请您一同观看！" + mShareInitUrl;

                    showWindow();
                    break;
            }
        }
    });
    private Bitmap h5bitmap;

    public ShareController() {

    }

    /**
     * 默认调用的
     *
     * @param parentView
     * @param shareTitle
     */
    public void showPopWindow(View parentView, String shareTitle,String id, String image,String shareUrl) {

        HshowPopWindow(parentView, parentView.getContext(),R.layout.pop_share, shareTitle,id,image, shareUrl, false);
    }


    /**
     * 自定义分享Url调用的
     *
     * @param parentView
     * @param
     * @param
     */
    public void showPopWindow(View parentView, String title, String url, String id, String videoLength, String image, String guid) {

        showPopWindow(parentView, parentView.getContext(), R.layout.pop_share, title, url, id, videoLength, image, guid, false);
    }

    /**
     * 视频地方调用的
     *
     * @param parentView
     * @param
     */
    public void showPopWindowVideo(View parentView, String title, String url, String id, String videoLength, String image, String guid) {

        showPopWindow(parentView, parentView.getContext(), R.layout.pop_share_fullscreen,
                title, url, id, videoLength, image, guid, true);
    }

    /**
     * 展示分享窗口
     *
     * @param v        popWindow所依附的view
     * @param context  上下文
     * @param layoutId popWindow的布局文件
     * @param
     */
    public final void showPopWindow(View v, Context context, int layoutId, String title,
                                    String url, String id, String videoLength, String image, String guid,
                                    boolean fullscreen) {
        //真正链接
        //单视频
        testUrl1 = "http://www.ipanda.com/special/share/pandah5/index.html";
        //直播
        testUrl2 = "http://www.ipanda.com/special/share/pandah5/index_live.html";
        //视频集
        testUrl3 = "http://www.ipanda.com/special/share/pandah5/proved.html";
        //图文分享
        testUrl4 = "http://www.ipanda.com/special/share/pandah5/news.html";
        // 测试链接  http://www.ipanda.com/special/share/pandah5/
//        testUrl1="http://www.ipanda.com/special/share/pandah5/test/index.html";
//        testUrl2="http://www.ipanda.com/special/share/pandah5/test/index_live.html";
//        testUrl3="http://www.ipanda.com/special/share/pandah5/test/proved.html";
//        testUrl4="http://www.ipanda.com/special/share/pandah5/test/news.html";

        // 测试链接 http://124.200.36.22:8080/client/xiongmao2/index.html
//        testUrl1="http://124.200.36.22:8080/client/xiongmao2/index.html";
//        testUrl2="http://124.200.36.22:8080/client/xiongmao2/index_live.html";
//        testUrl3="http://124.200.36.22:8080/client/xiongmao2/proved.html";
//        testUrl4="http://124.200.36.22:8080/client/xiongmao2/news.html";
        if ("3".equals(id)) {

            mURI = testUrl1 + "?guid=" + guid + "&url=" + url + "&image=" + image + "&id=" + id + "&videoLength=" + videoLength + "&title=" + title;
        } else if ("2".equals(id)) {
            mURI = testUrl2 + "?guid=" + guid + "&url=" + url + "&image=" + image + "&id=" + id + "&videoLength=" + videoLength + "&title=" + title;
        } else if ("1".equals(id)) {
            mURI = testUrl3 + "?guid=" + guid + "&url=" + url + "&image=" + image + "&id=" + id + "&videoLength=" + videoLength + "&title=" + title;
        } else if ("6".equals(id)) {
            mURI = testUrl4 +"?guid=" + guid + "&url=" + url + "&image=" + image + "&id=" + id + "&videoLength=" + videoLength + "&title=" + title;
        }


        //测试链接
        sinaurl = "http://api.t.sina.com.cn/short_url/shorten.json?source=3565212298";

        try {
            mUrl = URLEncoder.encode(mURI, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mShareTitle = StringUtil.isNullOrEmpty(title) ? "\t" : title;
        umImage = new UMImage(mContext, mShareImgPath);
        shareid=id;
        mLayoutId = layoutId;
        mIsFullScreen = fullscreen;

        mXjlHandler.getHttpString(sinaurl + "&url_long=" + mUrl, SORT_URL);

    }

    private void showWindow() {

        if (shareDialog != null && shareDialog.isShowing()) {
            return;
        }

        // 获取对话框布局View，背景设为圆角
        final View dialogView = mInflater.inflate(mLayoutId, null, false);
        View spaceView = dialogView.findViewById(R.id.layout_space);
        View facebookV = dialogView.findViewById(R.id.iv_plat_facebook);
        View twitterV = dialogView.findViewById(R.id.iv_plat_twitter);
        View sinaweboV = dialogView.findViewById(R.id.iv_plat_sinawebo);
        View wxV = dialogView.findViewById(R.id.iv_plat_wx);
        View momentsV = dialogView.findViewById(R.id.iv_plat_moments);
        View cancelV = dialogView.findViewById(R.id.cancelTv);

        if (spaceView != null) {
            // 点击空白部分，隐藏窗口
            spaceView.setOnClickListener(this);
        }

        if (facebookV != null) {
            // 分享facebook
            facebookV.setOnClickListener(this);
        }

        if (twitterV != null) {
            // 分享twitter
            twitterV.setOnClickListener(this);
        }

        if (sinaweboV != null) {
            // 分享sinawebo
            sinaweboV.setOnClickListener(this);
        }

        if (wxV != null) {
            // 分享微信
            wxV.setOnClickListener(this);
        }

        if (momentsV != null) {
            // 分享微信朋友圈

            momentsV.setOnClickListener(this);
        }
        if (cancelV != null) {
            // 取消
            cancelV.setOnClickListener(this);
        }

        // 创建弹出对话框，设置弹出对话框的背景为圆角
        if (mIsFullScreen) {
            shareDialog = createVideoShareDialog(mContext, dialogView);
        } else {
            shareDialog = createShareDialog(mContext, dialogView);
        }
        shareDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isGoShare) {
                    resumePlay();
                }
                isGoShare = false;
            }
        });
        isGoShare = false;
        shareDialog.show();
        // 弹出分享窗口时暂停播放
        pausePlay();
    }

    private Dialog createVideoShareDialog(Context context, View view) {

        Dialog dialog = new Dialog(context,  R.style.dialog_no_dark_fullscreen);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimationJustAlpha); //设置窗口弹出动画

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height=ViewGroup.LayoutParams.MATCH_PARENT;

        return dialog;
    }

    private Dialog createShareDialog(Context context, View view) {
        Dialog dialog = new Dialog(context, R.style.style_dialog);
        dialog.setContentView(view);

        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimationJustAlpha); //设置窗口弹出动画

        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;

        return dialog;
    }


    private void resumePlay() {
        if (mLiveControllerWref != null) {
            PlayLiveController live = mLiveControllerWref.get();
            if (live != null) {
                live.onResume();
            }
        }
        if (mVodControllerWref != null) {
            PlayVodController vod = mVodControllerWref.get();
            if (vod != null) {
                vod.onResume();
            }
        }
    }

    private void pausePlay() {
        if (mLiveControllerWref != null) {
            PlayLiveController playLiveController = mLiveControllerWref.get();
            if (playLiveController != null) {
                playLiveController.onPause();
            }
        }
        if (mVodControllerWref != null) {
            PlayVodController playVodController = mVodControllerWref.get();
            if (playVodController != null) {
                playVodController.onPause();
            }
        }
    }

    //H5页面和专题不上传数据
    public final void HshowPopWindow(View v, Context context, int layoutId, String shareTitle,String id, String image,String shareUrl,
                                     boolean isNetPic) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        shareid=id;
        Log.e("shareid",shareid);
        mShareTitle = StringUtil.isNullOrEmpty(shareTitle) ? "\t" : shareTitle;
        mShareText = "“"+mShareTitle+"”"+"  "+shareUrl;
        mH5ShareText ="“"+mShareTitle+"”"+"  "+shareUrl;
        //mShareUrl = shareUrl;
        url_long = mShareInitUrl = shareUrl;

        if(shareid.equals("4")&&image!=null){
            umImage = new UMImage(mContext, image);
            h5image=image;
            Glide.with(context)
                    .load(image)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            umImage = new UMImage(mContext, resource);
                            h5bitmap=resource;
                        }
                    });
        }else {
            umImage = new UMImage(mContext,mShareImgPath);
        }

        // 获取对话框布局View，背景设为圆角
        final View dialogView = mInflater.inflate(layoutId, null, false);
        View spaceView = dialogView.findViewById(R.id.layout_space);
        View facebookV = dialogView.findViewById(R.id.iv_plat_facebook);
        View twitterV = dialogView.findViewById(R.id.iv_plat_twitter);
        View sinaweboV = dialogView.findViewById(R.id.iv_plat_sinawebo);
        View wxV = dialogView.findViewById(R.id.iv_plat_wx);
        View momentsV = dialogView.findViewById(R.id.iv_plat_moments);
        View cancelV = dialogView.findViewById(R.id.cancelTv);

        if (spaceView != null) {
            // 点击空白部分，隐藏窗口
            spaceView.setOnClickListener(this);
        }

        if (facebookV != null) {
            // 分享facebook
            facebookV.setOnClickListener(this);
        }

        if (twitterV != null) {
            // 分享twitter
            twitterV.setOnClickListener(this);
        }

        if (sinaweboV != null) {
            // 分享sinawebo
            sinaweboV.setOnClickListener(this);
        }

        if (wxV != null) {
            // 分享微信
            wxV.setOnClickListener(this);
        }

        if (momentsV != null) {
            // 分享微信朋友圈
            momentsV.setOnClickListener(this);
        }
        if (cancelV != null) {
            // 取消
            cancelV.setOnClickListener(this);
        }

        // 创建弹出对话框，设置弹出对话框的背景为圆角
        shareDialog= createShareDialog(mContext, dialogView);
        shareDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isGoShare) {
                    resumePlay();
                }
                isGoShare = false;
            }
        });
        isGoShare = false;
        shareDialog.show();
        // 弹出分享窗口时暂停播放*/
        pausePlay();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        isGoShare = true;
        // 先隐藏，再执行取消或者分享
        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
        if (id == R.id.layout_space || id == R.id.cancelTv) {
            // 不显示分享窗口时，继续播放
            resumePlay();
        } else {
            doShare(v, true);
        }

    }


    public boolean isShow() {
        return  shareDialog != null && shareDialog.isShowing();
    }


    /**
     * 显示提示框
     *
     * @param parent
     */
    public void showTipPop(final View parent, int strId) {
        final View dialogView = mInflater.inflate(R.layout.player_tip, null, false);
        // 提示信息
        final TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip);
        tipTv.setText(mContext.getResources().getString(strId));
        // 创建弹出对话框，设置弹出对话框的背景为圆角
        tipPw = new PopupWindow(dialogView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        // 响应返回键
        tipPw.setFocusable(true);
        // Cancel按钮及其处理事件
        final TextView btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setText(mContext.getResources().getString(R.string.cancel));
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (tipPw != null && tipPw.isShowing()) {
                    tipPw.dismiss();// 关闭
                    // 取消时，继续播放
                    resumePlay();
                }
            }
        });
        final TextView btnOk = (TextView) dialogView.findViewById(R.id.btn_ok);
        btnOk.setText(mContext.getResources().getString(R.string.sure));
        btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (tipPw != null && tipPw.isShowing()) {
                    tipPw.dismiss();// 关闭
                }
                doShare(parent, false);
            }
        });

        // 显示RoundCorner对话框
        tipPw.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);

    }

    /**
     * 执行分享
     *
     * @param v
     */
    private void doShare(View v, boolean flag) {
        if (v.getId() == R.id.iv_plat_wx || v.getId() == R.id.iv_plat_moments) {
            // 分享到微信和朋友圈，第一次给出弹框提示
            if (flag && AppContext.getInstance().getSpUtil().getIsFirstShareWx()) {
                // 首次分享至微信好友、微信朋友圈，点击图标，弹出想要打开“微信”的弹框
                if (isShow()) {
                    shareDialog.dismiss();
                }
                showTipPop(v, R.string.share_tip2);
                AppContext.getInstance().getSpUtil().setIsFirstShareWx(false);
                return;
            }
        }

        if (v.getId() == R.id.iv_plat_sinawebo) {
            // 分享到新浪微博，第一次给出弹框提示
            if (flag && AppContext.getInstance().getSpUtil().getIsFirstShareSinaWeib()) {
                // 首次分享至微信好友、微信朋友圈，点击图标，弹出想要打开“微信”的弹框
                if (isShow()) {
                    shareDialog.dismiss();
                }
                showTipPop(v, R.string.share_tip3);
                AppContext.getInstance().getSpUtil().setIsFirstShareSinaWeib(false);
                return;
            }
        }
        // showDlg();
        // 初始化分享图片
        initShareImg();
        // 添加新浪SSO授权

        switch (v.getId()) {

            case R.id.iv_plat_facebook:

//
//                mShareUrl = mShareInitUrl;
//
//                ShareDialog shareDialog = new ShareDialog((Activity) mContext);
//
//                CallbackManager  callbackManager = CallbackManager.Factory.create();
//
//                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//                    @Override
//                    public void onSuccess(Sharer.Result result) {
//                        if(mWebview!=null){
//                            mWebview.reload();
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        if(mWebview!=null){
//                            mWebview.reload();
//                        }
//                    }
//
//                    @Override
//                    public void onError(FacebookException error) {
//                        if(mWebview!=null){
//                            mWebview.reload();
//                        }
//                    }
//                });
//
//
//                ShareLinkContent content = new ShareLinkContent.Builder()
//                        .setContentTitle("熊猫频道")
//                        .setImageUrl(Uri.parse("http://www.ipanda.com/special/share/pandah5/images/panda.png"))
//                        .setContentUrl(Uri.parse(mShareUrl))
//                        .setContentDescription(mH5ShareText)
//                        .build();
//
//                 shareDialog.show(content);
                UMFacebookHandler mFacebookHandler = new UMFacebookHandler((Activity) mContext);
                mFacebookHandler.addToSocialSDK();

                mShareUrl = mShareInitUrl;
                // mController.getConfig().supportAppPlatform(mContext, SHARE_MEDIA.FACEBOOK, "com.umeng.share", true);

                FaceBookShareContent fBShareContent = new FaceBookShareContent();
                fBShareContent.setTitle("熊猫频道");
                fBShareContent.setTargetUrl(mShareUrl);
                fBShareContent.setShareContent(mH5ShareText);
                if(shareid.equals("4")) {
                    fBShareContent.setShareImage(new UMImage(mContext,
                            h5image.toString()));
                }else{  fBShareContent.setShareImage(new UMImage(mContext,
                        "http://www.ipanda.com/special/share/pandah5/images/panda.png"));}
                mController.setShareMedia(fBShareContent);

                mController.directShare(mContext, SHARE_MEDIA.FACEBOOK, postListener);

                break;
            case R.id.iv_plat_twitter:

                mShareUrl = mShareInitUrl;

                mController.getConfig().supportAppPlatform(mContext, SHARE_MEDIA.TWITTER, "com.umeng.share", true);

                TwitterShareContent twitterShareContent = new TwitterShareContent();
                twitterShareContent.setShareContent(mH5ShareText);
                twitterShareContent.setTitle("熊猫频道");
                twitterShareContent.setTargetUrl(mShareUrl);
                twitterShareContent.setShareImage(umImage);
                mController.setShareMedia(twitterShareContent);

                mController.postShare(mContext, SHARE_MEDIA.TWITTER, postListener);

                break;
            case R.id.iv_plat_sinawebo:

                pauseMusicPlay();

                mShareUrl = mShareInitUrl;

                if (h5bitmap == null) {
                    h5bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.share_logo_ipnda);
                }
                WeiboShareManager.get(mContext).share((Activity)mContext, mShareText, h5bitmap);
                break;
            case R.id.iv_plat_wx:
                pauseMusicPlay();
                // 添加微信平台
                UMWXHandler wxHandler = new UMWXHandler(mContext, UMengEnum.WX_APP_ID.toString(), UMengEnum.WX_APP_SECRET.toString());
                wxHandler.addToSocialSDK();
                mShareUrl = mShareInitUrl;

                //微信分享的内容

                WeiXinShareContent weixinContent = new WeiXinShareContent();
                weixinContent.setShareContent(mShareText);
                weixinContent.setTitle("熊猫频道");
                weixinContent.setTargetUrl(mShareUrl);
                weixinContent.setShareImage(umImage);
                mController.setShareMedia(weixinContent);

                mController.postShare(mContext, SHARE_MEDIA.WEIXIN, postListener);

                break;
            case R.id.iv_plat_moments:

                mShareUrl = mShareInitUrl;
                // 支持微信朋友圈
                // 注意：在微信授权的时候，必须传递appSecret

                UMWXHandler wxCircleHandler = new UMWXHandler(mContext, UMengEnum.WX_APP_ID.toString(), UMengEnum.WX_APP_SECRET.toString());
                wxCircleHandler.setToCircle(true);
                wxCircleHandler.addToSocialSDK();


                CircleShareContent circleShareContent = new CircleShareContent();
                circleShareContent.setShareContent(mH5ShareText);
                circleShareContent.setTitle(mH5ShareText);
                circleShareContent.setTargetUrl(url_long);
                circleShareContent.setShareImage(umImage);
                mController.setShareMedia(circleShareContent);

                mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, postListener);
                break;

        }
    }



    SnsPostListener postListener = new SnsPostListener() {

        @Override
        public void onStart() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onComplete(SHARE_MEDIA arg0, int ecode, SocializeEntity arg2) {

            //互动
            if(mWebview!=null){
                mWebview.reload();
            }
            if (mContext != null) {

                View tView = ((Activity) mContext).getWindow().peekDecorView();

                if (tView != null) {

                    // 获取输入法接口
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                    // 强制隐藏键盘
                    imm.hideSoftInputFromWindow(tView.getWindowToken(), 0);
                }
            }
        }

    };


    /**
     * 互动页面分享时音乐播放暂停
     */
    private void pauseMusicPlay() {
        OnAudioFocusChangeListener listener = new OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                Log.d("audioManager", "onAudioFocusChange: "
                        + focusChange);
            }
        };
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {

        } else {
            try {

                AudioManager audioManager = (AudioManager) mContext.getSystemService("audio");
                int i = 0;
                do {
                    int result = audioManager.requestAudioFocus(listener
                            , AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        Log.d("AudioManager", "I get Audio right: ");
                        break;
                    }
                    i++;
                } while (i < 10);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * 初始化分享图片
     */
    private void initShareImg() {
        if (mIsNetPic) {
            PicUtil.getbitmapAndwrite(mShareImgPath, AppConfig.DEFAULT_IMAGE_PATH + "share_logo_ipnda.png");
        } else {
            PicUtil.copyImage2SDCard(mContext, R.drawable.share_logo_ipnda, mShareImgPath);
        }
    }


    WeakReference<PlayLiveController> mLiveControllerWref;
    WeakReference<PlayVodController> mVodControllerWref;

    WebView mWebview;
    public void setLiveController(PlayLiveController controller) {
        this.mLiveControllerWref = new WeakReference<PlayLiveController>(controller);
    }

    public void setVodController(PlayVodController controller) {
        this.mVodControllerWref = new WeakReference<PlayVodController>(controller);
    }
    public void setLoad(WebView webview) {
        this.mWebview = webview;
    }

}
