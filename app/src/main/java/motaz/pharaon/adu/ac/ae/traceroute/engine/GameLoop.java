package motaz.pharaon.adu.ac.ae.traceroute.engine;

/*
 * Purpose:
 * - Documents the game loop contract for the project.
 *
 * For the MVP, the game loop is driven by GLSurfaceView via GLRenderer.
 * onDrawFrame acts as the per-frame tick: update → render.
 *
 * This class can become an explicit loop controller if the project moves
 * away from GLSurfaceView's built-in frame callback.
 */
public class GameLoop {

    public interface LoopCallback {
        void onUpdate(float deltaTime);
        void onRender();
    }

    private LoopCallback callback;
    private boolean running;

    public GameLoop(LoopCallback callback) {
        this.callback = callback;
        running = false;
    }

    /** Called each frame by GLRenderer.onDrawFrame. */
    public void tick(float deltaTime) {
        if (!running || callback == null) {
            return;
        }
        callback.onUpdate(deltaTime);
        callback.onRender();
    }

    public void start() { running = true; }
    public void stop()  { running = false; }
    public boolean isRunning() { return running; }
}
