package com.example.mrx.printer.util.LayoutUtil;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ConstraintMaker {

    private ConstraintSet mConstraintSet;

    private List<ConstraintDescription> mConstraintDescriptionList;

    private View startView;

    public ConstraintMaker() {;
        this.mConstraintSet = new ConstraintSet();
        this.mConstraintDescriptionList = new ArrayList<>();
    }

    public void setStartView(View startView) {
        this.startView = startView;
    }

    public ConstraintMakerSingle left() {
        return makeSingle(ConstraintSides.LEFT);
    }
    public ConstraintMakerSingle right() {
        return makeSingle(ConstraintSides.RIGHT);
    }
    public ConstraintMakerSingle top() {
        return makeSingle(ConstraintSides.TOP);
    }
    public ConstraintMakerSingle bottom() {
        return makeSingle(ConstraintSides.BOTTOM);
    }
    public ConstraintMakerSingle baseline() {
        return makeSingle(ConstraintSides.BASELINE);
    }
    public ConstraintMakerSingle start() {
        return makeSingle(ConstraintSides.START);
    }
    public ConstraintMakerSingle end() {
        return makeSingle(ConstraintSides.END);
    }

    public ConstraintMakerRelatable edges() {
        ConstraintDescription description = new ConstraintDescription(this.startView);
        description.getStartSides().add(ConstraintSides.LEFT);
        description.getStartSides().add(ConstraintSides.RIGHT);
        description.getStartSides().add(ConstraintSides.TOP);
        description.getStartSides().add(ConstraintSides.BOTTOM);
        mConstraintDescriptionList.add(description);
        return new ConstraintMakerRelatable(description);
    }

    private ConstraintMakerSingle makeSingle(Integer side) {
        ConstraintDescription description = new ConstraintDescription(this.startView);
        description.getStartSides().add(side);
        mConstraintDescriptionList.add(description);
        return new ConstraintMakerSingle(description);
    }

    public void applyConstraintsTo(ConstraintLayout constraintLayout) {
        for (ConstraintDescription description : mConstraintDescriptionList) {
            description.convertToConstraint(mConstraintSet);
        }
        mConstraintSet.applyTo(constraintLayout);
        mConstraintSet = new ConstraintSet();
        mConstraintDescriptionList.clear();
    }

}
