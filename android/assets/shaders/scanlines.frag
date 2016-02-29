#ifdef GL_ES
precision highp float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

void main( void )
{
	vec2 uv = (gl_FragCoord.xy);
	uv = vec2(sin(uv.y), length(uv));
	vec4 ape = vec4(uv.x, 0.4, 0.0, 0.0)*0.4;
	gl_FragColor = vec4(v_texCoord0, 0.0, 0.0)*ape;
}
