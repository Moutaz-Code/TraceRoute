package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

public class RenderPipeline {
    private DebugQuad debugQuad;

    public void initialize() {
        // Initialize shaders, load textures, set up any necessary OpenGL state.
        debugQuad = new DebugQuad();
    }
    public void resize(int width, int height) {
        // Update projection matrices or any size-dependent resources.
    }
    public void drawSprite(float x, float y, float width, float height, int textureId) {
        // Bind the appropriate shader and texture, set up vertex data for a quad, and issue the draw call.
    }
    public void drawDebugQuad(float x, float y, float width, float height) {
        debugQuad.draw(x, y, width, height);
    }
}
