package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.ShaderProgram;

public class DebugQuad {
    private final FloatBuffer vertexBuffer;

    private static final String VERTEX_SHADER =
                    "#version 300 es\n"
                  + "layout(location = 0) in vec2 aPosition;\n"
                  + "void main() {\n"
                  + "    gl_Position = vec4(aPosition, 0.0, 1.0);\n"
                  + "}\n";

    private static final String FRAGMENT_SHADER =
            "#version 300 es\n"
          + "precision mediump float;\n"
          + "uniform vec4 uColor;\n"
          + "out vec4 fragColor;\n"
          + "void main() {\n"
          + "    fragColor = uColor;\n"
          + "}\n";



    private final ShaderProgram shaderProgram;

    public DebugQuad() {
        shaderProgram = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        vertexBuffer = ByteBuffer
                .allocateDirect(12 * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
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


        vertexBuffer.clear();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        shaderProgram.use();
        shaderProgram.setUniform4f("uColor", 0.1f, 0.9f, 0.7f, 1.0f);

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