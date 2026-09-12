package eleeter.elcanvas.api;

import java.util.Objects;


public class GradientStop
{

    private final float position;
    private final Color color;

    public GradientStop(float position, Color color)
    {
        this.position = Math.max(0.0f, Math.min(1.0f, position));
        this.color = Objects.requireNonNull(color, "Stop color forgot to show up!");
    }

    public static GradientStop of(float position, Color color)
    {
        return new GradientStop(position, color);
    }

    public float position()
    {
        return position;
    }

    public Color color()
    {
        return color;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradientStop that = (GradientStop) o;
        return Float.compare(that.position, position) == 0 && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(position, color);
    }

    @Override
    public String toString()
    {
        return "GradientStop{" + "position=" + position + ", color=" + color + '}';
    }
}
