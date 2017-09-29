package com.example.mrx.printer.util.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Android运行时权限申请
 * <p>
 * 需要申请的权限列表，<a "href=https://developer.android.google.cn/guide/topics/security/permissions.html?hl=zh-cn#normal-dangerous">Google Doc</a>
 * <p>
 * -CALENDAR<br>
 * {@link android.Manifest.permission#READ_CALENDAR}<br>
 * {@link android.Manifest.permission#WRITE_CALENDAR}<br>
 * <p>
 * -CAMERA<br>
 * {@link android.Manifest.permission#CAMERA}<br>
 * <p>
 * -CONTACTS<br>
 * {@link android.Manifest.permission#READ_CONTACTS}<br>
 * {@link android.Manifest.permission#WRITE_CONTACTS}<br>
 * {@link android.Manifest.permission#GET_ACCOUNTS}<br>
 * <p>
 * -LOCATION<br>
 * {@link android.Manifest.permission#ACCESS_FINE_LOCATION}<br>
 * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION}<br>
 * <p>
 * -MICROPHONE<br>
 * {@link android.Manifest.permission#RECORD_AUDIO}<br>
 * <p>
 * -PHONE<br>
 * {@link android.Manifest.permission#READ_PHONE_STATE}<br>
 * {@link android.Manifest.permission#CALL_PHONE}<br>
 * {@link android.Manifest.permission#READ_CALL_LOG}<br>
 * {@link android.Manifest.permission#WRITE_CALL_LOG}<br>
 * {@link android.Manifest.permission#ADD_VOICEMAIL}<br>
 * {@link android.Manifest.permission#USE_SIP}<br>
 * {@link android.Manifest.permission#PROCESS_OUTGOING_CALLS}<br>
 * <p>
 * -SENSORS<br>
 * {@link android.Manifest.permission#BODY_SENSORS}<br>
 * <p>
 * -SMS<br>
 * {@link android.Manifest.permission#SEND_SMS}<br>
 * {@link android.Manifest.permission#RECEIVE_SMS}<br>
 * {@link android.Manifest.permission#READ_SMS}<br>
 * {@link android.Manifest.permission#RECEIVE_WAP_PUSH}<br>
 * {@link android.Manifest.permission#RECEIVE_MMS}<br>
 * <p>
 * -STORAGE<br>
 * {@link android.Manifest.permission#READ_EXTERNAL_STORAGE}<br>
 * {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE}<br>
 */
public class PermissionReq {
    private static int sRequestCode = 0;
    private static SparseArray<PermissionResult> sResultArray= new SparseArray();

    private Activity mActivity;
    private String[] mPermissions;
    private PermissionResult mResult;

    private PermissionReq(Object object) {
        mActivity = getActivity(object);
    }

    public static PermissionReq with(@NonNull Activity activity) {
        return new PermissionReq(activity);
    }

    public static PermissionReq with(@NonNull Fragment fragment) {
        return new PermissionReq(fragment);
    }

    public PermissionReq permissions(@NonNull String... permissions) {
        mPermissions = permissions;
        return this;
    }

    public PermissionReq result(@Nullable PermissionResult result) {
        mResult = result;
        return this;
    }

    public void request() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mResult != null) {
                mResult.onGranted();
            }
            return;
        }

        List<String> deniedPermissionList = getDeniedPermissions(mActivity, mPermissions);
        if (deniedPermissionList.isEmpty()) {
            if (mResult != null) {
                mResult.onGranted();
            }
            return;
        }

        int requestCode = genRequestCode();
        String[] deniedPermissions = deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
        sResultArray.put(requestCode, mResult);
        requestPermissions(mActivity, deniedPermissions, requestCode);
    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        boolean isHave = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                isHave = false;
            }
        }
        return isHave;
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionResult result = sResultArray.get(requestCode);

        if (result == null) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                result.onDenied();
                return;
            }
        }
        result.onGranted();
        sResultArray.remove(requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, String[] permissions, int requestCode) {
        if (object instanceof Activity) {
            ((Activity) object).requestPermissions(permissions, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(permissions, requestCode);
        }
    }

    private static List<String> getDeniedPermissions(Context context, String[] permissions) {
        List<String> deniedPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissionList.add(permission);
            }
        }
        return deniedPermissionList;
    }

    private static Activity getActivity(Object object) {
        if (object != null) {
            if (object instanceof Activity) {
                return (Activity) object;
            } else if (object instanceof Fragment) {
                return ((Fragment) object).getActivity();
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
            }
        }
        throw new NullPointerException("activity can not is null");
    }

    private static int genRequestCode() {
        return ++sRequestCode;
    }

    /**
     * 是否彻底拒绝了某项权限
     */
    public static boolean hasAlwaysDeniedPermission(final Activity activity, final String... deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (!shouldShowRequestPermissionRationale(activity, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否应展示权限需要说明
     */
    public static boolean shouldShowRequestPermissionRationale(final Activity activity, final String... deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        boolean rationale;
        for (String permission : deniedPermissions) {
            rationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (rationale) return true;
        }
        return false;
    }
}
