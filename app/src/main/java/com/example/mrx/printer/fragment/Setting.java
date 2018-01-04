package com.example.mrx.printer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.activity.MainActivity;
import com.example.mrx.printer.customView.DialView;
import com.example.mrx.printer.customView.InputView;
import com.example.mrx.printer.customView.MultipleProgress;
import com.example.mrx.printer.util.LayoutUtil.MRXConstraint;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/9/15.
 */

public class Setting extends Fragment implements MultipleProgress.OnTouchEventEnded, View.OnClickListener{

    private ConstraintLayout layout;

    private DialView dial_m;
    private DialView dial_s;
    private DialView dial_c;

    private MultipleProgress feedRate;
    private MultipleProgress flowRate;
    private MultipleProgress speedM_Fan;
    private MultipleProgress speedS_Fan;
    private MultipleProgress onTouchTextProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layout == null) {
            layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_setting, container, false);

            dial_m = (DialView) layout.findViewById(R.id.dial_m);
            dial_s = (DialView) layout.findViewById(R.id.dial_s);
            dial_c = (DialView) layout.findViewById(R.id.dial_c);

            dial_m.setBackgroundDial(R.drawable.dial_m);
            dial_s.setBackgroundDial(R.drawable.dial_s);
            dial_c.setBackgroundDial(R.drawable.dial_c);

            dial_m.setDataType(DialView.DataType.TEMPERATURE);
            dial_s.setDataType(DialView.DataType.TEMPERATURE);
            dial_c.setDataType(DialView.DataType.TEMPERATURE);

            feedRate = (MultipleProgress) layout.findViewById(R.id.feed_rate);
            feedRate.setMaxValue(500);
            feedRate.setMinValue(0);
            feedRate.setWarningMax(500);
            feedRate.setWarningMin(25);
            feedRate.setProgressMin(25);
            feedRate.setProgressMax(500);
            feedRate.update();

            flowRate = (MultipleProgress) layout.findViewById(R.id.flow_rate);
            flowRate.setMaxValue(200);
            flowRate.setMinValue(0);
            flowRate.setWarningMax(200);
            flowRate.setWarningMin(25);
            flowRate.setProgressMin(25);
            flowRate.setProgressMax(200);
            flowRate.update();


            speedM_Fan = (MultipleProgress) layout.findViewById(R.id.speed_m_fan);
            speedM_Fan.setMaxValue(255);
            speedM_Fan.setMinValue(0);
            speedM_Fan.setWarningMax(255);
            speedM_Fan.setWarningMin(0);
            speedM_Fan.setProgressMin(0);
            speedM_Fan.setProgressMax(255);
            speedM_Fan.update();

            speedS_Fan = (MultipleProgress) layout.findViewById(R.id.speed_s_fan);
            speedS_Fan.setMaxValue(255);
            speedS_Fan.setMinValue(0);
            speedS_Fan.setWarningMax(255);
            speedS_Fan.setWarningMin(0);
            speedS_Fan.setProgressMin(0);
            speedS_Fan.setProgressMax(255);
            speedS_Fan.update();

            feedRate.setOnTouchEventEnded(this);
            flowRate.setOnTouchEventEnded(this);
            speedM_Fan.setOnTouchEventEnded(this);
            speedS_Fan.setOnTouchEventEnded(this);

        }
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        final MainActivity activity = (MainActivity) getActivity();
        switch (v.getId()) {
            case R.id.dial_m:
            case R.id.dial_s:
            case R.id.dial_c:
                activity.getInputView().setOnInputEnded(new InputView.OnInputEnded() {
                    @Override
                    public void onClickedCancel() {
                        activity.showInputView(false);
                    }

                    @Override
                    public void onClickedCommit(int value) {
                        activity.showInputView(false);
                    }
                });
                activity.showInputView(true);
                break;
        }
        v.setEnabled(true);
    }

    //progress touch事件回调
    @Override
    public void onTouchText(final MultipleProgress progress) {
        final MainActivity activity = (MainActivity) getActivity();
        activity.getInputView().setOnInputEnded(new InputView.OnInputEnded() {
            @Override
            public void onClickedCancel() {
                activity.showInputView(false);
            }

            @Override
            public void onClickedCommit(int value) {
                activity.showInputView(false);
                if (value != InputView.INPUT_ERROR) {
                    progress.setCurrentValue(value);
                    progress.update();
                }
            }
        });
        activity.showInputView(true);
    }


    @Override
    public void onDragIndicatorEnded(MultipleProgress progress, int currentValue) {

    }
}
