package motaz.pharaon.adu.ac.ae.traceroute.engine.input;

/*
 * Purpose:
 * - Central coordinator for normalized touch input from the GL surface.
 *
 * Responsibilities:
 * - Accept normalized (0..1) x/y tap coordinates from GameView.
 * - Forward them to the active Scene via the registered listener.
 * - Buffer input from the GL thread so it can be consumed on the game update tick.
 *
 * For the MVP, GameView directly calls GLRenderer.onTap which routes to
 * GameController.onTap.  This class is scaffolded for the case where
 * buffering or multi-touch is needed later.
 */
public class InputManager {

    public interface TapListener {
        void onTap(float normalizedX, float normalizedY);
    }

    private TapListener tapListener;
    private float pendingTapX = -1;
    private float pendingTapY = -1;
    private boolean hasPendingTap;

    public InputManager() {
    }

    public void setTapListener(TapListener tapListener) {
        this.tapListener = tapListener;
    }

    /** Called from the Android UI thread (GameView.onTouchEvent). */
    public synchronized void submitTap(float normalizedX, float normalizedY) {
        pendingTapX = normalizedX;
        pendingTapY = normalizedY;
        hasPendingTap = true;
    }

    /** Called from the GL thread each frame to consume any pending input. */
    public synchronized void processFrame() {
        if (hasPendingTap && tapListener != null) {
            tapListener.onTap(pendingTapX, pendingTapY);
            hasPendingTap = false;
        }
    }

    public synchronized boolean hasPendingTap() {
        return hasPendingTap;
    }
}
