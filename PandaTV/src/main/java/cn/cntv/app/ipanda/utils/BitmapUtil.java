package cn.cntv.app.ipanda.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;

import java.io.IOException;

/**
 * Created by yuerq on 2016/5/24.
 */
public class BitmapUtil {

  //  public static Bitmap

    public static Bitmap create(String path, int maxWidth, int maxHeight) {
        byte[] data = FileUtils.readFileContent(path);
        if (data == null) {
            return null;
        }

        return create(data, maxWidth, maxHeight);
    }

    public static Bitmap create(byte[] bytes, int maxWidth, int maxHeight) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        Bitmap bitmap;

        if (maxWidth <= 0 && maxHeight <= 0) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
        } else {
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
            int actualWidth = option.outWidth;
            int actualHeight = option.outHeight;

            // 计算出图片应该显示的宽高
            int desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth);

            option.inJustDecodeBounds = false;
            option.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
                    desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);

            // 做缩放
            if (tempBitmap != null
                    && (tempBitmap.getWidth() > desiredWidth || tempBitmap
                    .getHeight() > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
                        desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        }

        return bitmap;
    }

    /**
     * 图片缩放
     * @param bitmap 对象
     * @param w  最大宽
     * @param h  最大高
     * @return  新Bitmap对象
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= w && height <= h)
            return (bitmap);
        // 计算图片缩放比例
        float scale = 1;
        final float sx = w / (float) width;
        final float sy = h / (float) height;
        if (sx <= sy) {
            scale = sy;
        } else {
            scale = sx;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        try {
            if (null != bitmap) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bitmap = null;
        }
        return bit;
    }

    /**
     * 获取bitmap大小
     * @param bitmap
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static int getBitmapSize(Bitmap bitmap) {
         if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    static int findBestSampleSize(int actualWidth, int actualHeight,
                                  int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary,
                                           int actualPrimary, int actualSecondary) {
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }


    /**
     *  读取图像的旋转度
      */
    public static int readBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    
    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
    }

}
