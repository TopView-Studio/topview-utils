package com.topview.utils.push;

/**
 * @author yongPhone
 * @date on 2018/5/15
 */
public enum PlatformEnum {
    All("all"),
    Android("android"),
    IOS("ios"),
    WinPhone("winphone");

    private final String value;

    PlatformEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
