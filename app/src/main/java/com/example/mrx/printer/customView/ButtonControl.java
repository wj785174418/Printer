package com.example.mrx.printer.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mrx.printer.R;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/9/16.
 */

public class ButtonControl extends FrameLayout{

    private ImageView bgImg;
    private LinearLayout btnContainer;
    private Button btnStart;
    private Button btnEnd;

    public void setBackgroundImage(int resource) {
        bgImg.setImageResource(resource);
    }

    public void setOrientation(int orientation) {
        btnContainer.setOrientation(orientation);
    }

    public Button getBtnStart() {
        return btnStart;
    }

    public Button getBtnEnd() {
        return btnEnd;
    }

    public ButtonControl(Context context) {
        super(context);
        initViews();
    }

    public ButtonControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ButtonControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.button_control, this);

        btnContainer = (LinearLayout) findViewById(R.id.btn_container);
        btnStart = (Button) findViewById(R.id.start);
        btnEnd = (Button) findViewById(R.id.end);
        bgImg = (ImageView) findViewById(R.id.bg_img);

    }

}
