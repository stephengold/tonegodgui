/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.util;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.ElementManager;

/**
 *
 * @author t0neg0d
 */
public class ToolTip extends Label {
	/**
	 * Creates a new instance of the Label control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ToolTip(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("ToolTip").getVector4f("resizeBorders"),
			screen.getStyle("ToolTip").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Label control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public ToolTip(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.setIsResizable(true);
		this.setScaleNS(false);
		this.setScaleEW(false);
		this.setDocking(Docking.NW);
		
		// Load default font info
		this.setFontColor(screen.getStyle("ToolTip").getColorRGBA("fontColor"));
		this.setFontSize(screen.getStyle("ToolTip").getFloat("fontSize"));
		this.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("ToolTip").getString("textAlign")));
		this.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("ToolTip").getString("textVAlign")));
		this.setTextWrap(LineWrapMode.valueOf(screen.getStyle("ToolTip").getString("textWrap")));
		this.setTextPadding(screen.getStyle("ToolTip").getFloat("textPadding"));
		this.setTextClipPadding(screen.getStyle("ToolTip").getFloat("textPadding"));
		
		this.move(0,0,20);
	}
	
	public void useBackGroundColor() {
		this.getElementMaterial().setColor("Color", screen.getStyle("ToolTip").getColorRGBA("bgColor"));
		this.getElementMaterial().setTexture("ColorMap", null);
	}
	
	@Override
	public void show() {
		isVisible = true;
		isClipped = wasClipped;
		if ((Boolean)getElementMaterial().getParam("UseClipping").getValue())
			getElementMaterial().setBoolean("UseClipping", false);
	}
	
	@Override
	public void hide() {
		if (isVisible)
			wasVisible = isVisible;
		isVisible = false;
		isClipped = true;
		clippingBounds.set(0,0,0,0);
		getElementMaterial().setVector4("Clipping", clippingBounds);
		if (!(Boolean)getElementMaterial().getParam("UseClipping").getValue())
			getElementMaterial().setBoolean("UseClipping", true);
	}
	
}
