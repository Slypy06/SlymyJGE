uniform sampler2D scene;
uniform vec2 resolution;
uniform vec2 focusPoint;
uniform vec3 fogColor;
uniform float fogRadius;
uniform float fogSoftness;

void main() {
    vec2 uv = gl_TexCoord[0].xy;
    vec3 color = texture2D(scene, uv).rgb;

    float dist = distance(uv, focusPoint/resolution);
    float fog = smoothstep(fogRadius, fogRadius + fogSoftness, dist);

    color = mix(color, fogColor, fog);
    gl_FragColor = vec4(color, 1.0);
}