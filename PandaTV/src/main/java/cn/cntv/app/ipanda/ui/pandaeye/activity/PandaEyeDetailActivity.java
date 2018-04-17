package cn.cntv.app.ipanda.ui.pandaeye.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.di.HasComponent;
import cn.cntv.app.ipanda.di.components.DaggerFavoriteComponent;
import cn.cntv.app.ipanda.di.components.FavoriteComponent;
import cn.cntv.app.ipanda.domain.model.Favorite;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.pandaeye.fragment.PandaEyeDetailFragment;

public class PandaEyeDetailActivity extends BaseActivity implements HasComponent<FavoriteComponent> {


    public static final String TYPE = "type";
    public static final int TYPE_ARTICLE = 1000;

    private FavoriteComponent favoriteComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        
        initializeActivity(savedInstanceState);
        initializeInjector();

    }

    private void initializeInjector() {

        favoriteComponent = DaggerFavoriteComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private void initializeActivity(Bundle savedInstanceState) {

        int type = getIntent().getIntExtra(TYPE, 0);

        String url = (String) getIntent().getSerializableExtra("url");
        String title = (String) getIntent().getSerializableExtra("title");
        String path = (String) getIntent().getSerializableExtra("pic");
        String id = (String) getIntent().getSerializableExtra("id");

        Favorite favorite = new Favorite();
        favorite.setId(id);
        favorite.setPath(path);
        favorite.setTitle(title);
        favorite.setUrl(url);

        Fragment fragment = PandaEyeDetailFragment.newInstance(favorite, type);
        addFragment(R.id.fragmentContainer, fragment);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        View tView = getWindow().peekDecorView();

        if (tView != null) {

            // 获取输入法接口
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // 强制隐藏键盘
            imm.hideSoftInputFromWindow(tView.getWindowToken(), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1)) {
            try {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int i = 0;
                do {
                    int result = audioManager.requestAudioFocus(null,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        Log.d("Panda", "I get Audio right!");
                        break;
                    }
                    i++;
                } while (i < 10);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public FavoriteComponent getComponent() {
        return favoriteComponent;
    }
}
