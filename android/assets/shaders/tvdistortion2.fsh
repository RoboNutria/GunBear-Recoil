#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

#define PHI ((1.+sqrt(5.))/2.)

float hash(float x)
{
	return mod(mod(x, PHI)*x, 1.);
}


void main( void ) {

	vec2 pixel_coordinate = gl_FragCoord.xy;

	gl_FragColor = vec4(hash(gl_FragCoord.x+hash(gl_FragCoord.y)*time));
	gl_FragColor = gl_FragColor*0.5f;
}//sphinx