package motaz.pharaon.adu.ac.ae.traceroute.engine.gl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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

    @Override
    public void onDrawFrame(GL10 gl10) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }
}
