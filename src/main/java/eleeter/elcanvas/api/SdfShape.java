package eleeter.elcanvas.api;


public interface SdfShape
{
    String getShapeType();

    default String getUniformDeclarationsGlsl()
    {
        return "";
    }

    String getDistanceFunctionGlsl();

    void bindUniforms(UniformBinder binder);
}
