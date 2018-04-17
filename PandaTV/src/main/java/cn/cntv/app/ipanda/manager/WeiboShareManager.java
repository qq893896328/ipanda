package cn.cntv.app.ipanda.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.ui.share.AccessTokenKeeper;

/**
 * Created by yuerq on 16/8/12.
 */
public class WeiboShareManager {

    private static final String TAG = "WeiboShareManager";

    private static WeiboShareManager sInstance;

    private IWeiboShareAPI mWeiboShareAPI;

    private Context mContext;

    private WeiboShareManager(Context context) {

        mContext = context.getApplicationContext();
    }

    public static WeiboShareManager get(Context context) {
        if (sInstance == null) {
            sInstance = new WeiboShareManager(context);
        }
        return sInstance;
    }

    public void onCreate() {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, Constants.tSinaAppId);
        mWeiboShareAPI.registerApp();  // 将应用注册到微博客户端
    }

    public IWeiboShareAPI getWeiboShareAPI() {
        return mWeiboShareAPI;
    }

    public void share(Activity activity, String text, Bitmap b) {
        final WeiboMultiMessage wbmsg = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = text;
        wbmsg.textObject = textObject;

        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        final ImageObject imageObject = new ImageObject();

        imageObject.setImageObject(b);
        wbmsg.imageObject = imageObject;

        //发送分享消息请求,拉起分享页面
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = wbmsg;
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mContext);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        AuthInfo authInfo = new AuthInfo(mContext, Constants.tSinaAppId, Constants.tSinaRedirectUrl, Constants.tSinaScope);
        mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {

                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(mContext, newToken);

            }

            @Override
            public void onWeiboException(WeiboException e) {
            }

            @Override
            public void onCancel() {
            }

        });
    }

}
