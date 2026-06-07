package motaz.pharaon.adu.ac.ae.traceroute.engine.entity;

/*
 * Purpose:
 * - Base class placeholder for scene entities in the OpenGL world.
 *
 * For the MVP the full entity–component system is not needed.
 * WorldInvestigationScene drives rendering directly through RenderPipeline.
 * This class exists to satisfy potential future use and legacy imports.
 */
public class Entity {

    private float x;
    private float y;
    private boolean active;

    public Entity() {
        active = true;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setPosition(float x, float y) { this.x = x; this.y = y; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
