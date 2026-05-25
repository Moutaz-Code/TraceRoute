package motaz.pharaon.adu.ac.ae.traceroute.engine.gl;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import motaz.pharaon.adu.ac.ae.traceroute.core.GameController;

public class GLRenderer implements GLSurfaceView.Renderer {
    /*
     * Purpose:
     * - Bridge between Android's GLSurfaceView.Renderer callbacks and the game runtime.
     *
     * Responsibilities:
     * - Initialize OpenGL state and GPU resources.
     * - Maintain viewport size.
     * - Call GameController update/render each frame.
     * - Keep raw GLES calls contained in the engine layer.
     *
     * Depends on:
     * - GameController for game state and scene ownership.
     * - RenderPipeline for drawing higher-level sprites/rooms/UI effects.
     * - OpenGL ES 3.0 APIs.
     *
     * Used by:
     * - GameView.
     */
    private GameController gameController;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig eglConfig) {
        //GLES30.glClearColor(0.5f, 0.2f, 0.6f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        //GLES30.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        //GLES30.glViewport(0, 0, width, height);
    }
}
