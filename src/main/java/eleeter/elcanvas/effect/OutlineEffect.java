package eleeter.elcanvas.effect;

import eleeter.elcanvas.api.Color;
import eleeter.elcanvas.api.SdfEffect;
import eleeter.elcanvas.api.UniformBinder;

import java.util.Objects;


public class OutlineEffect implements SdfEffect
{

    public enum Alignment
    {
        /** Outline extends outward from the shape's boundary */
        OUTER,
        /** Outline straddles the boundary evenly */
        CENTERED,
        /** Outline extends inward from the shape's boundary */
        INNER
    }

    private static final String EFFECT_TYPE = "outline";

    private Color color;
    private float thickness;
    private float softness;
    private Alignment alignment;

    public OutlineEffect(Color color, float thickness, float softness, Alignment alignment)
    {
        this.color = Objects.requireNonNull(color, "Outline color forgot to exist");
        this.thickness = Math.max(0.0f, thickness);
        this.softness = Math.max(0.0f, softness);
        this.alignment = Objects.requireNonNull(alignment, "Outline alignment forgot to exist");
    }

    public OutlineEffect(Color color, float thickness, float softness)
    {
        this(color, thickness, softness, Alignment.OUTER);
    }

    public OutlineEffect(Color color, float thickness)
    {
        this(color, thickness, 0.0f, Alignment.OUTER);
    }

    public static OutlineEffect outer(Color color, float thickness)
    {
        return new OutlineEffect(color, thickness, 0.0f, Alignment.OUTER);
    }

    public static OutlineEffect centered(Color color, float thickness)
    {
        return new OutlineEffect(color, thickness, 0.0f, Alignment.CENTERED);
    }

    public static OutlineEffect inner(Color color, float thickness)
    {
        return new OutlineEffect(color, thickness, 0.0f, Alignment.INNER);
    }

    public OutlineEffect withColor(Color color)
    {
        this.color = Objects.requireNonNull(color);
        return this;
    }

    public OutlineEffect withThickness(float thickness)
    {
        this.thickness = Math.max(0.0f, thickness);
        return this;
    }

    public OutlineEffect withSoftness(float softness)
    {
        this.softness = Math.max(0.0f, softness);
        return this;
    }

    public OutlineEffect withAlignment(Alignment alignment)
    {
        this.alignment = Objects.requireNonNull(alignment);
        return this;
    }

    public Color getColor()
    {
        return color;
    }

    public float getThickness()
    {
        return thickness;
    }

    public float getSoftness()
    {
        return softness;
    }

    public Alignment getAlignment()
    {
        return alignment;
    }

    @Override
    public String getEffectType()
    {
        return EFFECT_TYPE + "_" + alignment.name().toLowerCase();
    }

    @Override
    public float getPaddingX()
    {
        return computePadding();
    }

    @Override
    public float getPaddingY()
    {
        return computePadding();
    }

    private float computePadding()
    {
        float effectiveSoft = (softness > 0.0f) ? softness : 2.0f;
        switch (alignment)
        {
            case OUTER:
                return thickness + effectiveSoft;
            case CENTERED:
                return thickness * 0.5f + effectiveSoft;
            case INNER:
            default:
                return 0.0f;
        }
    }

    private float getAlignmentOffset()
    {
        switch (alignment)
        {
            case OUTER:
                return -thickness * 0.5f;
            case INNER:
                return thickness * 0.5f;
            case CENTERED:
            default:
                return 0.0f;
        }
    }

    @Override
    public String getUniformDeclarationsGlsl(String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        return "uniform vec4 " + p + "outlineColor;\n" +
               "uniform float " + p + "outlineThickness;\n" +
               "uniform float " + p + "outlineSoftness;\n" +
               "uniform float " + p + "outlineOffset;\n";
    }

    @Override
    public String getApplyGlsl(String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        return
            "    {\n" +
            "        float " + p + "d = abs(dist + " + p + "outlineOffset) - " + p + "outlineThickness * 0.5;\n" +
            "        float " + p + "blur = (" + p + "outlineSoftness > 0.0) ? " + p + "outlineSoftness : edge;\n" +
            "        float " + p + "alpha = 1.0 - smoothstep(-" + p + "blur * 0.5, " + p + "blur * 0.5, " + p + "d);\n" +
            "        vec4 " + p + "col = vec4(" + p + "outlineColor.rgb, " + p + "outlineColor.a * " + p + "alpha);\n" +
            "        fragColor = mix(fragColor, " + p + "col, " + p + "col.a);\n" +
            "    }\n";
    }

    @Override
    public void bindUniforms(UniformBinder binder, String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        binder.setVec4(p + "outlineColor", color.r(), color.g(), color.b(), color.a());
        binder.setFloat(p + "outlineThickness", thickness);
        binder.setFloat(p + "outlineSoftness", softness);
        binder.setFloat(p + "outlineOffset", getAlignmentOffset());
    }
}
