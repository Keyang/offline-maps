package com.feedhenry.maygurney;

import org.mapsforge.core.graphics.Matrix;

public class GMapMatrix implements Matrix {
    final android.graphics.Matrix matrix = new android.graphics.Matrix();

    @Override
    public void reset() {
        this.matrix.reset();
    }

    @Override
    public void rotate(float theta) {
        this.matrix.preRotate((float) Math.toDegrees(theta));
    }

    @Override
    public void rotate(float theta, float pivotX, float pivotY) {
        this.matrix.preRotate((float) Math.toDegrees(theta), pivotX, pivotY);
    }

    @Override
    public void scale(float scaleX, float scaleY) {
        this.matrix.preScale(scaleX, scaleY);
    }

    @Override
    public void translate(float translateX, float translateY) {
        this.matrix.preTranslate(translateX, translateY);
    }
}
