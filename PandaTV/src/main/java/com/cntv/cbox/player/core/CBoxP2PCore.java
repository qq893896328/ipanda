package com.cntv.cbox.player.core;

import android.util.Log;

public class CBoxP2PCore {
	/*P2P 2.0*/
	private native String AutoStart(String SD_PATH);
	private native String AutoStop();
	private native String GetStat();
	private native String GetStatStr();
	private native String GetVersion();
	private native String GetCurVN();
	private native String GetP2PState(String arg);
	
	private static CBoxP2PCore mInstance = null;
	
	static {
		try {
			Log.e("jsx==loadcntvlive2", "cntvlive2");
			System.loadLibrary("cntvlive2");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("jsx==loaderror", e+"");
		}
		
	}
	
	public static synchronized CBoxP2PCore getInstance(){
		if(mInstance == null){
			mInstance = new CBoxP2PCore();
		}
		return mInstance;		
	}
	private CBoxP2PCore(){}
	
	public String InstanceAutoStart(String SD_PATH){
		return AutoStart(SD_PATH);
	}
	public String InstanceAutoStop(){
		return AutoStop();
	}
	public String InstanceGetStatStr(){
		return GetStatStr();
	}
	public String InstanceGetStat(){
		return GetStat();
	}
	public String InstanceGetVersion(){
		return GetVersion();
	}
	public String InstanceGetCurVN(){
		return GetCurVN();
	}
	public String InstanceGetP2PState(String arg){
		return GetP2PState(arg);
	}
	public void  StateChange(String s){  
        Log.e("P2P_State_Change", "@@@@@@@@@@@@@@@@@@@@@@@@@@@  in java code "+ s+"  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }
	public void init(){
		
        Log.e("P2P_State_Init", "@@@@@@@@@@@@@@@@@@@@@@@@@@@  Init  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}
}
