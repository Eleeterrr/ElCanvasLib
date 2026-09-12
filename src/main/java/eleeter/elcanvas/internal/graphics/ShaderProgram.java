package eleeter.elcanvas.internal.graphics;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL20;


public class ShaderProgram
{

    private int programId;
    private final Map<String, Integer> uniformLocationCache = new HashMap<>();
    private boolean linked = false;

    public ShaderProgram(String vertexShaderSource, String fragmentShaderSource)
    {
        compileAndLink(vertexShaderSource, fragmentShaderSource);
    }

    private void compileAndLink(String vertexSource, String fragmentSource)
    {
        int vertexShader = compileShader(GL20.GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = compileShader(GL20.GL_FRAGMENT_SHADER, fragmentSource);

        programId = GL20.glCreateProgram();
        if (programId == 0)
        {
            throw new RuntimeException("OpenGL shader program decided not to compile");
        }

        GL20.glAttachShader(programId, vertexShader);
        GL20.glAttachShader(programId, fragmentShader);
        GL20.glLinkProgram(programId);

        int linkStatus = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
        if (linkStatus == GL20.GL_FALSE)
        {
            String log = GL20.glGetProgramInfoLog(programId);
            GL20.glDeleteShader(vertexShader);
            GL20.glDeleteShader(fragmentShader);
            GL20.glDeleteProgram(programId);
            throw new RuntimeException("OpenGL shader program linking failed:\n" + log);
        }

        GL20.glDetachShader(programId, vertexShader);
        GL20.glDetachShader(programId, fragmentShader);
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        linked = true;
    }

    private int compileShader(int type, String source)
    {
        int shaderId = GL20.glCreateShader(type);
        if (shaderId == 0)
        {
            throw new RuntimeException("Failed to allocate OpenGL shader of type: " + type);
        }

        GL20.glShaderSource(shaderId, source);
        GL20.glCompileShader(shaderId);

        int compileStatus = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS);
        if (compileStatus == GL20.GL_FALSE)
        {
            String log = GL20.glGetShaderInfoLog(shaderId);
            GL20.glDeleteShader(shaderId);
            String typeName = (type == GL20.GL_VERTEX_SHADER) ? "Vertex" : "Fragment";
            throw new RuntimeException(typeName + " shader compilation failed:\n" + log + "\nSource:\n" + source);
        }

        return shaderId;
    }

    public void bind()
    {
        if (!linked)
        {
            throw new IllegalStateException("Cannot bind unlinked shader program");
        }
        GL20.glUseProgram(programId);
    }

    public void unbind()
    {
        GL20.glUseProgram(0);
    }

    public int getUniformLocation(String name)
    {
        return uniformLocationCache.computeIfAbsent(name, n -> GL20.glGetUniformLocation(programId, n));
    }

    public void dispose()
    {
        if (!linked)
        {
            return;
        }
        unbind();
        GL20.glDeleteProgram(programId);
        uniformLocationCache.clear();
        programId = 0;
        linked = false;
    }

    public int getProgramId()
    {
        return programId;
    }
}
