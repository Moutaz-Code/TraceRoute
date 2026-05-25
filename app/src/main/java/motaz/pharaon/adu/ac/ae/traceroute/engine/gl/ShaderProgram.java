package motaz.pharaon.adu.ac.ae.traceroute.engine.gl;
import android.opengl.GLES30;

        /*
        * Purpose:
        * - Encapsulate an OpenGL shader program, including compilation and linking.
        *
        * Responsibilities:
        * - Compile vertex and fragment shaders from source code.
        * - Link shaders into a complete program.
        * - Provide methods to set uniform variables and bind the program for rendering.
        *
        * Depends on:
        * - OpenGL ES 3.0 API for shader compilation and program management.
        *
        * Used by:
        * - GLRenderer for rendering game objects with specific shaders.
        */

public class ShaderProgram {
        private int programId;

        public ShaderProgram(String vertexSource, String fragmentSource) {
            // Compile vertex and fragment shaders, link them into a program, and store the program ID.
            int vertexShader = compileShader(GLES30.GL_VERTEX_SHADER, vertexSource);
            int fragmentShader = compileShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
            programId = GLES30.glCreateProgram();

            if(programId == 0) {
                throw new RuntimeException("Failed to create shader program.");
            }
            GLES30.glAttachShader(programId, vertexShader);
            GLES30.glAttachShader(programId, fragmentShader);
            GLES30.glLinkProgram(programId);

            int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus,0);

            if (linkStatus[0] == 0) {
                String infoLog = GLES30.glGetProgramInfoLog(programId);
                GLES30.glDeleteProgram(programId);
                throw new RuntimeException("Failed to link shader program: " + infoLog);
            }

            GLES30.glDetachShader(programId, vertexShader);
            GLES30.glDetachShader(programId, fragmentShader);
            GLES30.glDeleteShader(vertexShader);
            GLES30.glDeleteShader(fragmentShader);

        }

    private int compileShader(int glVertexShader, String vertexSource) {
        int shaderId = GLES30.glCreateShader(glVertexShader);
        if (shaderId == 0) {
            throw new RuntimeException("Failed to create shader.");
        }
        GLES30.glShaderSource(shaderId, vertexSource);
        GLES30.glCompileShader(shaderId);

        int[] compileStatus = new int[1];
        GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            String infoLog = GLES30.glGetShaderInfoLog(shaderId);
            GLES30.glDeleteShader(shaderId);
            throw new RuntimeException("Failed to compile shader: " + infoLog);
        }

        return shaderId;
    }

    public void use() {
            GLES30.glUseProgram(programId);
        }

        public void setUniform(String name, float value) {
            // Set a float uniform variable in the shader.
        }

        public void setUniform(String name, int value) {
            // Set an int uniform variable in the shader.
        }

        public void setUniform(String name, float[] matrix) {
            // Set a matrix uniform variable in the shader (e.g., for transformations).
        }

        public void delete() {
            GLES30.glDeleteProgram(programId);
        }

    public int getProgramId() {
        return programId;
    }
}
