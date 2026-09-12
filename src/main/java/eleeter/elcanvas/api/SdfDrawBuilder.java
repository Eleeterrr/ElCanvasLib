package eleeter.elcanvas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SdfDrawBuilder
{

    private final SdfCanvas canvas;
    private final SdfShape shape;
    private float x;
    private float y;
    private float width;
    private float height;
    private float rotation = 0.0f;
    private float pivotX = 0.5f;
    private float pivotY = 0.5f;
    private Fill fill = Color.WHITE;
    private final List<SdfEffect> effects = new ArrayList<>();

    public SdfDrawBuilder(SdfCanvas canvas, SdfShape shape)
    {
        this.canvas = Objects.requireNonNull(canvas, "Canvas forgot to show up!");
        this.shape = Objects.requireNonNull(shape, "Shape forgot to show up!");
    }

    public SdfDrawBuilder at(float x, float y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public SdfDrawBuilder size(float width, float height)
    {
        this.width = width;
        this.height = height;
        return this;
    }

    public SdfDrawBuilder bounds(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public SdfDrawBuilder color(Fill fill)
    {
        this.fill = Objects.requireNonNull(fill, "Fill cannot be null");
        return this;
    }

    public SdfDrawBuilder fill(Fill fill)
    {
        this.fill = Objects.requireNonNull(fill, "Fill cannot be null");
        return this;
    }

    public SdfDrawBuilder rotation(float angleRadians)
    {
        this.rotation = angleRadians;
        return this;
    }

    public SdfDrawBuilder rotationDegrees(float degrees)
    {
        this.rotation = (float) Math.toRadians(degrees);
        return this;
    }

    public SdfDrawBuilder pivot(float normalizedX, float normalizedY)
    {
        this.pivotX = normalizedX;
        this.pivotY = normalizedY;
        return this;
    }

    public SdfDrawBuilder with(SdfEffect effect)
    {
        if (effect != null)
        {
            this.effects.add(effect);
        }
        return this;
    }

    public SdfDrawBuilder withAll(List<SdfEffect> effects)
    {
        if (effects != null)
        {
            this.effects.addAll(effects);
        }
        return this;
    }

    public void draw()
    {
        canvas.drawRotatedShape(shape, x, y, width, height, rotation, pivotX, pivotY, fill, effects);
    }
}
