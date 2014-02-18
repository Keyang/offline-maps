package com.feedhenry.maygurney;

import android.graphics.BitmapFactory;

import org.mapsforge.core.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

class GMapBitmap implements Bitmap {
    private static final BitmapFactory.Options BITMAP_FACTORY_OPTIONS = createBitmapFactoryOptions();

    private static BitmapFactory.Options createBitmapFactoryOptions() {
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;
        return bitmapFactoryOptions;
    }

    final android.graphics.Bitmap bitmap;

    GMapBitmap(android.graphics.Bitmap bitmap) {
        if (bitmap.isRecycled()) {
            throw new IllegalArgumentException("bitmap is already recycled");
        }

        this.bitmap = bitmap;
    }

    GMapBitmap(InputStream inputStream) {
        this.bitmap = BitmapFactory.decodeStream(inputStream, null, BITMAP_FACTORY_OPTIONS);
    }

    GMapBitmap(int width, int height) {
        this.bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
    }

    void copyPixelsToBuffer(ByteBuffer buffer) {
        bitmap.copyPixelsToBuffer(buffer);
    }

    int getByteCount() {
        return bitmap.getByteCount();
    }

    @Override
    public void compress(OutputStream outputStream) {
        this.bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 0, outputStream);
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.compress(stream);
        return stream.toByteArray();
    }

    @Override
    public int getHeight() {
        return this.bitmap.getHeight();
    }

    @Override
    public int getWidth() {
        return this.bitmap.getWidth();
    }
}
