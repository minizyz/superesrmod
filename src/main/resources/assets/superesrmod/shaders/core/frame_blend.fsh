#version 150

// 帧混合着色器：current * (1 - t) + previous * t

uniform sampler2D CurrentFrame;
uniform sampler2D PreviousFrame;
uniform float BlendFactor;

out vec4 fragColor;

void main() {
    vec2 uv = gl_FragCoord.xy / vec2(textureSize(CurrentFrame, 0));
    vec3 curr = texture(CurrentFrame, uv).rgb;
    vec3 prev = texture(PreviousFrame, uv).rgb;
    fragColor = vec4(mix(curr, prev, BlendFactor), 1.0);
}
