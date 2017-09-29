package com.example.mrx.printer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by Administrator on 2017/3/28.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public View findSubViewById(int id) {
        return itemView.findViewById(id);
    }
}
