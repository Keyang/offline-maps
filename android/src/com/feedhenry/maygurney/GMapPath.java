package com.feedhenry.maygurney;

import org.mapsforge.core.graphics.FillRule;
import org.mapsforge.core.graphics.Path;

import android.graphics.Path.FillType;

public class GMapPath implements Path {
    private static FillType getWindingRule(FillRule fillRule) {
        switch (fillRule) {
            case EVEN_ODD:
                return FillType.EVEN_ODD;
            case NON_ZERO:
                return FillType.WINDING;
        }

        throw new IllegalArgumentException("unknown fill rule:" + fillRule);
    }

    final android.graphics.Path path = new android.graphics.Path();

    @Override
    public void clear() {
        this.path.rewind();
    }

    @Override
    public void lineTo(int x, int y) {
        this.path.lineTo(x, y);
    }

    @Override
    public void moveTo(int x, int y) {
        this.path.moveTo(x, y);
    }

    @Override
    public void setFillRule(FillRule fillRule) {
        this.path.setFillType(getWindingRule(fillRule));
    }
}
