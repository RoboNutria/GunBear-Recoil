// water turbulence effect by joltz0r 2013-07-04, improved 2013-07-07
#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
varying vec2 surfacePosition;

#define MAX_ITER 9

float hash( float n ) 
{ 
	return fract( (1.0 + cos(n)) * 415.92653);
} 
float noise2d( in vec2 x )
{ 
    float xhash = hash( x.x * 37.0 );
    float yhash = hash( x.y * 57.0 );
    return fract( xhash + yhash );
} 

void main( void )
{
	vec2 p = (( gl_FragCoord.xy / resolution.xy ) * 2.0 - 1.0) * 4.0;
	p*=normalize(resolution).xy;
	vec2 i = p;
	float c = 0.0;
	float inten = 1.0;
	float f = noise2d(p*p)/10.0;
	 p = (( gl_FragCoord.xy / resolution.xy ) * 2.0 - 1.0) * 10.0 * ( fract(time/3.1415) / f);
	for (int n = 0; n < MAX_ITER; n++) {
		float t = time * (1.0 - (1.0 / float(n+1)));
		i = p + vec2(cos(t - i.x) + sin(t + i.y), sin(t - i.y) + cos(t + i.x));
		c += 1.0/length(vec2(p.x / (sin(i.x+t)/inten),p.y / (cos(i.y+t)/inten)));
	}
	c /= float(MAX_ITER);
	
	gl_FragColor = vec4(vec3(pow(c,1.1))*vec3(1.0, 1.97, 1.0), 1.0);
}
