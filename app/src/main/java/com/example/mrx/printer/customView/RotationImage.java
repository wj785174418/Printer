package com.example.mrx.printer.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class RotationImage extends android.support.v7.widget.AppCompatImageView {

    private static final int NEXT_IMAGE = 110;

    private static final int NEXT_IMAGE_MOVE_DISTANCE = ConvertUtils.dp2px(8);

    private OnTouchRotation onTouchRotation;

    private int[] imageResources;

    private int curIndex;

    private float startX;

    private int startIndex;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == NEXT_IMAGE) {
                nextImage();
                handler.sendEmptyMessageDelayed(NEXT_IMAGE, 100);
                return true;
            }
            return false;
        }
    });



    public void startRotation() {
        handler.sendEmptyMessage(NEXT_IMAGE);
    }

    public void endRotation() {
        handler.removeMessages(NEXT_IMAGE);
    }

    public void setOnTouchRotation(OnTouchRotation onTouchRotation) {
        this.onTouchRotation = onTouchRotation;
    }

    public void setImages(int[] imageResources) {
        this.imageResources = imageResources;
    }

    public RotationImage(Context context) {
        super(context);
    }

    public RotationImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotationImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onTouchRotation != null) {
                    onTouchRotation.onTouchDown();
                }
                startX = event.getX();
                startIndex = curIndex;
                endRotation();
                break;

            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                nextImageOnTouchMove(curX);
                break;
            default:
                if (onTouchRotation != null) {
                    onTouchRotation.onTouchEnd();
                }
                startRotation();
                break;
        }
        return true;
    }

    // 滑动变换图片
    private void nextImageOnTouchMove(float curX) {
        float distance = Math.abs(curX - startX);
            int count = (int)distance / NEXT_IMAGE_MOVE_DISTANCE;
            int moveCount = count % imageResources.length;
            if (curX > startX) {
                moveRight(moveCount);
            } else {
                moveLeft(moveCount);
            }
    }

    private void moveLeft(int moveCount) {
        if (imageResources == null || imageResources.length == 0) {
            endRotation();
            return;
        }
        int index = (startIndex + moveCount) % imageResources.length;
        setImageResource(imageResources[index]);
        curIndex = index;
    }

    private void moveRight(int moveCount) {
        if (imageResources == null || imageResources.length == 0) {
            endRotation();
            return;
        }
        int index = startIndex - moveCount;
        if (index < 0) {
            index = imageResources.length + index;
        }
        setImageResource(imageResources[index]);
        curIndex = index;
    }

    // 下一个
    private void nextImage() {
        if (imageResources == null || imageResources.length == 0) {
            endRotation();
            return;
        }

        curIndex += 1;
        if (curIndex >= imageResources.length) {
            curIndex = 0;
        }
        setImageResource(imageResources[curIndex]);
    }

    public interface OnTouchRotation {
        void onTouchDown();

        void onTouchEnd();
    }

}
