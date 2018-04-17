package cn.cntv.play.thread;

import com.cntv.cbox.player.core.CBoxP2PCore;
import cn.cntv.play.core.CBoxStaticParam;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class P2PInitThread implements Runnable {
	private Handler handler;
	private CBoxP2PCore mCBoxP2PCore;
	private int iDelayTime = 300;

	public P2PInitThread(Handler handler) {
		this.handler = handler;
		this.mCBoxP2PCore = CBoxP2PCore.getInstance();
	}

	public void run() {
		int runState = 0;
		if (handler == null) {
			return;
		}
		handler.removeMessages(CBoxStaticParam.P2P_INIT);
		handler.removeMessages(CBoxStaticParam.P2P_INIT_SUCCESS);
		handler.removeMessages(CBoxStaticParam.P2P_INIT_FAIL);

		try {
			runState = Integer.parseInt(mCBoxP2PCore.InstanceGetStat());
		} catch (Exception e) {
			Message msg = new Message();
			msg.what = CBoxStaticParam.P2P_INIT_FAIL;
			msg.obj = "fff";
			handler.sendEmptyMessage(CBoxStaticParam.P2P_INIT_FAIL);
			// System.out.println(e.printStack);
		}
		if (runState == 5) {
			// CBoxLog.w("State:OK");
			// Log.e("jsx=P2PInitThread", "State:OK");
			// Log.e("wang", "初始化成功.State:OK");
			handler.sendEmptyMessage(CBoxStaticParam.P2P_INIT_SUCCESS);
		} else {
			// CBoxLog.w("State:"+runState);

			// Log.e("jsx=P2PInitThread=State=", "" + runState);
			// Log.e("wang","getstr="+ mCBoxP2PCore.InstanceGetStatStr());
			// Log.e("wang","getvesion="+ mCBoxP2PCore.InstanceGetVersion());

			Message msg = new Message();
			msg.obj = mCBoxP2PCore.InstanceGetStatStr();
			// if (runState == 3) {
			// msg.what = CBoxStaticParam.P2P_INIT_UPDATE;
			// handler.sendMessage(msg);
			// }
			if (runState < 0) {
				msg.what = CBoxStaticParam.P2P_INIT_FAIL;
				handler.sendMessage(msg);
			} else if (runState == 3) {
				msg.what = CBoxStaticParam.P2P_INIT_UPDATE;
				handler.sendMessage(msg);
			} else {
				msg.what = CBoxStaticParam.P2P_INIT;
				handler.sendMessageDelayed(msg, iDelayTime);
			}
		}
	}
}
