uniform sampler2D scene;
uniform vec2 resolution;

float luma(vec3 c) { return dot(c, vec3(0.299, 0.587, 0.114)); }

void main() {
    vec2 uv = gl_TexCoord[0].xy;
    vec2 texel = 1.0 / resolution;

    float tl = luma(texture2D(scene, uv + vec2(-1, 1) * texel).rgb);
    float t  = luma(texture2D(scene, uv + vec2( 0, 1) * texel).rgb);
    float tr = luma(texture2D(scene, uv + vec2( 1, 1) * texel).rgb);
    float l  = luma(texture2D(scene, uv + vec2(-1, 0) * texel).rgb);
    float r  = luma(texture2D(scene, uv + vec2( 1, 0) * texel).rgb);
    float bl = luma(texture2D(scene, uv + vec2(-1,-1) * texel).rgb);
    float b  = luma(texture2D(scene, uv + vec2( 0,-1) * texel).rgb);
    float br = luma(texture2D(scene, uv + vec2( 1,-1) * texel).rgb);

    float gx = -tl - 2.0*l - bl + tr + 2.0*r + br;
    float gy = -tl - 2.0*t - tr + bl + 2.0*b + br;
    float edge = sqrt(gx*gx + gy*gy);

    vec3 color = texture2D(scene, uv).rgb;
    gl_FragColor = vec4(color - edge, 1.0);
}