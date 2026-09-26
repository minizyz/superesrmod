#version 150

// FSR 1.0 RCAS simplified port (AMD FidelityFX SDK 1.0, MIT)

uniform sampler2D InputTexture;
uniform ivec2 OutputSize;
uniform float Sharpness;

out vec4 fragColor;

void main() {
    vec2 texel = 1.0 / vec2(OutputSize);
    vec2 uv = gl_FragCoord.xy * texel;

    vec3 c = texture(InputTexture, uv).rgb;
    vec3 l = texture(InputTexture, uv - vec2(texel.x, 0.0)).rgb;
    vec3 r = texture(InputTexture, uv + vec2(texel.x, 0.0)).rgb;
    vec3 u = texture(InputTexture, uv - vec2(0.0, texel.y)).rgb;
    vec3 d = texture(InputTexture, uv + vec2(0.0, texel.y)).rgb;
    vec3 ul = texture(InputTexture, uv - texel).rgb;
    vec3 ur = texture(InputTexture, uv + vec2(texel.x, -texel.y)).rgb;
    vec3 dl = texture(InputTexture, uv + vec2(-texel.x, texel.y)).rgb;
    vec3 dr = texture(InputTexture, uv + texel).rgb;

    vec3 localMin = min(min(min(l, r), min(u, d)), min(min(ul, ur), min(dl, dr)));
    vec3 localMax = max(max(max(l, r), max(u, d)), max(max(ul, ur), max(dl, dr)));

    float sharp = Sharpness * 0.5;
    vec3 sharpened = c + (c - (l + r + u + d) * 0.25) * sharp;
    sharpened = clamp(sharpened, localMin, localMax);

    fragColor = vec4(sharpened, 1.0);
}
