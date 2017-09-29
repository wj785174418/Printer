package com.example.mrx.printer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mrx.printer.R;
import com.example.mrx.printer.customView.ButtonControl;

import java.security.PrivateKey;
import java.sql.BatchUpdateException;

/**
 * Created by Administrator on 2017/9/15.
 */

public class Control extends Fragment {

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
    private ImageButton btnMotor;

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
            btnMotor = (ImageButton) view.findViewById(R.id.btn_motor);
        }
        return view;
    }
}
