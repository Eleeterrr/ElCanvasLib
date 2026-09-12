// __GLSL_VERSION__

#ifdef GL_ES
precision highp float;
#endif

layout(location = 0) in vec2 a_position;

uniform mat4 u_projection;
uniform vec2 u_position;
uniform vec2 u_size;
uniform vec4 u_padding;
uniform float u_rotation;
uniform vec2 u_pivot;

out vec2 v_localPos;
out vec2 v_halfSize;

void main()
{
    v_halfSize = u_size * 0.5;

    vec2 padMin = u_padding.xy;
    vec2 padMax = u_padding.zw;
    vec2 totalSize = u_size + padMin + padMax;

    vec2 localVertex = -padMin + a_position * totalSize;
    v_localPos = localVertex - v_halfSize;

    vec2 pivotLocal = u_pivot * u_size;
    vec2 offsetFromPivot = localVertex - pivotLocal;

    float cosA = cos(u_rotation);
    float sinA = sin(u_rotation);
    vec2 rotatedOffset = vec2(
        offsetFromPivot.x * cosA - offsetFromPivot.y * sinA,
        offsetFromPivot.x * sinA + offsetFromPivot.y * cosA
    );

    vec2 pivotScreen = u_position + pivotLocal;
    vec2 screenPos = pivotScreen + rotatedOffset;

    gl_Position = u_projection * vec4(screenPos, 0.0, 1.0);
}
