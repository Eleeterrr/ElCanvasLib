package eleeter.elcanvas.shape;

import eleeter.elcanvas.api.SdfShape;
import eleeter.elcanvas.api.UniformBinder;


public class TriangleShape implements SdfShape
{

    public static final TriangleShape INSTANCE = new TriangleShape();

    private static final String SHAPE_TYPE = "triangle";

    private static final String GLSL_DISTANCE =
            "float evaluateDistance(vec2 p, vec2 b) {\n" +
            "    p.x = abs(p.x);\n" +
            "    vec2 a = p - vec2(clamp(p.x, 0.0, b.x), b.y);\n" +
            "    vec2 e = vec2(b.x, 2.0 * b.y);\n" +
            "    vec2 w = p - vec2(0.0, -b.y);\n" +
            "    vec2 d = w - e * clamp(dot(w, e) / dot(e, e), 0.0, 1.0);\n" +
            "    float dist = sqrt(min(dot(a, a), dot(d, d)));\n" +
            "    bool inside = (p.y <= b.y) && (p.x * 2.0 * b.y - p.y * b.x - b.x * b.y <= 0.0001);\n" +
            "    return inside ? -dist : dist;\n" +
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
