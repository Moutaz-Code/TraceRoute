package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.Texture;

public class RenderPipeline {
    private static final float ROOM_BACKGROUND_SAFE_SCALE = 0.96f;

    private DebugQuad debugQuad;
    private ColoredQuad coloredQuad;

    private TexturedQuad texturedQuad;
    private Context context;
    private Map<String, Texture> textureCache;

    private int viewportWidth;
    private int viewportHeight;


    public void initialize(Context context) {
        // Initialize shaders, load textures, set up any necessary OpenGL state.
        this.context = context.getApplicationContext();
        textureCache = new HashMap<>();

        debugQuad = new DebugQuad();
        coloredQuad = new ColoredQuad();
        texturedQuad = new TexturedQuad();
    }

    public void resize(int width, int height) {
        viewportHeight = height;
        viewportWidth = width;
    }

    public void drawSprite(float x, float y, float width, float height, int textureId) {
        // Bind the appropriate shader and texture, set up vertex data for a quad, and issue the draw call.
    }

    public void drawDebugQuad(float x, float y, float width, float height) {
        debugQuad.draw(x, y, width, height);
    }

    public void drawColorQuad(float x, float y, float width, float height, float r, float g, float b, float a) {
        coloredQuad.draw(x, y, width, height, r, g, b, a);
    }

    public void drawRoomBackground(String texturePath) {
        if (texturePath == null || viewportWidth == 0 || viewportHeight == 0) {
            return;
        }

        Texture roomTexture = getTexture(texturePath);
        if (roomTexture == null) {
            return;
        }

        float screenAspect = (float) viewportWidth / viewportHeight;
        float textureAspect = (float) roomTexture.getWidth() / roomTexture.getHeight();

        float drawWidth;
        float drawHeight;
        
        if (textureAspect > screenAspect) {
            // Image is wider than screen, fit width.
            drawWidth = 2.0f;
            drawHeight = 2.0f * (screenAspect / textureAspect);
        } else {
            // Image is taller/narrower than screen, fit height.
            drawHeight = 2.0f;
            drawWidth = 2.0f * (textureAspect / screenAspect);
        }

        drawWidth *= ROOM_BACKGROUND_SAFE_SCALE;
        drawHeight *= ROOM_BACKGROUND_SAFE_SCALE;

        float x = -drawWidth / 2.0f;
        float y = -drawHeight / 2.0f;

        texturedQuad.draw(roomTexture.getTextureId(), x, y, drawWidth, drawHeight);

    }

    private Texture getTexture(String texturePath) {
        Texture texture = textureCache.get(texturePath);
        if (texture == null) {
            texture = Texture.fromAsset(context, texturePath);
            textureCache.put(texturePath, texture);
        }

        return texture;
    }
}
