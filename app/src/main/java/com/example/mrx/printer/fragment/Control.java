package com.example.mrx.printer.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.activity.MainActivity;
import com.example.mrx.printer.customView.ButtonControl;
import com.example.mrx.printer.customView.InputView;
import com.example.mrx.printer.model.Printer;
import com.example.mrx.printer.util.MyApplication;

import java.security.PrivateKey;
import java.sql.BatchUpdateException;

/**
 * Created by Administrator on 2017/9/15.
 */

public class Control extends Fragment implements View.OnClickListener {

    private final BroadcastReceiver controlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Printer.ACTION_PRINT_PAUSE)) {
                btnPause.setBackgroundResource(R.drawable.touch_bg_btn_start);
            } else if (action.equals(Printer.ACTION_PRINT_PLAY)) {
                btnPause.setBackgroundResource(R.drawable.touch_bg_btn_pause);
            } else if (action.equals(Printer.ACTION_TOUCH_MOTOR)) {
                btnMotor.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            mHandler.sendEmptyMessage(Printer.BTN_MOTOR_ENABLE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    };

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Printer.BTN_MOTOR_ENABLE:
                    btnMotor.setEnabled(true);
                    break;
            }
            return false;
        }
    });


    private View view;

    private ButtonControl x_left;
    private ButtonControl x_right;
    private ButtonControl y_top;
    private ButtonControl y_bottom;
    private ButtonControl z_top;
    private ButtonControl z_bottom;
    private ButtonControl em_top;
    private ButtonControl em_bottom;
    private ButtonControl es_top;
    private ButtonControl es_bottom;

    private Button btnStop;
    private Button btnPause;
    private Button btnKey;
    private Button btnMotor;
    private Button btnLighting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFilter();
    }

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Printer.ACTION_PRINT_PAUSE);
        filter.addAction(Printer.ACTION_PRINT_PLAY);
        filter.addAction(Printer.ACTION_TOUCH_MOTOR);
        MyApplication.getContext().registerReceiver(controlReceiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_control, container, false);

            x_left = (ButtonControl) view.findViewById(R.id.x_left);
            x_right = (ButtonControl) view.findViewById(R.id.x_right);
            y_top = (ButtonControl) view.findViewById(R.id.y_top);
            y_bottom = (ButtonControl) view.findViewById(R.id.y_bottom);
            z_top = (ButtonControl) view.findViewById(R.id.z_top);
            z_bottom = (ButtonControl) view.findViewById(R.id.z_bottom);
            em_top = (ButtonControl) view.findViewById(R.id.em_top);
            em_bottom = (ButtonControl) view.findViewById(R.id.em_bottom);
            es_top = (ButtonControl) view.findViewById(R.id.es_top);
            es_bottom = (ButtonControl) view.findViewById(R.id.es_bottom);

            //设置方向
            x_left.setOrientation(LinearLayout.HORIZONTAL);
            x_right.setOrientation(LinearLayout.HORIZONTAL);

            //设置图片
            x_left.setBackgroundImage(R.drawable.arrow_left);
            x_right.setBackgroundImage(R.drawable.arrow_right);
            y_bottom.setBackgroundImage(R.drawable.arrow_bottom);
            z_bottom.setBackgroundImage(R.drawable.arrow_bottom);
            em_bottom.setBackgroundImage(R.drawable.arrow_bottom);
            es_bottom.setBackgroundImage(R.drawable.arrow_bottom);

            btnStop = (Button) view.findViewById(R.id.btn_stop);
            btnPause = (Button) view.findViewById(R.id.btn_pause);
            btnKey = (Button) view.findViewById(R.id.btn_key);
            btnMotor = (Button) view.findViewById(R.id.btn_motor);
            btnLighting = (Button) view.findViewById(R.id.btn_lighting);

            //按钮点击
            btnKey.setOnClickListener((MainActivity) getActivity());
            btnPause.setOnClickListener((MainActivity) getActivity());
            btnStop.setOnClickListener((MainActivity) getActivity());
            btnMotor.setOnClickListener((MainActivity)getActivity());
            btnLighting.setOnClickListener((MainActivity)getActivity());

            Button btnEMTemperature = (Button) view.findViewById(R.id.btn_em_temperature);
            Button btnESTemperature = (Button) view.findViewById(R.id.btn_es_temperature);
            btnEMTemperature.setOnClickListener(this);
            btnESTemperature.setOnClickListener(this);
        }
        return view;
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
    public void onDestroy() {
        MyApplication.getContext().unregisterReceiver(controlReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        final MainActivity activity = (MainActivity) getActivity();
        switch (v.getId()) {
            case R.id.btn_em_temperature:
            case R.id.btn_es_temperature:
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
            default:
                break;
        }
        v.setEnabled(true);
    }
}
