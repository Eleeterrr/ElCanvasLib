package eleeter.elcanvas.api;

import eleeter.elcanvas.internal.SdfCanvasImpl;
import eleeter.elcanvas.shape.CapsuleShape;
import eleeter.elcanvas.shape.CircleShape;
import eleeter.elcanvas.shape.RectangleShape;
import eleeter.elcanvas.shape.RingShape;
import eleeter.elcanvas.shape.RoundedRectangleShape;
import eleeter.elcanvas.shape.TriangleShape;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public interface SdfCanvas extends AutoCloseable
{

    /**
     * Creates a new instance of {@link SdfCanvas}
     */
    static SdfCanvas create()
    {
        return new SdfCanvasImpl();
    }

    /**
     * Configures the GLSL version directive for this canvas
     */
    void setGlslVersionDirective(String directive);

    /**
     * Initializes the canvas
     */
    void init();

    /**
     * Prepares the canvas for rendering
     */
    void begin(float viewportWidth, float viewportHeight);

    /**
     * restores previously bound GL state
     */
    void end();

    default void drawRect(float x, float y, float width, float height, Fill fill)
    {
        drawRect(x, y, width, height, fill, Collections.emptyList());
    }

    void drawRect(float x, float y, float width, float height, Fill fill, List<SdfEffect> effects);

    default void drawRect(float x, float y, float width, float height, Fill fill, SdfEffect... effects)
    {
        drawRect(x, y, width, height, fill, effects != null ? Arrays.asList(effects) : Collections.emptyList());
    }


    default void drawCircle(float centerX, float centerY, float radius, Fill fill)
    {
        drawCircle(centerX, centerY, radius, fill, Collections.emptyList());
    }

    void drawCircle(float centerX, float centerY, float radius, Fill fill, List<SdfEffect> effects);

    default void drawCircle(float centerX, float centerY, float radius, Fill fill, SdfEffect... effects)
    {
        drawCircle(centerX, centerY, radius, fill, effects != null ? Arrays.asList(effects) : Collections.emptyList());
    }


    default void drawRoundedRect(float x, float y, float width, float height, float radius, Fill fill)
    {
        drawRoundedRect(x, y, width, height, radius, radius, radius, radius, fill, Collections.emptyList());
    }

    default void drawRoundedRect(float x, float y, float width, float height, float radius, Fill fill, List<SdfEffect> effects)
    {
        drawRoundedRect(x, y, width, height, radius, radius, radius, radius, fill, effects);
    }

    default void drawRoundedRect(float x, float y, float width, float height, float radius, Fill fill, SdfEffect... effects)
    {
        drawRoundedRect(x, y, width, height, radius, radius, radius, radius, fill, effects != null ? Arrays.asList(effects) : Collections.emptyList());
    }

    void drawRoundedRect(float x, float y, float width, float height, float tl, float tr, float br, float bl, Fill fill, List<SdfEffect> effects);

    default void drawRoundedRect(float x, float y, float width, float height, float tl, float tr, float br, float bl, Fill fill, SdfEffect... effects)
    {
        drawRoundedRect(x, y, width, height, tl, tr, br, bl, fill, effects != null ? Arrays.asList(effects) : Collections.emptyList());
    }

    default void drawCapsule(float x, float y, float width, float height, Fill fill)
    {
        drawCapsule(x, y, width, height, fill, Collections.emptyList());
    }

    void drawCapsule(float x, float y, float width, float height, Fill fill, List<SdfEffect> effects);

    default void drawCapsule(float x, float y, float width, float height, Fill fill, SdfEffect... effects)
    {
        drawCapsule(x, y, width, height, fill, effects != null ? Arrays.asList(effects) : Collections.emptyList());
    }


    default void drawRing(float centerX, float centerY, float radius, float thickness, Fill fill)
    {
        drawRing(centerX, centerY, radius, thickness, fill, Collections.emptyList());
    }

    void drawRing(float centerX, float centerY, float radius, float thickness, Fill fill, List<SdfEffect> effects);

    default void drawRing(float centerX, float centerY, float radius, float thickness, Fill fill, SdfEffect... effects)
    {
        drawRing(centerX, centerY, radius, thickness, fill, effects != null ? Arrays.asList(effects) : Collections.emptyList());
    }


    default void drawTriangle(float x, float y, float width, float height, Fill fill)
    {
        drawTriangle(x, y, width, height, fill, Collections.emptyList());
    }

    void drawTriangle(float x, float y, float width, float height, Fill fill, List<SdfEffect> effects);

    default void drawTriangle(float x, float y, float width, float height, Fill fill, SdfEffect... effects)
    {
        drawTriangle(x, y, width, height, fill, effects != null ? Arrays.asList(effects) : Collections.emptyList());
    }


    default void drawShape(SdfShape shape, float x, float y, float width, float height, Fill fill)
    {
        drawShape(shape, x, y, width, height, fill, Collections.emptyList());
    }

    default void drawShape(SdfShape shape, float x, float y, float width, float height, Fill fill, SdfEffect... effects)
    {
        drawShape(shape, x, y, width, height, fill, effects != null ? Arrays.asList(effects) : Collections.emptyList());
    }

    default void drawShape(SdfShape shape, float x, float y, float width, float height, Fill fill, List<SdfEffect> effects)
    {
        drawRotatedShape(shape, x, y, width, height, 0.0f, 0.5f, 0.5f, fill, effects);
    }


    void drawRotatedShape(SdfShape shape, float x, float y, float width, float height,
                          float angleRadians, float pivotX, float pivotY, Fill fill, List<SdfEffect> effects);

    default SdfDrawBuilder draw(SdfShape shape)
    {
        return new SdfDrawBuilder(this, shape);
    }

    default SdfDrawBuilder drawRect()
    {
        return draw(RectangleShape.INSTANCE);
    }

    default SdfDrawBuilder drawCircle()
    {
        return draw(CircleShape.INSTANCE);
    }

    default SdfDrawBuilder drawRoundedRect(float radius)
    {
        return draw(new RoundedRectangleShape(radius));
    }

    default SdfDrawBuilder drawRoundedRect(float tl, float tr, float br, float bl)
    {
        return draw(new RoundedRectangleShape(tl, tr, br, bl));
    }

    default SdfDrawBuilder drawCapsule()
    {
        return draw(CapsuleShape.INSTANCE);
    }

    default SdfDrawBuilder drawRing(float thickness)
    {
        return draw(new RingShape(thickness));
    }

    default SdfDrawBuilder drawTriangle()
    {
        return draw(TriangleShape.INSTANCE);
    }

    void dispose();

    @Override
    default void close()
    {
        dispose();
    }
}
