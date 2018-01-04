package com.example.mrx.printer.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.activity.MainActivity;
import com.example.mrx.printer.adapter.FilePreviewAdapter;
import com.example.mrx.printer.model.FileDirectory;
import com.example.mrx.printer.model.Printer;
import com.example.mrx.printer.util.MyApplication;
import com.example.mrx.printer.util.SDCardUtils;
import com.example.mrx.printer.util.permission.PermissionReq;
import com.example.mrx.printer.util.permission.PermissionResult;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class FilePreview extends Fragment implements View.OnClickListener{

    private final BroadcastReceiver filePreviewReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Printer.ACTION_PRINT_PAUSE)) {
                btnStart.setBackgroundResource(R.drawable.touch_bg_btn_start);
            } else if (action.equals(Printer.ACTION_PRINT_PLAY)) {
                btnStart.setBackgroundResource(R.drawable.touch_bg_btn_pause);
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

    private static final int FILE_NUM_IN_ROW = 6;

    private View view;

    private RecyclerView recyclerView;

    private Button btnStop;
    private Button btnKey;
    private Button btnMotor;
    private Button btnStart;
    private Button btnDelete;
    private Button btnLighting;
    private Button btnToSetting;

    private ImageButton btnWifi;
    private ImageButton btnSd;
    private ImageButton btnU;
    private ImageButton btnSuspend;

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
        MyApplication.getContext().registerReceiver(filePreviewReceiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_file_preview, container, false);

            btnStop = (Button) view.findViewById(R.id.btn_stop);
            btnKey = (Button) view.findViewById(R.id.btn_key);
            btnMotor = (Button) view.findViewById(R.id.btn_motor);
            btnStart = (Button) view.findViewById(R.id.btn_start);
            btnDelete = (Button) view.findViewById(R.id.btn_delete);
            btnLighting = (Button) view.findViewById(R.id.btn_lighting);
            btnToSetting = (Button) view.findViewById(R.id.btn_to_setting);

            btnWifi = (ImageButton) view.findViewById(R.id.btn_wifi);
            btnSd = (ImageButton) view.findViewById(R.id.btn_sd);
            btnU = (ImageButton) view.findViewById(R.id.btn_u);
            btnSuspend = (ImageButton) view.findViewById(R.id.btn_suspend);

            btnWifi.setOnClickListener(this);
            btnSd.setOnClickListener(this);
            btnU.setOnClickListener(this);
            btnSuspend.setOnClickListener(this);

            recyclerView = (RecyclerView) view.findViewById(R.id.file_preview);
            GridLayoutManager layoutManager = new GridLayoutManager(MyApplication.getContext(), FILE_NUM_IN_ROW);
            recyclerView.setLayoutManager(layoutManager);

            //按钮点击
            btnKey.setOnClickListener((MainActivity)getActivity());
            btnStop.setOnClickListener((MainActivity)getActivity());
            btnMotor.setOnClickListener((MainActivity)getActivity());
            btnStart.setOnClickListener((MainActivity)getActivity());
            btnLighting.setOnClickListener((MainActivity)getActivity());
            btnToSetting.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestExternalStoragePermission();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        MyApplication.getContext().unregisterReceiver(filePreviewReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.btn_wifi:
                setBarButtonEnable();
                view.setEnabled(false);
                setRecyclerAdapterWifi();
                break;
            case R.id.btn_sd:
                setBarButtonEnable();
                view.setEnabled(false);
                setRecyclerAdapterSDCard();
                break;
            case R.id.btn_u:
                setBarButtonEnable();
                view.setEnabled(false);
                setRecyclerAdapterUDisk();
                break;
            case R.id.btn_suspend:
                setBarButtonEnable();
                view.setEnabled(false);
                setRecyclerAdapterSuspend();
                break;
            case R.id.btn_to_setting:
                MainActivity activity = (MainActivity) getActivity();
                activity.showParamsSetting();
                break;
            default:
                break;
        }
        view.setEnabled(true);
    }

    private void requestExternalStoragePermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (PermissionReq.hasPermissions(MyApplication.getContext(), permissions)) {
            return;
        }

        PermissionReq.with(this)
                .permissions(permissions)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("无文件读写权限");
                    }
                })
                .request();
    }

    private void setRecyclerAdapterWifi() {
        FilePreviewAdapter adapter = new FilePreviewAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerAdapterSDCard() {
        List<String> paths = SDCardUtils.getSDCardPaths(false);
        FileDirectory fileDirectory = new FileDirectory(paths);
        FilePreviewAdapter adapter = new FilePreviewAdapter(fileDirectory);
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerAdapterUDisk() {
        List<String> paths = SDCardUtils.getSDCardPaths(true);
        FileDirectory fileDirectory = new FileDirectory(paths);
        FilePreviewAdapter adapter = new FilePreviewAdapter(fileDirectory);
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerAdapterSuspend() {
        FilePreviewAdapter adapter = new FilePreviewAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    private void setBarButtonEnable() {
        btnWifi.setEnabled(true);
        btnSd.setEnabled(true);
        btnU.setEnabled(true);
        btnSuspend.setEnabled(true);
    }
}
