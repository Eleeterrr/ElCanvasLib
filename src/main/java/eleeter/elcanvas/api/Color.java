package eleeter.elcanvas.api;

import java.util.Objects;


public  class Color implements Fill
{

    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f, 1.0f);
    public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f, 1.0f);
    public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f, 1.0f);
    public static final Color TRANSPARENT = new Color(0.0f, 0.0f, 0.0f, 0.0f);

    private final float r;
    private final float g;
    private final float b;
    private final float a;

    public Color(float r, float g, float b, float a)
    {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
        this.a = clamp(a);
    }

    public Color(float r, float g, float b)
    {
        this(r, g, b, 1.0f);
    }

    public static Color rgba(float r, float g, float b, float a)
    {
        return new Color(r, g, b, a);
    }

    public static Color rgb(float r, float g, float b)
    {
        return new Color(r, g, b, 1.0f);
    }


    public static Color fromHex(String hex)
    {
        if (hex == null)
        {
            throw new IllegalArgumentException("Hex string cannot be null");
        }
        String clean = hex.startsWith("#") ? hex.substring(1) : hex;
        if (clean.length() == 6)
        {
            int r = Integer.parseInt(clean.substring(0, 2), 16);
            int g = Integer.parseInt(clean.substring(2, 4), 16);
            int b = Integer.parseInt(clean.substring(4, 6), 16);
            return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
        } else if (clean.length() == 8)
        {
            int r = Integer.parseInt(clean.substring(0, 2), 16);
            int g = Integer.parseInt(clean.substring(2, 4), 16);
            int b = Integer.parseInt(clean.substring(4, 6), 16);
            int a = Integer.parseInt(clean.substring(6, 8), 16);
            return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
        }
        throw new IllegalArgumentException("Invalid hex color: " + hex + ". That is not a hex color.");
    }

    public float r()
    {
        return r;
    }

    public float g()
    {
        return g;
    }

    public float b()
    {
        return b;
    }

    public float a()
    {
        return a;
    }

    public Color withAlpha(float alpha)
    {
        return new Color(this.r, this.g, this.b, alpha);
    }

    @Override
    public int getFillType()
    {
        return Fill.TYPE_SOLID;
    }

    @Override
    public Color getPrimaryColor()
    {
        return this;
    }

    @Override
    public void bindFill(UniformBinder binder)
    {
        binder.setInt("u_fillType", Fill.TYPE_SOLID);
        binder.setVec4("u_color", r, g, b, a);
    }

    private static float clamp(float value)
    {
        return Math.max(0.0f, Math.min(1.0f, value));
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Color color = (Color) o;
        return Float.compare(color.r, r) == 0 &&
               Float.compare(color.g, g) == 0 &&
               Float.compare(color.b, b) == 0 &&
               Float.compare(color.a, a) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(r, g, b, a);
    }

    @Override
    public String toString()
    {
        return "Color{" + "r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + '}';
    }
}
