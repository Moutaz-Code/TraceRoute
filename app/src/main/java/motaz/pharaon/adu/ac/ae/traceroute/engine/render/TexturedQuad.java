package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.ShaderProgram;
import motaz.pharaon.adu.ac.ae.traceroute.engine.render.postprocess.RoomEffectSettings;

public class TexturedQuad {

    private final FloatBuffer vertexBuffer;

    private static final String VERTEX_SHADER =
              "#version 300 es\n"
            + "layout(location = 0) in vec2 aPosition;\n"
            + "layout(location = 1) in vec2 aTexCoord;\n"

            + "out vec2 vTexCoord;\n"

            + "void main() {\n"
            +       "vTexCoord = aTexCoord;\n"
            +       "gl_Position = vec4(aPosition, 0.0, 1.0);\n"
            + "}\n";

    private static final String FRAGMENT_SHADER =
              "#version 300 es\n"
            + "precision mediump float;\n"

            + "uniform sampler2D uTexture;\n"
            + "uniform float uTime;\n"
            + "uniform float uEffectStrength;\n"
            + "uniform float uCrtEnabled;\n"
            + "uniform float uCrtWarpStrength;\n"
            + "uniform float uScanlineFrequency;\n"
            + "uniform float uScanlineIntensity;\n"
            + "uniform float uScanlineSpeed;\n"
            + "uniform float uDitherEnabled;\n"
            + "uniform int uDitherMode;\n"
            + "uniform float uDitherLevels;\n"
            + "uniform float uDitherStrength;\n"
            + "uniform float uNoiseStrength;\n"
            + "uniform float uNoiseSeed;\n"
            + "uniform float uOrderedDitherPixelSize;\n"
            + "uniform float uOrderedDitherThresholdStrength;\n"
            + "uniform float uDitherPaletteEnabled;\n"
            + "uniform float uDitherPaletteMix;\n"
            + "uniform vec3 uPaletteShadow;\n"
            + "uniform vec3 uPaletteMid;\n"
            + "uniform vec3 uPaletteHighlight;\n"
            + "uniform float uVignetteEnabled;\n"
            + "uniform float uVignetteInnerRadius;\n"
            + "uniform float uVignetteOuterRadius;\n"
            + "uniform float uVignetteStrength;\n"
            + "uniform float uVignetteCenterLift;\n"
            + "uniform int uToneMapper;\n"
            + "uniform float uExposure;\n"
            + "uniform float uGamma;\n"
            + "uniform float uSaturation;\n"
            + "uniform float uContrast;\n"
            + "uniform float uBrightness;\n"
            + "uniform float uTemperature;\n"
            + "uniform float uTintBlend;\n"
            + "uniform vec3 uShadowTint;\n"
            + "uniform vec3 uHighlightTint;\n"
            + "in vec2 vTexCoord;\n"
            + "out vec4 fragColor;\n"

            + "float random(vec2 value) {\n"
            + "    return fract(sin(dot(value, vec2(12.9898, 78.233))) * 43758.5453);\n"
            + "}\n"

            + "vec3 paletteFromLuma(float value) {\n"
            + "    float t = clamp(value, 0.0, 1.0);\n"
            + "    if (t < 0.5) {\n"
            + "        return mix(uPaletteShadow, uPaletteMid, t * 2.0);\n"
            + "    }\n"
            + "    return mix(uPaletteMid, uPaletteHighlight, (t - 0.5) * 2.0);\n"
            + "}\n"

            + "float bayer2(vec2 position) {\n"
            + "    vec2 cell = floor(mod(position, 2.0));\n"
            + "    if (cell.x < 0.5) {\n"
            + "        return cell.y < 0.5 ? 0.0 : 3.0;\n"
            + "    }\n"
            + "    return cell.y < 0.5 ? 2.0 : 1.0;\n"
            + "}\n"

            + "float bayer4(vec2 position) {\n"
            + "    vec2 cell = floor(mod(position, 4.0));\n"
            + "    vec2 lowBits = mod(cell, 2.0);\n"
            + "    vec2 highBits = floor(cell / 2.0);\n"
            + "    return bayer2(lowBits) * 4.0 + bayer2(highBits);\n"
            + "}\n"

            + "float bayer8(vec2 position) {\n"
            + "    vec2 cell = floor(mod(position, 8.0));\n"
            + "    vec2 lowBits = mod(cell, 4.0);\n"
            + "    vec2 highBits = floor(cell / 4.0);\n"
            + "    return bayer4(lowBits) * 4.0 + bayer2(highBits);\n"
            + "}\n"

            + "float orderedDitherThreshold(vec2 fragCoord) {\n"
            + "    vec2 ditherPosition = floor(fragCoord / max(uOrderedDitherPixelSize, 1.0));\n"
            + "    if (uDitherMode == 2) {\n"
            + "        return (bayer4(ditherPosition) + 0.5) / 16.0;\n"
            + "    }\n"
            + "    if (uDitherMode == 3) {\n"
            + "        return (bayer8(ditherPosition) + 0.5) / 64.0;\n"
            + "    }\n"
            + "    return 0.5;\n"
            + "}\n"

            + "vec3 acesApproximation(vec3 color) {\n"
            + "    const float a = 2.51;\n"
            + "    const float b = 0.03;\n"
            + "    const float c = 2.43;\n"
            + "    const float d = 0.59;\n"
            + "    const float e = 0.14;\n"
            + "    return clamp((color * (a * color + b)) / (color * (c * color + d) + e), 0.0, 1.0);\n"
            + "}\n"

            + "vec3 applyToneMap(vec3 color) {\n"
            + "    color = max(color * max(uExposure, 0.0), vec3(0.0));\n"
            + "    if (uToneMapper == 1) {\n"
            + "        color = color / (color + vec3(1.0));\n"
            + "    } else if (uToneMapper == 2) {\n"
            + "        color = acesApproximation(color);\n"
            + "    }\n"
            + "    float safeGamma = max(uGamma, 0.001);\n"
            + "    return pow(clamp(color, 0.0, 1.0), vec3(1.0 / safeGamma));\n"
            + "}\n"

            + "void main() {\n"
            + "    vec2 sampleCoord = vTexCoord;\n"
            + "    if (uCrtEnabled > 0.5 && uCrtWarpStrength > 0.0) {\n"
            + "        vec2 centeredUv = sampleCoord * 2.0 - 1.0;\n"
            + "        float warpRadius = dot(centeredUv, centeredUv);\n"
            + "        sampleCoord += centeredUv * warpRadius * uCrtWarpStrength;\n"
            + "        sampleCoord = clamp(sampleCoord, 0.0, 1.0);\n"
            + "    }\n"
            + "    vec4 source = texture(uTexture, sampleCoord);\n"
            + "    vec2 centered = sampleCoord * 2.0 - 1.0;\n"
            + "    float radius = dot(centered, centered);\n"
            + "    float luma = dot(source.rgb, vec3(0.299, 0.587, 0.114));\n"
            + "    vec3 processed = mix(vec3(luma), source.rgb, max(uSaturation, 0.0));\n"
            + "    vec3 tint = mix(uShadowTint, uHighlightTint, smoothstep(0.18, 0.82, luma));\n"
            + "    processed = mix(processed, processed * tint, clamp(uTintBlend, 0.0, 1.0));\n"
            + "    vec3 temperatureTint = uTemperature >= 0.0 ? vec3(1.08, 1.00, 0.90) : vec3(0.88, 0.95, 1.08);\n"
            + "    processed = mix(processed, processed * temperatureTint, clamp(abs(uTemperature), 0.0, 1.0));\n"
            + "    processed = (processed - 0.5) * max(uContrast, 0.0) + 0.5 + uBrightness;\n"
            + "    if (uCrtEnabled > 0.5) {\n"
            + "        float scanline = sin((sampleCoord.y + uTime * uScanlineSpeed) * uScanlineFrequency) * uScanlineIntensity;\n"
            + "        processed += scanline;\n"
            + "    }\n"
            + "    if (uDitherEnabled > 0.5 && uDitherMode > 0) {\n"
            + "        float levels = max(uDitherLevels, 2.0);\n"
            + "        float ditherInput = luma;\n"
            + "        if (uDitherMode == 1) {\n"
            + "            float noise = (random(sampleCoord * vec2(1672.0, 941.0) + vec2(uNoiseSeed)) - 0.5) * uNoiseStrength;\n"
            + "            ditherInput += noise;\n"
            + "        } else {\n"
            + "            float threshold = orderedDitherThreshold(gl_FragCoord.xy) - 0.5;\n"
            + "            ditherInput += threshold * uOrderedDitherThresholdStrength / levels;\n"
            + "        }\n"
            + "        float ditheredLuma = floor(clamp(ditherInput, 0.0, 1.0) * levels) / levels;\n"
            + "        processed += (ditheredLuma - luma) * uDitherStrength;\n"
            + "        if (uDitherPaletteEnabled > 0.5) {\n"
            + "            processed = mix(processed, paletteFromLuma(ditheredLuma), clamp(uDitherPaletteMix, 0.0, 1.0));\n"
            + "        }\n"
            + "    }\n"
            + "    if (uVignetteEnabled > 0.5) {\n"
            + "        float vignette = 1.0 - smoothstep(uVignetteInnerRadius, uVignetteOuterRadius, radius);\n"
            + "        float vignetteScale = mix(1.0 - uVignetteStrength, 1.0 + uVignetteCenterLift, vignette);\n"
            + "        processed *= vignetteScale;\n"
            + "    }\n"
            + "    processed = applyToneMap(processed);\n"
            + "    vec3 color = mix(source.rgb, processed, clamp(uEffectStrength, 0.0, 1.0));\n"
            + "    fragColor = vec4(clamp(color, 0.0, 1.0), source.a);\n"
            + "}\n";



    private final ShaderProgram shaderProgram;

    public TexturedQuad() {
        shaderProgram = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        vertexBuffer = ByteBuffer
                .allocateDirect(24 * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }

    public void draw(
            int textureId,
            float x,
            float y,
            float width,
            float height,
            float time,
            RoomEffectSettings settings
    ) {
        float left = x;
        float right = x + width;
        float bottom = y;
        float top = y + height;

        float[] vertices = {
                left,  bottom, 0f, 1f,
                right, bottom, 1f, 1f,
                left,  top,    0f, 0f,

                left,  top,    0f, 0f,
                right, bottom, 1f, 1f,
                right, top,    1f, 0f
        };


        vertexBuffer.clear();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        shaderProgram.use();

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        shaderProgram.setUniform1i("uTexture", 0);
        shaderProgram.setUniform1f("uTime", time);
        applyEffectSettings(settings == null ? RoomEffectSettings.ACTIVE : settings);

        vertexBuffer.position(0);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(
                0,
                2,
                GLES30.GL_FLOAT,
                false,
                4 * 4,
                vertexBuffer
        );

        vertexBuffer.position(2);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(
                1,
                2,
                GLES30.GL_FLOAT,
                false,
                4 * 4,
                vertexBuffer
        );

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);

        GLES30.glDisableVertexAttribArray(1);
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }

    private void applyEffectSettings(RoomEffectSettings settings) {
        shaderProgram.setUniform1f("uEffectStrength", settings.effectStrength);
        shaderProgram.setUniform1f("uCrtEnabled", asUniform(settings.crtEnabled));
        shaderProgram.setUniform1f("uCrtWarpStrength", settings.crtWarpStrength);
        shaderProgram.setUniform1f("uScanlineFrequency", settings.scanlineFrequency);
        shaderProgram.setUniform1f("uScanlineIntensity", settings.scanlineIntensity);
        shaderProgram.setUniform1f("uScanlineSpeed", settings.scanlineSpeed);
        shaderProgram.setUniform1f("uDitherEnabled", asUniform(settings.ditherEnabled));
        shaderProgram.setUniform1i("uDitherMode", settings.ditherMode);
        shaderProgram.setUniform1f("uDitherLevels", settings.ditherLevels);
        shaderProgram.setUniform1f("uDitherStrength", settings.ditherStrength);
        shaderProgram.setUniform1f("uNoiseStrength", settings.noiseStrength);
        shaderProgram.setUniform1f("uNoiseSeed", settings.noiseSeed);
        shaderProgram.setUniform1f("uOrderedDitherPixelSize", settings.orderedDitherPixelSize);
        shaderProgram.setUniform1f("uOrderedDitherThresholdStrength", settings.orderedDitherThresholdStrength);
        shaderProgram.setUniform1f("uDitherPaletteEnabled", asUniform(settings.ditherPaletteEnabled));
        shaderProgram.setUniform1f("uDitherPaletteMix", settings.ditherPaletteMix);
        shaderProgram.setUniform3f("uPaletteShadow", settings.paletteShadowR, settings.paletteShadowG, settings.paletteShadowB);
        shaderProgram.setUniform3f("uPaletteMid", settings.paletteMidR, settings.paletteMidG, settings.paletteMidB);
        shaderProgram.setUniform3f("uPaletteHighlight", settings.paletteHighlightR, settings.paletteHighlightG, settings.paletteHighlightB);
        shaderProgram.setUniform1f("uVignetteEnabled", asUniform(settings.vignetteEnabled));
        shaderProgram.setUniform1f("uVignetteInnerRadius", settings.vignetteInnerRadius);
        shaderProgram.setUniform1f("uVignetteOuterRadius", settings.vignetteOuterRadius);
        shaderProgram.setUniform1f("uVignetteStrength", settings.vignetteStrength);
        shaderProgram.setUniform1f("uVignetteCenterLift", settings.vignetteCenterLift);
        shaderProgram.setUniform1i("uToneMapper", settings.toneMapper);
        shaderProgram.setUniform1f("uExposure", settings.exposure);
        shaderProgram.setUniform1f("uGamma", settings.gamma);
        shaderProgram.setUniform1f("uSaturation", settings.saturation);
        shaderProgram.setUniform1f("uContrast", settings.contrast);
        shaderProgram.setUniform1f("uBrightness", settings.brightness);
        shaderProgram.setUniform1f("uTemperature", settings.temperature);
        shaderProgram.setUniform1f("uTintBlend", settings.tintBlend);
        shaderProgram.setUniform3f("uShadowTint", settings.shadowTintR, settings.shadowTintG, settings.shadowTintB);
        shaderProgram.setUniform3f("uHighlightTint", settings.highlightTintR, settings.highlightTintG, settings.highlightTintB);
    }

    private float asUniform(boolean enabled) {
        return enabled ? 1.0f : 0.0f;
    }

}
