package com.yyyu.ezbooking_seller.bean.foo;

import android.support.v4.app.Fragment;

/**
 * 功能：
 * Created by yyyu on 2016/10/25.
 */
public class TabBean {

    private int id;
    private Fragment fragment;

    public TabBean( int id , Fragment fragment) {
        this.fragment = fragment;
        this.id = id;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
