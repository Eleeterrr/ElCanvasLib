package eleeter.elcanvas.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class LinearGradient implements Fill
{

    private static final int MAX_STOPS = 5;

    private final float startX;
    private final float startY;
    private final float endX;
    private final float endY;
    private final List<GradientStop> stops;

    public LinearGradient(float startX, float startY, float endX, float endY, List<GradientStop> stops)
    {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        Objects.requireNonNull(stops, "Stops list cannot be null");
        if (stops.size() < 2)
        {
            throw new IllegalArgumentException("Gradients require at least 2 color stops, got: " + stops.size());
        }
        if (stops.size() > MAX_STOPS)
        {
            throw new IllegalArgumentException("Gradients support up to " + MAX_STOPS + " stops, got: " + stops.size());
        }
        this.stops = Collections.unmodifiableList(new ArrayList<>(stops));
    }

    public LinearGradient(float startX, float startY, Color startColor, float endX, float endY, Color endColor)
    {
        this(startX, startY, endX, endY, List.of(
                new GradientStop(0.0f, startColor),
                new GradientStop(1.0f, endColor)
        ));
    }

    public LinearGradient(float startX, float startY, float endX, float endY, GradientStop... stops)
    {
        this(startX, startY, endX, endY, Arrays.asList(stops));
    }

    public static LinearGradient vertical(Color... colors)
    {
        return new LinearGradient(0.5f, 0.0f, 0.5f, 1.0f, createEquallySpacedStops(colors));
    }

    public static LinearGradient vertical(GradientStop... stops)
    {
        return new LinearGradient(0.5f, 0.0f, 0.5f, 1.0f, Arrays.asList(stops));
    }

    public static LinearGradient horizontal(Color... colors)
    {
        return new LinearGradient(0.0f, 0.5f, 1.0f, 0.5f, createEquallySpacedStops(colors));
    }

    public static LinearGradient horizontal(GradientStop... stops)
    {
        return new LinearGradient(0.0f, 0.5f, 1.0f, 0.5f, Arrays.asList(stops));
    }

    public static LinearGradient diagonal(Color start, Color end)
    {
        return new LinearGradient(0.0f, 0.0f, 1.0f, 1.0f, List.of(
                new GradientStop(0.0f, start),
                new GradientStop(1.0f, end)
        ));
    }

    public static LinearGradient of(float x0, float y0, Color c0, float x1, float y1, Color c1)
    {
        return new LinearGradient(x0, y0, c0, x1, y1, c1);
    }

    private static List<GradientStop> createEquallySpacedStops(Color[] colors)
    {
        Objects.requireNonNull(colors, "Colors cannot be null");
        if (colors.length < 2 || colors.length > MAX_STOPS)
        {
            throw new IllegalArgumentException("Must provide between 2 and " + MAX_STOPS + " colors");
        }
        List<GradientStop> list = new ArrayList<>();
        int count = colors.length;
        for (int i = 0; i < count; i++)
        {
            float pos = (float) i / (count - 1);
            list.add(new GradientStop(pos, colors[i]));
        }
        return list;
    }

    @Override
    public int getFillType()
    {
        return Fill.TYPE_LINEAR_GRADIENT;
    }

    @Override
    public Color getPrimaryColor()
    {
        return stops.get(0).color();
    }

    @Override
    public void bindFill(UniformBinder binder)
    {
        binder.setInt("u_fillType", Fill.TYPE_LINEAR_GRADIENT);
        Color c0 = stops.get(0).color();
        binder.setVec4("u_color", c0.r(), c0.g(), c0.b(), c0.a());
        binder.setVec2("u_gradP0", startX, startY);
        binder.setVec2("u_gradP1", endX, endY);
        binder.setInt("u_gradNumStops", stops.size());

        for (int i = 0; i < stops.size(); i++)
        {
            GradientStop stop = stops.get(i);
            Color c = stop.color();
            binder.setVec4("u_gradColors[" + i + "]", c.r(), c.g(), c.b(), c.a());
            binder.setFloat("u_gradPositions[" + i + "]", stop.position());
        }
    }

    public List<GradientStop> getStops()
    {
        return stops;
    }
}
