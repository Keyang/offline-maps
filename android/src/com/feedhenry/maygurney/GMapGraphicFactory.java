package com.feedhenry.maygurney;

import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.io.InputStream;

public class GMapGraphicFactory implements GraphicFactory {
    public static final GraphicFactory INSTANCE = new GMapGraphicFactory();

    public static GMapBitmap convertToBitmap(Drawable drawable) {
        android.graphics.Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);

            Rect rect = drawable.getBounds();
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
            drawable.setBounds(rect);
        }

        return new GMapBitmap(bitmap);
    }

    public static Canvas createGraphicContext(android.graphics.Canvas canvas) {
        return new GMapCanvas(canvas);
    }

    public static android.graphics.Bitmap getBitmap(Bitmap bitmap) {
        return ((GMapBitmap) bitmap).bitmap;
    }

    public static android.graphics.Canvas getCanvas(Canvas canvas) {
        return ((GMapCanvas) canvas).canvas;
    }

    static int getColor(Color color) {
        switch (color) {
            case BLACK:
                return android.graphics.Color.BLACK;
            case BLUE:
                return android.graphics.Color.BLUE;
            case GREEN:
                return android.graphics.Color.GREEN;
            case RED:
                return android.graphics.Color.RED;
            case TRANSPARENT:
                return android.graphics.Color.TRANSPARENT;
            case WHITE:
                return android.graphics.Color.WHITE;
        }

        throw new IllegalArgumentException("unknown color: " + color);
    }

    static android.graphics.Matrix getMatrix(Matrix matrix) {
        return ((GMapMatrix) matrix).matrix;
    }

    static android.graphics.Paint getPaint(Paint paint) {
        return ((GMapPaint) paint).paint;
    }

    static android.graphics.Path getPath(Path path) {
        return ((GMapPath) path).path;
    }

    private GMapGraphicFactory() {
        // do nothing
    }

    @Override
    public Bitmap createBitmap(InputStream inputStream) {
        return new GMapBitmap(inputStream);
    }

    @Override
    public Bitmap createBitmap(int width, int height) {
        return new GMapBitmap(width, height);
    }

    @Override
    public Canvas createCanvas() {
        return new GMapCanvas();
    }

    @Override
    public int createColor(Color color) {
        return getColor(color);
    }

    @Override
    public int createColor(int alpha, int red, int green, int blue) {
        return android.graphics.Color.argb(alpha, red, green, blue);
    }

    @Override
    public Matrix createMatrix() {
        return new GMapMatrix();
    }

    @Override
    public Paint createPaint() {
        return new GMapPaint();
    }

    @Override
    public Path createPath() {
        return new GMapPath();
    }
}
