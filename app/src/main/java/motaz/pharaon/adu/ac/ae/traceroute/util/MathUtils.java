package motaz.pharaon.adu.ac.ae.traceroute.util;

/*
 * Purpose:
 * - Small numeric utilities shared across the engine and gameplay layers.
 */
public final class MathUtils {

    private MathUtils() {}

    /** Clamp a value between min and max (inclusive). */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /** Linear interpolation. */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * clamp(t, 0f, 1f);
    }

    /** Convert normalized screen coordinate (0..1) to NDC (-1..1). */
    public static float normalizedToNdc(float normalized) {
        return normalized * 2.0f - 1.0f;
    }

    /** Convert NDC (-1..1) to normalized screen coordinate (0..1). */
    public static float ndcToNormalized(float ndc) {
        return (ndc + 1.0f) * 0.5f;
    }

    /** Screen-space Y is top-down; NDC Y is bottom-up. */
    public static float normalizedYToNdcY(float normalizedY) {
        return 1.0f - normalizedY * 2.0f;
    }

    /** True if (px, py) lies within the axis-aligned rect defined by origin + size. */
    public static boolean rectContains(float rx, float ry, float rw, float rh,
                                       float px, float py) {
        return px >= rx && px <= rx + rw && py >= ry && py <= ry + rh;
    }
}
