package eleeter.elcanvas.internal;

import eleeter.elcanvas.api.Fill;
import eleeter.elcanvas.api.SdfCanvas;
import eleeter.elcanvas.api.SdfEffect;
import eleeter.elcanvas.api.SdfShape;
import eleeter.elcanvas.internal.graphics.GlUniformBinder;
import eleeter.elcanvas.internal.graphics.QuadMesh;
import eleeter.elcanvas.internal.graphics.SdfProgramRegistry;
import eleeter.elcanvas.internal.graphics.ShaderProgram;
import eleeter.elcanvas.shape.CapsuleShape;
import eleeter.elcanvas.shape.CircleShape;
import eleeter.elcanvas.shape.RectangleShape;
import eleeter.elcanvas.shape.RingShape;
import eleeter.elcanvas.shape.RoundedRectangleShape;
import eleeter.elcanvas.shape.TriangleShape;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL14.GL_BLEND_DST_ALPHA;
import static org.lwjgl.opengl.GL14.GL_BLEND_DST_RGB;
import static org.lwjgl.opengl.GL14.GL_BLEND_SRC_ALPHA;
import static org.lwjgl.opengl.GL14.GL_BLEND_SRC_RGB;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_VERTEX_ARRAY_BINDING;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


public class SdfCanvasImpl implements SdfCanvas
{

    private final QuadMesh quadMesh = new QuadMesh();
    private final SdfProgramRegistry programRegistry = new SdfProgramRegistry();
    private final GlUniformBinder uniformBinder = new GlUniformBinder();
    private final Matrix4f projectionMatrix = new Matrix4f();

    private final RectangleShape rectShape = RectangleShape.INSTANCE;
    private final CircleShape circleShape = CircleShape.INSTANCE;
    private final CapsuleShape capsuleShape = CapsuleShape.INSTANCE;
    private final TriangleShape triangleShape = TriangleShape.INSTANCE;

    private boolean prevBlendEnabled;
    private int prevBlendSrcRgb;
    private int prevBlendDstRgb;
    private int prevBlendSrcAlpha;
    private int prevBlendDstAlpha;
    private int prevProgram;
    private int prevVao;

    private boolean initialized = false;

    @Override
    public void setGlslVersionDirective(String directive)
    {
        programRegistry.setGlslVersionDirective(directive);
    }

    @Override
    public void init()
    {
        if (initialized)
        {
            return;
        }

        quadMesh.init();
        programRegistry.init();

        programRegistry.getOrCreateProgram(rectShape, Collections.emptyList());
        programRegistry.getOrCreateProgram(circleShape, Collections.emptyList());
        programRegistry.getOrCreateProgram(new RoundedRectangleShape(), Collections.emptyList());
        programRegistry.getOrCreateProgram(capsuleShape, Collections.emptyList());
        programRegistry.getOrCreateProgram(new RingShape(), Collections.emptyList());
        programRegistry.getOrCreateProgram(triangleShape, Collections.emptyList());

        initialized = true;
    }

    @Override
    public void begin(float viewportWidth, float viewportHeight)
    {
        if (!initialized)
        {
            init();
        }

        prevBlendEnabled = glIsEnabled(GL_BLEND);
        prevBlendSrcRgb = glGetInteger(GL_BLEND_SRC_RGB);
        prevBlendDstRgb = glGetInteger(GL_BLEND_DST_RGB);
        prevBlendSrcAlpha = glGetInteger(GL_BLEND_SRC_ALPHA);
        prevBlendDstAlpha = glGetInteger(GL_BLEND_DST_ALPHA);
        prevProgram = glGetInteger(GL_CURRENT_PROGRAM);
        prevVao = glGetInteger(GL_VERTEX_ARRAY_BINDING);

        projectionMatrix.setOrtho2D(0.0f, viewportWidth, viewportHeight, 0.0f);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void end()
    {
        if (!prevBlendEnabled)
        {
            glDisable(GL_BLEND);
        }
        glBlendFuncSeparate(prevBlendSrcRgb, prevBlendDstRgb, prevBlendSrcAlpha, prevBlendDstAlpha);
        glUseProgram(prevProgram);
        glBindVertexArray(prevVao);
    }

    @Override
    public void drawRect(float x, float y, float width, float height, Fill fill, List<SdfEffect> effects)
    {
        drawShape(rectShape, x, y, width, height, fill, effects);
    }

    @Override
    public void drawCircle(float centerX, float centerY, float radius, Fill fill, List<SdfEffect> effects)
    {
        float diameter = radius * 2.0f;
        drawShape(circleShape, centerX - radius, centerY - radius, diameter, diameter, fill, effects);
    }

    @Override
    public void drawRoundedRect(float x, float y, float width, float height, float tl, float tr, float br, float bl, Fill fill, List<SdfEffect> effects)
    {
        drawShape(new RoundedRectangleShape(tl, tr, br, bl), x, y, width, height, fill, effects);
    }

    @Override
    public void drawCapsule(float x, float y, float width, float height, Fill fill, List<SdfEffect> effects)
    {
        drawShape(capsuleShape, x, y, width, height, fill, effects);
    }

    @Override
    public void drawRing(float centerX, float centerY, float radius, float thickness, Fill fill, List<SdfEffect> effects)
    {
        float diameter = radius * 2.0f;
        drawShape(new RingShape(thickness), centerX - radius, centerY - radius, diameter, diameter, fill, effects);
    }

    @Override
    public void drawTriangle(float x, float y, float width, float height, Fill fill, List<SdfEffect> effects)
    {
        drawShape(triangleShape, x, y, width, height, fill, effects);
    }

    @Override
    public void drawRotatedShape(SdfShape shape, float x, float y, float width, float height,
                                 float angleRadians, float pivotX, float pivotY, Fill fill, List<SdfEffect> effects)
    {
        Objects.requireNonNull(shape, "SdfShape cannot be null");
        Objects.requireNonNull(fill, "Fill cannot be null");

        if (!initialized)
        {
            init();
        }

        float padLeft = 0.0f;
        float padTop = 0.0f;
        float padRight = 0.0f;
        float padBottom = 0.0f;

        if (effects != null && !effects.isEmpty())
        {
            for (SdfEffect effect : effects)
            {
                padLeft = Math.max(padLeft, effect.getPaddingLeft(angleRadians));
                padTop = Math.max(padTop, effect.getPaddingTop(angleRadians));
                padRight = Math.max(padRight, effect.getPaddingRight(angleRadians));
                padBottom = Math.max(padBottom, effect.getPaddingBottom(angleRadians));
            }
        }

        ShaderProgram shadertypeshit = programRegistry.getOrCreateProgram(shape, effects);
        shadertypeshit.bind();
        uniformBinder.setActiveProgram(shadertypeshit);

        uniformBinder.setMat4("u_projection", projectionMatrix);
        uniformBinder.setVec2("u_position", x, y);
        uniformBinder.setVec2("u_size", width, height);
        uniformBinder.setVec2("u_halfSize", width * 0.5f, height * 0.5f);
        uniformBinder.setVec4("u_padding", padLeft, padTop, padRight, padBottom);
        uniformBinder.setFloat("u_rotation", angleRadians);
        uniformBinder.setVec2("u_pivot", pivotX, pivotY);

        fill.bindFill(uniformBinder);

        shape.bindUniforms(uniformBinder);

        if (effects != null && !effects.isEmpty())
        {
            for (int i = 0; i < effects.size(); i++)
            {
                String prefix = "fx" + i + "_";
                effects.get(i).bindUniforms(uniformBinder, prefix);
            }
        }

        quadMesh.draw();
    }

    @Override
    public void dispose()
    {
        if (!initialized)
        {
            return;
        }
        quadMesh.dispose();
        programRegistry.dispose();
        initialized = false;
    }
}
