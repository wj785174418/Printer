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
import com.example.mrx.printer.customView.DialView;
import com.example.mrx.printer.customView.InputView;
import com.example.mrx.printer.customView.MultipleProgress;
import com.example.mrx.printer.util.LayoutUtil.MRXConstraint;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/9/15.
 */

public class Setting extends Fragment implements MultipleProgress.OnTouchEventEnded{

    private ConstraintLayout layout;

    private DialView dial_m;
    private DialView dial_s;
    private DialView dial_c;

    private MultipleProgress feedRate;
    private MultipleProgress flowRate;
    private MultipleProgress speedM_Fan;
    private MultipleProgress speedS_Fan;
    private MultipleProgress t_axisX;
    private MultipleProgress t_axisY;
    private MultipleProgress t_PCBA;

    private MultipleProgress onTouchTextProgress;

    private InputView inputView;

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
            flowRate = (MultipleProgress) layout.findViewById(R.id.flow_rate);
            speedM_Fan = (MultipleProgress) layout.findViewById(R.id.speed_m_fan);
            speedS_Fan = (MultipleProgress) layout.findViewById(R.id.speed_s_fan);
            t_axisX = (MultipleProgress) layout.findViewById(R.id.t_axis_x);
            t_axisY = (MultipleProgress) layout.findViewById(R.id.t_axis_y);
            t_PCBA = (MultipleProgress) layout.findViewById(R.id.t_pcba);

            feedRate.setOnTouchEventEnded(this);
            flowRate.setOnTouchEventEnded(this);
            speedM_Fan.setOnTouchEventEnded(this);
            speedS_Fan.setOnTouchEventEnded(this);
            t_axisX.setOnTouchEventEnded(this);
            t_axisY.setOnTouchEventEnded(this);
            t_PCBA.setOnTouchEventEnded(this);

            inputView = new InputView(MyApplication.getContext());
            inputView.setId(R.id.inputView);

            inputView.setOnInputEnded(new InputView.OnInputEnded() {
                @Override
                public void onClickedCancel() {
                    layout.removeView(inputView);
                }

                @Override
                public void onClickedCommit(int value) {
                    layout.removeView(inputView);
                    onTouchTextProgress.setCurrentValue(value);
                    onTouchTextProgress.update();
                }
            });
        }

        return layout;
    }


    //progress touch事件回调
    @Override
    public void onTouchText(MultipleProgress progress) {

        if (inputView.getParent() != null) {
            return;
        }
        onTouchTextProgress = progress;
        layout.addView(inputView);
        MRXConstraint.constraint(inputView).edges().equalTo(layout);
        MRXConstraint.applyConstraintsTo(layout);
    }


    @Override
    public void onDragIndicatorEnded(MultipleProgress progress, int currentValue) {

    }
}
