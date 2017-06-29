package com.cq.ln.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by YS on 2016/2/26.
 * 类说明：处理图片的工具类.
 */
public class ImageTool {
    private static final String TAG = "ImageTool";
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int TOP = 3;
    private static final int BOTTOM = 4;
    private static ImageLoader imageLoader;
    private static DisplayImageOptions options = null;

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }


    public static Bitmap onSizeChange(Bitmap bitmap, int xSize, int ySize) {
        return Bitmap.createScaledBitmap(bitmap, xSize, ySize, false);
    }


    /**
     * Bitmap → byte[]
     *
     * @param bitmap Bitmap
     * @return byte[]
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }


    /**
     * 把图片设置上红色边框
     *
     * @param bitmap bitmap 格式图片
     * @return Bitmap
     */
    public static Bitmap setFrameBitmap(Bitmap bitmap) {

        int w = bitmap.getWidth() - 20;
        int h = bitmap.getHeight() - 20;
        double lineStrokeWidth = 20;

        int lineStrokeWidthInt = (int) lineStrokeWidth;
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint sourceBitmapPaint = new Paint();
        final Rect rect = new Rect(-lineStrokeWidthInt, -lineStrokeWidthInt, w - lineStrokeWidthInt, h - lineStrokeWidthInt);
        sourceBitmapPaint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        Paint storePaint = new Paint();
        storePaint.setColor(Color.RED);


        storePaint.setStyle(Paint.Style.STROKE);
        storePaint.setStrokeWidth((float) lineStrokeWidth);

        canvas.drawRect(0, 0, w, h, storePaint);
        sourceBitmapPaint.setColor(color);
        sourceBitmapPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
        canvas.drawBitmap(bitmap, rect, rect, sourceBitmapPaint);
        return output;
    }

    public static Bitmap colorFrameBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        double lineStrokeWidth = -1;
        final double lineWidth = w * 0.05;
        final double lineHeight = h * 0.05;
        lineStrokeWidth = (lineWidth > lineHeight ? lineWidth : lineHeight);

        int lineStrokeWidthInt = (int) lineStrokeWidth;
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        final Rect rect = new Rect(lineStrokeWidthInt, lineStrokeWidthInt, w - lineStrokeWidthInt, h - lineStrokeWidthInt);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * @param context 获取图片加载器实例
     * @return
     */
    public static ImageLoader getImageLoader(Context context) {
        if (imageLoader != null)
            return imageLoader;


//        StorageUtils.getAppCacheDir(context)
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.
                Builder(context).threadPriority(Thread.NORM_PRIORITY - 2) // 线程优先级
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(new UnlimitedDiscCache(new File(StorageUtils.getAppCacheDir(context))))
                .diskCacheSize(80 * 1024 * 1024) // 50
                .threadPoolSize(5).tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        imageLoader.init(config);


        return imageLoader;
    }


    /*********************************************
     * @param pix
     * @param imageRes （默认图，不存在图，失败图）
     * @return
     * @fun 设置图片加载器配置
     *********************************************/
    public static DisplayImageOptions getImageOptions(int pix, int... imageRes) {
        if (options != null)
            return options;
        int Image_Loading, Image_EmptyUri, ImageFail;
        if (imageRes.length >= 1)
            Image_Loading = imageRes[0];
        else
            return null;
        if (imageRes.length >= 2)
            Image_EmptyUri = imageRes[1];
        else
            Image_EmptyUri = imageRes[0];
        if (imageRes.length >= 3)
            ImageFail = imageRes[2];
        else
            ImageFail = imageRes[0];
        options = new DisplayImageOptions.Builder().showImageOnLoading(Image_Loading) // 默认
                .showImageForEmptyUri(Image_EmptyUri) // URI不存在
                .showImageOnFail(ImageFail) // 加载失败
                .displayer(new RoundedBitmapDisplayer(pix)) // 图片圆角
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.NONE_SAFE).bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    public interface LoadSuccess { // 成功回调
        void onSucceeded(Bitmap bitmap);
    }

    public interface Loadfail { // 失败回调
        void onFailed();
    }

    /**
     * @param ctx
     * @param imageButton 图片控件
     * @param img         图片地址
     * @param pix         圆角像素
     * @param defaultImg  默认图片
     * @param success     成功回调
     * @param fail        失败回调
     **/
    public static void loadImage(final Context ctx, final ImageView imageButton, String img, int pix, final int defaultImg, final LoadSuccess success, final Loadfail fail) {
        if (imageButton == null)
            return;
        ImageLoader imageLoader = getImageLoader(ctx);
//        final DisplayImageOptions options = getImageOptions(pix, defaultImg);
//        String stringUrl = img;
        XLog.d(TAG, "图片地址：" + img);
        imageLoader.displayImage(img, imageButton, null, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                if (fail != null)
                    fail.onFailed();
                if (imageButton != null)
                    imageButton.setBackgroundDrawable(ctx.getResources().getDrawable(defaultImg));
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                if (success != null)
                    success.onSucceeded(arg2);
                if (imageButton != null) {
                    imageButton.setImageBitmap(null);
                    imageButton.setBackgroundDrawable(bitmapToDrawable(arg2));
                }
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                if (fail != null)
                    fail.onFailed();
                if (imageButton != null)
                    imageButton.setBackgroundDrawable(ctx.getResources().getDrawable(defaultImg));
            }
        }, null);
    }

    public static void loadImageWithUrl(Context context, String url, ImageView imageView, DisplayImageOptions options) {
        ImageLoader imageLoader = getImageLoader(context);
        XLog.d(TAG, "图片地址：" + url);
        imageLoader.displayImage(url, imageView, options);
    }

    /**
     * @param context      仅仅使用于首页图片加载-->加载图片，并且后续自己处理
     * @param url
     * @param imageView
     * @param options
     * @param failImageRes
     */
    public static void loadImageAndUserHandleOption(final Context context, final String url, final ImageView imageView, final DisplayImageOptions options, final int failImageRes) {
        final ImageLoader imageLoader = getImageLoader(context);
        XLog.d(TAG, "图片地址：" + url);
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageLoader.displayImage(url, imageView, options, new mainPageLoadListener(context, failImageRes));
            }
        });
    }

    static class mainPageLoadListener implements ImageLoadingListener {
        int failImageRes = -1;
        Context context;

        public mainPageLoadListener(Context context, int failImageRes) {
            this.context = context;
            this.failImageRes = failImageRes;
        }

        @Override
        public void onLoadingStarted(String s, View view) {
        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {
            if (failImageRes != -1) {//不同位置的控件放置不同的默认图片
                view.setBackgroundDrawable(context.getResources().getDrawable(failImageRes));
            }

        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));//设置成背景
        }

        @Override
        public void onLoadingCancelled(String s, View view) {
        }
    }


    /**
     * 根据一个网络连接(String)获取bitmap图像
     *
     * @param imageUri
     * @return
     * @throws MalformedURLException
     */
    public static Bitmap getbitmap(String imageUri) {
        Log.v(TAG, "getbitmap:" + imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.v(TAG, "image download finished." + imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "getbitmap bmp fail---");
            return null;
        }
        return bitmap;
    }


    /** */
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public synchronized static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /** */
    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public synchronized static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    public synchronized static Drawable toRoundCorner(Drawable drawable, int pixels) {
        long start = SystemClock.currentThreadTimeMillis();
        Bitmap bitmap = drawableToBitmap(drawable);
        Bitmap roudBitmap = toRoundCorner(bitmap, pixels);
        Drawable outdrawable = bitmapToDrawable(roudBitmap);
        XLog.d(TAG, "裁图耗时：" + (SystemClock.currentThreadTimeMillis() - start) + "毫秒。");
        return outdrawable;
    }

    /** */
    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public synchronized static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        if (bitmap == null)
            return Bitmap.createBitmap(new int[]{0xffD9DCDE}, 10, 10, Config.RGB_565);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xffD9DCDE;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /** */
    /**
     * 使圆角功能支持BitampDrawable
     *
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    public synchronized static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }

    /**
     * 转换图片成圆形图片
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public synchronized static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return Bitmap.createBitmap(new int[]{0xffD9DCDE}, 10, 10, Config.RGB_565);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int ovalLen = Math.min(width, height);
        Rect src = new Rect((width - ovalLen) / 2, (height - ovalLen) / 2, (width - ovalLen) / 2 + ovalLen, (height - ovalLen) / 2 + ovalLen);
        Rect dst = new Rect(0, 0, ovalLen, ovalLen);
        Bitmap output = Bitmap.createBitmap(ovalLen, ovalLen, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawOval(new RectF(0, 0, ovalLen, ovalLen), paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 获取本地图片
     *
     * @param localURI
     * @return
     */
    public synchronized static Bitmap getDiskBitmap(String localURI) {
        Uri uri = null;
        try {
            uri = Uri.parse(localURI);
        } catch (Exception ex) {
        }
        return getDiskBitmap(uri);
    }

    /**
     * 获取本地图片
     *
     * @param localURI
     * @return
     */
    public synchronized static Bitmap getDiskBitmap(Uri localURI) {
        if (localURI == null)
            return Bitmap.createBitmap(new int[]{0xffD9DCDE}, 10, 10, Config.RGB_565);
        File file = new File(localURI.getPath());
        return getDiskBitmap(file);
    }

    /**
     * 获取本地图片
     *
     * @param file
     * @return
     */
    public synchronized static Bitmap getDiskBitmap(File file) {
        Bitmap bitmap = null;
        try {
            if (file.exists()) {
                // BitmapFactory.Options options = new BitmapFactory.Options();
                // options.inSampleSize = 2;// 图片宽高都为原来的二分之一，即图片为原来的四分之一
                // bitmap = BitmapFactory.decodeFile(localURI.getPath(), options);
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                bitmap = BitmapFactory.decodeFile(file.getPath(), opt);
            }
        } catch (Exception ex) {
        }
        if (bitmap == null)
            bitmap = Bitmap.createBitmap(new int[]{0xffD9DCDE}, 10, 10, Config.RGB_565);
        return bitmap;
    }

    // 读取优化后的图片（最大不超过1500*1500）
    public synchronized static Bitmap readOptBitmapFile(String pathName) {
        return readOptBitmapFile(pathName, false);
    }

    // 读取优化后的图片（最大不超过1500*1500）
    public synchronized static Bitmap readOptBitmapFile(String pathName, boolean isHighQuality) {
        // 优化图片解析速度，防止OOM
        int baseSize = 1000;// inSampleSize 基数，得出inSampleSize四舍五入取整
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options); // 仅获取图片的宽和高
        int mW = options.outWidth;
        int mH = options.outHeight;
        BigDecimal nW = new BigDecimal(mW / baseSize).setScale(0, BigDecimal.ROUND_HALF_UP);
        BigDecimal nH = new BigDecimal(mH / baseSize).setScale(0, BigDecimal.ROUND_HALF_UP);
        int sampleSize = nW.compareTo(nH) == -1 ? nH.intValue() : nW.compareTo(nH) == 1 ? nW.intValue() : nW.intValue();// mW < mH == -1；mW > mH = 1; mW == mH = 0
        options.inSampleSize = sampleSize == 0 ? 1 : sampleSize;
        options.inPreferredConfig = isHighQuality ? Config.ARGB_8888 : Config.RGB_565;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, options);// 解析图片
        return bitmap;
    }

    /**
     * 读取路径中的图片，然后将其转化为缩放后的bitmap（相当于一半的采样率）
     *
     * @param path
     */
    public synchronized static void saveBefore(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = 2; // 图片长宽各缩小二分之一
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + " " + h);
        // savePNG_After(bitmap,path);
        saveJPGE_After(bitmap, path);
    }

    /**
     * 图片缩放到指定大小（等比）
     *
     * @param originalBitmap 原始的Bitmap
     * @param newWidth       自定义宽度
     * @param newHeight
     * @return 缩放后的Bitmap
     */
    public synchronized static Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight) {
        if (originalBitmap == null)
            return Bitmap.createBitmap(new int[]{0xffD9DCDE}, newWidth, newHeight, Config.RGB_565);
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        // 计算宽、高缩放率
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        if (width <= newWidth)
            scaleWidth = 1f;
        if (height <= scaleHeight)
            scaleHeight = 1f;
        float scale = scaleWidth > scaleHeight ? scaleHeight : scaleWidth < scaleHeight ? scaleWidth : scaleWidth;
        // 创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scale, scale);
        // 旋转图片 动作
        // matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    /**
     * 创建带倒影的图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);
        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * 保存图片为PNG
     *
     * @param bitmap
     * @param path
     */
    public synchronized static void savePNG_After(Bitmap bitmap, String path) {
        File file = new File(path);
        savePNG_After(bitmap, file);
    }

    /**
     * 保存图片为PNG
     *
     * @param bitmap
     * @param file
     */
    public synchronized static void savePNG_After(Bitmap bitmap, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param path
     */
    public synchronized static void saveJPGE_After(Bitmap bitmap, String path) {
        File file = new File(path);
        saveJPGE_After(bitmap, file);
    }

    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param file
     */
    public synchronized static void saveJPGE_After(Bitmap bitmap, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 水印
     *
     * @param src
     * @param watermark
     * @return
     */
    public synchronized static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 图片合成
     *
     * @return
     */
    public synchronized static Bitmap potoMix(int direction, Bitmap... bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }
        if (bitmaps.length == 1) {
            return bitmaps[0];
        }
        Bitmap newBitmap = bitmaps[0];
        // newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
        for (int i = 1; i < bitmaps.length; i++) {
            newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
        }
        return newBitmap;
    }

    private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second, int direction) {
        if (first == null) {
            return null;
        }
        if (second == null) {
            return first;
        }
        int fw = first.getWidth();
        int fh = first.getHeight();
        int sw = second.getWidth();
        int sh = second.getHeight();
        Bitmap newBitmap = null;
        if (direction == LEFT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, sw, 0, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == RIGHT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw, 0, null);
        } else if (direction == TOP) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, sh, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == BOTTOM) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, fh, null);
        }
        return newBitmap;
    }

    /**
     * Bitmap缩放到指定大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        // bitmap.recycle();
        return newbmp;
    }

    /**
     * 将Bitmap转换成指定大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * Drawable 转 Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * Bitmap 转 Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * localURI 转 Drawable
     *
     * @param localURI
     * @return
     */
    public static Drawable getDrawableByURI(Uri localURI) {
        return getDrawableByURI(localURI, -1);
    }

    public static BitmapDrawable getDrawableByURI(Uri localURI, int pixels) {
        Bitmap bm = null;
        switch (pixels) {
            case -1:// 不处理圆角
                bm = getDiskBitmap(localURI);
                break;
            case 0:// 圆形图片处理
                bm = toRoundBitmap(getDiskBitmap(localURI));
                break;
            default:// 圆角图片处理
                bm = toRoundCorner(getDiskBitmap(localURI), pixels);
                break;
        }
        return new BitmapDrawable(bm);
    }

    public static Drawable getDrawableByBitmap(Bitmap bitmap) {
        return getDrawableByBitmap(bitmap, -1);
    }

    public static BitmapDrawable getDrawableByBitmap(Bitmap bitmap, int pixels) {
        Bitmap bm = null;
        switch (pixels) {
            case -1:// 不处理圆角
                bm = bitmap;
                break;
            case 0:// 圆形图片处理
                bm = toRoundBitmap(bitmap);
                break;
            default:// 圆角图片处理
                bm = toRoundCorner(bitmap, pixels);
                break;
        }
        return new BitmapDrawable(bm);
    }

    /**
     * byte[] 转 bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * base64Str 转 bitmap
     *
     * @param base64Str
     * @return
     */
    public static Bitmap base64StringToBitmap(String base64Str) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(base64Str, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap == null ? Bitmap.createBitmap(new int[]{0xffD9DCDE}, 10, 10, Config.RGB_565) : bitmap;
    }

    /**
     * bitmap 转 byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, baos);
        return baos.toByteArray();
    }

    /**
     * byte 转 base64 String
     *
     * @param bytes
     * @return
     */
    public static String byteToBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * bitmap 转 base64 String
     *
     * @param bm
     * @return
     */
    public static String bitmapToBase64String(Bitmap bm) {
        return byteToBase64(bitmapToBytes(bm));
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public synchronized static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public synchronized static Drawable readBitMapForDrawable(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return bitmapToDrawable(BitmapFactory.decodeStream(is, null, opt));
    }

    // 将InputStream转换成Bitmap
    public synchronized static Bitmap InputStream2Bitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    // Bitmap转Drawable
    public synchronized static Drawable InputStream2Drawable(InputStream is) {
        Bitmap bitmap = InputStream2Bitmap(is);
        return (Drawable) bitmapToDrawable(bitmap);
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public synchronized static Bitmap compressImage(Bitmap image) {
        return compressImage(image, 80);
    }

    /**
     * 压缩图片（quality 50-100）
     *
     * @param image
     * @param quality
     * @return
     */
    public synchronized static Bitmap compressImage(Bitmap image, int quality) {
        return compressImage(image, quality, 100);// 最大输出为100KB图片
    }

    /**
     * 压缩图片（quality 50-100），maxSizeKB 图片容量最大不能超过多少kb
     *
     * @param image
     * @param quality
     * @param maxSizeKB
     * @return
     */
    public synchronized static Bitmap compressImage(Bitmap image, int quality, int maxSizeKB) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSizeKB) { // 循环判断如果压缩后图片是否大于maxSize kb,大于继续压缩（最小压缩到50%）
            if (options < 50)
                options = 50;
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options == 50)
                break;
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, opt);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 获取缩略图（待完善）
     *
     * @param path
     * @return
     */
    public synchronized static Bitmap getThumbnail(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int w = options.outWidth;
        int h = options.outHeight;
        int ie = w > h ? w : h;
        int be = ie / 20;// 应该直接除200的，但这里出20是为了增加一位数的精度
        if (be % 10 != 0)
            be += 10; // 尽量取大点图片，否则会模糊
        be = be / 10;
        if (be <= 0) // 判断200是否超过原始图片高度
            be = 1; // 如果超过，则不进行缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = be;
        options.inPreferredConfig = Config.RGB_565; // 默认是Bitmap.Config.ARGB_8888
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Bitmap newBitmap;
        try {
            newBitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100); // 生成新的缩略图
            bitmap.recycle();
            return newBitmap;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 将图片URI转为真实路径
     *
     * @param context
     * @param contentUri
     * @return
     */
    public synchronized static String getRealPathFromURI(Context context, Uri contentUri) {
        if (contentUri == null)
            return null;
        if (contentUri.getPath().startsWith(Environment.getExternalStorageDirectory().getPath()))
            return contentUri.getPath();
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    /**
     * string转成bitmap
     *
     * @param str
     */
    public static Bitmap convertStringToIcon(String str) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(str, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


}