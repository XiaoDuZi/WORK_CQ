package com.cq.ln.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cq.ln.R;

/**
 * Created by Administrator on 2016/3/7.
 */
public class BlurMaskFilterView extends ImageView {
    private Paint shadowPaint;// 画笔
    private Context mContext;// 上下文环境引用
    private Bitmap srcBitmap, shadowBitmap;// 位图和阴影位图

    private int x, y;// 位图绘制时左上角的起点坐标

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    private float width;
    private float height;

    public BlurMaskFilterView(Context context) {
        this(context, null);
    }

    public BlurMaskFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 记得设置模式为SOFTWARE
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        // 初始化画笔
        initPaint();

        // 初始化资源
        initRes(context);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        // 实例化画笔
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        shadowPaint.setColor(Color.DKGRAY);
        shadowPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));


//        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
//        shadowPaint.setColor(Color.DKGRAY);
//        shadowPaint.setMaskFilter(new BlurMaskFilter(10,BlurMaskFilter.Blur.OUTER));
//
    }

    /**
     * 初始化资源
     */
    private void initRes(Context context) {
        // 获取位图
        srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.focus_store);
        // 获取位图的Alpha通道图
        shadowBitmap = srcBitmap.extractAlpha();
        /*
         * 计算位图绘制时左上角的坐标使其位于屏幕中心
         */
//        x = MeasureUtil.getScreenSize((Activity) mContext)[0] / 2 - srcBitmap.getWidth() / 2;
//        y = MeasureUtil.getScreenSize((Activity) mContext)[1] / 2 - srcBitmap.getHeight() / 2;


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 先绘制阴影
//        canvas.drawBitmap(shadowBitmap, x, y, shadowPaint);
//        Matrix localMetr = new Matrix();
//        localMetr.setScale(width/100,height/100);
//        canvas.drawBitmap(srcBitmap,localMetr,null);



        // 再绘制位图
//        canvas.drawBitmap(srcBitmap, x, y, null);
    }

}
