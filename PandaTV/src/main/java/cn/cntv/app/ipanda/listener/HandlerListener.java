package cn.cntv.app.ipanda.listener;

import android.os.Handler;

import cn.cntv.app.ipanda.custom.HandlerMessage;

/**
 * @author: Xiao JinLai
 * @Date: 2015-12-11 17:58
 * @Description: Handler 监听接口
 */
public interface HandlerListener {

    void handlerMessage(HandlerMessage msg);
}