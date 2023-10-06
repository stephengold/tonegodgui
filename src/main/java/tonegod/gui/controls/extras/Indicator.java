/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class Indicator extends Element {
	private float maxValue = 0, currentValue = 0, percentage = 0;
	private Orientation orientation;
	private ColorRGBA indicatorColor;
	private String alphaMapPath;
	private String overlayImg;
	private Element elIndicator, elOverlay;
	private boolean displayValues = false, displayPercentages = false;
	private Vector2f indDimensions = new Vector2f();
	private Vector4f indPadding = Vector4f.ZERO.clone();
	private Vector4f clipTest = Vector4f.ZERO.clone();
	private boolean reverseDirection = false;
	private ClippingDefine def = null;
	
	/**
	 * Creates a new instance of the Indicator control
	 * 
	 * @param screen screen control to which the Element should be added
         * @param orientation desired orientation
	 */
	public Indicator(ElementManager screen, Orientation orientation) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Indicator").getVector2f("defaultSize"),
			screen.getStyle("Indicator").getVector4f("resizeBorders"),
			screen.getStyle("Indicator").getString("defaultImg"),
			orientation, true
		);
	}
	
	/**
	 * Creates a new instance of the Indicator control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
         * @param orientation desired orientation
	 */
	public Indicator(ElementManager screen, Vector2f position, Orientation orientation) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Indicator").getVector2f("defaultSize"),
			screen.getStyle("Indicator").getVector4f("resizeBorders"),
			screen.getStyle("Indicator").getString("defaultImg"),
			orientation, true
		);
	}
	
	/**
	 * Creates a new instance of the Indicator control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
         * @param orientation desired orientation
         * @param useOverlay true &rarr; use, false &rarr; don't use
	 */
	public Indicator(ElementManager screen, Vector2f position, Vector2f dimensions, Orientation orientation, boolean useOverlay) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Indicator").getVector4f("resizeBorders"),
			screen.getStyle("Indicator").getString("defaultImg"),
			orientation, useOverlay
		);
	}
	
	/**
	 * Creates a new instance of the Indicator control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Element
         * @param orientation desired orientation
	 */
	public Indicator(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Orientation orientation) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg, orientation, true);
	}
	
	/**
	 * Creates a new instance of the Indicator control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
         * @param orientation desired orientation
	 */
	public Indicator(ElementManager screen, String UID, Vector2f position, Orientation orientation) {
		this(screen, UID, position,
			screen.getStyle("Indicator").getVector2f("defaultSize"),
			screen.getStyle("Indicator").getVector4f("resizeBorders"),
			screen.getStyle("Indicator").getString("defaultImg"),
			orientation, true
		);
	}
	
	/**
	 * Creates a new instance of the Indicator control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
         * @param orientation desired orientation
	 */
	public Indicator(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Orientation orientation) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Indicator").getVector4f("resizeBorders"),
			screen.getStyle("Indicator").getString("defaultImg"),
			orientation, true
		);
	}
	
	/**
	 * Creates a new instance of the Indicator control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Indicator
         * @param orientation desired orientation
         * @param useOverlay true &rarr; use, false &rarr; don't use
	 */
	public Indicator(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Orientation orientation, boolean useOverlay) {
		super(screen, UID, position, dimensions, resizeBorders, null);
		
		indDimensions.set(dimensions);
		
		setScaleEW(true);
		setScaleNS(false);
		
		this.overlayImg = defaultImg;
		this.orientation = orientation;
		
		elIndicator = new Element(
			screen,
			UID + ":Indicator",
			Vector2f.ZERO,
			dimensions,
			resizeBorders,
			null
		) {
			@Override
			public void updateLocalClippingLayer() {
				Indicator ind = ((Indicator)this.getElementParent());
				if (def == null) def = elIndicator.getClippingDefine(elIndicator);
				
				if (getIsVisible()) {
					float clipX = 0, clipY = 0, clipW, clipH;
					if (ind.getOrientation() == Indicator.Orientation.HORIZONTAL) {
						if (reverseDirection) {
							clipX = def.getElement().getWidth()-ind.getCurrentPercentage();
							clipW = def.getElement().getWidth();
						} else {
							clipW = def.getElement().getWidth()-(def.getElement().getWidth()-ind.getCurrentPercentage());
						}
						clipH = def.getElement().getHeight();
					} else {
						if (reverseDirection) {
							clipY = def.getElement().getHeight()-ind.getCurrentPercentage();
							clipH = def.getElement().getHeight();
						} else {
							clipH = def.getElement().getHeight()-(def.getElement().getHeight()-ind.getCurrentPercentage());
						}
						clipW = def.getElement().getWidth();
					}
					def.clip.set(clipX, clipY, clipW, clipH);
					super.updateLocalClippingLayer();
					
				}
			}
		};
		elIndicator.addClippingLayer(elIndicator, new Vector4f(0,0,elIndicator.getWidth(),elIndicator.getHeight()));
		elIndicator.setIgnoreMouse(true);
		elIndicator.setDocking(Docking.SW);
		elIndicator.setScaleEW(true);
		elIndicator.setScaleNS(false);
		addChild(elIndicator);
		
                    elOverlay = new Element(
                            screen,
                            UID + ":Overlay",
                            Vector2f.ZERO,
                            dimensions,
                            resizeBorders,
                            overlayImg
                    );
                    elOverlay.setIgnoreMouse(true);
                    elOverlay.setDocking(Docking.SW);
                    elOverlay.setScaleEW(true);
                    elOverlay.setScaleNS(false);

                    // Load default font info
                    elOverlay.setFontColor(screen.getStyle("Indicator").getColorRGBA("fontColor"));
                    elOverlay.setFontSize(screen.getStyle("Indicator").getFloat("fontSize"));
                    elOverlay.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Indicator").getString("textAlign")));
                    elOverlay.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Indicator").getString("textVAlign")));
                    elOverlay.setTextWrap(LineWrapMode.valueOf(screen.getStyle("Indicator").getString("textWrap")));
                    elOverlay.setTextClipPaddingByKey("Indicator","textPadding");
                    elOverlay.setTextPaddingByKey("Indicator","textPadding");

                if(useOverlay) {
                    addChild(elOverlay);
                }
		
	}
	
	/**
         * Alter whether the element should scale with parent when resized.
	 * Use this method in place of setScaleEW and setScaleNS.
	 * @param scaleNS true &rarr; scale, false &rarr; don't scale
	 * @param scaleEW true &rarr; scale, false &rarr; don't scale
	 */
	public void setScaling(boolean scaleNS, boolean scaleEW) {
		setScaleNS(scaleNS);
		elIndicator.setScaleNS(scaleNS);
		elOverlay.setScaleNS(scaleNS);
		setScaleEW(scaleEW);
		elIndicator.setScaleEW(scaleEW);
		elOverlay.setScaleEW(scaleEW);
	}
	
	@Override
	public void controlResizeHook() {
		setIndicatorPadding(indPadding);
		refactorIndicator();
	}
	
	/**
	 * Returns the Indicator.Orientation of the Indicator instance
	 * @return Indicator.Orientation
	 */
	public Orientation getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Alter the color of the Indicator.
	 * @param indicatorColor desired color
	 */
	public void setIndicatorColor(ColorRGBA indicatorColor) {
		this.indicatorColor = indicatorColor;
		elIndicator.getElementMaterial().setColor("Color", this.indicatorColor);
	}
	
	/**
	 * Alter the maximum value of the Indicator (e.g. float  = 100%)
	 * @param maxValue desired maximum value
	 */
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
		refactorIndicator();
	}
	
	/**
	 * Read the maximum value for the Indicator.
	 * @return maxValue desired maximum value
	 */
	public float getMaxValue() {
		return this.maxValue;
	}
	
	/**
	 * Alter the current value of the Indicator
	 * @param currentValue desired value
	 */
	public void setCurrentValue(float currentValue) {
		this.currentValue = currentValue;
		refactorIndicator();
	}
	
	public void setReverseDirection(boolean reverseDirection) {
		this.reverseDirection = reverseDirection;
	}
	
	/**
	 * Read the current value of the Indicator.
	 * @return currentValue
	 */
	public float getCurrentValue() {
		return this.currentValue;
	}
	
	private void refactorIndicator() {
		if (currentValue > maxValue) {
			currentValue = maxValue;
		} else if (currentValue < 0f) {
			currentValue = 0f;
		}
		percentage = currentValue/maxValue;
		if (orientation == Orientation.HORIZONTAL) {
			percentage *= indDimensions.x;
		} else {
			percentage *= indDimensions.y;
		}
		elIndicator.updateLocalClippingLayer();
		
		if (this.displayValues) {
			elOverlay.setText(String.valueOf((int)this.currentValue) + "/" + String.valueOf((int)this.maxValue));
		} else if (this.displayPercentages) {
			elOverlay.setText(String.valueOf((int)((this.currentValue/this.maxValue)*100f)) + "%");
		} else {
		//	elOverlay.setText("");
		}
		
		onChange(currentValue, currentValue/maxValue*100f);
	}
	
	/**
	 * Returns current value as a percent of the max value
	 * @return percentage
	 */
	public float getCurrentPercentage() {
		return this.percentage;
	}
	
	/**
	 * Use setAlphaMap instead
	 * @param alphaMapPath desired path
	 */
	@Deprecated
	public void setIndicatorAlphaMap(String alphaMapPath) {
		this.alphaMapPath = alphaMapPath;
		elIndicator.setAlphaMap(this.alphaMapPath);
	}
	
	/**
	 * Applies an alpha map to the indicator, allowing for curved shapes
	 * @param alphaMapPath desired path
	 */
	@Override
	public void setAlphaMap(String alphaMapPath) {
		this.alphaMapPath = alphaMapPath;
		elIndicator.setAlphaMap(this.alphaMapPath);
	}
	
	public Element getIndicatorElement() {
		return elIndicator;
	}
	
	public Element getOverlayElement() {
		return elOverlay;
	}
	
	/**
	 * Return the element used for displaying overlay text
	 * @return elOverlay
	 */
	public Element getTextDisplayElement() {
		return this.elOverlay;
	}
	
	/**
	 * Sets the display text to format as currentValue / maxValue
	 */
	public void setDisplayValues() {
		this.displayPercentages = false;
		this.displayValues = true;
	}
	
	/**
	 * Sets the display text to current value as percent %
	 */
	public void setDisplayPercentage() {
		this.displayPercentages = true;
		this.displayValues = false;
	}
	
	/**
	 * Hides the overlay display text
	 */
	public void setHideText() {
		this.displayPercentages = false;
		this.displayValues = false;
	}
	
	/**
	 * Set the image to use behind the indicator
	 * @param imgPath path to desired image
	 */
	public void setBaseImage(String imgPath) {
		setColorMap(imgPath);
	}
	
	/**
	 * Set the image to use in front of the indicator
	 * @param imgPath path to desired image
	 */
	public void setOverlayImage(String imgPath) {
		elOverlay.setColorMap(imgPath);
	}
	
	public void setIndicatorImage(String imgPath) {
		elIndicator.setColorMap(imgPath);
	}
	
	public void setIndicatorPadding(Vector4f padding) {
		indPadding.set(padding);
		indDimensions.set(getWidth()-(padding.x+padding.z),getHeight()-(padding.y+padding.w));
		elIndicator.setPosition(padding.x,padding.y);
		elIndicator.setDimensions(indDimensions);
	}
	
	public abstract void onChange(float currentValue, float currentPercentage);
}
