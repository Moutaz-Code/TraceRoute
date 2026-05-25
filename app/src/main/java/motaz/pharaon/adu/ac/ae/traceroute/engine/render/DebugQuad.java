package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.ShaderProgram;

public class DebugQuad {
    private static final String VERTEX_SHADER =
            "#version 300 es\n" +
                    "layout(location = 0) in vec2 aPosition;\n" +
                    "void main() {\n" +
                    "    gl_Position = vec4(aPosition, 0.0, 1.0);\n" +
                    "}\n";

    private static final String FRAGMENT_SHADER =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "out vec4 fragColor;\n" +
                    "void main() {\n" +
                    "    fragColor = vec4(0.1, 0.9, 0.7, 1.0);\n" +
                    "}\n";

    private final ShaderProgram shaderProgram;

    public DebugQuad() {
        shaderProgram = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    public void draw(float x, float y, float width, float height) {
        float left = x;
        float right = x + width;
        float bottom = y;
        float top = y + height;

        float[] vertices = {
                left, bottom,
                right, bottom,
                left, top,

                left, top,
                right, bottom,
                right, top
        };

        FloatBuffer vertexBuffer = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        shaderProgram.use();

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(
                0,
                2,
                GLES30.GL_FLOAT,
                false,
                2 * 4,
                vertexBuffer
        );

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);

        GLES30.glDisableVertexAttribArray(0);
    }
}