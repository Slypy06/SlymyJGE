uniform sampler2D scene;
uniform vec2 resolution;
uniform float pixelSize; // e.g. 4.0
uniform float levels;

void main() {
    vec2 uv = floor(gl_TexCoord[0].xy * resolution / pixelSize) * pixelSize / resolution;
    vec3 color = texture2D(scene, uv).rgb;
    // Quantize to N levels per channel
    color = floor(color * levels) / levels;
    gl_FragColor = vec4(color, 1.0);
}