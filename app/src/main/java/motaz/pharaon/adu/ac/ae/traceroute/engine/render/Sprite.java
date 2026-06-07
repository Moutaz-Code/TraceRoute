package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

/*
 * Purpose:
 * - Data container for a 2D textured quad with position, size, and texture reference.
 *
 * For the MVP the render pipeline draws room backgrounds directly.
 * This class is scaffolded for future evidence image sprites or UI
 * elements that need independent positioning and layering.
 */
public class Sprite {

    private float x;
    private float y;
    private float width;
    private float height;
    private int textureId;
    private float alpha;
    private boolean visible;

    public Sprite(float x, float y, float width, float height, int textureId) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textureId = textureId;
        this.alpha = 1.0f;
        this.visible = true;
    }

    public void draw(RenderPipeline renderPipeline) {
        if (!visible || renderPipeline == null) {
            return;
        }
        renderPipeline.drawSprite(x, y, width, height, textureId);
    }

    public float getX()      { return x; }
    public float getY()      { return y; }
    public float getWidth()  { return width; }
    public float getHeight() { return height; }
    public int   getTextureId() { return textureId; }
    public float getAlpha()  { return alpha; }
    public boolean isVisible() { return visible; }

    public void setPosition(float x, float y)  { this.x = x; this.y = y; }
    public void setSize(float w, float h)       { this.width = w; this.height = h; }
    public void setTextureId(int textureId)     { this.textureId = textureId; }
    public void setAlpha(float alpha)           { this.alpha = Math.max(0, Math.min(1, alpha)); }
    public void setVisible(boolean visible)     { this.visible = visible; }
}
