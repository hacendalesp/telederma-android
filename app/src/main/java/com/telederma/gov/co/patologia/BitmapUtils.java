package com.telederma.gov.co.patologia;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.LruCache;

public class BitmapUtils {

    private static LruCache<String, Bitmap> mMemoryCache;

    static {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 4;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public static Bitmap resAsBitmap(@DrawableRes int idDrawable, Drawable drawable) {
        return convertToBitmap(idDrawable, drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public static Bitmap convertToBitmap(@DrawableRes int idDrawable, Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = getBitmapFromMemCache(String.valueOf(idDrawable));
        if(mutableBitmap == null)
            addBitmapToMemoryCache(
                    String.valueOf(idDrawable),
                    mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_4444)
            );

        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    public static Bitmap getEmptyBitmap() {
        Bitmap.Config conf = Bitmap.Config.ARGB_4444;

        return Bitmap.createBitmap(1, 1, conf);
    }

}