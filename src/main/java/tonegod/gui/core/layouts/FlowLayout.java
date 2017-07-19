package tonegod.gui.core.layouts;

import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;

/**
 *
 * @author t0neg0d
 */
public class FlowLayout extends AbstractLayout {
	
	public FlowLayout(ElementManager screen, String... constraints) {
		super(screen, constraints);
	}
	
	@Override
	public void resize() {
		
	}
	
	@Override
	public void setOwner(Element el) {
		this.owner = el;
	}

	@Override
	public void layoutChildren() {
		Element lastEl = null;
		LayoutHelper.reset();
		LayoutHelper.setPadding(padding.x,padding.y,padding.x,padding.w);
		LayoutHelper.advanceX(margins.x);
		LayoutHelper.advanceY(margins.y);
		
		for (Element el : owner.getElements()) {
			this.convertElementProperties(el);
			
			LayoutHint fill = el.getLayoutHints().get("fill");
			boolean fillx = (fill == null) ? false : (Boolean)fill.getValues().get("x").getValue();

			LayoutHint pad = el.getLayoutHints().get("pad");
			float padLeft = (pad == null) ? 0 : (Float)pad.getValues().get("left").getValue();
			float padTop = (pad == null) ? 0 : (Float)pad.getValues().get("top").getValue();
			float padRight = (pad == null) ? 0 : (Float)pad.getValues().get("right").getValue();
			float padBottom = (pad == null) ? 0 : (Float)pad.getValues().get("bottom").getValue();
			
			boolean advanceY;
			
			if (lastEl != null) {
				advanceY = (lastEl.getLayoutHints().get("wrap") == null) ? false : true;

				if (advanceY) {
					LayoutHelper.resetX();
					LayoutHelper.advanceX(margins.x);
					LayoutHelper.advanceY(lastEl,true);
					LayoutHelper.advanceX(padLeft);
					LayoutHelper.advanceY(padTop);
				} else {
					if (LayoutHelper.position().x + el.getWidth() > owner.getWidth()-margins.x) {
						LayoutHelper.resetX();
						LayoutHelper.advanceX(margins.x);
						LayoutHelper.advanceY(lastEl,true);
						LayoutHelper.advanceX(padLeft);
						LayoutHelper.advanceY(padTop);
					} else {
						LayoutHelper.advanceX(lastEl,true);
						LayoutHelper.advanceX(padLeft);
						LayoutHelper.advanceY(padTop);
					}
				}
			}
			
			el.setPosition(LayoutHelper.position());
			el.setY(owner.getHeight()-el.getY()-el.getHeight());
			
			if (fillx) {
				float resizeW;
				if (fillx)	resizeW = el.getAbsoluteX()+(owner.getWidth()-margins.x);
				else		resizeW = el.getWidth();
				el.resize(
					resizeW,
					el.getAbsoluteHeight(),
					Element.Borders.SE
				);
			}
			
			LayoutHelper.advanceX(padRight);
			LayoutHelper.advanceY(padBottom);
			
			lastEl = el;
		}
		
		LayoutHelper.reset();
	}
	/*
	private void fill() {
		Element lastEl = null;
		for (Element el : owner.getElements()) {
			if (owner.getDimensions() != Vector2f.ZERO) {
				SizeUnit xUnit = (SizeUnit)el.getLayoutHints().get("fillx").getValues().get("fillx").getSizeUnit();
				SizeUnit yUnit = (SizeUnit)el.getLayoutHints().get("filly").getValues().get("filly").getSizeUnit();
				float fillX = (Float)el.getLayoutHints().get("fillx").getValues().get("fillx").getValue();
				float fillY = (Float)el.getLayoutHints().get("filly").getValues().get("filly").getValue();
				
				switch (xUnit) {
					case fill:
						float lWidth = 0;
						if (lastEl != null)
							lWidth = lastEl.getX()+lastEl.getWidth();
						el.setWidth(owner.getWidth()-(margins.x*2)-lWidth);
						break;
					case percent:
						el.setWidth((owner.getWidth()-(margins.x*2))*fillX);
						break;
				}
				switch (yUnit) {
					case fill:
						float lHeight = 0;
						if (lastEl != null)
							lHeight = lastEl.getY()+lastEl.getHeight();
						System.out.println(lHeight);
						el.setHeight(owner.getHeight()-(margins.y*2)-lHeight);
						break;
					case percent:
						el.setHeight((owner.getHeight()-(margins.y*2))*fillY);
						break;
				}
				lastEl = el;
			}
		}
	}
	*/
}
