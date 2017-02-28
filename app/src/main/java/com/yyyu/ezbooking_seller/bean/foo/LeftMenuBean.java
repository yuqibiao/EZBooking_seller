package com.yyyu.ezbooking_seller.bean.foo;

/**
 * 功能：侧滑菜单数据封装bean
 *
 * Created by yyyu on 2016/8/12.
 */
public class LeftMenuBean {

    private int menuIcon;
    private String menuTitle;

    public LeftMenuBean(int menuIcon, String menuTitle) {
        this.menuIcon = menuIcon;
        this.menuTitle = menuTitle;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

}
