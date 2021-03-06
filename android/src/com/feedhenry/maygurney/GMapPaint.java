package com.feedhenry.maygurney;

import android.graphics.BitmapShader;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;

import org.mapsforge.core.graphics.Align;
import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;

public class GMapPaint implements Paint {
    private static android.graphics.Paint.Align getAndroidAlign(Align align) {
        switch (align) {
            case CENTER:
                return android.graphics.Paint.Align.CENTER;
            case LEFT:
                return android.graphics.Paint.Align.LEFT;
            case RIGHT:
                return android.graphics.Paint.Align.RIGHT;
        }

        throw new IllegalArgumentException("unknown align: " + align);
    }

    private static android.graphics.Paint.Cap getAndroidCap(Cap cap) {
        switch (cap) {
            case BUTT:
                return android.graphics.Paint.Cap.BUTT;
            case ROUND:
                return android.graphics.Paint.Cap.ROUND;
            case SQUARE:
                return android.graphics.Paint.Cap.SQUARE;
        }

        throw new IllegalArgumentException("unknown cap: " + cap);
    }

    private static android.graphics.Paint.Style getAndroidStyle(Style style) {
        switch (style) {
            case FILL:
                return android.graphics.Paint.Style.FILL;
            case STROKE:
                return android.graphics.Paint.Style.STROKE;
        }

        throw new IllegalArgumentException("unknown style: " + style);
    }

    private static int getFontStyle(FontStyle fontStyle) {
        switch (fontStyle) {
            case BOLD:
                return Typeface.BOLD;
            case BOLD_ITALIC:
                return Typeface.BOLD_ITALIC;
            case ITALIC:
                return Typeface.ITALIC;
            case NORMAL:
                return Typeface.NORMAL;
        }

        throw new IllegalArgumentException("unknown font style: " + fontStyle);
    }

    private static Typeface getTypeface(org.mapsforge.core.graphics.FontFamily fontFamily) {
        switch (fontFamily) {
            case DEFAULT:
                return Typeface.DEFAULT;
            case MONOSPACE:
                return Typeface.MONOSPACE;
            case SANS_SERIF:
                return Typeface.SANS_SERIF;
            case SERIF:
                return Typeface.SERIF;
        }

        throw new IllegalArgumentException("unknown font family: " + fontFamily);
    }

    final android.graphics.Paint paint = new android.graphics.Paint();

    GMapPaint() {
        this.paint.setAntiAlias(true);
        this.paint.setStrokeCap(getAndroidCap(Cap.ROUND));
        this.paint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        this.paint.setStyle(getAndroidStyle(Style.FILL));
    }

    @Override
    public int getTextHeight(String text) {
        Rect rect = new Rect();
        this.paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    @Override
    public int getTextWidth(String text) {
        Rect rect = new Rect();
        this.paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    @Override
    public boolean isTransparent() {
        return this.paint.getShader() == null && this.paint.getAlpha() == 0;
    }

    @Override
    public void setBitmapShader(org.mapsforge.core.graphics.Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }

        this.paint
                .setShader(new BitmapShader(GMapGraphicFactory.getBitmap(bitmap), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
    }

    @Override
    public void setColor(Color color) {
        this.paint.setColor(GMapGraphicFactory.getColor(color));
    }

    @Override
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    @Override
    public void setDashPathEffect(float[] strokeDasharray) {
        PathEffect pathEffect = new DashPathEffect(strokeDasharray, 0);
        this.paint.setPathEffect(pathEffect);
    }

    @Override
    public void setStrokeCap(Cap cap) {
        this.paint.setStrokeCap(getAndroidCap(cap));
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        this.paint.setStrokeWidth(strokeWidth);
    }

    @Override
    public void setStyle(Style style) {
        this.paint.setStyle(getAndroidStyle(style));
    }

    @Override
    public void setTextAlign(Align align) {
        this.paint.setTextAlign(getAndroidAlign(align));
    }

    @Override
    public void setTextSize(float textSize) {
        this.paint.setTextSize(textSize);
    }

    @Override
    public void setTypeface(FontFamily fontFamily, FontStyle fontStyle) {
        this.paint.setTypeface(Typeface.create(getTypeface(fontFamily), getFontStyle(fontStyle)));
    }
}
