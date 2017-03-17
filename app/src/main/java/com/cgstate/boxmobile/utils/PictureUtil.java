package com.cgstate.boxmobile.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureUtil {


    public static Bitmap getSmallBitmap1(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }


    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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


    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }


    /**
     * 质量压缩
     *
     * @param image
     * @param maxkb
     * @return
     * @author ping 2015-1-5 下午1:29:58
     */
    public static Bitmap compressBitmap(Bitmap image, int maxkb) {
        //L.showlog(压缩图片);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        Log.i("PictureUtil", "原始大小: " + baos.toByteArray().length);
        while (baos.toByteArray().length / 1024 > maxkb) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
//          Log.i(test,压缩一次!);
            baos.reset();// 重置baos即清空baos
            quality -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
//      Log.i(test,压缩后大小 + baos.toByteArray().length);


        byte[] bytes = baos.toByteArray();
        if (!image.isRecycled()) {
            image.recycle();
        }
        Bitmap bitmap2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     *
     */

    public static boolean compressAndSave2(String srcFileUrl, String destFileUrl, int maxKB) {

        Bitmap srcBitMap = getSmallBitmap(srcFileUrl);


        if (srcBitMap != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            srcBitMap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            Log.d("PictureUtil", "压缩前的baos.toByteArray().length:" + (baos.toByteArray().length));

            int count = 0;

            while (baos.toByteArray().length / 1024 > maxKB && quality > 0) {
                baos.reset();
                quality -= 10;
                srcBitMap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                count++;
                Log.d("PictureUtil", "图片压缩了" + count + "次");
            }
            if (quality < 0) {
                return false;
            }
            byte[] bytes = baos.toByteArray();

            Log.d("PictureUtil", "压缩后的bytes.length:" + bytes.length);

            if (!srcBitMap.isRecycled()) {
                srcBitMap.recycle();
            }

            BufferedOutputStream bufferedOutputStream = null;
            File destFile = new File(destFileUrl);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(destFile);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                bufferedOutputStream.write(bytes);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return true;
    }


    private static Bitmap getSmallBitmap(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(filePath);
            BitmapFactory.decodeStream(inputStream, null, options);

//        BitmapFactory.decodeFile(filePath, options);

            options.inSampleSize = 2;

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            Bitmap bm = BitmapFactory.decodeFile(filePath, options);
            if (bm == null) {
                return null;
            }
            int degree = readPictureDegree(filePath);
            if (degree != 0) {
                bm = rotateBitmap(bm, degree);
            }
            return bm;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 压缩保存
     */

    public static boolean compressAndSave(String srcFileUrl, String destFileUrl, int maxKB) {


        File srcFile = new File(srcFileUrl);

        if (srcFile == null) {
            return false;
        }

        InputStream inputStream = null;
        Bitmap srcBitMap = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(srcFile));
            srcBitMap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        if (srcBitMap != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            srcBitMap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            Log.d("PictureUtil", "压缩前的baos.toByteArray().length:" + (baos.toByteArray().length));

            while (baos.toByteArray().length / 1024 > maxKB && quality > 0) {
                baos.reset();
                quality -= 10;
                srcBitMap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                Log.d("PictureUtil", "执行了");
            }
            if (quality < 0) {
                return false;
            }
            byte[] bytes = baos.toByteArray();

            Log.d("PictureUtil", "压缩后的bytes.length:" + bytes.length);

            if (!srcBitMap.isRecycled()) {
                srcBitMap.recycle();
            }

            BufferedOutputStream bufferedOutputStream = null;
            File destFile = new File(destFileUrl);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(destFile);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                bufferedOutputStream.write(bytes);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return true;
    }

    /**
     * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     * 官网：获取压缩后的图片
     *
     * @param res
     * @param resId
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 官网：获取压缩后的图片
     *
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filepath,
                                                     int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                       int reqWidth, int reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] data = baos.toByteArray();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 计算压缩比例值(改进版 by touch_ping)
     * <p/>
     * 原版2>4>8...倍压缩
     * 当前2>3>4...倍压缩
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度O
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int picheight = options.outHeight;
        final int picwidth = options.outWidth;

        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;

        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight >= reqHeight
                    && targetwidth >= reqWidth) {
                inSampleSize += 1;
                targetheight = picheight / inSampleSize;
                targetwidth = picwidth / inSampleSize;
            }
        }

        return inSampleSize;
    }
}