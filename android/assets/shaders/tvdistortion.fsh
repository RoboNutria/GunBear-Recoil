#ifdef GL_ES
precision highp float;
#endif

uniform vec2 resolution;
uniform float time;

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

// Hash without Sine
// https://www.shadertoy.com/view/4djSRW
#define MOD3 vec3(443.8975,397.2973, 491.1871)
//  1 out, 2 in...
float hash12(vec2 p)
{
    vec3 p3 = fract(vec3(p.xyx) * MOD3);
    p3 += dot(p3, p3.yzx + 19.19);
    return fract((p3.x + p3.y) * p3.z);
}


void main( void ) 
{
	vec2 uv = (gl_FragCoord.xy + time);
	uv = vec2(atan(uv.x, uv.y), length(uv));
	vec4 ape = vec4(hash12(uv))*0.4f;
	gl_FragColor = vec4(v_texCoord0, 0.0, 1.0)*ape;
}
