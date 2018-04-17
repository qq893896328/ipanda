package cn.cntv.app.ipanda.ui.personal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import cn.cntv.app.ipanda.AppConfig;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.utils.BitmapUtil;
import cn.cntv.app.ipanda.utils.FileUtils;
import cn.cntv.app.ipanda.view.ClipImageLayout;

public class PersonalClipPictureActivity extends BaseActivity implements View.OnClickListener{

    private ClipImageLayout mClipImage;
    private TextView mSelectSure,mSelectCancel;
    String path = AppConfig.DEFAULT_IMAGE_PATH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clippicture);
        init();
    }
    private void init(){
        mClipImage = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        mSelectCancel = (TextView) findViewById(R.id.tv_photocancel);
        mSelectSure = (TextView) findViewById(R.id.tv_photosure);

        mSelectCancel.setOnClickListener(this);
        mSelectSure.setOnClickListener(this);
        Intent intent = getIntent();
        Uri uri = (Uri) intent.getExtras().get("uri");
        String path = FileUtils.getFilePath(this, uri);

        try {
            int degree = BitmapUtil.readBitmapDegree(path);
            Bitmap bitmap = BitmapUtil.create(path, 1080, 1920);
            if (degree != 0) {
               bitmap = BitmapUtil.rotateBitmap(degree, bitmap);
            }
            mClipImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String savaBitmap(Bitmap bitmap){
        FileOutputStream b = null;
        String filename="";
//        if (Environment.getExternalStorageState()
//                .equals(Environment.MEDIA_MOUNTED)) {
//
//        } else {
//            Toast.makeText(this,
//                    "SD卡不存在", Toast.LENGTH_SHORT).show();
//            return null;
//        }
        File file = new File(path);

        if(!file.exists()){
            file.mkdirs();
        }

        filename = path +File.separator+android.text.format.DateFormat
                .format("yyyyMMddkkmmss",
                        new Date()).toString()+".jpg";


        try {
            b = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件


            /**
             * 这里做上传服务器的操作
             */

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return filename;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_photosure:
                Bitmap bitmap = mClipImage.clip();
                bitmap = BitmapUtil.zoomBitmap(bitmap, 300,300);
                Intent intent = new Intent();
                intent.putExtra("file",savaBitmap(bitmap));
                PersonalClipPictureActivity.this.setResult(123,intent);

                PersonalClipPictureActivity.this.finish();
                break;
            case R.id.tv_photocancel:
                finish();
                break;

        }
    }


}
