package motaz.pharaon.adu.ac.ae.traceroute.engine.render.postprocess;

/*
 * Single manual tuning surface for the room/background OpenGL effect.
 *
 * Values here affect only the GLSurfaceView room art. Android UI overlays,
 * menus, handheld, laptop, and dialogue screens are not postprocessed.
 */
public final class RoomEffectSettings {
    public static final int TONEMAPPER_NONE = 0;
    public static final int TONEMAPPER_REINHARD = 1;
    public static final int TONEMAPPER_ACES_APPROXIMATION = 2;

    public static final int DITHER_MODE_NONE = 0;
    public static final int DITHER_MODE_HASH_NOISE = 1;
    public static final int DITHER_MODE_BAYER_4X4 = 2;
    public static final int DITHER_MODE_BAYER_8X8 = 3;

    public static final RoomEffectSettings ACTIVE = new RoomEffectSettings();

    public final float canvasScale = 1.0f;
    public final float effectStrength = 1.0f;

    public final boolean crtEnabled = false;
    public final float crtWarpStrength = 0.0f;
    public final float scanlineFrequency = 1200.0f;
    public final float scanlineIntensity = 0.025f;
    public final float scanlineSpeed = 0.0f;

    // DITHER_MODE_HASH_NOISE is static screen/texture hash noise.
    // DITHER_MODE_BAYER_4X4 and DITHER_MODE_BAYER_8X8 are ordered Bayer matrices.
    // orderedDitherPixelSize controls how many screen pixels each matrix cell covers.
    public final boolean ditherEnabled = false;
    public final int ditherMode = DITHER_MODE_BAYER_8X8;
    public final float ditherLevels = 32.0f;
    public final float ditherStrength = 1.0f;
    public final float noiseStrength = 0.25f;
    public final float noiseSeed = 0.0f;
    public final float orderedDitherPixelSize = 16.0f;
    public final float orderedDitherThresholdStrength = 1.0f;
    public final boolean ditherPaletteEnabled = false;
    public final float ditherPaletteMix = 0.0f;
    public final float paletteShadowR = 0.12f;
    public final float paletteShadowG = 0.17f;
    public final float paletteShadowB = 0.20f;
    public final float paletteMidR = 0.52f;
    public final float paletteMidG = 0.57f;
    public final float paletteMidB = 0.56f;
    public final float paletteHighlightR = 0.94f;
    public final float paletteHighlightG = 0.87f;
    public final float paletteHighlightB = 0.70f;

    public final boolean vignetteEnabled = true;
    public final float vignetteInnerRadius = 0.52f;
    public final float vignetteOuterRadius = 1.85f;
    public final float vignetteStrength = 0.8f;
    public final float vignetteCenterLift = 0.03f;

    public final int toneMapper = TONEMAPPER_ACES_APPROXIMATION;
    public final float exposure = 0.82f;
    public final float gamma = 0.9f;

    public final float saturation = 0.74f;
    public final float contrast = 0.9f;
    public final float brightness = 0.0f;
    public final float temperature = 0.0f;
    public final float tintBlend = 0.65f;
    public final float shadowTintR = 0.82f;
    public final float shadowTintG = 0.91f;
    public final float shadowTintB = 1.02f;
    public final float highlightTintR = 1.05f;
    public final float highlightTintG = 0.99f;
    public final float highlightTintB = 0.88f;

    private RoomEffectSettings() {
    }
}
