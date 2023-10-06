/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.core;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class AnimLayer extends Element implements Control {
	private Map<String, AnimElement> animElements = new LinkedHashMap<String, AnimElement>();
	private List<AnimElement> tempElements = new LinkedList<>();
	private float childZOrder = -1;
	private float zOrderStepMid = 0.001f;
	private boolean isPaused;
	
	public AnimLayer(ElementManager screen) {
		this(screen, UIDUtil.getUID());
	}
	
	public AnimLayer(ElementManager screen, String UID) {
		super(
			screen,
			UID,
			Vector2f.ZERO,
			new Vector2f(screen.getWidth(),screen.getHeight()),
			Vector4f.ZERO,
			null
		);
		setAsContainerOnly();
		setIgnoreMouse(true);
		setEffectZOrder(false);
	}
	
	public void addAnimElement(String UID, AnimElement el) {
		if (childZOrder == -1)
			childZOrder = getLocalTranslation().z;
		el.setElementKey(UID);
		el.setParentLayer(this);
		animElements.put(UID, el);
		el.setPositionZ(childZOrder);
		childZOrder -= zOrderStepMid;
		attachChild(el);
	}
	
	public AnimElement removeAnimElement(String UID) {
		AnimElement el = animElements.remove(UID);
		if (el != null)
			el.removeFromParent();
		return el;
	}
	
	public void removeAnimElement(AnimElement el) {
		animElements.remove(el.getElementKey());
		el.removeFromParent();
	}
	
	public void bringAnimElementToFront(AnimElement el) {
		animElements.remove(el.getElementKey());
		animElements.put(el.getElementKey(), el);
		el.removeFromParent();
		attachChild(el);
		resetZOrder();
	}
	
	public void sendAnimElementToBack(AnimElement el) {
		tempElements.clear();
		for (AnimElement ae : animElements.values()) {
			if (ae != el)
				tempElements.add(ae);
		}
		animElements.clear();
		animElements.put(el.getElementKey(), el);
		for (AnimElement ae : tempElements) {
			animElements.put(ae.getElementKey(),ae);
		}
		resetZOrder();
	}
	
	private void resetZOrder() {
		childZOrder = getLocalTranslation().z;
		for (AnimElement ae : animElements.values()) {
			ae.setPositionZ(childZOrder);
			ae.resetZOrder();
			childZOrder -= zOrderStepMid;
		}
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return this;
	}

	@Override
	public void setSpatial(Spatial spatial) {
	}
	
	public void pause() { this.isPaused = true; }
	public void resume() { this.isPaused = false; }
	public boolean getIsPaused() { return this.isPaused; }
	
	@Override
	public void update(float tpf) {
		if (!isPaused) {
			try {
				for (AnimElement el : animElements.values()) {
					el.update(tpf);
				}
			} catch (Exception ex) {  }
		}
	}

	@Override
	public void render(RenderManager rm, ViewPort vp) {
		
	}
}
