attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform mat4 uTextureMatrix;
varying vec2 vTextureCoord;
void main()
{
    vTextureCoord = (uTextureMatrix * aTextureCoord).xy;
    gl_Position = aPosition;
}