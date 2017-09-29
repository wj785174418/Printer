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

    private List<Bitmap> imageList;

    private Bitmap curImage;
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

    public void setImageList(List<Bitmap> imageList) {
        this.imageList = imageList;
    }

    public List<Bitmap> getImageList() {
        return imageList;
    }

    public Bitmap getCurImage() {
        return curImage;
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
            int moveCount = count % imageList.size();
            if (curX > startX) {
                moveRight(moveCount);
            } else {
                moveLeft(moveCount);
            }
    }

    private void moveLeft(int moveCount) {
        if (imageList == null || imageList.size() == 0) {
            endRotation();
            return;
        }
        int index = (startIndex + moveCount) % imageList.size();
        setImageBitmap(imageList.get(index));
        curIndex = index;
    }

    private void moveRight(int moveCount) {
        if (imageList == null || imageList.size() == 0) {
            endRotation();
            return;
        }
        int index = startIndex - moveCount;
        if (index < 0) {
            index = imageList.size() + index;
        }
        setImageBitmap(imageList.get(index));
        curIndex = index;
    }

    // 下一个
    private void nextImage() {
        if (imageList == null || imageList.size() == 0) {
            endRotation();
            return;
        }

        curIndex += 1;
        if (curIndex >= imageList.size()) {
            curIndex = 0;
        }
        setImageBitmap(imageList.get(curIndex));
        curImage = imageList.get(curIndex);
    }

    public interface OnTouchRotation {
        void onTouchDown();

        void onTouchEnd();
    }

}
