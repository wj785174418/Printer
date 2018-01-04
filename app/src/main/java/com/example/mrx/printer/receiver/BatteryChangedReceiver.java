package com.example.mrx.printer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.blankj.utilcode.util.LogUtils;
import com.example.mrx.printer.R;

public class BatteryChangedReceiver extends BroadcastReceiver {

    private OnBatteryChanged onBatteryChanged;

    public void setOnBatteryChanged(OnBatteryChanged onBatteryChanged) {
        this.onBatteryChanged = onBatteryChanged;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 电池当前的电量, 它介于0和 EXTRA_SCALE之间
        float level = (float) intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        // 电池电量的最大值
        float scale = (float) intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float rate = level / scale;

        int resourceId;

        if (rate >= 0 && rate <= 0.1) {
            resourceId = R.drawable.battery_0;
        } else if (rate > 0.1 && rate <= 0.3) {
            resourceId = R.drawable.battery_20;
        } else if (rate > 0.3 && rate <= 0.5) {
            resourceId = R.drawable.battery_40;
        } else if (rate > 0.5 && rate <= 0.7) {
            resourceId = R.drawable.battery_60;
        } else if (rate > 0.7 && rate <= 0.9) {
            resourceId = R.drawable.battery_80;
        } else {
            resourceId = R.drawable.battery_100;
        }

        if (onBatteryChanged != null) {
            onBatteryChanged.updateBatteryStateImage(resourceId);
        }
    }

    public interface OnBatteryChanged {
        void updateBatteryStateImage(int resourceId);
    }
}
