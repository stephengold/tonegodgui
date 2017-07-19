#ifdef USE_TEXTURE
uniform sampler2D m_Texture;
varying vec4 texCoord;
#endif

varying vec4 particleColor;

void main(){
	vec4 color = texture2D(m_Texture, texCoord.xy);
	color.a = color.r;
	float alpha = color.a;
	
	if (color.a <= 0.75)
		discard;
	
	color *= particleColor;
	color.a = alpha;
	gl_FragColor = color;
}