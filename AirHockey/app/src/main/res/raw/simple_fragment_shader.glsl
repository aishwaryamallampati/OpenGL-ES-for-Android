// Primitives are brokend down to fragments which are analogous to pixels and each fragment has one color
precision mediump float;
uniform vec4 u_Color; // uniform keeps the same color for all the vertices until it is changed

void main()
{
    gl_FragColor = u_Color;
}