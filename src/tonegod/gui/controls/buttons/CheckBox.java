/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.buttons;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.BitmapTextUtil;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.Effect;
import tonegod.gui.listeners.MouseButtonListener;
import tonegod.gui.listeners.MouseFocusListener;

/**
 *
 * @author t0neg0d
 */
public class CheckBox extends ButtonAdapter {
	
	ClickableLabel label;
	float labelFontSize;
	
	/**
	 * Creates a new instance of the CheckBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public CheckBox(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("CheckBox").getVector2f("defaultSize"),
			screen.getStyle("CheckBox").getVector4f("resizeBorders"),
			screen.getStyle("CheckBox").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the CheckBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public CheckBox(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("CheckBox").getVector2f("defaultSize"),
			screen.getStyle("CheckBox").getVector4f("resizeBorders"),
			screen.getStyle("CheckBox").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the CheckBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public CheckBox(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("CheckBox").getVector4f("resizeBorders"),
			screen.getStyle("CheckBox").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the CheckBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the CheckBox
	 */
	public CheckBox(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg);
	}
	
	/**
	 * Creates a new instance of the CheckBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public CheckBox(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("CheckBox").getVector2f("defaultSize"),
			screen.getStyle("CheckBox").getVector4f("resizeBorders"),
			screen.getStyle("CheckBox").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the CheckBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public CheckBox(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("CheckBox").getVector4f("resizeBorders"),
			screen.getStyle("CheckBox").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the CheckBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the CheckBox
	 */
	public CheckBox(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.clearAltImages();
		this.removeEffect(Effect.EffectEvent.Hover);
		this.removeEffect(Effect.EffectEvent.Press);
		this.removeEffect(Effect.EffectEvent.LoseFocus);
		
		labelFontSize = screen.getStyle("CheckBox").getFloat("fontSize");
		
		label = new ClickableLabel(
			this,
			screen,
			UID + ":Label",
			new Vector2f(getWidth(), 0),
			new Vector2f(100,getHeight())
		);
		label.setDocking(Docking.SW);
		label.setScaleEW(true);
		label.setScaleNS(false);
		label.setIgnoreMouse(false);
		
		label.setFontColor(screen.getStyle("CheckBox").getColorRGBA("fontColor"));
		label.setFontSize(labelFontSize);
		label.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("CheckBox").getString("textAlign")));
		label.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("CheckBox").getString("textVAlign")));
		label.setTextWrap(LineWrapMode.valueOf(screen.getStyle("CheckBox").getString("textWrap")));
		
		this.setIsToggleButton(true);
		
		if (screen.getStyle("CheckBox").getString("hoverImg") != null) {
			setButtonHoverInfo(
				screen.getStyle("CheckBox").getString("hoverImg"),
				screen.getStyle("CheckBox").getColorRGBA("hoverColor")
			);
		}
		if (screen.getStyle("CheckBox").getString("pressedImg") != null) {
			setButtonPressedInfo(
				screen.getStyle("CheckBox").getString("pressedImg"),
				screen.getStyle("CheckBox").getColorRGBA("pressedColor")
			);
		}
		
		populateEffects("CheckBox");
		if (Screen.isAndroid()) {
			removeEffect(Effect.EffectEvent.Hover);
			removeEffect(Effect.EffectEvent.TabFocus);
			removeEffect(Effect.EffectEvent.LoseTabFocus);
		}
	}
	
	/**
	 * Sets text for the check Label
	 * @param text 
	 */
	public void setLabelText(String text) {
		if (label.getParent() != null) {
			elementChildren.remove(label.getUID());
			label.removeFromParent();
		}
		
		float width = BitmapTextUtil.getTextWidth(this, text);
		float height = BitmapTextUtil.getTextLineHeight(this, text);
		float nextY = height-getHeight();
		nextY /= 2;
		nextY = (float) Math.ceil(nextY+1);
		
		label.setWidth(width+getWidth()+4);
		label.setX(getWidth()+4);
		label.setY(-nextY);
		label.setDocking(Docking.SW);
		label.setScaleEW(true);
		label.setScaleNS(false);
		label.setFontColor(screen.getStyle("CheckBox").getColorRGBA("fontColor"));
		label.setFontSize(labelFontSize);
		label.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("CheckBox").getString("textAlign")));
		label.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("CheckBox").getString("textVAlign")));
		label.setTextWrap(LineWrapMode.valueOf(screen.getStyle("CheckBox").getString("textWrap")));
		
		label.setText(text);
		
		addChild(label);
	}
	
	/**
	 * Checks/unchecks the checkbox
	 * @param isChecked 
	 */
	public void setIsChecked(boolean isChecked) {
		setIsToggled(isChecked);
	}
	
	public void setIsCheckedNoCallback(boolean isChecked) {
		setIsToggledNoCallback(isChecked);
	}
	
	public Label getLabel() {
		return this.label;
	}
	
	/**
	 * Returns if the checkbox is checked/unchecked
	 * @return boolean
	 */
	public boolean getIsChecked() {
		return this.getIsToggled();
	}
	
	public class ClickableLabel extends Label implements MouseButtonListener, MouseFocusListener {
		Button owner;
		
		public ClickableLabel(Button owner, ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
			super(screen, UID, position, dimensions);
			this.owner = owner;
		}
		
		@Override
		public void onMouseLeftPressed(MouseButtonEvent evt) {
			MouseButtonEvent nEvt = new MouseButtonEvent(
				0,
				true,
				(int)owner.getAbsoluteX(),
				(int)owner.getAbsoluteY()
			);
			owner.onMouseLeftPressed(nEvt);
		}

		@Override
		public void onMouseLeftReleased(MouseButtonEvent evt) {
			MouseButtonEvent nEvt = new MouseButtonEvent(
				0,
				false,
				(int)owner.getAbsoluteX(),
				(int)owner.getAbsoluteY()
			);
			owner.onMouseLeftReleased(nEvt);
		}

		@Override
		public void onMouseRightPressed(MouseButtonEvent evt) {
			MouseButtonEvent nEvt = new MouseButtonEvent(
				1,
				true,
				(int)owner.getAbsoluteX(),
				(int)owner.getAbsoluteY()
			);
			owner.onMouseRightPressed(nEvt);
		}

		@Override
		public void onMouseRightReleased(MouseButtonEvent evt) {
			MouseButtonEvent nEvt = new MouseButtonEvent(
				1,
				false,
				(int)owner.getAbsoluteX(),
				(int)owner.getAbsoluteY()
			);
			owner.onMouseRightReleased(nEvt);
		}

		@Override
		public void onGetFocus(MouseMotionEvent evt) {
			MouseMotionEvent nEvt = new MouseMotionEvent(
				(int)owner.getAbsoluteX(),
				(int)owner.getAbsoluteY(),
				0,0,
				0,0
			);
			owner.onGetFocus(nEvt);
		}

		@Override
		public void onLoseFocus(MouseMotionEvent evt) {
			MouseMotionEvent nEvt = new MouseMotionEvent(
				Screen.isAndroid() ? (int)screen.getTouchXY().x : (int)screen.getMouseXY().x,
				Screen.isAndroid() ? (int)screen.getTouchXY().y : (int)screen.getMouseXY().y,
				0,0,
				0,0
			);
			owner.onLoseFocus(nEvt);
		}
	}
}
