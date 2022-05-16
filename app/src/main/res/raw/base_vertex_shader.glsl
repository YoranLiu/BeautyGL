attribute vec4 vPosition; // float[4], size match to gl_position, one vertex(passed from kotlin/java)

attribute vec2 vCoord; // float[2], texture coordinate, one vertex(passed from kotlin/java)

varying vec2 aCoord;

uniform mat4 vMatrix;

void main() {
    gl_Position = vPosition;
    aCoord = (vMatrix * vec4(vCoord, 1.0, 1.0)).xy; // make camera right orientation
}
