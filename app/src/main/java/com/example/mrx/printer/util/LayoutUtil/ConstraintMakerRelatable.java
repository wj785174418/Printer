package com.example.mrx.printer.util.LayoutUtil;

import android.view.View;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ConstraintMakerRelatable {

    private ConstraintDescription constraintDescription;

    public ConstraintMakerRelatable(ConstraintDescription constraintDescription) {
        this.constraintDescription = constraintDescription;
    }

    public ConstraintDescription getConstraintDescription() {
        return constraintDescription;
    }


    public ConstraintMakerEditable equalTo(View endView) {
        this.constraintDescription.setEndView(endView);
        return new ConstraintMakerEditable(this.constraintDescription);
    }


    public ConstraintMakerEditable equalToParent() {
        this.constraintDescription.setConstraintToParent(true);
        return new ConstraintMakerEditable(this.constraintDescription);
    }


}
