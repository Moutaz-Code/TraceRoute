package motaz.pharaon.adu.ac.ae.traceroute.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public final class AssetImageLoader {
    /*
     * Purpose:
     * - Small UI helper for loading optional bitmap assets from app assets.
     *
     * Responsibilities:
     * - Decode asset-backed images into ImageViews.
     * - Return false instead of throwing when art has not been dropped in yet.
     */

    private AssetImageLoader() {
    }

    public static boolean loadInto(ImageView imageView, String assetPath) {
        if (assetPath == null || assetPath.trim().isEmpty()) {
            return false;
        }

        Context context = imageView.getContext().getApplicationContext();
        try (InputStream inputStream = context.getAssets().open(assetPath)) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                return false;
            }

            imageView.setImageBitmap(bitmap);
            return true;
        } catch (IOException exception) {
            return false;
        }
    }
}
