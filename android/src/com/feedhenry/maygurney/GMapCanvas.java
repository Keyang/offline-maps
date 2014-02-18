package com.feedhenry.maygurney;

import android.graphics.PorterDuff;
import android.graphics.Region;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.model.Dimension;

public class GMapCanvas implements Canvas {
    final android.graphics.Canvas canvas;
    private final android.graphics.Paint bitmapPaint = new android.graphics.Paint();

    GMapCanvas() {
        this.canvas = new android.graphics.Canvas();

        this.bitmapPaint.setAntiAlias(true);
        this.bitmapPaint.setFilterBitmap(true);
    }

    GMapCanvas(android.graphics.Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int left, int top) {
        this.canvas.drawBitmap(GMapGraphicFactory.getBitmap(bitmap), left, top, null);
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix) {
        this.canvas.drawBitmap(GMapGraphicFactory.getBitmap(bitmap), GMapGraphicFactory.getMatrix(matrix),
                this.bitmapPaint);
    }

    @Override
    public void drawCircle(int x, int y, int radius, Paint paint) {
        if (paint.isTransparent()) {
            return;
        }

        this.canvas.drawCircle(x, y, radius, GMapGraphicFactory.getPaint(paint));
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Paint paint) {
        if (paint.isTransparent()) {
            return;
        }

        this.canvas.drawLine(x1, y1, x2, y2, GMapGraphicFactory.getPaint(paint));
    }

    @Override
    public void drawPath(Path path, Paint paint) {
        if (paint.isTransparent()) {
            return;
        }

        this.canvas.drawPath(GMapGraphicFactory.getPath(path), GMapGraphicFactory.getPaint(paint));
    }

    @Override
    public void drawText(String text, int x, int y, Paint paint) {
        if (paint.isTransparent()) {
            return;
        }

        this.canvas.drawText(text, x, y, GMapGraphicFactory.getPaint(paint));
    }

    @Override
    public void drawTextRotated(String text, int x1, int y1, int x2, int y2, Paint paint) {
        if (paint.isTransparent()) {
            return;
        }

        android.graphics.Path path = new android.graphics.Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        this.canvas.drawTextOnPath(text, path, 0, 3, GMapGraphicFactory.getPaint(paint));
    }

    @Override
    public void fillColor(Color color) {
        this.canvas.drawColor(GMapGraphicFactory.getColor(color), PorterDuff.Mode.CLEAR);
    }

    @Override
    public void fillColor(int color) {
        this.canvas.drawColor(color);
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public int getHeight() {
        return this.canvas.getHeight();
    }

    @Override
    public int getWidth() {
        return this.canvas.getWidth();
    }

    @Override
    public void resetClip() {
        this.canvas.clipRect(0, 0, getWidth(), getHeight(), Region.Op.REPLACE);
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.canvas.setBitmap(GMapGraphicFactory.getBitmap(bitmap));
    }

    @Override
    public void setClip(int left, int top, int width, int height) {
        this.canvas.clipRect(left, top, left + width, top + height, Region.Op.REPLACE);
    }
}
