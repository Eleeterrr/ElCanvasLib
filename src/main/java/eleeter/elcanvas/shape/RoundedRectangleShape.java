package eleeter.elcanvas.shape;

import eleeter.elcanvas.api.SdfShape;
import eleeter.elcanvas.api.UniformBinder;

public  class RoundedRectangleShape implements SdfShape
{

    private static final String SHAPE_TYPE = "rounded_rectangle";

    private static final String GLSL_UNIFORMS =
            "uniform vec4 u_radii;\n";

    private static final String GLSL_DISTANCE =
            "float evaluateDistance(vec2 p, vec2 b) {\n" +
            "    float r = (p.x > 0.0) ?\n" +
            "        ((p.y > 0.0) ? u_radii.z : u_radii.y) :\n" +
            "        ((p.y > 0.0) ? u_radii.w : u_radii.x);\n" +
            "    r = clamp(r, 0.0, min(b.x, b.y));\n" +
            "    vec2 q = abs(p) - b + vec2(r);\n" +
            "    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - r;\n" +
            "}\n";

    private float topLeft;
    private float topRight;
    private float bottomRight;
    private float bottomLeft;

    public RoundedRectangleShape()
    {
        this(0.0f);
    }

    public RoundedRectangleShape(float uniformRadius)
    {
        this(uniformRadius, uniformRadius, uniformRadius, uniformRadius);
    }

    public RoundedRectangleShape(float topLeft, float topRight, float bottomRight, float bottomLeft)
    {
        setRadii(topLeft, topRight, bottomRight, bottomLeft);
    }

    public RoundedRectangleShape setRadius(float uniformRadius)
    {
        return setRadii(uniformRadius, uniformRadius, uniformRadius, uniformRadius);
    }

    public RoundedRectangleShape setRadii(float topLeft, float topRight, float bottomRight, float bottomLeft)
    {
        this.topLeft = Math.max(0.0f, topLeft);
        this.topRight = Math.max(0.0f, topRight);
        this.bottomRight = Math.max(0.0f, bottomRight);
        this.bottomLeft = Math.max(0.0f, bottomLeft);
        return this;
    }

    public float getTopLeft()
    {
        return topLeft;
    }

    public float getTopRight()
    {
        return topRight;
    }

    public float getBottomRight()
    {
        return bottomRight;
    }

    public float getBottomLeft()
    {
        return bottomLeft;
    }

    @Override
    public String getShapeType()
    {
        return SHAPE_TYPE;
    }

    @Override
    public String getUniformDeclarationsGlsl()
    {
        return GLSL_UNIFORMS;
    }

    @Override
    public String getDistanceFunctionGlsl()
    {
        return GLSL_DISTANCE;
    }

    @Override
    public void bindUniforms(UniformBinder binder)
    {
        binder.setVec4("u_radii", topLeft, topRight, bottomRight, bottomLeft);
    }
}
