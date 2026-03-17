#version 120

uniform sampler2D textureLayer;

void main() {
    // --- Constants ---
    float curvatureStrength = 0.03;  // higher = more bent
    float crtLines         = 480;  // number of scanlines
    float scanlineStrength = 0.06;   // darkness of scanlines
    float borderFade       = 0.01;   // size of the border fade (0.0 to 0.5)

    // --- Curvature (applied first so everything uses curved UV) ---
    vec2 curved = gl_TexCoord[0].xy * 2.0 - 1.0;
    curved *= 1.0 + dot(curved.yx, curved.yx) * curvatureStrength;
    vec2 uv = curved * 0.5 + 0.5;

    // --- Out of bounds = black ---
    if (uv.x < 0.0 || uv.x > 1.0 || uv.y < 0.0 || uv.y > 1.0) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        return;
    }

    // --- Sample texture with curved UV ---
    vec4 color = texture2D(textureLayer, uv);

    // --- Scanlines ---
    float scanline = sin(uv.y * crtLines * 3.14159) * scanlineStrength;
    color.rgb -= scanline;

    // --- Border fade (smooth black vignette at edges) ---
    float fadeX = smoothstep(0.0, borderFade, uv.x) * smoothstep(1.0, 1.0 - borderFade, uv.x);
    float fadeY = smoothstep(0.0, borderFade, uv.y) * smoothstep(1.0, 1.0 - borderFade, uv.y);
    color.rgb *= fadeX * fadeY;

    gl_FragColor = vec4(color.rgb, 1.0);
}