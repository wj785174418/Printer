package com.example.mrx.printer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.blankj.utilcode.util.LogUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.activity.MainActivity;
import com.example.mrx.printer.customView.DialView;
import com.example.mrx.printer.customView.InputView;
import com.example.mrx.printer.customView.RotationImage;
import com.example.mrx.printer.model.Printer;
import com.example.mrx.printer.util.MyApplication;
import com.example.mrx.printer.util.StlUtil.STLReader;
import com.example.mrx.printer.util.StlUtil.STLRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class HomePage extends Fragment implements View.OnClickListener{

    private static final int STL_WHAT = 10010;

    private final BroadcastReceiver homePageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Printer.ACTION_PRINT_PAUSE)) {
                btnPause.setBackgroundResource(R.drawable.touch_bg_btn_start);
            } else if (action.equals(Printer.ACTION_PRINT_PLAY)) {
                btnPause.setBackgroundResource(R.drawable.touch_bg_btn_pause);
            }
        }
    };

    private View view;

    private DialView dial_m;
    private DialView dial_s;
    private DialView dial_c;
    private DialView dial_t;

    Button btnStop;
    Button btnPause;
    Button btnKey;
    Button btnLighting;

    //STL  绘制3D模型相关
    private boolean supportsEs2;
    private GLSurfaceView glView;
    private float rotateDegreen = 0;
    private STLRenderer stlRenderer;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == STL_WHAT) {
                rotateDegreen += 5;
                rotate(rotateDegreen);
                handler.sendEmptyMessageDelayed(STL_WHAT, 100);
            }
            return true;
        }
    });

    public void rotate(float degree) {
        stlRenderer.rotate(degree);
        glView.invalidate();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFilter();
    }

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Printer.ACTION_PRINT_PAUSE);
        filter.addAction(Printer.ACTION_PRINT_PLAY);
        MyApplication.getContext().registerReceiver(homePageReceiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_page, container, false);

            dial_m = (DialView) view.findViewById(R.id.dial_m);
            dial_s = (DialView) view.findViewById(R.id.dial_s);
            dial_c = (DialView) view.findViewById(R.id.dial_c);
            dial_t = (DialView) view.findViewById(R.id.dial_t);

            dial_m.setBackgroundDial(R.drawable.dial_m);
            dial_s.setBackgroundDial(R.drawable.dial_s);
            dial_c.setBackgroundDial(R.drawable.dial_c);
            dial_t.setBackgroundDial(R.drawable.dial_t);

            dial_m.setDataType(DialView.DataType.TEMPERATURE);
            dial_s.setDataType(DialView.DataType.TEMPERATURE);
            dial_c.setDataType(DialView.DataType.TEMPERATURE);
            dial_t.setDataType(DialView.DataType.TIME);

            btnStop = (Button) view.findViewById(R.id.btn_stop);
            btnPause = (Button) view.findViewById(R.id.btn_pause);
            btnKey = (Button) view.findViewById(R.id.btn_key);
            btnLighting = (Button) view.findViewById(R.id.btn_lighting);

            dial_m.updateValue(80, 300);
            dial_s.updateValue(120, 300);
            dial_c.updateValue(160, 300);
            dial_t.updateValue(200, 300);

            dial_t.setClickable(false);

            dial_m.setOnClickListener(this);
            dial_s.setOnClickListener(this);
            dial_c.setOnClickListener(this);

            //按钮点击
            btnKey.setOnClickListener((MainActivity)getActivity());
            btnPause.setOnClickListener((MainActivity)getActivity());
            btnStop.setOnClickListener((MainActivity)getActivity());
            btnLighting.setOnClickListener((MainActivity)getActivity());

            glView = (GLSurfaceView) view.findViewById(R.id.glView);
            stlRenderer = new STLRenderer(MyApplication.getContext());
            glView.setRenderer(stlRenderer);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();
        }
        handler.sendEmptyMessageDelayed(STL_WHAT, 100);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(STL_WHAT);
        if (glView != null) {
            glView.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        MyApplication.getContext().unregisterReceiver(homePageReceiver);
        super.onDestroy();
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
}
