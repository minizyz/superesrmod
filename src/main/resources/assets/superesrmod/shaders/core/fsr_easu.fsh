#version 150

// FSR 1.0 EASU simplified port (AMD FidelityFX SDK 1.0, MIT)

uniform sampler2D InputTexture;
uniform ivec2 InputSize;
uniform ivec2 OutputSize;
uniform float Sharpness;

out vec4 fragColor;

vec3 fsrEasuSample(vec2 uv) {
    vec2 texel = vec2(1.0) / vec2(InputSize);
    vec3 c  = texture(InputTexture, uv).rgb;
    vec3 l  = texture(InputTexture, uv - vec2(texel.x, 0.0)).rgb;
    vec3 r  = texture(InputTexture, uv + vec2(texel.x, 0.0)).rgb;
    vec3 u  = texture(InputTexture, uv - vec2(0.0, texel.y)).rgb;
    vec3 d  = texture(InputTexture, uv + vec2(0.0, texel.y)).rgb;
    vec3 ul = texture(InputTexture, uv - texel).rgb;
    vec3 ur = texture(InputTexture, uv + vec2(texel.x, -texel.y)).rgb;
    vec3 dl = texture(InputTexture, uv + vec2(-texel.x, texel.y)).rgb;
    vec3 dr = texture(InputTexture, uv + texel).rgb;

    float hGrad = length(l - r);
    float vGrad = length(u - d);
    float diag1 = length(ul - dr);
    float diag2 = length(ur - dl);

    if (hGrad > vGrad && hGrad > diag1 && hGrad > diag2) {
        return c * 0.4 + l * 0.3 + r * 0.3;
    } else if (vGrad > hGrad && vGrad > diag1 && vGrad > diag2) {
        return c * 0.4 + u * 0.3 + d * 0.3;
    } else if (diag1 > hGrad && diag1 > vGrad && diag1 > diag2) {
        return c * 0.5 + ul * 0.25 + dr * 0.25;
    } else if (diag2 > hGrad && diag2 > vGrad) {
        return c * 0.5 + ur * 0.25 + dl * 0.25;
    } else {
        return c * 0.5 + (l + r + u + d) * 0.125;
    }
}

void main() {
    vec2 outTexel = 1.0 / vec2(OutputSize);
    vec2 uv = gl_FragCoord.xy * outTexel;
    vec2 inputUv = uv * vec2(OutputSize) / vec2(InputSize);
    vec3 color = fsrEasuSample(inputUv);
    color = pow(color, vec3(1.0 / 2.2));
    fragColor = vec4(color, 1.0);
}
