package motaz.pharaon.adu.ac.ae.traceroute;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.GLRenderer;

public class GameView extends GLSurfaceView {
    /*
     * Purpose:
     * - Native Android view that hosts the OpenGL ES game surface.
     *
     * Responsibilities:
     * - Configure OpenGL ES 3.0.
     * - Own GLRenderer.
     * - Receive touch events and pass them to InputManager.
     * - Stay focused on rendering/input, not case logic or UI overlays.
     *
     * Depends on:
     * - GLRenderer for rendering lifecycle.
     * - InputManager for touch forwarding.
     *
     * Used by:
     * - WorldInvestigationFragment.
     */

    private GLRenderer renderer;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(3);
        setPreserveEGLContextOnPause(true);
        renderer = new GLRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return true;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return true;
        }

        final float normalizedX = event.getX() / getWidth();
        final float normalizedY = event.getY() / getHeight();

        queueEvent(() -> renderer.onTap(normalizedX, normalizedY));
        return true;
    }

}
