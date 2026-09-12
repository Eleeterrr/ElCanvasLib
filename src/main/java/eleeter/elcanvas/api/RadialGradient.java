package eleeter.elcanvas.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class RadialGradient implements Fill
{

    private static final int MAX_STOPS = 5;

    private final float centerX;
    private final float centerY;
    private final float radius;
    private final List<GradientStop> stops;

    public RadialGradient(float centerX, float centerY, float radius, List<GradientStop> stops)
    {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = Math.max(0.0001f, radius);
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

    public RadialGradient(float centerX, float centerY, float radius, Color centerColor, Color edgeColor)
    {
        this(centerX, centerY, radius, List.of(
                new GradientStop(0.0f, centerColor),
                new GradientStop(1.0f, edgeColor)
        ));
    }

    public RadialGradient(float centerX, float centerY, float radius, GradientStop... stops)
    {
        this(centerX, centerY, radius, Arrays.asList(stops));
    }

    public static RadialGradient centered(Color... colors)
    {
        return new RadialGradient(0.5f, 0.5f, 0.5f, createEquallySpacedStops(colors));
    }

    public static RadialGradient centered(GradientStop... stops)
    {
        return new RadialGradient(0.5f, 0.5f, 0.5f, Arrays.asList(stops));
    }

    public static RadialGradient of(float cx, float cy, float radius, Color center, Color edge)
    {
        return new RadialGradient(cx, cy, radius, center, edge);
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
        return Fill.TYPE_RADIAL_GRADIENT;
    }

    @Override
    public Color getPrimaryColor()
    {
        return stops.get(0).color();
    }

    @Override
    public void bindFill(UniformBinder binder)
    {
        binder.setInt("u_fillType", Fill.TYPE_RADIAL_GRADIENT);
        Color c0 = stops.get(0).color();
        binder.setVec4("u_color", c0.r(), c0.g(), c0.b(), c0.a());
        binder.setVec2("u_gradP0", centerX, centerY);
        binder.setVec2("u_gradP1", radius, 0.0f);
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
