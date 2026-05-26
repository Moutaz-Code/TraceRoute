package motaz.pharaon.adu.ac.ae.traceroute.engine.gl;


import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import motaz.pharaon.adu.ac.ae.traceroute.core.GameController;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameUiEventListener;
import motaz.pharaon.adu.ac.ae.traceroute.engine.render.RenderPipeline;

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
public class GLRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private final GameUiEventListener uiEventListener;
    private GameController gameController;
    private RenderPipeline renderPipeline;
    private long lastFrameTimeNanos;

    public GLRenderer(Context context, GameUiEventListener uiEventListener) {
        this.context = context.getApplicationContext();
        this.uiEventListener = uiEventListener;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig eglConfig) {
        GLES30.glClearColor(0.5f, 0.2f, 0.6f, 1.0f);
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        renderPipeline = new RenderPipeline();
        renderPipeline.initialize(context);

        gameController = new GameController(uiEventListener);

        lastFrameTimeNanos = System.nanoTime();
    }


    @Override
    public void onDrawFrame(GL10 unused) {
        long now = System.nanoTime();
        float deltaTime = (now - lastFrameTimeNanos) / 1_000_000_000.0f;
        lastFrameTimeNanos = now;

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        gameController.update(deltaTime);
        gameController.render(renderPipeline);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        renderPipeline.resize(width, height);
    }

    public void onTap(float normalizedX, float normalizedY) {
        if (gameController != null) {
            gameController.onTap(normalizedX, normalizedY);
        }
    }
}
