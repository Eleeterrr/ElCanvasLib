package eleeter.elcanvas.api;


public interface SdfEffect
{

    /**
     * Used in composite program caching keys
     */
    String getEffectType();

    /**
     * Outer horizontal padding
     */
    default float getPaddingX()
    {
        return 0.0f;
    }

    /**
     * Outer vertical padding
     */
    default float getPaddingY()
    {
        return 0.0f;
    }

    /**
     * Outer left padding in pixels
     */
    default float getPaddingLeft()
    {
        return getPaddingX();
    }

    /**
     * Outer left padding
     */
    default float getPaddingLeft(float rotationRadians)
    {
        return getPaddingLeft();
    }

    /**
     * Outer top padding
     */
    default float getPaddingTop()
    {
        return getPaddingY();
    }

    /**
     * Outer top padding
     */
    default float getPaddingTop(float rotationRadians)
    {
        return getPaddingTop();
    }

    /**
     * Outer right padding
     */
    default float getPaddingRight()
    {
        return getPaddingX();
    }

    /**
     * Outer right padding
     */
    default float getPaddingRight(float rotationRadians)
    {
        return getPaddingRight();
    }

    /**
     * Outer bottom padding
     */
    default float getPaddingBottom()
    {
        return getPaddingY();
    }

    /**
     * Outer bottom padding
     */
    default float getPaddingBottom(float rotationRadians)
    {
        return getPaddingBottom();
    }

    /**
     * GLSL declarations for uniforms
     */
    default String getUniformDeclarationsGlsl(String prefix)
    {
        return getUniformDeclarationsGlsl();
    }

    default String getUniformDeclarationsGlsl()
    {
        return "";
    }

    /**
     * Optional GLSL helper functions needed by this effect
     */
    default String getFunctionsGlsl(String prefix)
    {
        return getFunctionsGlsl();
    }

    default String getFunctionsGlsl()
    {
        return "";
    }

    default String getApplyGlsl(String prefix)
    {
        return getApplyGlsl();
    }

    default String getApplyGlsl()
    {
        return "";
    }

    default void bindUniforms(UniformBinder binder, String prefix)
    {
        bindUniforms(binder);
    }

    default void bindUniforms(UniformBinder binder)
    {
    }
}
