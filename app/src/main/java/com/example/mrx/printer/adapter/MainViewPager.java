package com.example.mrx.printer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mrx.printer.fragment.Control;
import com.example.mrx.printer.fragment.FilePreview;
import com.example.mrx.printer.fragment.HomePage;
import com.example.mrx.printer.fragment.Setting;

/**
 * Created by Administrator on 2017/9/15.
 */

public class MainViewPager extends FragmentPagerAdapter {

    private static final int FRAGMENTNUM = 4;


    public MainViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment ft = null;

        switch (position) {
            case 0:
                ft = new HomePage();
                break;
            case 1:
                ft = new Setting();
                break;
            case 2:
                ft = new Control();
                break;
            case 3:
                ft = new FilePreview();
                break;
            default:
                break;
        }
        return ft;
    }

    @Override
    public int getCount() {
        return FRAGMENTNUM;
    }
}
