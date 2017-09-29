package com.example.mrx.printer.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrx.printer.R;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/9/18.
 */

public class FileView extends LinearLayout {

    private ImageView fileIcon;
    private TextView fileName;

    public ImageView getFileIcon() {
        return fileIcon;
    }

    public TextView getFileName() {
        return fileName;
    }

    public FileView(Context context) {
        super(context);
        initViews();
    }

    public FileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public FileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.view_file, this);
        fileIcon = findViewById(R.id.file_icon);
        fileName = findViewById(R.id.file_name);
        setBackgroundResource(R.drawable.touch_bg);
        setClickable(true);
    }
}
