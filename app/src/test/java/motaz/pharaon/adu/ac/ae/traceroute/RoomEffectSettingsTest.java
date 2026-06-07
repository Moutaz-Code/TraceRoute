package motaz.pharaon.adu.ac.ae.traceroute;

import motaz.pharaon.adu.ac.ae.traceroute.engine.render.postprocess.RoomEffectSettings;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RoomEffectSettingsTest {
    @Test
    public void activeRoomEffectSettingsStayInUsableRanges() {
        RoomEffectSettings settings = RoomEffectSettings.ACTIVE;

        assertBetween("canvasScale", settings.canvasScale, 0.10f, 1.25f);
        assertBetween("effectStrength", settings.effectStrength, 0.0f, 1.0f);
        assertBetween("crtWarpStrength", settings.crtWarpStrength, 0.0f, 0.25f);
        assertBetween("scanlineIntensity", settings.scanlineIntensity, 0.0f, 0.15f);
        assertTrue("scanlineFrequency must be positive.", settings.scanlineFrequency > 0.0f);

        assertTrue("ditherLevels must be at least 2.", settings.ditherLevels >= 2.0f);
        assertTrue("ditherMode must be a known mode.",
                settings.ditherMode == RoomEffectSettings.DITHER_MODE_NONE
                        || settings.ditherMode == RoomEffectSettings.DITHER_MODE_HASH_NOISE
                        || settings.ditherMode == RoomEffectSettings.DITHER_MODE_BAYER_4X4
                        || settings.ditherMode == RoomEffectSettings.DITHER_MODE_BAYER_8X8);
        assertBetween("ditherStrength", settings.ditherStrength, 0.0f, 1.0f);
        assertBetween("noiseStrength", settings.noiseStrength, 0.0f, 0.25f);
        assertBetween("noiseSeed", settings.noiseSeed, -10000.0f, 10000.0f);
        assertBetween("orderedDitherPixelSize", settings.orderedDitherPixelSize, 1.0f, 16.0f);
        assertBetween("orderedDitherThresholdStrength", settings.orderedDitherThresholdStrength, 0.0f, 4.0f);
        assertBetween("ditherPaletteMix", settings.ditherPaletteMix, 0.0f, 1.0f);

        assertTrue("vignetteOuterRadius must be greater than inner radius.",
                settings.vignetteOuterRadius > settings.vignetteInnerRadius);
        assertBetween("vignetteStrength", settings.vignetteStrength, 0.0f, 1.0f);
        assertBetween("vignetteCenterLift", settings.vignetteCenterLift, 0.0f, 0.35f);

        assertTrue("toneMapper must be a known mode.",
                settings.toneMapper == RoomEffectSettings.TONEMAPPER_NONE
                        || settings.toneMapper == RoomEffectSettings.TONEMAPPER_REINHARD
                        || settings.toneMapper == RoomEffectSettings.TONEMAPPER_ACES_APPROXIMATION);
        assertTrue("exposure must be non-negative.", settings.exposure >= 0.0f);
        assertTrue("gamma must be positive.", settings.gamma > 0.0f);

        assertBetween("saturation", settings.saturation, 0.0f, 3.0f);
        assertBetween("contrast", settings.contrast, 0.0f, 3.0f);
        assertBetween("brightness", settings.brightness, -1.0f, 1.0f);
        assertBetween("temperature", settings.temperature, -1.0f, 1.0f);
        assertBetween("tintBlend", settings.tintBlend, 0.0f, 1.0f);
    }

    private void assertBetween(String name, float value, float min, float max) {
        assertTrue(name + " must be >= " + min + ", was " + value, value >= min);
        assertTrue(name + " must be <= " + max + ", was " + value, value <= max);
    }
}
