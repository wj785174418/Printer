package com.example.mrx.printer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.adapter.MainViewPager;
import com.example.mrx.printer.customView.InputView;
import com.example.mrx.printer.customView.NoScrollViewPager;
import com.example.mrx.printer.customView.ParamsSetting;
import com.example.mrx.printer.model.Printer;
import com.example.mrx.printer.receiver.BatteryChangedReceiver;
import com.example.mrx.printer.util.permission.PermissionReq;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private final BroadcastReceiver mainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


    private static final int UPDATE_TIME = 100;

    private static final int UPDATE_TIME_INTERVAL = 60000;

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private static final int PAGE_NUM = 4;

    //默认小圆点选中颜色
    private static final int POINT_SELECTED_COLOR = Color.BLACK;

    //小圆点未选中颜色
    private static final int POINT_UNSELECTED_COLOR = Color.WHITE;

    //小圆点大小
    private static final int POINT_SIZE = ConvertUtils.dp2px(10);

    private NoScrollViewPager viewPager;

    //键入view
    private InputView inputView;

    //viewPager页标指示器
    private LinearLayout viewPagerIndicator;

    private TextView textTime;

    //更新时间显示的handler
    private Handler timeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == UPDATE_TIME) {
                Date now = TimeUtils.getNowDate();
                textTime.setText(TimeUtils.date2String(now, dateFormat));
                timeHandler.sendEmptyMessageDelayed(UPDATE_TIME, UPDATE_TIME_INTERVAL);
            }
            return true;
        }
    });

    BatteryChangedReceiver batteryReceiver;

    private ImageView batteryImage;

    private ParamsSetting paramsSetting;

    /**
     * 设置viewPager能否滑动
     * @param enable
     */
    public void setViewPagerScrollable(boolean enable) {
        viewPager.setScroll(enable);
    }

    public void showInputView(boolean isShow) {
        if (isShow) {
            inputView.setVisibility(View.VISIBLE);
        } else {
            inputView.setVisibility(View.GONE);
        }
    }

    public InputView getInputView() {
        return inputView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        viewPager = (NoScrollViewPager) findViewById(R.id.view_pager);
        addListenerForViewPager();
        MainViewPager adapter = new MainViewPager(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //TODO
//        viewPager.setCurrentItem(3);

        inputView = (InputView) findViewById(R.id.input_view);

        viewPagerIndicator = (LinearLayout) findViewById(R.id.view_pager_indicator);
        initViewPagerIndicator();

        textTime = (TextView) findViewById(R.id.text_time);
        if (!timeHandler.hasMessages(UPDATE_TIME)) {
            timeHandler.sendEmptyMessage(UPDATE_TIME);
        }

        batteryImage = (ImageView) findViewById(R.id.battery_state);

        batteryReceiver = new BatteryChangedReceiver();
        batteryReceiver.setOnBatteryChanged(new BatteryChangedReceiver.OnBatteryChanged() {
            @Override
            public void updateBatteryStateImage(int resourceId) {
                batteryImage.setImageResource(resourceId);
            }
        });

        //参数设置页面
        paramsSetting = (ParamsSetting) findViewById(R.id.params_setting);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(batteryReceiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 为viewPage添加pageChangeListener
     */
    private void addListenerForViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateSelectedPoint();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 更新选中的小圆点
     */
    private void updateSelectedPoint() {
        if (viewPagerIndicator == null) {
            return;
        }
        int index = viewPager.getCurrentItem();
        for (int i = 0; i < PAGE_NUM; i++) {
            if (i == index) {
                viewPagerIndicator.getChildAt(i).getBackground().setColorFilter(POINT_SELECTED_COLOR, PorterDuff.Mode.DARKEN);
            } else {
                viewPagerIndicator.getChildAt(i).getBackground().setColorFilter(POINT_UNSELECTED_COLOR, PorterDuff.Mode.DARKEN);
            }
        }
    }

    //初始化viewPager指示器
    private void  initViewPagerIndicator() {
        for (int i = 0; i < PAGE_NUM; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.point_bg);
            int width = POINT_SIZE;
            int margin = ConvertUtils.dp2px(4);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            params.setMargins(margin, 0, margin, 0);
            view.setLayoutParams(params);
            viewPagerIndicator.addView(view);
            if (i == 0) {
                view.getBackground().setColorFilter(POINT_SELECTED_COLOR, PorterDuff.Mode.DARKEN);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.btn_key:
                break;
            case R.id.btn_start:
                if (Printer.isPrinting) {
                    if (Printer.isPause) {
                        Intent intent = new Intent(Printer.ACTION_PRINT_PLAY);
                        sendBroadcast(intent);
                        Printer.isPause = false;
                        ToastUtils.showShort("暂停->继续打印");
                    } else {
                        Intent intent = new Intent(Printer.ACTION_PRINT_PAUSE);
                        sendBroadcast(intent);
                        Printer.isPause = true;
                        ToastUtils.showShort("打印中->暂停");
                    }
                } else {
                    v.setBackgroundResource(R.drawable.touch_bg_btn_pause);
                    Intent intent = new Intent(Printer.ACTION_PRINT_START);
                    sendBroadcast(intent);
                    Printer.isPrinting = true;
                    ToastUtils.showShort("开始打印");
                }
                break;
            case R.id.btn_pause:
                if (Printer.isPrinting) {
                    if (Printer.isPause) {
                        Intent intent = new Intent(Printer.ACTION_PRINT_PLAY);
                        sendBroadcast(intent);
                        Printer.isPause = false;
                        ToastUtils.showShort("暂停->继续打印");
                    } else {
                        Intent intent = new Intent(Printer.ACTION_PRINT_PAUSE);
                        sendBroadcast(intent);
                        Printer.isPause = true;
                        ToastUtils.showShort("打印中->暂停");
                    }
                } else {
                    ToastUtils.showShort("未在打印中");
                }
                break;
            case R.id.btn_stop:
                Intent intent = new Intent(Printer.ACTION_PRINT_STOP);
                sendBroadcast(intent);
                Printer.isPrinting = false;
                ToastUtils.showShort("结束打印");
                break;
            case R.id.btn_motor:
                v.setEnabled(false);
                Intent intentMotor = new Intent(Printer.ACTION_TOUCH_MOTOR);
                sendBroadcast(intentMotor);
                break;
            case R.id.btn_lighting:
                if (v.isSelected()) {
                    v.setSelected(false);
                    Printer.isLighting = false;
                } else {
                    v.setSelected(true);
                    Printer.isLighting = true;
                }
                Intent intentLighting = new Intent(Printer.ACTION_TOUCH_LIGHTING);
                sendBroadcast(intentLighting);
                break;
            default:
                break;
        }
        v.setEnabled(true);
    }

    public void showParamsSetting() {
        float height = paramsSetting.getHeight();
        float translationY = paramsSetting.getTranslationY();
        if (translationY == 0) {
            paramsSetting.setTranslationY(height);
        }
        paramsSetting.setVisibility(View.VISIBLE);
        paramsSetting.animate().translationY(0).alpha(1).setDuration(300);
        paramsSetting.setClickable(true);
    }
}
