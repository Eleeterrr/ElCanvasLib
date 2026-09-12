package eleeter.elcanvas.shape;

import eleeter.elcanvas.api.SdfShape;
import eleeter.elcanvas.api.UniformBinder;


public class CapsuleShape implements SdfShape
{

    public static final CapsuleShape INSTANCE = new CapsuleShape();

    private static final String SHAPE_TYPE = "capsule";

    private static final String GLSL_DISTANCE =
            "float evaluateDistance(vec2 p, vec2 b) {\n" +
            "    if (b.x > b.y) {\n" +
            "        float r = b.y;\n" +
            "        p.x = max(abs(p.x) - (b.x - r), 0.0);\n" +
            "        return length(p) - r;\n" +
            "    } else {\n" +
            "        float r = b.x;\n" +
            "        p.y = max(abs(p.y) - (b.y - r), 0.0);\n" +
            "        return length(p) - r;\n" +
            "    }\n" +
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
