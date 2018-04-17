package cn.cntv.app.ipanda.ui.livechina.activity;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.play.PlayLiveController;

public class LiveChinaPlayActivity extends BaseActivity{
	PlayLiveController player;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_play_full_screen);
		
		
		
		
		RelativeLayout playLayout = (RelativeLayout) findViewById(R.id.giraffe_player);
//		int[] display = ComonUtils.getDisplay(this);
//		LayoutParams pLayoutParams = playLayout.getLayoutParams();
//		  //小窗口的比例
//  		float ratio = (float) 16 / 9;
//		pLayoutParams.height = (int) Math.ceil((float) display[0] / ratio);
//		playLayout.setLayoutParams(pLayoutParams);
		
		player = new PlayLiveController(this,playLayout);
		player.setScale16_9(playLayout);
		
//		player.tryFullScreen(true);
		
		String url = "http://asp.cntv.lxdns.com/asp/hls/850/0303000a/3/default/29978c0b08964a59a927b90836dd7485/850.m3u8";
		player.play(url);
		
		
		
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }
	
    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
            player.doStart();
        }
    }
    

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }
}
