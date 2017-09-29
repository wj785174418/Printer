package com.example.mrx.printer.util.LayoutUtil;

import android.support.constraint.ConstraintSet;
import android.util.ArraySet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ConstraintDescription {
    private View startView;

    private View endView;

    private int startViewId;

    private int endViewId;

    private Set<Integer> startSides;

    private Integer endSide;

    private boolean isConstraintToParent;

    private int margin;

    public ConstraintDescription(View startView) {
        this.startView = startView;
        this.startViewId = startView.getId();
        this.startSides = new HashSet<>();
    }

    public Set<Integer> getStartSides() {
        return startSides;
    }

    public void setEndView(View endView) {
        this.endView = endView;
        this.endViewId = endView.getId();
    }

    public void setConstraintToParent(boolean constraintToParent) {
        isConstraintToParent = constraintToParent;
        View parent = (View) startView.getParent();
        if (parent != null) {
            this.endView = parent;
            this.endViewId = parent.getId();
        } else {
            throw new NullPointerException("约束view没有父view");
        }
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setEndSide(Integer endSide) {
        this.endSide = endSide;
    }

    public void convertToConstraint(ConstraintSet constraintSet) {

        for (Integer side : this.startSides) {
            int startSide = side;
            int endSide;
            if (this.endSide == null) {
                endSide = startSide;
            } else {
                endSide = this.endSide;
            }

            if (this.margin > 0) {
                constraintSet.connect(this.startViewId, startSide, this.endViewId, endSide, this.margin);
            } else {
                constraintSet.connect(this.startViewId, startSide, this.endViewId, endSide);
            }
        }

    }

}
