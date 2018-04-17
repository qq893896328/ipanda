package cn.cntv.app.ipanda.ui.personal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gridsum.mobiledissector.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.db.DBInterface;
import cn.cntv.app.ipanda.db.entity.PlayHistoryEntity;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.personal.adapter.PersonalHistoryListViewAdapter;
import cn.cntv.app.ipanda.ui.personal.fragment.PersonalHistoryFragment;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;

public class PersonalHistoryActivity extends BaseActivity {

	private FragmentManager mFragManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);
		PersonalHistoryFragment personalHistoryFragment=new PersonalHistoryFragment();
		mFragManager =getSupportFragmentManager();
		mFragManager.beginTransaction().replace(R.id.fragmentContainer, personalHistoryFragment).commit();
	}
}