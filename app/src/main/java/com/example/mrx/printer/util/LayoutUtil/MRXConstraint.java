package com.example.mrx.printer.util.LayoutUtil;

import android.support.constraint.ConstraintLayout;
import android.view.View;

/**
 * Created by Administrator on 2017/9/15.
 */

public class MRXConstraint {

    private static ConstraintMaker maker = new ConstraintMaker();

    public static ConstraintMaker constraint(View startView) {
        maker.setStartView(startView);
        return maker;
    }

    public static void applyConstraintsTo(ConstraintLayout constraintLayout) {
        maker.applyConstraintsTo(constraintLayout);
    }
}
