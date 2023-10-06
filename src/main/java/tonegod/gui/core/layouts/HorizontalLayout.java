/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core.layouts;

import com.jme3.math.Vector2f;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;

/**
 *
 * @author t0neg0d
 */
public class HorizontalLayout extends AbstractLayout {
	
	public HorizontalLayout(ElementManager screen, String... constraints) {
		super(screen, constraints);
	}
	
	@Override
	public void resize() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setOwner(Element el) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void layoutChildren() {
		Element lastEl = null;
		
		LayoutHelper.reset();
		LayoutHelper.setPadding(padding.x, padding.y, padding.z, padding.w);
		
		fill();
		
		for (Element el : owner.getElements()) {
			float padLeft = 0, padTop = 0, padRight = 0, padBottom = 0;
			LayoutHint pad = el.getLayoutHints().get("pad");
			if (pad != null) {
				padLeft		= (Float)pad.getValues().get("left").getValue();
				padTop		= (Float)pad.getValues().get("top").getValue();
			}
			if (lastEl != null) {
				if (LayoutHelper.position().x + el.getWidth() > owner.getWidth()-margins.x) {
					LayoutHelper.resetX();
					LayoutHelper.advanceX(margins.x);
					LayoutHelper.advanceY(lastEl, true);
				} else {
					LayoutHelper.advanceX(lastEl, true);
				}
			}
			LayoutHelper.advanceX(padLeft);
			LayoutHelper.advanceY(padTop);
			
			el.setPosition(LayoutHelper.position());
			el.setY(owner.getHeight()-el.getY()-el.getHeight());
			lastEl = el;
		}
		
		LayoutHelper.reset();
	}
	
	private void fill() {
		Element lastEl = null;
		for (Element el : owner.getElements()) {
			if (owner.getDimensions() != Vector2f.ZERO) {
				LayoutHint fill = el.getLayoutHints().get("fill");
				boolean fillx = (fill == null) ? false : (Boolean)fill.getValues().get("x").getValue();
				boolean filly = (fill == null) ? false : (Boolean)fill.getValues().get("y").getValue();
				
				if (fillx) {
					float lWidth = 0;
					if (lastEl != null)
						lWidth = lastEl.getX()+lastEl.getWidth();
					el.setWidth(owner.getWidth()-(margins.x*2)-lWidth);
				} else {
					el.setWidth(owner.getWidth()-(margins.x*2));
				}
				if (filly) {
					float lHeight = 0;
					if (lastEl != null)
						lHeight = lastEl.getY()+lastEl.getHeight();
					el.setHeight(owner.getHeight()-(margins.y*2)-lHeight);
				} else {
					el.setHeight(owner.getHeight()-(margins.y*2));
				}
				lastEl = el;
			}
		}
	}
}
