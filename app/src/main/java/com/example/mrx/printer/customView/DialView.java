package com.example.mrx.printer.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.mrx.printer.R;
import com.example.mrx.printer.util.LayoutUtil.MRXConstraint;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/9/12.
 */

public class DialView extends ConstraintLayout {

    //最小角度
    private static final float MINANGLE = 148;
    //最大角度
    private static final float MAXANGLE = 392;

    //环状条内侧半径比率
    private static final float INNER_RATE = 0.665f;
    //环状条外侧半径比率
    private static final float OUTER_RATE = 0.820f;
    //环状条内侧颜色
    private static final int INNER_COLOR = Color.argb(0, 255, 255, 255);
    //环状条外侧颜色
    private static final int OUTER_COLOR = Color.RED;

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
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    private void initViews() {
        LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.view_dial, this);
        pointerImgView = (ImageView) findViewById(R.id.pointer);

        backgroundImgView = new BackgroundImageView(MyApplication.getContext());
        backgroundImgView.setDraw(new Draw() {
            @Override
            public void drawOnCanvas(Canvas canvas) {
                int width = canvas.getWidth();

                float dialRadius = (float) width / 2;

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                float paintStrokeWidth = (OUTER_RATE - INNER_RATE) * dialRadius;
                paint.setStrokeWidth(paintStrokeWidth);

                int[] colors = {INNER_COLOR, OUTER_COLOR};
                float[] stops = {INNER_RATE, OUTER_RATE};

                RadialGradient shader = new RadialGradient(dialRadius, dialRadius, dialRadius, colors, stops, Shader.TileMode.CLAMP);

                paint.setShader(shader);

                float arcRadius = INNER_RATE * dialRadius + paintStrokeWidth / 2;
                float left = dialRadius - arcRadius;
                float top = dialRadius - arcRadius;
                float right = dialRadius + arcRadius;
                float bottom = dialRadius + arcRadius;

                RectF rectF = new RectF(left, top, right, bottom);

                float sweepAngle = rate * (MAXANGLE - MINANGLE);

                canvas.drawArc(rectF, MINANGLE, sweepAngle, false, paint);
            }
        });

        this.addView(backgroundImgView, 0);

//
//        MRXConstraint.constraint(backgroundImgView).edges().equalToParent();
//        MRXConstraint.applyConstraintsTo(this);
    }

    public static enum DataType {
        TEMPERATURE,//温度
        TIME;//时间
    }


    private class BackgroundImageView extends AppCompatImageView {

        private Draw draw;

        public BackgroundImageView(Context context) {
            super(context);
        }

        public BackgroundImageView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public BackgroundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
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

