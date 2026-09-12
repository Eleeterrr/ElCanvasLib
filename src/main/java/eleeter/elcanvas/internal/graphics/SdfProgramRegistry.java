package eleeter.elcanvas.internal.graphics;

import eleeter.elcanvas.api.SdfEffect;
import eleeter.elcanvas.api.SdfShape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class SdfProgramRegistry
{

    private String glslVersionDirective = "#version 330 core";

    private final Map<String, ShaderProgram> programCache = new HashMap<>();
    private String rawVertexSource;
    private String rawFragmentTemplate;
    private boolean initialized = false;

    public void setGlslVersionDirective(String directive)
    {
        this.glslVersionDirective = Objects.requireNonNull(directive, "GLSL version directive cannot be null");
    }

    public String getGlslVersionDirective()
    {
        return glslVersionDirective;
    }

    public void init()
    {
        if (initialized)
        {
            return;
        }
        rawVertexSource = loadResource("/shaders/quad.vert");
        rawFragmentTemplate = loadResource("/shaders/sdf_template.frag");
        initialized = true;
    }

    public ShaderProgram getOrCreateProgram(SdfShape shape, List<SdfEffect> effects)
    {
        if (!initialized)
        {
            init();
        }

        String programKey = buildCompositeKey(shape, effects);
        return programCache.computeIfAbsent(programKey, k -> compileProgram(shape, effects));
    }

    public static String buildCompositeKey(SdfShape shape, List<SdfEffect> effects)
    {
        Objects.requireNonNull(shape, "Shape cannot be null");
        if (effects == null || effects.isEmpty())
        {
            return shape.getShapeType();
        }

        StringBuilder sb = new StringBuilder(shape.getShapeType()).append("[");
        for (int i = 0; i < effects.size(); i++)
        {
            if (i > 0)
            {
                sb.append("+");
            }
            sb.append(effects.get(i).getEffectType());
        }
        sb.append("]");
        return sb.toString();
    }

    private ShaderProgram compileProgram(SdfShape shape, List<SdfEffect> effects)
    {
        String vertexSource = rawVertexSource.replace("// __GLSL_VERSION__", glslVersionDirective);

        StringBuilder shapeUniforms = new StringBuilder();
        if (shape.getUniformDeclarationsGlsl() != null)
        {
            shapeUniforms.append(shape.getUniformDeclarationsGlsl()).append("\n");
        }

        StringBuilder effectUniforms = new StringBuilder();
        StringBuilder effectFunctions = new StringBuilder();
        StringBuilder effectApply = new StringBuilder();

        if (effects != null)
        {
            for (int i = 0; i < effects.size(); i++)
            {
                SdfEffect effect = effects.get(i);
                String prefix = "fx" + i + "_";

                String uDecl = effect.getUniformDeclarationsGlsl(prefix);
                if (uDecl != null && !uDecl.isEmpty())
                {
                    effectUniforms.append(uDecl).append("\n");
                }

                String fDecl = effect.getFunctionsGlsl(prefix);
                if (fDecl != null && !fDecl.isEmpty())
                {
                    effectFunctions.append(fDecl).append("\n");
                }

                String aDecl = effect.getApplyGlsl(prefix);
                if (aDecl != null && !aDecl.isEmpty())
                {
                    effectApply.append(aDecl).append("\n");
                }
            }
        }

        String fragSource = rawFragmentTemplate
                .replace("// __GLSL_VERSION__", glslVersionDirective)
                .replace("// __SHAPE_UNIFORMS__", shapeUniforms.toString())
                .replace("// __EFFECT_UNIFORMS__", effectUniforms.toString())
                .replace("// __SHAPE_FUNCTION__", shape.getDistanceFunctionGlsl())
                .replace("// __EFFECT_FUNCTIONS__", effectFunctions.toString())
                .replace("// __EFFECT_APPLY__", effectApply.toString());

        return new ShaderProgram(vertexSource, fragSource);
    }

    private static String loadResource(String path)
    {
        try (InputStream is = SdfProgramRegistry.class.getResourceAsStream(path))
        {
            if (is == null)
            {
                throw new IllegalStateException("Shader resource not found on classpath: " + path);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
            {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        
}
catch (IOException e)
        {
            throw new RuntimeException("Failed to read shader resource: " + path, e);
        }
    }

    public void dispose()
    {
        for (ShaderProgram program : programCache.values())
        {
            program.dispose();
        }
        programCache.clear();
        initialized = false;
    }
}
