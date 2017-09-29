package com.example.mrx.printer.util.LayoutUtil;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ConstraintMakerEditable {

    private ConstraintDescription constraintDescription;

    public ConstraintMakerEditable(ConstraintDescription constraintDescription) {
        this.constraintDescription = constraintDescription;
    }

    public void margin(int margin) {
        this.constraintDescription.setMargin(margin);
    }
}
