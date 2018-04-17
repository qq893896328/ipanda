package cn.cntv.app.ipanda.ui.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;

public class PersonalAgreePostActivity extends BaseActivity {
    private static final String sStyleKey = "StyleKey";
   // private static final int sDefaultValue = R.style.ResourceBlueStyle;
    TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setTheme(getPersistStyle());
        setContentView(R.layout.activity_personal_agree_post);
        findViewById(R.id.agree_post_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mContent = (TextView) findViewById(R.id.agree_post_text);
//        try {
//            InputStream is = getResources().getAssets().open("cc.html");
//            String content = inputStream2String(is, 1024);
//            content = Html.fromHtml(content).toString();
//            mContent.setText(content);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private String inputStream2String(InputStream inputStream, int bufferSize)
            throws Exception {
        if (inputStream == null || bufferSize < 1) {
            return null;
        }
        int i = -1;
        byte[] b = new byte[bufferSize];
        StringBuffer sb = new StringBuffer();
        while ((i = inputStream.read(b)) != -1) {
            sb.append(new String(b, 0, i));
        }
        return sb.toString();
    }
}
