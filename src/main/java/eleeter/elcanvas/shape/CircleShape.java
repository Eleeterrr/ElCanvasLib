package eleeter.elcanvas.shape;

import eleeter.elcanvas.api.SdfShape;
import eleeter.elcanvas.api.UniformBinder;


public class CircleShape implements SdfShape
{

    public static final CircleShape INSTANCE = new CircleShape();

    private static final String SHAPE_TYPE = "circle";

    private static final String GLSL_DISTANCE =
            "float evaluateDistance(vec2 p, vec2 b) {\n" +
            "    float radius = min(b.x, b.y);\n" +
            "    return length(p) - radius;\n" +
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
