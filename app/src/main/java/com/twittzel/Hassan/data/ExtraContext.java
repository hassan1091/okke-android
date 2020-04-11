package com.twittzel.Hassan.data;

public class ExtraContext {
    public static final String REQ_BODY = "REQ_BODY";
    public static final String THIS_URL = "THIS_URL";
    public static final String REQ_URL = "REQ_URL";
    public static final String UI_MODE = "UI_MODE";

    private int mode;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
