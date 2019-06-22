package com.smookcreative.icare.Adapters;

import android.graphics.drawable.Drawable;

public class paramsObject {

    private String paramName, slug;
    private Drawable icon;
    private boolean isSwitch=false, switchState = false;

    public paramsObject(String paramName, Drawable icon) {
        this.paramName = paramName;
        this.icon = icon;
    }

    public paramsObject(String paramName, Drawable icon, boolean isSwitch, String slug) {
        this.paramName = paramName;
        this.icon = icon;
        this.isSwitch = isSwitch;
        this.slug= slug;
    }

    public paramsObject(String paramName, Drawable icon, boolean isSwitch, boolean switchState, String slug) {
        this.paramName = paramName;
        this.icon = icon;
        this.isSwitch = isSwitch;
        this.slug= slug;
        this.switchState = switchState;
    }

    public paramsObject(String paramName, Drawable icon , String slug) {
        this.paramName = paramName;
        this.icon = icon;
        this.slug= slug;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
