package cn.cntv.app.ipanda.ui.personal.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;

public class PersonalAboutUsActivity extends BaseActivity{
	private TextView titleLeft,titleCenter;
	private TextView versionName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_about);

		initView();
	}

	private void initView(){
		titleLeft = (TextView)this.findViewById(R.id.common_title_left);
		titleCenter = (TextView)this.findViewById(R.id.common_title_center);
		versionName = (TextView)this.findViewById(R.id.about_version);
		
		titleCenter.setText(R.string.set_about_panda);
		versionName.setText(getVersion());
	
		titleLeft.setOnClickListener(new ViewClick());
	}
	
	
	
	 public String getVersion() {
		      try {
		          PackageManager manager = this.getPackageManager();
		          PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
		          String version = info.versionName;
		         return getString(R.string.app_name)+" V" + version;
		     } catch (Exception e) {
		         e.printStackTrace();
		         return getString(R.string.have_no_version_name);
		     }
		 }
	
	
	
	   
	class ViewClick implements OnClickListener{
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.common_title_left:
				finish();
				break;
			}
		}
		
	}
}
	