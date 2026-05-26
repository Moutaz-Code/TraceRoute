package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

public class VisualAnnotation {
    /*
     * Purpose:
     * - Defines one non-functional scene visualization label for graybox/debug layout.
     *
     * Responsibilities:
     * - Mark visual scene elements that are not currently tappable.
     * - Keep mental-map labels separate from Hotspot touchzones.
     */

    private final String id;
    private final String label;
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public VisualAnnotation(String id, String label, float x, float y, float width, float height) {
        this.id = id;
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
