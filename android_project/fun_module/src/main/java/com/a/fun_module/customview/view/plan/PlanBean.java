package com.a.fun_module.customview.view.plan;

import android.graphics.RectF;

public class PlanBean {

    private String planId;
    private String planName;
    private String planStartTime;
    private String planEndTime;
    private String color;
    private int dayIndex;

    //计划的坐标
    private RectF rectF;
    //文本是否被省略
    private boolean isEllipsis;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    public boolean isEllipsis() {
        return isEllipsis;
    }

    public void setEllipsis(boolean ellipsis) {
        isEllipsis = ellipsis;
    }

}
