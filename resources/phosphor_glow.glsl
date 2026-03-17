uniform sampler2D scene;
uniform vec2 resolution;
uniform float time;

void main() {
    vec2 uv = gl_TexCoord[0].xy;
    vec3 color = texture2D(scene, uv).rgb;

    float scanY = uv.y * resolution.y;
    float scan = sin(scanY * 3.14159) * 0.5 + 0.5;
    scan = pow(scan, 0.4);
    color *= scan;

    color *= 0.95 + 0.05 * sin(time * 60.0);

    gl_FragColor = vec4(color, 1.0);
}