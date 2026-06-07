package motaz.pharaon.adu.ac.ae.traceroute.engine.gl;

import android.opengl.GLES30;

/*
 * Purpose:
 * - Wraps an OpenGL framebuffer object (FBO) for off-screen rendering.
 *
 * For the MVP, all rendering goes directly to the default framebuffer.
 * This class is scaffolded for a future post-process pass (scanlines,
 * vignette, glitch) and is not yet wired into the active render path.
 */
public class Framebuffer {

    private int fboId;
    private int colorTextureId;
    private int width;
    private int height;
    private boolean valid;

    public static Framebuffer create(int width, int height) {
        Framebuffer framebuffer = new Framebuffer();
        framebuffer.width = width;
        framebuffer.height = height;

        int[] ids = new int[1];

        // Color texture
        GLES30.glGenTextures(1, ids, 0);
        framebuffer.colorTextureId = ids[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, framebuffer.colorTextureId);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, width, height,
                0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        // FBO
        GLES30.glGenFramebuffers(1, ids, 0);
        framebuffer.fboId = ids[0];
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, framebuffer.fboId);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0,
                GLES30.GL_TEXTURE_2D, framebuffer.colorTextureId, 0);

        int status = GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);
        framebuffer.valid = status == GLES30.GL_FRAMEBUFFER_COMPLETE;
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);

        return framebuffer;
    }

    public void bind()   { GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fboId); }
    public void unbind() { GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0); }

    public int getColorTextureId() { return colorTextureId; }
    public int getWidth()  { return width; }
    public int getHeight() { return height; }
    public boolean isValid() { return valid; }

    public void delete() {
        GLES30.glDeleteFramebuffers(1, new int[]{fboId}, 0);
        GLES30.glDeleteTextures(1, new int[]{colorTextureId}, 0);
        valid = false;
    }
}
