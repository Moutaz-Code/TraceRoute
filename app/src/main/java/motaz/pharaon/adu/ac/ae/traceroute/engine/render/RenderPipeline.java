package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

import android.content.Context;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.Texture;

public class RenderPipeline {
    private DebugQuad debugQuad;
    private ColoredQuad coloredQuad;
    private Texture roomTestTexture;

    public void initialize(Context context) {
        // Initialize shaders, load textures, set up any necessary OpenGL state.
        debugQuad = new DebugQuad();
        coloredQuad = new ColoredQuad();
        roomTestTexture = Texture.fromAsset(context, "textures/scenes/room_test.png");
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
    public void drawColorQuad(float x, float y, float width, float height, float r, float g, float b, float a) {
        coloredQuad.draw(x, y, width, height);
    }
    public void drawRoomBackground(){
        // Texture loading is now verified here; textured drawing comes next.
        if (roomTestTexture == null) {
            return;
        }
    }
}
