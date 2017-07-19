/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core.layouts;

import java.util.HashMap;
import java.util.Map;
import tonegod.gui.core.layouts.LayoutHint.Value;

/**
 *
 * @author t0neg0d
 */
public class LayoutHints {
	public Map<String,LayoutHint> params = new HashMap<>();
	
	public LayoutHints(String... params) {
		for (String param : params) {
			LayoutHint hint = new LayoutHint(param);
			this.params.put(hint.type.name(),hint);
		}
	}
	
	public LayoutHints define(String... params) {
		for (String param : params) {
			LayoutHint hint = new LayoutHint(param);
			this.params.put(hint.type.name(),hint);
		}
		return this;
	}
	
	public LayoutHints set(String param) {
		LayoutHint hint = new LayoutHint(param);
		this.params.put(hint.type.name(),hint);
		return this;
	}
	
	public LayoutHint get(String key) {
		return params.get(key);
	}
	
	public void print() {
		for (LayoutHint param : this.params.values()) {
			System.out.println("Type: " + param.type);
			for (Value val : param.getValues().values()) {
				System.out.println("Value Type: " + val.unit);
				System.out.println("Value: " + val.value);
			}
		}
	}
	/*
	public static enum FillType {
		Fill,
		Percent,
		Absolute
	}
	public static enum Alignment {
		Left,
		Center,
		Right
	}
	public static enum VAlignment {
		Top,
		Center,
		Bottom
	}
	private boolean useLayoutPadX = true;
	private boolean useLayoutPadY = true;
	private boolean advanceY = false;
	private boolean lineFeed = false;
	private int numLineFeeds = 1;
	private float elementPadX = 0;
	private float elementPadY = 0;
	private FillType fillTypeX = FillType.Absolute;
	private FillType fillTypeY = FillType.Absolute;
	private float fillX = 1;
	private float fillY = 1;
	private Alignment align = Alignment.Left;
	private VAlignment vAlign = VAlignment.Top;
	private Docking docking = Docking.NW;
	
	public LayoutHints setLayoutAdvanceY(boolean advanceY) {
		this.advanceY = advanceY;
		return this;
	}
	public boolean getLayoutAdvanceY() { return this.advanceY; }
	public LayoutHints setLayoutLineFeed(boolean lineFeed) {
		this.lineFeed = lineFeed;
		return this;
	}
	public boolean getLayoutLineFeed() { return this.lineFeed; }
	public LayoutHints setLayoutNumLineFeeds(int numLineFeeds) {
		this.numLineFeeds = numLineFeeds;
		return this;
	}
	public int getLayoutNumLineFeeds() { return this.numLineFeeds; }
	public LayoutHints setUseLayoutPadX(boolean useLayoutPadX) {
		this.useLayoutPadX = useLayoutPadX;
		return this;
	}
	public boolean getUseLayoutPadX() { return this.useLayoutPadX; }
	public LayoutHints setUseLayoutPadY(boolean useLayoutPadY) {
		this.useLayoutPadY = useLayoutPadY;
		return this;
	}
	public boolean getUseLayoutPadY() { return this.useLayoutPadY; }
	public LayoutHints setElementPadX(float elementPadX) {
		this.elementPadX = elementPadX;
		return this;
	}
	public float getElementPadX() { return this.elementPadX; }
	public LayoutHints setElementPadY(float elementPadY) {
		this.elementPadY = elementPadY;
		return this;
	}
	public float getElementPadY() { return this.elementPadY; }
	public LayoutHints setFillTypeX(FillType fillTypeX) {
		this.fillTypeX = fillTypeX;
		return this;
	}
	public FillType getFillTypeX() { return fillTypeX; }
	public LayoutHints setFillTypeY(FillType fillTypeY) {
		this.fillTypeY = fillTypeY;
		return this;
	}
	public FillType getFillTypeY() { return fillTypeY; }
	public LayoutHints setFillX(float fillX) {
		if (fillX > 1) fillX /= 100;
		this.fillX = fillX;
		return this;
	}
	public LayoutHints setFillX(String fillX) {
		float nFillX = 1;
		if (fillX.indexOf("%") != -1) {
			setFillTypeX(FillType.Percent);
			nFillX = Float.parseFloat(fillX.substring(0,fillX.indexOf("%")));
			if (nFillX > 1) nFillX /= 100;
			this.fillX = nFillX;
		} else if (fillX.equals("*")) {
			setFillTypeX(FillType.Fill);
		} else {
			setFillTypeX(FillType.Percent);
			nFillX = Float.parseFloat(fillX);
			if (nFillX > 1) nFillX /= 100;
			this.fillX = nFillX;
		}
		return this;
	}
	public float getFillX() { return this.fillX; }
	public LayoutHints setFillY(float fillY) {
		if (fillY > 1) fillY /= 100;
		this.fillY = fillY;
		return this;
	}
	public LayoutHints setFillY(String fillY) {
		float nFillY = 1;
		if (fillY.indexOf("%") != -1) {
			setFillTypeY(FillType.Percent);
			nFillY = Float.parseFloat(fillY.substring(0,fillY.indexOf("%")));
			if (nFillY > 1) nFillY /= 100;
			this.fillY = nFillY;
		} else if (fillY.equals("*")) {
			setFillTypeY(FillType.Fill);
		} else {
			setFillTypeY(FillType.Percent);
			nFillY = Float.parseFloat(fillY);
			if (nFillY > 1) nFillY /= 100;
			this.fillY = nFillY;
		}
		return this;
	}
	public float getFillY() { return this.fillY; }
	public LayoutHints setDocking(Docking docking) {
		this.docking = docking;
		return this;
	}
	public Docking getDocking() { return this.docking; }
	public LayoutHints setAlignment(Alignment align) {
		this.align = align;
		return this;
	}
	public Alignment getAlignment() { return this.align; }
	public LayoutHints setVAlignment(VAlignment vAlign) {
		this.vAlign = vAlign;
		return this;
	}
	public VAlignment getVAlignment() { return this.vAlign; }
	*/
}