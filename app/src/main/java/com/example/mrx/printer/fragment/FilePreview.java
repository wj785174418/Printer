package com.example.mrx.printer.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.blankj.utilcode.util.ToastUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.adapter.FilePreviewAdapter;
import com.example.mrx.printer.model.FileDirectory;
import com.example.mrx.printer.util.MyApplication;
import com.example.mrx.printer.util.SDCardUtils;
import com.example.mrx.printer.util.permission.PermissionReq;
import com.example.mrx.printer.util.permission.PermissionResult;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class FilePreview extends Fragment implements View.OnClickListener{

    private static final int FILE_NUM_IN_ROW = 6;

    private View view;

    private RecyclerView recyclerView;

    private Button btnStop;
    private Button btnPause;
    private Button btnKey;
    private ImageButton btnMotor;
    private Button btnStart;
    private Button btnDelete;

    private ImageButton btnWifi;
    private ImageButton btnSd;
    private ImageButton btnU;
    private ImageButton btnSuspend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_file_preview, container, false);

            btnStop = (Button) view.findViewById(R.id.btn_stop);
            btnPause = (Button) view.findViewById(R.id.btn_pause);
            btnKey = (Button) view.findViewById(R.id.btn_key);
            btnMotor = (ImageButton) view.findViewById(R.id.btn_motor);
            btnStart = (Button) view.findViewById(R.id.btn_start);
            btnDelete = (Button) view.findViewById(R.id.btn_delete);

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

            requestExternalStoragePermission();

        }
        return view;
    }

    @Override
    public void onClick(View view) {
        setBarButtonEnable();
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.btn_wifi:
                setRecyclerAdapterWifi();
                break;
            case R.id.btn_sd:
                setRecyclerAdapterSDCard();
                break;
            case R.id.btn_u:
                setRecyclerAdapterUDisk();
                break;
            case R.id.btn_suspend:
                setRecyclerAdapterSuspend();
                break;
            default:
                break;
        }
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
