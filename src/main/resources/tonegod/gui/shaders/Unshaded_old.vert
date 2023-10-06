#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldViewMatrix;
attribute vec3 inPosition;
varying vec4 pos;

attribute vec2 inTexCoord;
attribute vec4 inColor;
uniform bool m_UseEffectTexCoords;
uniform vec2 m_OffsetTexCoord;
uniform vec2 m_OffsetAlphaTexCoord;
varying vec2 texCoord1;
varying vec2 texCoord2;
varying vec2 alphaTexCoord;
varying vec4 vertColor;

void main(){
    pos = g_WorldViewMatrix * vec4(inPosition, 1.0);
	texCoord1 = inTexCoord;
	texCoord2 = inTexCoord+m_OffsetTexCoord;
	alphaTexCoord = inTexCoord+m_OffsetAlphaTexCoord;
	
    #ifdef HAS_VERTEXCOLOR
        vertColor = inColor;
    #endif

    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}