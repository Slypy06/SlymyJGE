uniform sampler2D scene;
uniform vec2 resolution;

void main() {
    vec2 uv = gl_TexCoord[0].xy;
    vec2 texel = 1.0 / resolution;
    int radius = 3;

    vec3 meanQ[4];
    float varQ[4];
    for(int q = 0; q < 4; q++) { meanQ[q] = vec3(0.0); varQ[q] = 0.0; }

    vec2 offsets[4] = vec2[]( vec2(-1,-1), vec2(0,-1), vec2(-1,0), vec2(0,0) );

    for(int q = 0; q < 4; q++) {
        float count = 0.0;
        vec3 sum = vec3(0.0);
        vec3 sum2 = vec3(0.0);
        for(int dx = 0; dx <= radius; dx++) {
            for(int dy = 0; dy <= radius; dy++) {
                vec2 offset = (offsets[q] * float(radius) + vec2(dx, dy)) * texel;
                vec3 s = texture2D(scene, uv + offset).rgb;
                sum += s;
                sum2 += s * s;
                count++;
            }
        }
        meanQ[q] = sum / count;
        vec3 v = sum2 / count - meanQ[q] * meanQ[q];
        varQ[q] = dot(v, vec3(1.0));
    }

    vec3 result = meanQ[0];
    float minVar = varQ[0];
    for(int q = 1; q < 4; q++) {
        if(varQ[q] < minVar) { minVar = varQ[q]; result = meanQ[q]; }
    }

    gl_FragColor = vec4(result, 1.0);
}