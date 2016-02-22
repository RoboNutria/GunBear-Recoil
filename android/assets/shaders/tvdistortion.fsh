#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

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
	gl_FragColor = vec4(hash12(uv));
	gl_FragColor = gl_FragColor*0.4f;
}
