package cn.cntv.app.ipanda.ui.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

import cn.cntv.app.ipanda.manager.WeiboShareManager;

/**
 * Created by maqingwei on 16/8/4.
 */
public class WeiboShareCallbackActivity extends Activity implements IWeiboHandler.Response {

    private static final String TAG = "WeiboCallbackActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeiboShareManager weiboShareManager = WeiboShareManager.get(this);
        if (getIntent() != null) {
            weiboShareManager.getWeiboShareAPI().handleWeiboResponse(getIntent(), this);
        }
        Log.e(TAG, "onCreate: ");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WeiboShareManager.get(this).getWeiboShareAPI().handleWeiboResponse(intent, this);
    }


    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Toast.makeText(this, "取消分享", Toast.LENGTH_SHORT).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Toast.makeText(this, "分享失败 " + baseResponse.errMsg, Toast.LENGTH_SHORT).show();
                break;
        }
        finish();
    }

}