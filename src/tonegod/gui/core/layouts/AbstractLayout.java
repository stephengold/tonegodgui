/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core.layouts;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.HashMap;
import java.util.Map;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;

/**
 *
 * @author t0neg0d
 */
public abstract class AbstractLayout implements Layout {
	public Map<String,LayoutParam> params = new HashMap<>();
	protected ElementManager screen;
	protected Element owner;
	protected Vector4f margins = new Vector4f(10,10,10,10);
	protected Vector4f padding = new Vector4f(5,5,5,5);
	protected Vector4f tempV4 = new Vector4f();
	protected boolean handlesResize = true;
	protected boolean props = false;
	protected boolean clip = false;
	protected Vector2f tempV2 = new Vector2f();
	
	public AbstractLayout(ElementManager screen, String... constraints) {
		this.screen = screen;
		for (String param : constraints) {
			LayoutParam lp = new LayoutParam(param);
			this.params.put(lp.type.name(),lp);
		}
		LayoutParam m = params.get("margin");
		if (m != null) {
			margins.set(
				(Float)m.getValues().get("left").getValue(),
				(Float)m.getValues().get("top").getValue(),
				(Float)m.getValues().get("right").getValue(),
				(Float)m.getValues().get("bottom").getValue()
			);
		}
		LayoutParam p = params.get("pad");
		if (p != null) {
			padding.set(
				(Float)p.getValues().get("left").getValue(),
				(Float)p.getValues().get("top").getValue(),
				(Float)p.getValues().get("right").getValue(),
				(Float)p.getValues().get("bottom").getValue()
			);
		}
		LayoutParam c = params.get("clip");
		if (c != null) {
			clip = (Boolean)c.getValues().get("clip").getValue();
		}
	}
	
	@Override
	public Layout define(String... params) {
		for (String param : params) {
			LayoutParam lp = new LayoutParam(param);
			this.params.put(lp.type.name(),lp);
		}
		return this;
	}
	
	@Override
	public Layout set(String param) {
		LayoutParam lp = new LayoutParam(param);
		this.params.put(lp.type.name(),lp);
		return this;
	}
	
	@Override
	public LayoutParam get(String key) {
		return params.get(key);
	}
	
	@Override
	public ElementManager getScreen() {
		return screen;
	}
	
	@Override
	public void setHandlesResize(boolean handlesResize) {
		this.handlesResize = handlesResize;
	}
	
	@Override
	public boolean getHandlesResize() {
		return handlesResize;
	}
	
	
	
	protected void convertElementProperties(Element el) {
		if (!props) {
			LayoutHint min = el.getLayoutHints().get("min");
			if (min == null) {
				el.getLayoutHints().set("min " + (el.getResizeBorderEastSize()+el.getResizeBorderWestSize()) + " " + (el.getResizeBorderNorthSize()+el.getResizeBorderSouthSize()));
				el.setMinDimensions(
					tempV2.set(
						el.getResizeBorderEastSize()+el.getResizeBorderWestSize(),
						el.getResizeBorderNorthSize()+el.getResizeBorderSouthSize()
					)
				);
			} else {
				el.setMinDimensions(tempV2.set((Float)min.getValues().get("x").getValue(),(Float)min.getValues().get("y").getValue()));
			}
			LayoutHint grow = el.getLayoutHints().get("grow");
			if (grow == null) {
				el.getLayoutHints().set("grow " + el.getScaleEW() + " " + el.getScaleNS());
			}
		}
	}
}
