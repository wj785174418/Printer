package com.example.mrx.printer.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.util.LayoutUtil.MRXConstraint;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/9/12.
 */

public class DialView extends ConstraintLayout {

    //最小角度
    private static final float MIN_ANGLE = 148;
    //最大角度
    private static final float MAX_ANGLE = 392;

    //环状条内侧半径比率
    private static final float INNER_RATE = 0.66f;
    //环状条外侧半径比率
    private static final float OUTER_RATE = 0.815f;
    //环状条内侧颜色
    private static final int INNER_COLOR = Color.argb(0, 255, 255, 255);
    //环状条外侧颜色
    private static final int OUTER_COLOR = Color.RED;
    //外侧白条宽度
    private static final float WHITE_STRIP_WIDTH = 0.01f;

    private static final float POINTER_CENTER_RATE = 0.74f;

    //数据类型
    private DataType dataType;
    //最大数值
    private float maxValue;
    //当前数值
    private float currentValue;
    //当前比率
    private float rate;

    //背景imageView
    private BackgroundImageView backgroundImgView;

    //指针imageView
    private ImageView pointerImgView;
    //shader画笔
    private Paint shaderPaint;
    //shader
    private RadialGradient shader;
    //进度rect
    private RectF progressRect;
    //表盘半径
    private float dialRadius;
    //进度画笔宽度
    private float progressStrokeWidth;
    //白色指示器rect
    private RectF whiteIndicatorRect;
    //白色指示器画笔
    private Paint whiteIndicatorPaint;

    private boolean isDragPointer = false;

    private PointF startPoint;


    public DialView(Context context) {
        super(context);
        initViews();
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    /**
     * 设置背景刻度盘图片
     * @param resource 背景刻度盘图片id
     */
    public void setBackgroundDial(int resource) {
        backgroundImgView.setImageResource(resource);
    }

    /**
     * @param dataType 设置显示数据类型
     */
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void updateValue(float currentValue, float maxValue) {

        if (this.dataType == DataType.TIME || this.maxValue == 0) {
            this.maxValue = maxValue;
        }
        this.currentValue = currentValue;
        if (this.maxValue != 0) {
            this.rate = this.currentValue / this.maxValue;
        }
        this.backgroundImgView.invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startPoint = new PointF(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragPointer) {
                    updatePointerTransformation(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                PointF point = new PointF(event.getX(), event.getY());
                if (point.equals(startPoint)) {
                    performClick();
                }
                isDragPointer = false;
                break;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int width = right - left;

            if (width > 0) {
                dialRadius = (float) width / 2;

                progressStrokeWidth = (OUTER_RATE + WHITE_STRIP_WIDTH - INNER_RATE) * dialRadius;
                shaderPaint.setStrokeWidth(progressStrokeWidth);
                whiteIndicatorPaint.setStrokeWidth(progressStrokeWidth);

                int[] colors = {INNER_COLOR, OUTER_COLOR, Color.WHITE, Color.WHITE};
                float[] stops = {INNER_RATE, OUTER_RATE, OUTER_RATE, OUTER_RATE + WHITE_STRIP_WIDTH};

                shader = new RadialGradient(dialRadius, dialRadius, dialRadius, colors, stops, Shader.TileMode.CLAMP);
                shaderPaint.setShader(shader);

                float arcRadius = INNER_RATE * dialRadius + progressStrokeWidth / 2;
                float rectLeft = dialRadius - arcRadius;
                float rectTop = dialRadius - arcRadius;
                float rectRight = dialRadius + arcRadius;
                float rectBottom = dialRadius + arcRadius;

                progressRect.set(rectLeft, rectTop, rectRight, rectBottom);
            }
        }
    }

    private void initViews() {
        LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.view_dial, this);
        pointerImgView = (ImageView) findViewById(R.id.pointer);
        shaderPaint = new Paint();
        shaderPaint.setStyle(Paint.Style.STROKE);
        shaderPaint.setAntiAlias(true);
        whiteIndicatorPaint = new Paint();
        whiteIndicatorPaint.setStyle(Paint.Style.STROKE);
        whiteIndicatorPaint.setColor(Color.WHITE);
        whiteIndicatorPaint.setAntiAlias(true);
        progressRect = new RectF();
        whiteIndicatorRect = new RectF();

        backgroundImgView = new BackgroundImageView(MyApplication.getContext());
        backgroundImgView.setDraw(new Draw() {
            @Override
            public void drawOnCanvas(Canvas canvas) {

                float sweepAngle = rate * (MAX_ANGLE - MIN_ANGLE);
                canvas.drawArc(progressRect, MIN_ANGLE, sweepAngle, false, shaderPaint);

                float whiteIndicatorAngle = 10;
                float currentAngle = rate * (MAX_ANGLE - MIN_ANGLE) + MIN_ANGLE;
                float startAngle = currentAngle - whiteIndicatorAngle / 2;
                updateWhiteIndicatorRect();
                canvas.drawArc(whiteIndicatorRect, startAngle, whiteIndicatorAngle, false, whiteIndicatorPaint);
            }
        });

        this.addView(backgroundImgView, 0);

        setClickable(true);

        MRXConstraint.constraint(backgroundImgView).edges().equalToParent();
        MRXConstraint.applyConstraintsTo(this);

        pointerImgView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isDragPointer = true;
                return false;
            }
        });
    }

    private void updateWhiteIndicatorRect() {
        float currentAngle = rate * (MAX_ANGLE - MIN_ANGLE) + MIN_ANGLE;
        //弧度
        double angleValue = currentAngle / 180f * Math.PI;

        float indicatorX = (float) Math.cos(angleValue) * dialRadius * INNER_RATE + dialRadius;
        float indicatorY = (float) Math.sin(angleValue) * dialRadius * INNER_RATE + dialRadius;

        float indicatorLeft = indicatorX - progressStrokeWidth / 2;
        float indicatorRight = indicatorX + progressStrokeWidth / 2;
        float indicatorTop = indicatorY - progressStrokeWidth / 2;
        float indicatorBottom = indicatorY + progressStrokeWidth / 2;
        whiteIndicatorRect.set(indicatorLeft, indicatorTop, indicatorRight, indicatorBottom);
    }

    private void updatePointerTransformation(float x, float y) {
        float distance = (float) Math.sqrt(Math.pow((x - dialRadius), 2) + Math.pow((y - dialRadius), 2));
        if (distance >= dialRadius * (INNER_RATE - 0.35f) && distance <= dialRadius * (OUTER_RATE + 0.15f)) {
            //弧度
            double angleValue = Math.atan2((y - dialRadius), (x - dialRadius));
            float degrees = (float) Math.toDegrees(angleValue);

            if (degrees < MIN_ANGLE && degrees >= 90) {
                angleValue = Math.toRadians(MIN_ANGLE);
            } else if (degrees < 90 && degrees > (MAX_ANGLE - 360)) {
                angleValue = Math.toRadians(MAX_ANGLE);
            }
            float newX = (float) Math.cos(angleValue) * dialRadius * POINTER_CENTER_RATE + dialRadius;
            float newY = (float) Math.sin(angleValue) * dialRadius * POINTER_CENTER_RATE + dialRadius;
            pointerImgView.setRotation((float) Math.toDegrees(angleValue) - 90);
            pointerImgView.setTranslationX(newX - dialRadius);
            pointerImgView.setTranslationY(newY - dialRadius * (1 - POINTER_CENTER_RATE));
        }
    }

    public static enum DataType {
        TEMPERATURE,//温度
        TIME;//时间
    }


    private class BackgroundImageView extends AppCompatImageView {

        private Draw draw;

        public BackgroundImageView(Context context) {
            super(context);
            init();
        }

        public BackgroundImageView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public BackgroundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
//            this.setScaleType(ScaleType.CENTER_CROP);
        }

        public void setDraw(Draw draw) {
            this.draw = draw;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            draw.drawOnCanvas(canvas);
        }
    }

    private interface Draw {
        void drawOnCanvas(Canvas canvas);
    }

}

