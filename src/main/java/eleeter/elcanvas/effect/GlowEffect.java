package eleeter.elcanvas.effect;

import eleeter.elcanvas.api.Color;
import eleeter.elcanvas.api.SdfEffect;
import eleeter.elcanvas.api.UniformBinder;

import java.util.Objects;


public class GlowEffect implements SdfEffect
{

    private static final String EFFECT_TYPE = "glow";

    private Color color;
    private float radius;
    private float intensity;

    public GlowEffect(Color color, float radius, float intensity)
    {
        this.color = Objects.requireNonNull(color, "Glow color forgot to exist");
        this.radius = Math.max(0.0f, radius);
        this.intensity = Math.max(0.0f, intensity);
    }

    public GlowEffect(Color color, float radius)
    {
        this(color, radius, 1.0f);
    }

    public GlowEffect withColor(Color color)
    {
        this.color = Objects.requireNonNull(color);
        return this;
    }

    public GlowEffect withRadius(float radius)
    {
        this.radius = Math.max(0.0f, radius);
        return this;
    }

    public GlowEffect withIntensity(float intensity)
    {
        this.intensity = Math.max(0.0f, intensity);
        return this;
    }

    public Color getColor()
    {
        return color;
    }

    public float getRadius()
    {
        return radius;
    }

    public float getIntensity()
    {
        return intensity;
    }

    @Override
    public String getEffectType()
    {
        return EFFECT_TYPE;
    }

    @Override
    public float getPaddingX()
    {
        return radius + 4.0f;
    }

    @Override
    public float getPaddingY()
    {
        return radius + 4.0f;
    }

    @Override
    public String getUniformDeclarationsGlsl(String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        return "uniform vec4 " + p + "glowColor;\n" +
               "uniform float " + p + "glowRadius;\n" +
               "uniform float " + p + "glowIntensity;\n";
    }

    @Override
    public String getApplyGlsl(String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        return
            "    {\n" +
            "        float " + p + "d = max(dist, 0.0);\n" +
            "        float " + p + "r = max(" + p + "glowRadius, 0.001);\n" +
            "        float " + p + "norm = clamp(" + p + "d / " + p + "r, 0.0, 1.0);\n" +
            "        float " + p + "falloff = pow(1.0 - " + p + "norm, 2.5);\n" +
            "        float " + p + "alpha = clamp(" + p + "falloff * " + p + "glowIntensity, 0.0, 1.0);\n" +
            "        vec4 " + p + "col = vec4(" + p + "glowColor.rgb, " + p + "glowColor.a * " + p + "alpha);\n" +
            "        fragColor = mix(" + p + "col, fragColor, fragColor.a);\n" +
            "    }\n";
    }

    @Override
    public void bindUniforms(UniformBinder binder, String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        binder.setVec4(p + "glowColor", color.r(), color.g(), color.b(), color.a());
        binder.setFloat(p + "glowRadius", radius);
        binder.setFloat(p + "glowIntensity", intensity);
    }
}
