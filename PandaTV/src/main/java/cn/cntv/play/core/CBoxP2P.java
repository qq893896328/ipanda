package cn.cntv.play.core;

import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.cntv.cbox.player.core.CBoxP2PCore;

import cn.cntv.play.thread.P2PBufferThread;
import cn.cntv.play.thread.P2PInitThread;

public class CBoxP2P {
    //	  private Context context;
    private Handler initHandler, playHandler;
    private CBoxP2PCore mCBoxP2PCore;
    private P2PInitThread pThread;
    private P2PBufferThread bThread;

    private String clientId, port, playId, portStr, bufferStr, playUrl;

    public CBoxP2P(Handler handler) {
//    	  this.context = context;
        this.initHandler = handler;
        this.clientId = "cntv.cn." + (int) (Math.random() * 100000000 + 1);
        this.portStr = "GetWebPort:void";
        this.playId = null;
        this.port = null;
        this.pThread = null;     
        this.bThread = null;  
        String path = "SD=" + CBoxFileUtil.getAppFilePath() + "&SYS=" + CBoxFileUtil.getDataPath() + "cn.cntv.app.ipanda";
       // String path = "SD=" + CBoxFileUtil.getAppFilePath() + "&SYS=" + CBoxFileUtil.getDataPath() + "cn.cntv";
        Log.e("wang", "Path:" + path);
        this.mCBoxP2PCore = CBoxP2PCore.getInstance();
        this.mCBoxP2PCore.InstanceAutoStart(path);
        
        Log.e("wang", "p2p="+mCBoxP2PCore.InstanceGetStatStr());
        
        Log.e("jsx=Path", "new Path:" + path);
//    	  this.mCBoxP2PCore = CBoxP2PCore.getInstance(context, handler);
//    	  this.mCBoxP2PCore.InstanceAutoStart(CBoxFileUtil.getAppFilePath());
    }

    //start p2pinit thread
    public void Start() {
        pThread = new P2PInitThread(initHandler);
        ThreadManager.getLongPool().execute(pThread);
        
        
    	//  pThread.start();
        
             
    }     
       

    //play start p2pbuffer thread
    public void Play(String id, Handler playHandler) {
        this.playHandler = playHandler;
        if (port == null) {
            port = mCBoxP2PCore.InstanceGetP2PState(portStr);
        }
        if (id == null) {
            id = playId;
        } else {
            playId = id;
        }
        bufferStr = createBufferString();
        bThread = new P2PBufferThread(playHandler, bufferStr);
        ThreadManager.getLongPool().execute(bThread);
//		  bThread.start();
    }

    public void Stop() {
//    	  if (playHandler != null) {
//    		  StopChannel();
//    			playHandler.removeMessages(CBoxStaticParam.P2P_BUFFER);
//		}
        mCBoxP2PCore.InstanceAutoStop();
    }

    public void StopHandler() {
        if (bThread != null) {
            bThread.StopBufferHandler();
//    		  bThread.interrupt();
            bThread = null;
        }
        playHandler = null;
    }


    private String createBufferString() {
        String verTag = "&Ver=1&";
        if (CBoxPlayerUtil.systemVersions() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            verTag = "&Ver=0&";
        }
        return "GetBufferState:ClientID=" + clientId + "&ChannelID=" + playId + verTag;
    }


    public String StopChannel() {
        String arg = "StopChannel:ClientID=" + clientId + "&ChannelID=" + playId;
        return mCBoxP2PCore.InstanceGetP2PState(arg);
    }

    public String createPlayUrl() {
        return "http://127.0.0.1:" + port + "/plug_in/M3u8Mod/LiveStream.m3u8?ClientID=" + clientId + "&ChannelID=" + playId;
    }

    private String P2PStats(String arg) {
        String str = "";
        if (!TextUtils.isEmpty(arg)) {
            str = mCBoxP2PCore.InstanceGetP2PState(arg);
        }
        return str;
    }

    /**
     * 核心版本号
     **/
    public String P2PCoreVN() {
        return mCBoxP2PCore.InstanceGetCurVN();
    }

    /**
     * 统计信息-开始缓冲
     **/
    public void P2PBegin(String id, String info) {
        P2PStats("SetGSInfor:state=" + id + "+beginload&" + info);
    }

    /**
     * 统计信息-缓冲结束
     **/
    public void P2PEnd(String id, String info) {
        P2PStats("SetGSInfor:state=" + id + "+endload&" + info);
    }

    /**
     * 传入其他需要统计的信息。如：页面地址.频道名称.浏览器信息...等(串名=串值&串名=串值&串名=串值)
     *
     * @return
     **/
    public String P2PSetInfo(String info) {
        return P2PStats("SetGSInfor:" + info);
    }

    /**
     * 在播放过程中暂停和串值更新(串名=串值&串名=串值&串名=串值)
     **/
    public void P2PPauseInfo(String id, String info) {
        CBoxLog.w("P2PLockInfo:" + id);
        String setInfo = "SetGSInfor:state=" + id + "+pause";
        if (!TextUtils.isEmpty(info)) {
            setInfo += "&&" + info;
        }
        P2PStats(setInfo);
    }

    /**
     * 在播放过程中重新播放和串值更新(串名=串值&串名=串值&串名=串值)
     **/
    public void P2PPlayInfo(String id, String info) {
        CBoxLog.w("P2PReaginInfo:" + id);
        String setInfo = "SetGSInfor:state=" + id + "+play";
        if (!TextUtils.isEmpty(info)) {
            setInfo += "&&" + info;
        }
        P2PStats(setInfo);
    }

    /**
     * 在播放过程中卡顿和串值更新(串名=串值&串名=串值&串名=串值)
     **/
    public void P2PLockInfo(String id, String info) {
        CBoxLog.w("P2PLockInfo:" + id);
        String setInfo = "SetGSInfor:state=" + id + "+bufwait";
        if (!TextUtils.isEmpty(info)) {
            setInfo += "&&" + info;
        }
        P2PStats(setInfo);
    }

    /**
     * 在播放过程中恢复和串值更新(串名=串值&串名=串值&串名=串值)
     **/
    public void P2PReaginInfo(String id, String info) {
        CBoxLog.w("P2PReaginInfo:" + id);
        String setInfo = "SetGSInfor:state=" + id + "+bufend";
        if (!TextUtils.isEmpty(info)) {
            setInfo += "&&" + info;
        }
        P2PStats(setInfo);
    }

    /**
     * 频道播放结束-统计模块结束统计信息报告
     **/
    public void P2PFinish(String id) {
        P2PStats("SetGSInfor:state=" + id + "+end");
    }
}
