package eleeter.elcanvas.api;

import org.joml.Matrix4fc;


public interface UniformBinder
{

    /**
     * Sets a float uniform.
     */
    void setFloat(String name, float value);

    /**
     * Sets a 2-component vector uniform
     */
    void setVec2(String name, float x, float y);

    /**
     * Sets a 3-component vector uniform.
     */
    void setVec3(String name, float x, float y, float z);

    /**
     * Sets a 4-component vector uniform.
     */
    void setVec4(String name, float x, float y, float z, float w);

    /**
     * Sets an integer uniform.
     */
    void setInt(String name, int value);

    /**
     * Sets a 4x4 matrix uniform.
     */
    void setMat4(String name, Matrix4fc matrix);
}
