package com.example.mrx.printer.customView;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.mrx.printer.R;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/10/10.
 */

public class ParamsSetting extends ConstraintLayout implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{

    private static final int PROGRESS_MIN = 33;
    private static final int PROGRESS_MAX = 967;

    private SeekBar seekBarSound;

    private SeekBar seekBarLuminance;

    private Button btn_home;

    public ParamsSetting(Context context) {
        super(context);
        initViews();
    }

    public ParamsSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ParamsSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.view_setting, this);
        seekBarSound = findViewById(R.id.seek_sound);
        seekBarLuminance = findViewById(R.id.seek_luminance);

        seekBarSound.setOnSeekBarChangeListener(this);
        seekBarLuminance.setOnSeekBarChangeListener(this);

        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress <= PROGRESS_MIN) {
            seekBar.setProgress(PROGRESS_MIN);
        } else if (progress >= PROGRESS_MAX) {
            seekBar.setProgress(PROGRESS_MAX);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                hideSelf();
                break;
            default:
                break;
        }
    }

    private void hideSelf() {
        animate().translationY(getHeight()).alpha(0).setDuration(300);
        setClickable(false);
        setVisibility(GONE);
    }
}
