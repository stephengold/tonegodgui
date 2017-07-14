/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.style;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import java.util.HashMap;
import java.util.Map;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public class Style {
	Map<String, Object> styleTags = new HashMap<>();
	
	public Style() {  }
	
	public void putTag(String key, Object value) {
		styleTags.put(key, value);
	}
	
	public String getString(String key) {
		return (String)styleTags.get(key);
	}
	
	public float getFloat(String key) {
		return ((Number)styleTags.get(key)).floatValue();
	}
	
	public int getInt(String key) {
		return ((Number)styleTags.get(key)).intValue();
	}
	
	public boolean getBoolean(String key) {
		return ((Boolean)styleTags.get(key)).booleanValue();
	}
	
	public Vector2f getVector2f(String key) {
		return ((Vector2f)styleTags.get(key)).clone();
	}
	
	public Vector3f getVector3f(String key) {
		return ((Vector3f)styleTags.get(key)).clone();
	}
	
	public Vector4f getVector4f(String key) {
		return ((Vector4f)styleTags.get(key)).clone();
	}
	
	public ColorRGBA getColorRGBA(String key) {
		return ((ColorRGBA)styleTags.get(key)).clone();
	}
	
	public Effect getEffect(String key) {
		return (Effect)styleTags.get(key);
	}
	
	public Object getObject(String key) {
		return styleTags.get(key);
	}
}
