package com.primed.jobfi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import java.io.File;
import java.io.OutputStream;

public class ImageCache {
    private LruCache<String, Bitmap> memoryCache;

    public ImageCache() {
        // Use 1/8th of the available memory for this memory cache.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
    
    public void saveBitmapToInternalStorage(Context context, Bitmap bitmap, String filename) {
        try {
            OutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadBitmapFromInternalStorage(Context context, String filename) {
        Bitmap bitmap = null;
        try {
            File filePath = context.getFileStreamPath(filename);
            bitmap = BitmapFactory.decodeFile(filePath.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
