/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.core;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.framework.animation.TemporalAction;

/**
 *
 * @author t0neg0d
 */
public class QuadData implements Transformable {
	public List<TemporalAction> actions = new ArrayList<>();
	public AnimElement element;
	public QuadData parent;
	public String key;
	private TextureRegion region;
	public int userIndex;
	public int index;
	private Vector2f position = new Vector2f(0f,0f);
	private Vector2f initPosition = new Vector2f(0f,0f);
	private float z = 1;
	private Vector2f dimensions = new Vector2f();
	private Vector2f initDimensions = new Vector2f();
	private Vector2f scale = new Vector2f();
	private float rotation = 0f;
	private Vector2f tcOffset = new Vector2f();
	private ColorRGBA color = new ColorRGBA(1f,1f,1f,1f);
	private Vector2f origin = new Vector2f(0f,0f);
	private boolean visible = true;
	private Vector2f borders = new Vector2f(4f,4f);
	private Vector2f skew = new Vector2f(0f,0f);
	private boolean ignoreMouse = false;
	private boolean isMovable = false;
	
	public QuadData(AnimElement element, String quadKey, TextureRegion region, float x, float y, float width, float height, Vector2f origin) {
		this.element = element;
		this.key = quadKey;
		this.position.set(x,y);
		this.initPosition.set(x,y);
		this.dimensions.set(width,height);
		this.initDimensions.set(width,height);
		this.scale.set(1f,1f);
		this.origin.set(origin);
		this.region = region;
	}
	
	public void setTextureRegion(TextureRegion region) {
		this.region = region;
		element.mesh.buildTexCoords = true;
	}
	
	public TextureRegion getTextureRegion() { return this.region; }
	
	@Override
	public void addAction(TemporalAction action) {
		action.setTransformable(this);
		actions.add(action);
	}
	
	public void update(float tpf) {
		for (TemporalAction a : actions) {
			a.act(tpf);
			if (a.getTime() >= a.getDuration() && a.getAutoRestart())
				a.restart();
		}
		
		for (TemporalAction a : actions) {
			if (a.getTime() >= a.getDuration()) {
				actions.remove(a);
				break;
			}
		}
	}
	
	public void hide() {
		if (visible) {
			initDimensions.set(dimensions);
			dimensions.set(0f,0f);
			element.mesh.buildPosition = true;
			visible = false;
		}
	}
	
	public void show() {
		if (!visible) {
			dimensions.set(initDimensions);
			element.mesh.buildPosition = true;
			visible = true;
		}
	}

	public boolean getIsVisible() {
		return visible;
	}
	
	@Override
	public void setPositionX(float x) {
		this.position.x = x;
		element.mesh.buildPosition = true;
	}

	@Override
	public void setPositionY(float y) {
		this.position.y = y;
		element.mesh.buildPosition = true;
	}

	@Override
	public void setPositionZ(float z) {
		this.z = z;
		element.mesh.buildPosition = true;
	}

	@Override
	public void setPosition(float x, float y) {
		this.position.set(x,y);
		element.mesh.buildPosition = true;
	}

	@Override
	public void setPosition(Vector2f pos) {
		this.position.set(pos);
		element.mesh.buildPosition = true;
	}

	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
		element.mesh.buildPosition = true;
	}

	@Override
	public void setScaleX(float scaleX) {
		this.scale.x = scaleX;
		element.mesh.buildPosition = true;
	}

	@Override
	public void setScaleY(float scaleY) {
		this.scale.y = scaleY;
		element.mesh.buildPosition = true;
	}

	@Override
	public void setScale(float x, float y) {
		this.scale.set(x,y);
		element.mesh.buildPosition = true;
		
	}

	@Override
	public void setScale(Vector2f scale) {
		this.scale.set(scale);
		element.mesh.buildPosition = true;
	}

	@Override
	public void setOrigin(float x, float y) {
		this.origin.set(x,y);
		element.mesh.buildPosition = true;
	}

	@Override
	public void setOrigin(Vector2f origin) {
		this.origin.set(origin);
		element.mesh.buildPosition = true;
	}

	@Override
	public void setOriginX(float originX) {
		this.origin.x = originX;
		element.mesh.buildPosition = true;
	}

	@Override
	public void setOriginY(float originY) {
		this.origin.y = originY;
		element.mesh.buildPosition = true;
	}

	@Override
	public void setColor(ColorRGBA color) {
		this.color.set(color);
		element.mesh.buildColor = true;
	}

	@Override
	public void setColorR(float r) {
		this.color.r = r;
		element.mesh.buildColor = true;
	}

	@Override
	public void setColorG(float g) {
		this.color.g = g;
		element.mesh.buildColor = true;
	}

	@Override
	public void setColorB(float b) {
		this.color.b = b;
		element.mesh.buildColor = true;
	}

	@Override
	public void setColorA(float a) {
		this.color.a = a;
		element.mesh.buildColor = true;
	}

	@Override
	public void setTCOffsetX(float x) {
		this.tcOffset.x = x;
		element.mesh.buildTexCoords = true;
	}

	@Override
	public void setTCOffsetY(float y) {
		this.tcOffset.y = y;
		element.mesh.buildTexCoords = true;
	}
	@Override
	public void setIgnoreMouse(boolean ignoreMouse) {
		this.ignoreMouse = ignoreMouse;
	}
	@Override
	public void setIsMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}

	@Override
	public Vector2f getPosition() {
		return position;
	}

	@Override
	public float getPositionX() {
		return position.x;
	}

	@Override
	public float getPositionY() {
		return position.y;
	}

	@Override
	public float getPositionZ() {
		return z;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public Vector2f getScale() {
		return scale;
	}

	@Override
	public float getScaleX() {
		return scale.x;
	}

	@Override
	public float getScaleY() {
		return scale.y;
	}

	@Override
	public Vector2f getOrigin() {
		return this.origin;
	}

	@Override
	public float getOriginX() {
		return this.origin.x;
	}

	@Override
	public float getOriginY() {
		return this.origin.y;
	}

	@Override
	public ColorRGBA getColor() {
		return this.color;
	}

	@Override
	public float getColorR() {
		return this.color.r;
	}

	@Override
	public float getColorG() {
		return this.color.g;
	}

	@Override
	public float getColorB() {
		return this.color.b;
	}

	@Override
	public float getColorA() {
		return this.color.a;
	}

	@Override
	public Vector2f getDimensions() {
		return this.dimensions;
	}

	@Override
	public float getWidth() {
		return this.dimensions.x;
	}

	@Override
	public float getHeight() {
		return this.dimensions.y;
	}

	@Override
	public Vector2f getTCOffset() {
		return this.tcOffset;
	}

	@Override
	public float getTCOffsetX() {
		return this.tcOffset.x;
	}

	@Override
	public float getTCOffsetY() {
		return this.tcOffset.y;
	}
	@Override
	public void setDimensions(Vector2f dim) {
		this.dimensions.set(dim);
		element.mesh.buildPosition = true;
	}
	@Override
	public void setDimensions(float w, float h) {
		this.dimensions.set(w,h);
		element.mesh.buildPosition = true;
	}
	@Override
	public void setWidth(float w) {
		this.dimensions.setX(w);
		element.mesh.buildPosition = true;
	}
	@Override
	public void setHeight(float h) {
		this.dimensions.setY(h);
		element.mesh.buildPosition = true;
	}
	@Override
	public void setSkew(Vector2f skew) {
		this.skew.set(skew);
		element.mesh.buildPosition = true;
	}
	@Override
	public void setSkew(float x, float y) {
		this.skew.set(x,y);
		element.mesh.buildPosition = true;
	}
	@Override
	public void setSkewX(float x) {
		this.skew.setX(x);
		element.mesh.buildPosition = true;
	}
	@Override
	public void setSkewY(float y) {
		this.skew.setY(y);
		element.mesh.buildPosition = true;
	}
	@Override
	public Vector2f getSkew() {
		return this.skew;
	}
	@Override
	public float getSkewX() {
		return skew.x;
	}
	@Override
	public float getSkewY() {
		return skew.y;
	}
	@Override
	public boolean getIgnoreMouse() {
		return this.ignoreMouse;
	}
	@Override
	public boolean getIsMovable() {
		return this.isMovable;
	}
	
	@Override
	public boolean getContainsAction(TemporalAction action) {
		return actions.contains(action);
	}
	
	public void setBorders(Vector2f borders) {
		this.borders.set(borders);
	}
	
	public void setBorders(float x, float y) {
		this.borders.set(x,y);
	}
	
	public Vector2f getBorders() {
		return borders;
	}
	
	public Object dataStruct;
	
	public <T extends Object> void setDataStruct(T dataStruct) {
		this.dataStruct = dataStruct;
	}
	
        @SuppressWarnings("unchecked")
	public <T extends Object> T getDataStruct() {
		return (T)dataStruct;
	}
}
