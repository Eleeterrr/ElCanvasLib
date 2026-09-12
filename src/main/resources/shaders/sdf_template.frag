// __GLSL_VERSION__

#ifdef GL_ES
precision highp float;
#endif

in vec2 v_localPos;
in vec2 v_halfSize;

out vec4 fragColor;

uniform vec2 u_size;
uniform vec2 u_halfSize;
uniform vec4 u_padding;
uniform float u_rotation;

uniform int u_fillType;
uniform vec4 u_color;
uniform vec2 u_gradP0;
uniform vec2 u_gradP1;
uniform int u_gradNumStops;
uniform vec4 u_gradColors[5];
uniform float u_gradPositions[5];

// __SHAPE_UNIFORMS__
// __EFFECT_UNIFORMS__
// __SHAPE_FUNCTION__
// __EFFECT_FUNCTIONS__

vec4 evaluateGradient(float t)
 {
    if (u_gradNumStops <= 1)
    {
        return u_gradColors[0];
    }

    if (t <= u_gradPositions[0])
     {
        return u_gradColors[0];
    }

    for (int i = 0; i < 4; i++)
    {
        if (i + 1 >= u_gradNumStops)
        {
            break;
        }

        float p0 = u_gradPositions[i];
        float p1 = u_gradPositions[i + 1];

        if (t <= p1)
        {
            float span = max(p1 - p0, 0.0001);
            float f = clamp((t - p0) / span, 0.0, 1.0);
            return mix(u_gradColors[i], u_gradColors[i + 1], f);
        }
    }
    return u_gradColors[u_gradNumStops - 1];
}

void main()
{
    float dist = evaluateDistance(v_localPos, v_halfSize);
    
    float edgy = fwidth(dist);
    float alphagen = 1.0 - smoothstep(-edge * 0.5, edge * 0.5, dist);
    
    vec2 safeSize = max(u_size, vec2(0.001));
    vec2 uv = (v_localPos + v_halfSize) / safeSize;

    vec4 baseFillColor = u_color;

    if (u_fillType == 1)
    {
        vec2 dir = u_gradP1 - u_gradP0;
        float lenSq = dot(dir, dir);
        float t = (lenSq > 0.0001) ? clamp(dot(uv - u_gradP0, dir) / lenSq, 0.0, 1.0) : 0.0;
        baseFillColor = evaluateGradient(t);

    } else if (u_fillType == 2)
    {
        float distFromCenter = distance(uv, u_gradP0);
        float t = clamp(distFromCenter / max(u_gradP1.x, 0.001), 0.0, 1.0);
        baseFillColor = evaluateGradient(t);
    }

    vec4 baseColor = vec4(baseFillColor.rgb, baseFillColor.a * alphagen);
    fragColor = baseColor;
    
// __EFFECT_APPLY__
    
    if (fragColor.a <= 0.0)
    {
        discard;
    }
}
