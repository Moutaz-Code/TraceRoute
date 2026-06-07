package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

/*
 * Purpose:
 * - Represents the orthographic camera used for 2D room rendering.
 *
 * For the MVP the render pipeline works in NDC (-1..1) directly, so
 * the camera is effectively fixed.  This class documents that design
 * and provides a home for any future zoom or pan support.
 */
public class Camera {

    private float viewportWidth;
    private float viewportHeight;

    /** NDC projection — no matrix multiplication needed for 2D quads. */
    public Camera(float viewportWidth, float viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    public void resize(float width, float height) {
        viewportWidth = width;
        viewportHeight = height;
    }

    /**
     * Convert normalized screen coordinates (0..1) to NDC (-1..1).
     * Used by WorldInvestigationScene when rendering hotspot overlays.
     */
    public float normalizedToNdcX(float normalizedX) {
        return normalizedX * 2.0f - 1.0f;
    }

    public float normalizedToNdcY(float normalizedY) {
        // Screen Y is top-down; NDC Y is bottom-up.
        return 1.0f - normalizedY * 2.0f;
    }

    public float getViewportWidth()  { return viewportWidth; }
    public float getViewportHeight() { return viewportHeight; }
    public float getAspect() {
        return viewportHeight == 0 ? 1.0f : viewportWidth / viewportHeight;
    }
}
