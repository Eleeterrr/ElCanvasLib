package eleeter.elcanvas.internal.graphics;

import eleeter.elcanvas.api.UniformBinder;
import org.joml.Matrix4fc;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;


public class GlUniformBinder implements UniformBinder
{

    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
    private ShaderProgram activeProgram;

    public void setActiveProgram(ShaderProgram program)
    {
        this.activeProgram = program;
    }

    private int requireLocation(String name)
    {
        if (activeProgram == null)
        {
            throw new IllegalStateException("No active ShaderProgram set on GlUniformBinder");
        }
        return activeProgram.getUniformLocation(name);
    }

    @Override
    public void setFloat(String name, float value)
    {
        int loc = requireLocation(name);
        if (loc != -1)
        {
            glUniform1f(loc, value);
        }
    }

    @Override
    public void setVec2(String name, float x, float y)
    {
        int loc = requireLocation(name);
        if (loc != -1)
        {
            glUniform2f(loc, x, y);
        }
    }

    @Override
    public void setVec3(String name, float x, float y, float z)
    {
        int loc = requireLocation(name);
        if (loc != -1)
        {
            glUniform3f(loc, x, y, z);
        }
    }

    @Override
    public void setVec4(String name, float x, float y, float z, float w)
    {
        int loc = requireLocation(name);
        if (loc != -1)
        {
            glUniform4f(loc, x, y, z, w);
        }
    }

    @Override
    public void setInt(String name, int value)
    {
        int loc = requireLocation(name);
        if (loc != -1)
        {
            glUniform1i(loc, value);
        }
    }

    @Override
    public void setMat4(String name, Matrix4fc matrix)
    {
        int loc = requireLocation(name);
        if (loc != -1)
        {
            matrixBuffer.clear();
            matrix.get(matrixBuffer);
            glUniformMatrix4fv(loc, false, matrixBuffer);
        }
    }
}
