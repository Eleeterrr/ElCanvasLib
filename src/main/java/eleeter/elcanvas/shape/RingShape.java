package eleeter.elcanvas.shape;

import eleeter.elcanvas.api.SdfShape;
import eleeter.elcanvas.api.UniformBinder;


public class RingShape implements SdfShape
{

    private static final String SHAPE_TYPE = "ring";

    private static final String GLSL_UNIFORMS =
            "uniform float u_thickness;\n";

    private static final String GLSL_DISTANCE =
            "float evaluateDistance(vec2 p, vec2 b) {\n" +
            "    float outerR = min(b.x, b.y);\n" +
            "    float halfThick = clamp(u_thickness, 0.0, outerR) * 0.5;\n" +
            "    float midR = outerR - halfThick;\n" +
            "    return abs(length(p) - midR) - halfThick;\n" +
            "}\n";

    private float thickness;

    public RingShape()
    {
        this(4.0f);
    }

    public RingShape(float thickness)
    {
        this.thickness = Math.max(0.1f, thickness);
    }

    public float getThickness()
    {
        return thickness;
    }

    public RingShape setThickness(float thickness)
    {
        this.thickness = Math.max(0.1f, thickness);
        return this;
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
        binder.setFloat("u_thickness", thickness);
    }
}
