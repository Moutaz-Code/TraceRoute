package motaz.pharaon.adu.ac.ae.traceroute.engine.render;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.ShaderProgram;

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
            + "in vec2 vTexCoord;\n"
            + "out vec4 fragColor;\n"

            + "void main() {\n"
            +       "fragColor = texture(uTexture, vTexCoord);\n"
            + "}\n";



    private final ShaderProgram shaderProgram;

    public TexturedQuad() {
        shaderProgram = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        vertexBuffer = ByteBuffer
                .allocateDirect(24 * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }

    public void draw(int textureId, float x, float y, float width, float height) {
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

}
