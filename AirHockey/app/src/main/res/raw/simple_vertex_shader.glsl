// a_Position - current vertex position
// vec4 - (x,y,z,w) OpenGLs default behavior is to set the first three coordinates of a vector to 0 and the last to 1
// attribute keyword is used to feed vertex attributes such as position, color into the shader
// gl_Position is the final position for the current vertex which will be used by OpenGL for primitive assembly
attribute vec4 a_Position;

void main()
{
    gl_Position = a_Position;
    gl_PointSize = 10.0;
}