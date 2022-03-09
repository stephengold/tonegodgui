#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;

#ifdef USE_TEXTURE
	varying vec4 texCoord;
#endif

attribute vec3 inPosition;
attribute vec4 inColor;
attribute vec4 inTexCoord;

varying vec4 particleColor;

void main(){
    vec4 pos = vec4(inPosition, 1.0);

    gl_Position = g_WorldViewProjectionMatrix * pos;
	
	particleColor = inColor;
	
    #ifdef USE_TEXTURE
        texCoord = inTexCoord;
    #endif
}