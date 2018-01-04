package com.example.mrx.printer.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * Created by Administrator on 2017/9/17.
 */

public class MultipleProgress extends View {

    private static final int TOUCH_EVENT_TEXT = 0;
    private static final int TOUCH_EVENT_DRAG = 1;



    private static final int COLOR_BG = Color.rgb(1, 52 ,83);
    private static final int COLOR_ERROR = Color.RED;
    private static final int COLOR_WARNING = Color.YELLOW;
    private static final int COLOR_INDICATOR = Color.rgb(254, 1, 229);
    private static final int COLOR_PROGRESS_BG = Color.rgb(126, 227, 247);
    private static final int COLOR_PROGRESS_TINT = Color.rgb(19, 145, 247);

    //数值字体大小
    private static final int TEXT_SIZE = ConvertUtils.sp2px(12);

    private int inset;

    private float width;
    private float height;

    private float minValue;
    private float maxValue;
    private float warningMin;
    private float warningMax;
    private float progressMin;
    private float progressMax;
    private float currentValue;
    private float valueOfRange;

    //拖动指示器开始时的值，用于手势取消时用于恢复
    private float onDragStartValue;

    //区域大小
    private RectF rectIndicatorLeft;
    private RectF rectIndicatorRight;
    private RectF rectText;

    private int touchEventType;

    private Paint paint;

    private OnTouchEventEnded onTouchEventEnded;
    //是否可以交互
    private boolean isTouchable = true;

    public void setTouchable(boolean touchable) {
        isTouchable = touchable;
    }

    public void update() {
        this.valueOfRange = this.maxValue - this.minValue;
        invalidate();
    }

    public void setOnTouchEventEnded(OnTouchEventEnded onTouchEventEnded) {
        this.onTouchEventEnded = onTouchEventEnded;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setWarningMin(float warningMin) {
        this.warningMin = warningMin;
    }

    public void setWarningMax(float warningMax) {
        this.warningMax = warningMax;
    }

    public void setProgressMin(float progressMin) {
        this.progressMin = progressMin;
    }

    public void setProgressMax(float progressMax) {
        this.progressMax = progressMax;
    }

    public MultipleProgress(Context context) {
        super(context);
        init();
    }

    public MultipleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultipleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.minValue = 0;
        this.maxValue = 100;
        this.warningMin = 10;
        this.warningMax = 90;
        this.progressMin = 20;
        this.progressMax = 80;
        this.currentValue = 50;
        this.valueOfRange = this.maxValue - this.minValue;

        rectIndicatorLeft = new RectF();
        rectIndicatorRight = new RectF();
        rectText = new RectF();

        touchEventType = -1;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            this.width = right - left;
            this.height = bottom - top;
            this.inset = (int) (width / 4);
            updateRectText();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawError(canvas);
        drawWarning(canvas);
        drawProgressBg(canvas);
        drawProgress(canvas);
        drawIndicator(canvas);
        drawValue(canvas);

        updateRectIndicator();
    }

    private void drawValue(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(TEXT_SIZE);
        paint.setStyle(Paint.Style.FILL);
        float startX = rectText.right - rectText.left;
        float startY = rectText.bottom;
        canvas.drawText(String.valueOf((int) currentValue), startX, startY, paint);
    }

    private void drawIndicator(Canvas canvas) {
        RectF rectF = rectOfDraw(this.progressMin, this.currentValue);
        float offset = rectF.left * 0.75f;
        PointF top = new PointF(rectF.left - offset, rectF.top - offset);
        PointF bottom = new PointF(rectF.left - offset, rectF.top + offset);
        PointF other = new PointF(rectF.left, rectF.top);
        PointF[] points = {top, bottom, other};
        paint.setColor(COLOR_INDICATOR);
        drawShape(points, paint, canvas);

        top.x = rectF.right + offset;
        top.y = rectF.top - offset;
        bottom.x = rectF.right + offset;
        bottom.y = rectF.top + offset;
        other.x = rectF.right;
        drawShape(points, paint, canvas);

        //画线
        paint.setColor(COLOR_BG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ConvertUtils.dp2px(1));
        canvas.drawLine(rectF.left, rectF.top, rectF.right, rectF.top, paint);
    }

    private void drawProgress(Canvas canvas) {
        paint.setColor(COLOR_PROGRESS_TINT);
        RectF rectF = rectOfDraw(this.progressMin, this.currentValue);
        canvas.drawRect(rectF.left, rectF.top, rectF.right, rectF.bottom, paint);
    }

    private void drawProgressBg(Canvas canvas) {
        paint.setColor(COLOR_PROGRESS_BG);
        RectF rectF = rectOfDraw(this.progressMin, this.progressMax);
        canvas.drawRect(rectF.left, rectF.top, rectF.right, rectF.bottom, paint);
    }

    private void drawWarning(Canvas canvas) {
        paint.setColor(COLOR_WARNING);
        RectF rectF = rectOfDraw(this.warningMin, this.warningMax);
        canvas.drawRect(rectF.left, rectF.top, rectF.right, rectF.bottom, paint);
    }

    private void drawError(Canvas canvas) {
        paint.setColor(COLOR_ERROR);
        RectF rectF = rectOfDraw(this.minValue, this.maxValue);
        canvas.drawRect(rectF.left, rectF.top, rectF.right, rectF.bottom, paint);
    }

    private void drawBackground(Canvas canvas) {
        paint.setColor(COLOR_BG);
        paint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(0, 0, this.width, this.height);
        canvas.drawRoundRect(rectF, ConvertUtils.dp2px(5), ConvertUtils.dp2px(5), paint);
    }

    private RectF rectOfDraw(float min, float max) {
        float height = this.height - 2 * inset;

        float left = this.width * 0.25f;
        float right = this.width * 0.75f;
        float top = (this.maxValue - max) / this.valueOfRange * height + inset;
        float bottom = this.height - (min - this.minValue) / this.valueOfRange * height - inset;

        return new RectF(left, top, right, bottom);
    }

    private void drawShape(PointF[] points, Paint paint, Canvas canvas) {
        Path shapePath = new Path();
        shapePath.moveTo(points[0].x, points[0].y);
        for (int i = 1; i < points.length; i++) {
            shapePath.lineTo(points[i].x, points[i].y);
        }
        shapePath.close();
        canvas.drawPath(shapePath, paint);

    }

    private void updateRectText() {
        this.rectText.left = this.width * 0.25f;
        this.rectText.right = this.width * 0.75f;
        this.rectText.top = (this.height - TEXT_SIZE) / 2;
        this.rectText.bottom = (this.height + TEXT_SIZE) / 2;
    }

    private void updateRectIndicator() {
        RectF rect = rectOfDraw(this.progressMin, this.currentValue);
        float offset = this.width * 0.25f;
        float top = rect.top - offset;
        float bottom = rect.top + offset;
        float leftOfLeft = 0;

        rectIndicatorLeft.top = top;
        rectIndicatorLeft.bottom = bottom;
        rectIndicatorLeft.left = leftOfLeft;
        rectIndicatorLeft.right = offset;

        float leftOfRight = this.width - offset;
        float rightOfRight = this.width;

        rectIndicatorRight.top = top;
        rectIndicatorRight.bottom = bottom;
        rectIndicatorRight.left = leftOfRight;
        rectIndicatorRight.right = rightOfRight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchable) {
            return true;
        }

        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (rectText.contains(x, y)) {
                    touchEventType = TOUCH_EVENT_TEXT;
                } else if (rectIndicatorLeft.contains(x, y) || rectIndicatorRight.contains(x, y)) {
                    touchEventType = TOUCH_EVENT_DRAG;
                    onDragStartValue = this.currentValue;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchEventType == TOUCH_EVENT_DRAG) {
                    onDragIndicator(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchEventType == TOUCH_EVENT_TEXT && rectText.contains(x, y)) {
                    if (onTouchEventEnded != null) {
                        onTouchEventEnded.onTouchText(this);
                    }
                } else if (touchEventType == TOUCH_EVENT_DRAG) {
                    if (onTouchEventEnded != null) {
                        onTouchEventEnded.onDragIndicatorEnded(this, (int)this.currentValue);
                    }
                }
                touchEventType = -1;
                break;
            default:
                if (touchEventType == TOUCH_EVENT_DRAG) {
                    this.currentValue = this.onDragStartValue;
                    update();
                }
                break;
        }
        return true;
    }

    private void onDragIndicator(MotionEvent event) {
        RectF rectSafe = rectOfDraw(this.progressMin, this.progressMax);
        float y = event.getY();
        if (y < rectSafe.top) {
            if (this.currentValue == this.progressMax) {
                return;
            }
            this.currentValue = this.progressMax;
            this.update();
            return;
        }

        if (y > rectSafe.bottom) {
            if (this.currentValue == this.progressMin) {
                return;
            }
            this.currentValue = this.progressMin;
            this.update();
            return;
        }

        //当前y所占值的高度
        float currentHeight = this.height - inset - y;
        //所有值的高度
        float totalHeight = this.height - 2 * inset;

        this.currentValue = this.minValue + currentHeight / totalHeight * this.valueOfRange;
        this.update();
    }

    public interface OnTouchEventEnded {
        //点击数值
        void onTouchText(MultipleProgress progress);
        //拖动指示器结束
        void onDragIndicatorEnded(MultipleProgress progress, int currentValue);
    }

}
