#version 150
#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform vec2 ScreenSize;

in vec4 texProj0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    // Fix Y flip
    vec4 texProj = texProj0;
    texProj.y = -texProj.y;

    // Process to true size
    vec2 texCoord = texProj.xy / texProj.w;
    // Scaling
    vec2 textureSize = vec2(textureSize(Sampler0, 0));
    float ratio = max(ScreenSize.x / textureSize.x, ScreenSize.y / textureSize.y);
    texCoord *= (ScreenSize / textureSize) / ratio;
    // Put back to vec4
    vec4 stretchedTexProj = vec4(texCoord * texProj.w, texProj.zw);

    // Merge Result
    fragColor = textureProj(Sampler0, stretchedTexProj) * vec4(vertexColor.rgb, 0.5);
}