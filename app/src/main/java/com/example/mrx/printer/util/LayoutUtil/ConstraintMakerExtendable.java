package com.example.mrx.printer.util.LayoutUtil;

import java.util.Set;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ConstraintMakerExtendable extends ConstraintMakerRelatable {

    public ConstraintMakerExtendable(ConstraintDescription constraintDescription) {
        super(constraintDescription);
    }

    public ConstraintMakerExtendable left() {
        this.getConstraintDescription().getStartSides().add(ConstraintSides.LEFT);
        return this;
    }

    public ConstraintMakerExtendable right() {
        this.getConstraintDescription().getStartSides().add(ConstraintSides.RIGHT);
        return this;
    }
    public ConstraintMakerExtendable top() {
        this.getConstraintDescription().getStartSides().add(ConstraintSides.TOP);
        return this;
    }
    public ConstraintMakerExtendable bottom() {
        this.getConstraintDescription().getStartSides().add(ConstraintSides.BOTTOM);
        return this;
    }
    public ConstraintMakerExtendable baseline() {
        this.getConstraintDescription().getStartSides().add(ConstraintSides.BASELINE);
        return this;
    }
    public ConstraintMakerExtendable start() {
        this.getConstraintDescription().getStartSides().add(ConstraintSides.START);
        return this;
    }
    public ConstraintMakerExtendable end() {
        this.getConstraintDescription().getStartSides().add(ConstraintSides.END);
        return this;
    }

}
