package cn.cntv.app.ipanda.ui.personal.activity;

import android.os.Bundle;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.personal.fragment.CollectionFragment;

public class PersonalShouCangActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        addFragment(R.id.fragmentContainer,new CollectionFragment());

    }

}
