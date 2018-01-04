package com.example.mrx.printer.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mrx.printer.R;
import com.example.mrx.printer.util.MyApplication;

/**
 * Created by Administrator on 2017/9/24.
 */

public class InputView extends ConstraintLayout implements View.OnClickListener{

    private static final String INTEGER_REGEX = "\\d+([.]\\d+)?";
    public static final int INPUT_ERROR = -1;

    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_left;
    private Button btn_right;
    private Button btn_cancel;
    private Button btn_ok;
    private Button btn_point;
    private Button btn_delete;

    private TextView inputText;

    private OnInputEnded onInputEnded;

    public void setOnInputEnded(OnInputEnded onInputEnded) {
        this.onInputEnded = onInputEnded;
    }

    public InputView(Context context) {
        super(context);
        initViews();
    }

    public InputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public InputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.layout_input, this);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_commit);
        btn_point = (Button) findViewById(R.id.btn_point);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_point.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        inputText = (TextView) findViewById(R.id.input_text);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        String newInput = "";
        String oldInput = inputText.getText().toString();
        switch (id) {
            case R.id.btn_0:
                newInput = "0";
                break;
            case R.id.btn_1:
                newInput = "1";
                break;
            case R.id.btn_2:
                newInput = "2";
                break;
            case R.id.btn_3:
                newInput = "3";
                break;
            case R.id.btn_4:
                newInput = "4";
                break;
            case R.id.btn_5:
                newInput = "5";
                break;
            case R.id.btn_6:
                newInput = "6";
                break;
            case R.id.btn_7:
                newInput = "7";
                break;
            case R.id.btn_8:
                newInput = "8";
                break;
            case R.id.btn_9:
                newInput = "9";
                break;
            case R.id.btn_left:
                break;
            case R.id.btn_right:
                break;
            case R.id.btn_cancel:
                if (this.onInputEnded != null) {
                    this.onInputEnded.onClickedCancel();
                    oldInput = "";
                    this.onInputEnded = null;
                }
                break;
            case R.id.btn_commit:
                if (this.onInputEnded != null) {
                    int value;
                    if (!oldInput.matches(INTEGER_REGEX)) {
                        value = INPUT_ERROR;
                    } else {
                        float v = Float.valueOf(oldInput);
                        value = (int) v;
                    }
                    this.onInputEnded.onClickedCommit(value);
                    oldInput = "";
                    this.onInputEnded = null;
                }
                break;
            case R.id.btn_point:
                newInput = ".";
                break;
            case R.id.btn_delete:
                if (oldInput.length() > 0) {
                    oldInput = oldInput.substring(0, oldInput.length() - 1);
                }
                break;
            default:
                break;
        }
        inputText.setText(String.format("%s%s", oldInput, newInput));
    }

    public interface OnInputEnded {
        //取消
        void onClickedCancel();
        //提交
        void onClickedCommit(int value);
    }
}
