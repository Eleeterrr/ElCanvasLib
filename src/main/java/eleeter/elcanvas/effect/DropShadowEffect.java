package eleeter.elcanvas.effect;

import eleeter.elcanvas.api.Color;
import eleeter.elcanvas.api.SdfEffect;
import eleeter.elcanvas.api.UniformBinder;

import java.util.Objects;


public class DropShadowEffect implements SdfEffect
{

    private static final String EFFECT_TYPE = "drop_shadow";

    private Color color;
    private float offsetX;
    private float offsetY;
    private float blur;
    private float spread;
    private boolean screenSpace = true;

    public DropShadowEffect(Color color, float offsetX, float offsetY, float blur, float spread)
    {
        this.color = Objects.requireNonNull(color, "Shadow color forgot to exist");
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.blur = Math.max(0.0f, blur);
        this.spread = spread;
    }

    public DropShadowEffect(Color color, float offsetX, float offsetY, float blur)
    {
        this(color, offsetX, offsetY, blur, 0.0f);
    }

    public DropShadowEffect(Color color, float blur)
    {
        this(color, 0.0f, 4.0f, blur, 0.0f);
    }

    public DropShadowEffect withColor(Color color)
    {
        this.color = Objects.requireNonNull(color);
        return this;
    }

    public DropShadowEffect withOffset(float offsetX, float offsetY)
    {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    public DropShadowEffect withBlur(float blur)
    {
        this.blur = Math.max(0.0f, blur);
        return this;
    }

    public DropShadowEffect withSpread(float spread)
    {
        this.spread = spread;
        return this;
    }

    /**
     * Sets whether the offset is evaluated in screen space or local shape space
     */
    public DropShadowEffect withScreenSpaceOffset(boolean screenSpace)
    {
        this.screenSpace = screenSpace;
        return this;
    }

    public DropShadowEffect withLocalOffset()
    {
        return withScreenSpaceOffset(false);
    }

    public DropShadowEffect withScreenOffset()
    {
        return withScreenSpaceOffset(true);
    }

    public Color getColor()
    {
        return color;
    }

    public float getOffsetX()
    {
        return offsetX;
    }

    public float getOffsetY()
    {
        return offsetY;
    }

    public float getBlur()
    {
        return blur;
    }

    public float getSpread()
    {
        return spread;
    }

    public boolean isScreenSpace()
    {
        return screenSpace;
    }

    @Override
    public String getEffectType()
    {
        return EFFECT_TYPE;
    }

    @Override
    public float getPaddingX()
    {
        return Math.max(getPaddingLeft(0.0f), getPaddingRight(0.0f));
    }

    @Override
    public float getPaddingY()
    {
        return Math.max(getPaddingTop(0.0f), getPaddingBottom(0.0f));
    }

    @Override
    public float getPaddingLeft(float rotationRadians)
    {
        float ox = getLocalOffsetX(rotationRadians);
        return Math.max(0.0f, -ox) + blur + Math.max(0.0f, spread) + 4.0f;
    }

    @Override
    public float getPaddingRight(float rotationRadians)
    {
        float ox = getLocalOffsetX(rotationRadians);
        return Math.max(0.0f, ox) + blur + Math.max(0.0f, spread) + 4.0f;
    }

    @Override
    public float getPaddingTop(float rotationRadians)
    {
        float oy = getLocalOffsetY(rotationRadians);
        return Math.max(0.0f, -oy) + blur + Math.max(0.0f, spread) + 4.0f;
    }

    @Override
    public float getPaddingBottom(float rotationRadians)
    {
        float oy = getLocalOffsetY(rotationRadians);
        return Math.max(0.0f, oy) + blur + Math.max(0.0f, spread) + 4.0f;
    }

    private float getLocalOffsetX(float rot)
    {
        if (!screenSpace || rot == 0.0f)
        {
            return offsetX;
        }
        double cos = Math.cos(rot);
        double sin = Math.sin(rot);
        return (float) (offsetX * cos + offsetY * sin);
    }

    private float getLocalOffsetY(float rot)
    {
        if (!screenSpace || rot == 0.0f)
        {
            return offsetY;
        }
        double cos = Math.cos(rot);
        double sin = Math.sin(rot);
        return (float) (-offsetX * sin + offsetY * cos);
    }

    @Override
    public String getUniformDeclarationsGlsl(String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        return "uniform vec4 " + p + "shadowColor;\n" +
               "uniform vec2 " + p + "shadowOffset;\n" +
               "uniform float " + p + "shadowBlur;\n" +
               "uniform float " + p + "shadowSpread;\n" +
               "uniform int " + p + "shadowScreenSpace;\n";
    }

    @Override
    public String getApplyGlsl(String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        return
            "    {\n" +
            "        vec2 " + p + "effOffset = " + p + "shadowOffset;\n" +
            "        if (" + p + "shadowScreenSpace != 0 && u_rotation != 0.0) {\n" +
            "            float " + p + "cosA = cos(u_rotation);\n" +
            "            float " + p + "sinA = sin(u_rotation);\n" +
            "            " + p + "effOffset = vec2(\n" +
            "                " + p + "shadowOffset.x * " + p + "cosA + " + p + "shadowOffset.y * " + p + "sinA,\n" +
            "                -" + p + "shadowOffset.x * " + p + "sinA + " + p + "shadowOffset.y * " + p + "cosA\n" +
            "            );\n" +
            "        }\n" +
            "        vec2 " + p + "pos = v_localPos - " + p + "effOffset;\n" +
            "        float " + p + "dist = evaluateDistance(" + p + "pos, v_halfSize) - " + p + "shadowSpread;\n" +
            "        float " + p + "blurWidth = max(" + p + "shadowBlur, edge);\n" +
            "        float " + p + "alpha = 1.0 - smoothstep(-" + p + "blurWidth * 0.5, " + p + "blurWidth * 0.5, " + p + "dist);\n" +
            "        if (baseFillColor.a <= 0.0) {\n" +
            "            " + p + "alpha *= smoothstep(-edge * 0.5, edge * 0.5, dist);\n" +
            "        }\n" +
            "        vec4 " + p + "col = vec4(" + p + "shadowColor.rgb, " + p + "shadowColor.a * " + p + "alpha);\n" +
            "        fragColor = mix(" + p + "col, fragColor, fragColor.a);\n" +
            "    }\n";
    }

    @Override
    public void bindUniforms(UniformBinder binder, String prefix)
    {
        String p = (prefix != null) ? prefix : "";
        binder.setVec4(p + "shadowColor", color.r(), color.g(), color.b(), color.a());
        binder.setVec2(p + "shadowOffset", offsetX, offsetY);
        binder.setFloat(p + "shadowBlur", blur);
        binder.setFloat(p + "shadowSpread", spread);
        binder.setInt(p + "shadowScreenSpace", screenSpace ? 1 : 0);
    }
}
