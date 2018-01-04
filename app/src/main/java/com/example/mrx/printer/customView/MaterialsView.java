package com.example.mrx.printer.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ImageUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/10/12.
 */

public class MaterialsView extends View {

    private static final int SINGLE_ANGLE = 60;

    private static final int START_ANGLE = 220;

    float minValue;

    float maxValue;

    float currentValue;

    Bitmap materials;

    BitmapShader shader;

    Bitmap shaderBitmap;

    Paint paint;

    RectF canvasRect;

    public void updateValue(float minValue, float maxValue, float currentValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        invalidate();
    }

    public MaterialsView(Context context) {
        super(context);
        initViews();
    }

    public MaterialsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public MaterialsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }


    private void initViews() {
        materials = ImageUtils.getBitmap(R.drawable.materials_1);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        canvasRect = new RectF();

        minValue = 0;
        maxValue = 100;
        currentValue = 71;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int width = right - left;
            int height = bottom - top;
            if (width <= 0 || height <= 0) {
                return;
            }
            shaderBitmap = ImageUtils.scale(materials, width, height);
            shader = new BitmapShader(shaderBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            float rectLeft = (float) width * 0.25f;
            float rectTop = (float) height * 0.25f;
            float rectRight = (float) width * 0.75f;
            float rectBottom = (float) height * 0.75f;
            float strokeWidth = (float) width * 0.5f;
            canvasRect.set(rectLeft, rectTop, rectRight, rectBottom);
            paint.setShader(shader);
            paint.setStrokeWidth(strokeWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int angleNum = angleNum();

        canvas.drawArc(canvasRect, START_ANGLE, angleNum * SINGLE_ANGLE, false, paint);
    }

    private int angleNum() {
        if (!(maxValue > minValue && (currentValue >= minValue && currentValue <= maxValue))) {
            return 0;
        }
        float rate = (currentValue - minValue) / (maxValue - minValue);
        if (rate < 0.1) {
            return 0;
        } else if (rate < 0.3) {
            return 1;
        } else if (rate < 0.5) {
            return 2;
        } else if (rate < 0.7) {
            return 3;
        } else if (rate < 0.9) {
            return 4;
        } else {
            return 5;
        }
    }
}
