uniform sampler2D scene;
uniform vec2 resolution;

float bayer[16] = float[](
     0.0/16.0,  8.0/16.0,  2.0/16.0, 10.0/16.0,
    12.0/16.0,  4.0/16.0, 14.0/16.0,  6.0/16.0,
     3.0/16.0, 11.0/16.0,  1.0/16.0,  9.0/16.0,
    15.0/16.0,  7.0/16.0, 13.0/16.0,  5.0/16.0
);

void main() {
    vec2 uv = gl_TexCoord[0].xy;
    vec3 color = texture2D(scene, uv).rgb;
    vec2 pixelPos = uv * resolution;
    int bx = int(mod(pixelPos.x, 4.0));
    int by = int(mod(pixelPos.y, 4.0));
    float threshold = bayer[by * 4 + bx];
    color = step(threshold, color);
    gl_FragColor = vec4(color, 1.0);
}