/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.core;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tonegod.gui.framework.animation.TemporalAction;

/**
 *
 * @author t0neg0d
 */
public abstract class AnimElement extends Node implements Transformable {
	public static enum ZOrderEffect {
		Self,
		Child,
		Both,
		None
	}
	public List<TemporalAction> actions = new ArrayList<>();
	protected Map<String, QuadData> quads = new LinkedHashMap<>();
	private List<QuadData> tempQuads = new LinkedList<>();
	protected Texture tex;
	protected Map<String, TextureRegion> uvs = new HashMap<>();
	protected AnimElementMesh mesh;
	protected Vector2f position = new Vector2f(0,0);
	protected float z = 1;
	protected Vector2f scale = new Vector2f(1,1);
	protected Vector2f origin = new Vector2f(0,0);
	protected Vector2f dimensions = new Vector2f(0,0);
	protected Vector2f skew = new Vector2f(0,0);
	protected ColorRGBA color = new ColorRGBA();
	protected float rotation;
	protected Spatial spatial;
	protected Material mat = null;
	protected AssetManager am;
	protected String elementKey;
	protected Object dataStruct;
	protected ZOrderEffect zOrderEffect = ZOrderEffect.Child;
	protected boolean ignoreMouse = false;
	protected boolean isMovable = false;
	protected AnimLayer parentLayer = null;
	protected float zOrder = -1f;
	private float zOrderStepMinor = 0.00001f;
	private Geometry geom;
	private Vector2f worldPosition = new Vector2f();
	private float worldRotation = 0;
	private Vector2f tempV = new Vector2f(),
			tempV2 = new Vector2f();
	private Vector4f clippingPosition = new Vector4f(-10000,-10000,10000,10000);
	
	public AnimElement(AssetManager am) {
		this.am = am;
		mesh = new AnimElementMesh(this);
	}
	
	public void initialize() {
		flagForUpdate();
		mesh.initialize();
		
		Vector4f clip = null;
		boolean useClip = false;
		boolean reset = false;
		if (mat != null) {
			if (useClip)
				clip = (Vector4f)mat.getParam("Clipping").getValue();
			else
				clip = new Vector4f(0,0,0,0);
			useClip = (Boolean)getMaterial().getParam("UseClipping").getValue();
			reset = true;
		}
		mat = new Material(am, "tonegod/gui/shaders/Unshaded.j3md");
		mat.setTexture("ColorMap", tex);
		mat.setBoolean("VertexColor", true);
		if (reset) {
			if (useClip)
				mat.setVector4("Clipping", clip);
			else
				mat.setVector4("Clipping", new Vector4f(0,0,0,0));
			mat.setBoolean("UseClipping", useClip);
		} else {
			mat.setVector4("Clipping", new Vector4f(0,0,0,0));
			mat.setBoolean("UseClipping", false);
		}
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		
		geom = new Geometry();
		geom.setMesh(mesh);
		
		attachChild(geom);
		setMaterial(mat);
		
		flagForUpdate();
		geom.updateModelBound();
	}
	
	public void setZOrderEffect(ZOrderEffect zOrderEffect) {
		this.zOrderEffect = zOrderEffect;
	}
	
	public ZOrderEffect getZOrderEffect() {
		return this.zOrderEffect;
	}
	
	public void setParentLayer(AnimLayer layer) {
		this.parentLayer = layer;
	}
	
	public AnimLayer getParentLayer() {
		return this.parentLayer;
	}
	
	public boolean getIsInitialized() {
		return this.mesh.init;
	}
	
	public void setTexture(Texture tex) {
		this.tex = tex;
		if (mat != null)
			mat.setTexture("ColorMap", tex);
	}
	
	public Texture getTexture() {
		return tex;
	}
	
	public void addTextureRegion(String regionKey, TextureRegion tr) {
		uvs.put(regionKey, tr);
	}
	
	public TextureRegion addTextureRegion(String regionKey, int x, int y, int w, int h) {
		TextureRegion tr = new TextureRegion(tex, x, y, w, h);
		tr.flip(false, true);
		uvs.put(regionKey, tr);
		return tr;
	}
	
	public TextureRegion getTextureRegion(String regionKey) {
		return uvs.get(regionKey);
	}
	
	public Material getMaterial() {
		return this.mat;
	}
	
	public void setElementMaterial(Material mat) {
		this.mat = mat;
	}
	
	public Map<String,TextureRegion> getTextureRegions() {
		return this.uvs;
	}
	
	public QuadData addQuad(String quadKey, String regionKey, Vector2f position, Vector2f origin) {
		if (zOrder == -1)
			zOrder = getPositionZ();
		Vector2f pos = new Vector2f(position).subtract(origin);
		
		QuadData qd = new QuadData(this, quadKey, uvs.get(regionKey), pos.x, pos.y, uvs.get(regionKey).getRegionWidth(), uvs.get(regionKey).getRegionHeight(), origin);
		quads.put(quadKey, qd);
		qd.setPositionZ(zOrder);
		zOrder += zOrderStepMinor;
		flagForUpdate();
		return qd;
	}
	
	public QuadData addQuad(String quadKey, String regionKey, Vector2f position, Vector2f origin, String parentKey) {
		if (zOrder == -1)
			zOrder = getPositionZ();
		Vector2f pos = new Vector2f(position).subtract(origin);
		
		QuadData qd = new QuadData(this, quadKey, uvs.get(regionKey), pos.x, pos.y, uvs.get(regionKey).getRegionWidth(), uvs.get(regionKey).getRegionHeight(), origin);
		qd.parent = quads.get(parentKey);
	//	qd.setPositionX(qd.getPositionX()-qd.parent.getPositionX());
	//	qd.setPositionY(qd.getPositionY()-qd.parent.getPositionY());
		qd.setPositionZ(zOrder);
		zOrder += zOrderStepMinor;
		quads.put(quadKey, qd);
		flagForUpdate();
		return qd;
	}
	
	public void flagForUpdate() {
		mesh.buildPosition = true;
		mesh.buildTexCoords = true;
		mesh.buildColor = true;
		mesh.buildIndices = true;
	}
	
	public void setQuadParent(String key, String parentKey) {
		QuadData qd = getQuads().get(key);
		qd.parent = quads.get(parentKey);
		qd.setPositionX(qd.getPositionX()-qd.parent.getPositionX());
		qd.setPositionY(qd.getPositionY()-qd.parent.getPositionY());
	}
	
	public void rotateQuad(String quadKey, int rotation) {
		quads.get(quadKey).setRotation(rotation);
	}
	
	public void moveQuad(String quadKey, float x, float y) {
		QuadData q = quads.get(quadKey);
		q.setPositionX(x);
		q.setPositionY(y);
	}
	
	public void moveQuad(String quadKey, float z) {
		QuadData q = quads.get(quadKey);
		q.setPositionZ(z);
	}
	
	public void scaleQuad(String quadKey, float scaleX, float scaleY) {
		QuadData q = quads.get(quadKey);
		q.setScaleX(scaleX);
		q.setScaleY(scaleY);
	}
	
	public void addQuadAction(String quadKey, TemporalAction action) {
		quads.get(quadKey).addAction(action);
	}
	
	public Map<String, QuadData> getQuads() {
		return this.quads;
	}
	
	public QuadData getQuad(String key) {
		return this.quads.get(key);
	}
	
	public QuadData getQuad(int index) {
		return this.quads.values().toArray(new QuadData[0])[index];
	}
	
	public void bringQuadToFront(QuadData quad) {
		quads.remove(quad.key);
		quads.put(quad.key, quad);
		resetZOrder();
	}
	
	public void sendQuadToBack(QuadData quad) {
		tempQuads.clear();
		for (QuadData qd : quads.values()) {
			if (qd != quad)
				tempQuads.add(qd);
		}
		quads.clear();
		quads.put(quad.key, quad);
		for (QuadData qd : tempQuads) {
				quads.put(qd.key,qd);
		}
		resetZOrder();
	}
	
	public void resetZOrder() {
		zOrder = getPositionZ();
		for (QuadData qd : quads.values()) {
			qd.setPositionZ(zOrder);
			zOrder -= zOrderStepMinor;
		}
		mesh.buildPosition = true;
		mesh.buildTexCoords = true;
		mesh.buildColor = true;
	}
	
	public void centerQuads() {
		float totalWidth = 0, totalHeight = 0;
		for (QuadData q : quads.values()) {
			if (q.getPositionX()+q.getTextureRegion().regionWidth > totalWidth)
				totalWidth = q.getPositionX()+q.getTextureRegion().regionWidth;
			if (q.getPositionY()+q.getTextureRegion().regionHeight > totalHeight)
				totalHeight = q.getPositionY()+q.getTextureRegion().regionHeight;
		}
		for (QuadData q : quads.values()) {
			if (q.parent == null) {
				q.setPositionX(q.getPositionX()-(totalWidth/2));
				q.setPositionY(q.getPositionY()-(totalHeight/2));
			}
		}
	}
	
	public Map<String, TextureRegion> getUVs() {
		return this.uvs;
	}
	
	public void deallocateBuffers() {
		mesh.deallocateBuffers();
	}
	
	public void update(float tpf) {
	//	mesh.update(tpf);
	//	if (mesh.updateCol)
	//		geom.updateModelBound();
		
		for (TemporalAction a : actions) {
			a.act(tpf);
			if (a.getTime() >= a.getDuration() && a.getAutoRestart()) {
				a.restart();
			}
		}
		for (TemporalAction a : actions) {
			if (a.getTime() >= a.getDuration()) {
				actions.remove(a);
				break;
			}
		}
		animElementUpdate(tpf);
		
		mesh.update(tpf);
		if (mesh.updateCol)
			geom.updateModelBound();
	}
	
	public abstract void animElementUpdate(float tpf);
	
	//<editor-fold desc="TRANSFORMABLE">
	@Override
	public void setPositionX(float x) {
		this.position.x = x;
		mesh.buildPosition = true;
	}
	@Override
	public void setPositionY(float y) {
		this.position.y = y;
		mesh.buildPosition = true;
	}
	@Override
	public void setPosition(float x, float y) {
		this.position.set(x,y);
		mesh.buildPosition = true;
	}
	@Override
	public void setPosition(Vector2f pos) {
		this.position.set(pos);
		mesh.buildPosition = true;
	}
	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
		mesh.buildPosition = true;
	}
	@Override
	public void setScaleX(float scaleX) {
		this.scale.x = scaleX;
		mesh.buildPosition = true;
	}
	@Override
	public void setScaleY(float scaleY) {
		this.scale.y = scaleY;
		mesh.buildPosition = true;
	}
	@Override
	public void setScale(float x, float y) {
		this.scale.set(x,y);
		mesh.buildPosition = true;
		
	}
	@Override
	public void setScale(Vector2f scale) {
		this.scale.set(scale);
		mesh.buildPosition = true;
	}
	@Override
	public void setOrigin(float x, float y) {
		this.origin.set(x,y);
		mesh.buildPosition = true;
	}
	@Override
	public void setOrigin(Vector2f origin) {
		this.origin.set(origin);
		mesh.buildPosition = true;
	}
	@Override
	public void setOriginX(float originX) {
		this.origin.setX(originX);
		mesh.buildPosition = true;
	}
	@Override
	public void setOriginY(float originY) {
		this.origin.setY(originY);
		mesh.buildPosition = true;
	}
	@Override
	public void setColor(ColorRGBA color) {
		this.color.set(color);
		mesh.buildColor = true;
	}
	@Override
	public void setColorR(float r) {
		this.color.r = r;
		mesh.buildColor = true;
	}
	@Override
	public void setColorG(float g) {
		this.color.g = g;
		mesh.buildColor = true;
	}
	@Override
	public void setColorB(float b) {
		this.color.b = b;
		mesh.buildColor = true;
	}
	@Override
	public void setColorA(float a) {
		this.color.a = a;
		mesh.buildColor = true;
	}
	@Override
	public void setTCOffsetX(float x) {
		
	}
	@Override
	public void setTCOffsetY(float y) {
		
	}
	@Override
	public void setDimensions(Vector2f dim) {
		this.dimensions.set(dim);
		mesh.buildPosition = true;
	}
	@Override
	public void setDimensions(float w, float h) {
		this.dimensions.set(w,h);
		mesh.buildPosition = true;
	}
	@Override
	public void setWidth(float w) {
		this.dimensions.setX(w);
		mesh.buildPosition = true;
	}
	@Override
	public void setHeight(float h) {
		this.dimensions.setY(h);
		mesh.buildPosition = true;
	}
	@Override
	public void setSkew(Vector2f skew) {
		this.skew.set(skew);
		mesh.buildPosition = true;
	}
	@Override
	public void setSkew(float x, float y) {
		this.skew.set(x,y);
		mesh.buildPosition = true;
	}
	@Override
	public void setSkewX(float x) {
		this.skew.setX(x);
		mesh.buildPosition = true;
	}
	@Override
	public void setSkewY(float y) {
		this.skew.setY(y);
		mesh.buildPosition = true;
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
	public float getPositionX() {
		return position.x;
	}
	@Override
	public float getPositionY() {
		return position.y;
	}
	@Override
	public float getRotation() {
		return rotation;
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
		return color;
	}
	@Override
	public float getColorR() {
		return color.r;
	}
	@Override
	public float getColorG() {
		return color.g;
	}
	@Override
	public float getColorB() {
		return color.b;
	}
	@Override
	public float getColorA() {
		return color.g;
	}
	@Override
	public float getWidth() {
		return dimensions.x;
	}
	@Override
	public float getHeight() {
		return dimensions.y;
	}
	@Override
	public float getTCOffsetX() { return 0; }
	@Override
	public float getTCOffsetY() { return 0; }
	@Override
	public void setPositionZ(float z) {
		this.z = z;
		zOrder = z;
		for (QuadData qd : quads.values()) {
			qd.setPositionZ(zOrder);
			zOrder -= zOrderStepMinor;
		}
		mesh.buildPosition = true;
	}
	
	@Override
	public float getPositionZ() { return z; }
	@Override
	public Vector2f getPosition() { return this.position; }
	@Override
	public Vector2f getScale() { return scale; }
	@Override
	public Vector2f getDimensions() {
		return this.dimensions;
	}
	@Override
	public Vector2f getTCOffset() {
		return null;
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
	public void addAction(TemporalAction action) {
		action.setTransformable(this);
		actions.add(action);
	}
	@Override
	public boolean getContainsAction(TemporalAction action) {
		return actions.contains(action);
	}
	//</editor-fold>
	
	public void setElementKey(String key) {
		this.elementKey = key;
	}
	
	public String getElementKey() {
		return elementKey;
	}
	
	public <T extends Object> void setDataStruct(T dataStruct) {
		this.dataStruct = dataStruct;
	}
	
	public <T extends Object> T getDataStruct() {
		return (T)dataStruct;
	}
	
	// Potential Additions
	private void setWorldTransforms(QuadData qd) {
		AnimElement a = this;
		QuadData p = qd.parent;
		worldPosition.set(0,0);
		worldPosition.subtractLocal(qd.getOrigin());
		worldPosition.multLocal(qd.getScale());
		worldPosition.set(mesh.rot(worldPosition, qd.getRotation()));
		worldPosition.addLocal(qd.getOrigin());
		worldPosition.addLocal(qd.getPosition());
		worldRotation = qd.getRotation();
		while (p != null) {
			worldPosition.subtractLocal(p.getOrigin());
			worldPosition.multLocal(p.getScale());
			worldPosition.set(mesh.rot(worldPosition, p.getRotation()));
			worldPosition.addLocal(p.getOrigin());
			worldPosition.addLocal(p.getPosition());
			worldRotation += p.getRotation();
			p = p.parent;
		}
		while (a != null) {
			worldPosition.subtractLocal(a.getOrigin());
			worldPosition.multLocal(a.getScale());
			worldPosition.set(mesh.rot(worldPosition, a.getRotation()));
			worldPosition.addLocal(a.getOrigin());
			worldPosition.addLocal(a.getPosition());
			worldRotation += a.getRotation();
			if (a.getParent() instanceof AnimElement)
				a = (AnimElement)a.getParent();
			else
				a = null;
		}
		if (worldPosition.x > clippingPosition.x) clippingPosition.x = worldPosition.x;
		if (worldPosition.y > clippingPosition.y) clippingPosition.y = worldPosition.y;
		if (worldPosition.x+qd.getWidth() < clippingPosition.z) clippingPosition.z = worldPosition.x+qd.getWidth();
		if (worldPosition.y+qd.getHeight() < clippingPosition.w) clippingPosition.w = worldPosition.y+qd.getHeight();
	}
	
	public Vector2f getQuadWorldPosition(QuadData qd) {
		setWorldTransforms(qd);
		return worldPosition;
	}
	
	public float getQuadWorldRotation(QuadData qd) {
		setWorldTransforms(qd);
		return worldRotation;
	}
	
	public void setClippingBounds(float x, float y, float z, float w) {
		clippingPosition.set(x,y,z,w);
		mat.setVector4("Clipping", clippingPosition);
		mat.setBoolean("UseClipping", true);
	}
	
	public void setClippingBounds(Vector4f clip) {
		clippingPosition.set(clip);
		mat.setVector4("Clipping", clippingPosition);
		mat.setBoolean("UseClipping", true);
	}
	
	public void setClippingBounds() {
		resetClippingPosition();
		for (QuadData qd : quads.values()) {
			setWorldTransforms(qd);
		}
	}
	public void resetClippingPosition() { clippingPosition.set(-10000,-10000,10000,10000); }
	public Vector4f getClippingPosition() { return clippingPosition; }
}
