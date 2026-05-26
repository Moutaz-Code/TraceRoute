package motaz.pharaon.adu.ac.ae.traceroute.engine.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

/*
        * Purpose:
        * - Encapsulate OpenGL texture creation, binding, and management.
        *
        * Responsibilities:
        * - Load image data into OpenGL textures.
        * - Provide methods to bind/unbind textures for rendering.
        * - Handle texture parameters (filtering, wrapping).
        *
        * Depends on:
        * - OpenGL ES 3.0 for texture APIs.
        *
        * Used by:
        * - RenderPipeline for drawing textured sprites.
        */
public class Texture {

    private int textureId;
    private int width;
    private int height;

    public int getTextureId() {
        return textureId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static Texture fromAsset(Context context, String assetPath) {
        Bitmap bitmap = loadBitmap(context, assetPath);

        int[] textureIds = new int[1];
        GLES30.glGenTextures(1, textureIds, 0);
        int textureId = textureIds[0];

        if (textureId == 0) {
            bitmap.recycle();
            throw new RuntimeException("Failed to generate OpenGL texture for asset: " + assetPath);
        }

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

        Texture texture = new Texture();
        texture.textureId = textureId;
        texture.width = bitmap.getWidth();
        texture.height = bitmap.getHeight();

        bitmap.recycle();
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        return texture;
    }

    private static Bitmap loadBitmap(Context context, String assetPath) {
        try (InputStream stream = context.getAssets().open(assetPath)) {
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            if (bitmap == null) {
                throw new RuntimeException("Failed to decode bitmap asset: " + assetPath);
            }
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture asset: " + assetPath, e);
        }
    }

}
