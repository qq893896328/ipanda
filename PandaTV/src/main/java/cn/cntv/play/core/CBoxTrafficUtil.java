package cn.cntv.play.core;

import java.text.DecimalFormat;

import android.net.TrafficStats;

public class CBoxTrafficUtil {
	private float iRealRx,iRealTx,iMaxRx,iMaxTx;
	private float oldRealRx,oldRealTx,oldMaxRx,oldMaxTx;
	
    public CBoxTrafficUtil(){
    	initTraffic();
    }
    public void initTraffic(){
    	iRealRx = iRealTx = iMaxRx = iMaxTx = 0;
    	oldRealRx = oldRealTx = oldMaxRx = oldMaxTx = 0;
    }
    public void compute(){
    	float tempRx = TrafficStats.getTotalRxBytes();
    	float tempTx = TrafficStats.getTotalTxBytes();
    	
    	if(oldRealRx <=0){
    		iRealRx = 1;
    	}else{
    		iRealRx = tempRx - oldRealRx;
    	}
    	if(oldRealTx <=0){
    		iRealTx = 1;
    	}else{
    		iRealTx = tempTx - oldRealTx;
    	}
    	oldRealRx = tempRx;
    	oldRealTx = tempTx;
    }
    /**
     * ��ȡʵʱ��������
     * */
    public String getRealRx(){
		return formatTraffic(iRealRx);	
    }
    /**
     * ��ȡʵʱ�ϴ�����
     * */
    public String getRealTx(){
		return formatTraffic(iRealTx);
    }
  
    public static String formatTraffic(float size) {
		String[] units = new String[] { "B", "KB", "MB", "GB", "TB", "PB" };
		double mod = 1024.0;
		int i = 0;
		while (size >= mod) {
			size /= mod;
			i++;
			if (i >= units.length) {
				i = units.length - 1;
				break;
			} else {
				i = i;
			}
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(1);
		df.setMinimumFractionDigits(1);
		return df.format(size) + units[i];
	}

}
