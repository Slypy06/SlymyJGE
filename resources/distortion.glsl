uniform sampler2D scene;
uniform vec2 resolution;
uniform float time;

void main() {
    vec2 uv = gl_TexCoord[0].xy;
    float strength = 0.003;
    float speed = 2.0;
    float scale = 8.0;

    float offsetX = sin(uv.y * scale + time * speed) * strength;
    float offsetY = cos(uv.x * scale + time * speed * 0.7) * strength;

    vec2 distorted = uv + vec2(offsetX, offsetY);
    gl_FragColor = vec4(texture2D(scene, distorted).rgb, 1.0);
}