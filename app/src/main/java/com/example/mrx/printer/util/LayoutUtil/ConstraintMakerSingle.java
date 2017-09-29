package com.example.mrx.printer.util.LayoutUtil;

import android.view.View;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ConstraintMakerSingle extends ConstraintMakerExtendable {

    public ConstraintMakerSingle(ConstraintDescription constraintDescription) {
        super(constraintDescription);
    }

    public ConstraintMakerEditable leftOf(View endView) {
        this.getConstraintDescription().setEndView(endView);
        this.getConstraintDescription().setEndSide(ConstraintSides.LEFT);
        return new ConstraintMakerEditable(this.getConstraintDescription());
    }

    public ConstraintMakerEditable rightOf(View endView) {
        this.getConstraintDescription().setEndView(endView);
        this.getConstraintDescription().setEndSide(ConstraintSides.RIGHT);
        return new ConstraintMakerEditable(this.getConstraintDescription());
    }

    public ConstraintMakerEditable topOf(View endView) {
        this.getConstraintDescription().setEndView(endView);
        this.getConstraintDescription().setEndSide(ConstraintSides.TOP);
        return new ConstraintMakerEditable(this.getConstraintDescription());
    }

    public ConstraintMakerEditable bottomOf(View endView) {
        this.getConstraintDescription().setEndView(endView);
        this.getConstraintDescription().setEndSide(ConstraintSides.BOTTOM);
        return new ConstraintMakerEditable(this.getConstraintDescription());
    }

    public ConstraintMakerEditable baselineOf(View endView) {
        this.getConstraintDescription().setEndView(endView);
        this.getConstraintDescription().setEndSide(ConstraintSides.BASELINE);
        return new ConstraintMakerEditable(this.getConstraintDescription());
    }

    public ConstraintMakerEditable startOf(View endView) {
        this.getConstraintDescription().setEndView(endView);
        this.getConstraintDescription().setEndSide(ConstraintSides.START);
        return new ConstraintMakerEditable(this.getConstraintDescription());
    }

    public ConstraintMakerEditable endOf(View endView) {
        this.getConstraintDescription().setEndView(endView);
        this.getConstraintDescription().setEndSide(ConstraintSides.END);
        return new ConstraintMakerEditable(this.getConstraintDescription());
    }
}
