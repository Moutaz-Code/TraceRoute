package motaz.pharaon.adu.ac.ae.traceroute;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import motaz.pharaon.adu.ac.ae.traceroute.core.GameUiEventListener;
import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.GLRenderer;

public class GameView extends GLSurfaceView implements GameUiEventListener {
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
    private GameUiEventListener uiEventListener;

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
        renderer = new GLRenderer(context, this);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void setUiEventListener(GameUiEventListener uiEventListener) {
        this.uiEventListener = uiEventListener;
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

    @Override
    public void onOpenHandheldRequested() {
        post(() -> {
            if (uiEventListener != null) {
                uiEventListener.onOpenHandheldRequested();
            }
        });
    }

    @Override
    public void onOpenLaptopRequested() {
        post(() -> {
            if (uiEventListener != null) {
                uiEventListener.onOpenLaptopRequested();
            }
        });
    }

    @Override
    public void onEvidenceInspectionRequested(String evidenceId, boolean alreadyCollected) {
        post(() -> {
            if (uiEventListener != null) {
                uiEventListener.onEvidenceInspectionRequested(evidenceId, alreadyCollected);
            }
        });
    }

    @Override
    public void onRoomContextChanged(String roomName, String contextText, String hotspotHint) {
        post(() -> {
            if (uiEventListener != null) {
                uiEventListener.onRoomContextChanged(roomName, contextText, hotspotHint);
            }
        });
    }

}
