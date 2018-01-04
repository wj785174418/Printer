package com.example.mrx.printer.model;

/**
 * Created by Administrator on 2017/10/30.
 */

public class Printer {

    /*
     *打印状态
     */

    public static boolean isPrinting = false;

    public static boolean isPause = false;

    public static boolean isLighting = false;

    /*
     *打印状态
     */

    /*
     *全局通知
     */
    //开始
    public static final String ACTION_PRINT_START = "com.example.mrx.printer.print.start";
    //暂停
    public static final String ACTION_PRINT_PAUSE = "com.example.mrx.printer.print.pause";
    //继续
    public static final String ACTION_PRINT_PLAY = "com.example.mrx.printer.print.play";
    //停止
    public static final String ACTION_PRINT_STOP = "com.example.mrx.printer.print.stop";
    //点击电机按钮
    public static final String ACTION_TOUCH_MOTOR = "com.example.mrx.printer.touch_motor";
    //照明
    public static final String ACTION_TOUCH_LIGHTING = "com.example.mrx.printer.touch_lighting";

    /*
     *全局通知
     */

    /*
     *handler what
     */

    public static final int BTN_MOTOR_ENABLE = 100;
    /*
     *handler what
     */
}
