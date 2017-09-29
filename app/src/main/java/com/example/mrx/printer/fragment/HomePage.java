package com.example.mrx.printer.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.example.mrx.printer.customView.RotationImage;
import com.example.mrx.printer.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class HomePage extends Fragment {

    private View view;

    private DialView dial_m;
    private DialView dial_s;
    private DialView dial_c;
    private DialView dial_t;

    Button btnStop;
    Button btnPause;
    Button btnKey;

    RotationImage rotationImage;

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

            dial_m.updateValue(100, 300);
            dial_s.updateValue(100, 300);
            dial_c.updateValue(100, 300);
            dial_t.updateValue(100, 300);

            rotationImage = view.findViewById(R.id.rotation_image);

            // 资源图片集合
            int[] srcs = new int[] { R.drawable.p52, R.drawable.p51,
                R.drawable.p50, R.drawable.p49, R.drawable.p48, R.drawable.p47,
                    R.drawable.p46, R.drawable.p45, R.drawable.p44, R.drawable.p43,
                    R.drawable.p42, R.drawable.p41, R.drawable.p40, R.drawable.p39,
                    R.drawable.p38, R.drawable.p37, R.drawable.p36, R.drawable.p35,
                    R.drawable.p34, R.drawable.p33, R.drawable.p32, R.drawable.p31,
                    R.drawable.p30, R.drawable.p29, R.drawable.p28, R.drawable.p27,
                    R.drawable.p26, R.drawable.p25, R.drawable.p24, R.drawable.p23,
                    R.drawable.p22, R.drawable.p21, R.drawable.p20, R.drawable.p19,
                    R.drawable.p18, R.drawable.p17, R.drawable.p16, R.drawable.p15,
                    R.drawable.p14, R.drawable.p13, R.drawable.p12, R.drawable.p11,
                    R.drawable.p10, R.drawable.p9, R.drawable.p8, R.drawable.p7,
                    R.drawable.p6, R.drawable.p5, R.drawable.p4, R.drawable.p3,
                    R.drawable.p2, R.drawable.p1 };

            List<Bitmap> bitmapList = new ArrayList<>();
            for (int src : srcs) {
                Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), src);
                bitmapList.add(bitmap);
            }

            rotationImage.setImageList(bitmapList);
            rotationImage.setOnTouchRotation(new RotationImage.OnTouchRotation() {
                @Override
                public void onTouchDown() {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.setViewPagerScrollable(false);
                }

                @Override
                public void onTouchEnd() {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.setViewPagerScrollable(true);
                }
            });
        }
        rotationImage.startRotation();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rotationImage.endRotation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rotationImage != null) {
            rotationImage.getImageList().clear();
        }
    }
}
