package com.sdl.cac.serachview;

public class Model {
    String title;
    String desc;
    String URLC;
    int icon;

    //constructor
    public Model(String title, String desc, String URLC, int icon) {
        this.title = title;
        this.desc = desc;
        this.URLC = URLC;
        this.icon = icon;
    }

    //getters


    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getURLC() {return this.URLC; }

    public int getIcon() {
        return this.icon;
    }
}