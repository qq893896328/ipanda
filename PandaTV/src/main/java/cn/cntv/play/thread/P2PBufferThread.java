package cn.cntv.play.thread;

import com.cntv.cbox.player.core.CBoxP2PCore;

import cn.cntv.play.core.CBoxStaticParam;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class P2PBufferThread implements Runnable {
    private Handler handler;
    private String bufferStr;
    private CBoxP2PCore mCBoxP2PCore;
    private int iDelayTime = 300;

    public P2PBufferThread(Handler handler, String bufferStr) {
        this.handler = handler;
        this.mCBoxP2PCore = CBoxP2PCore.getInstance();
        this.bufferStr = bufferStr;
    }

    public void StopBufferHandler() {
        handler = null;
        this.mCBoxP2PCore = null;
    }

    public void run() {
        int bufferState = 0;
        if (handler == null) {
            return;
        }
        handler.removeMessages(CBoxStaticParam.P2P_BUFFER);
        handler.removeMessages(CBoxStaticParam.P2P_BUFFER_SUCCESS);
        handler.removeMessages(CBoxStaticParam.P2P_BUFFER_FAIL);

        try {
            bufferState = Integer.parseInt(mCBoxP2PCore.InstanceGetP2PState(bufferStr));
            if (bufferState == 200) {
                handler.sendEmptyMessage(CBoxStaticParam.P2P_BUFFER_SUCCESS);
            } else {
                if (bufferState == 404 || bufferState == 0) {

                    /***2013-12-20 新增 用于404超时***/
                    Message msg = new Message();
                    msg.what = CBoxStaticParam.P2P_BUFFER;
                    msg.obj = bufferState;
                    handler.sendMessageDelayed(msg, iDelayTime);

                } else {
                    sendError(bufferState);
                }
            }
        } catch (Exception e) {
            sendError(0);
        }
    }

    public void sendError(int errorNum) {
        if (handler == null) {
            return;
        }
        String errorStr = "";
        switch (errorNum) {
            case 501:
                errorStr = "您所在的地区处于限制播放区域！";
                break;
            case 502:
                errorStr = "您所选择的节目信号源中断！\r\n请重新选择。";
                break;
            case 503:
                errorStr = "播放数据正在准备。\r\n请稍候选择！";
                break;
            case 504:
                errorStr = "因版权原因。\r\n本时段节目暂停播放！";
                break;
            case 506:
                errorStr = "内核版本过低。\r\n请升级后观看！";
                break;
            default:
                errorStr = "系统错误,请重新运行!";
                break;
        }
        Message msg = new Message();
        msg.what = CBoxStaticParam.P2P_BUFFER_FAIL;
        msg.obj = errorNum;
        handler.sendMessage(msg);
    }
}