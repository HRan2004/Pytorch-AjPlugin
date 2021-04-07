package com.hraps.pytorch.od;

import android.graphics.Rect;

public class OdResult {
    public int classIndex;
    public Float score;
    public Rect rect;

    public OdResult(int cls, Float output, Rect rect) {
        this.classIndex = cls;
        this.score = output;
        this.rect = rect;
    }
};