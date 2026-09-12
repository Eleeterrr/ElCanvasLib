package eleeter.elcanvas.shape;

import eleeter.elcanvas.api.SdfShape;
import eleeter.elcanvas.api.UniformBinder;


public class RectangleShape implements SdfShape
{

    public static final RectangleShape INSTANCE = new RectangleShape();

    private static final String SHAPE_TYPE = "rectangle";

    private static final String GLSL_DISTANCE =
            "float evaluateDistance(vec2 p, vec2 b) {\n" +
            "    vec2 d = abs(p) - b;\n" +
            "    return length(max(d, 0.0)) + min(max(d.x, d.y), 0.0);\n" +
            "}\n";

    @Override
    public String getShapeType()
    {
        return SHAPE_TYPE;
    }

    @Override
    public String getDistanceFunctionGlsl()
    {
        return GLSL_DISTANCE;
    }

    @Override
    public void bindUniforms(UniformBinder binder)
    {
    }
}
