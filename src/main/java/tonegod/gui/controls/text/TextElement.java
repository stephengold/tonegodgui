package tonegod.gui.controls.text;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.framework.core.AnimText;

/**
 *
 * @author t0neg0d
 */
public abstract class TextElement extends Element implements Control {
	AnimText animText;
	String teText = "";
	boolean useTextClipping = false;
//	LineWrapMode wrapMode = LineWrapMode.NoWrap;
//	VAlign vAlign = VAlign.Top;
//	Align hAlign = Align.Left;
	
	
	int qdIndex = 0;
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public TextElement(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Label").getVector2f("defaultSize"),
			Vector4f.ZERO,
			null,
			screen.getDefaultGUIFont()
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
         * @param font font to use
	 */
	public TextElement(ElementManager screen, BitmapFont font) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Label").getVector2f("defaultSize"),
			Vector4f.ZERO,
			null,
			font
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
         * @param font font to use
	 */
	public TextElement(ElementManager screen, Vector2f position, BitmapFont font) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Label").getVector2f("defaultSize"),
			Vector4f.ZERO,
			null,
			font
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
         * @param font font to use
	 */
	public TextElement(ElementManager screen, Vector2f position, Vector2f dimensions, BitmapFont font) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			Vector4f.ZERO,
			null,
			font
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
         * @param font font to use
	 */
	public TextElement(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, BitmapFont font) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg, font);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
         * @param font font to use
	 */
	public TextElement(ElementManager screen, String UID, Vector2f position, BitmapFont font) {
		this(screen, UID, position,
			screen.getStyle("Label").getVector2f("defaultSize"),
			Vector4f.ZERO,
			null,
			font
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
         * @param font font to use
	 */
	public TextElement(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, BitmapFont font) {
		this(screen, UID, position, dimensions,
			Vector4f.ZERO,
			null,
			font
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
         * @param font font to use
	 */
	public TextElement(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, BitmapFont font) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		this.setIsResizable(false);
		this.setIsMovable(false);
		this.setIgnoreMouse(true);
		this.setDocking(Docking.NW);
		this.setClippingLayer(this);
		
		textWrap = LineWrapMode.NoWrap;
		
		BitmapFont teFont = (font == null) ? getFont() : font;
		
		animText = new AnimText(
			screen.getApplication().getAssetManager(),
			teFont
		);
		animText.setBounds(dimensions);
		animText.setScale(1,1);
		animText.setOrigin(0,0);
		attachChild(animText);
	}
	
	public AnimText getAnimText() {
		return this.animText;
	}
	
	@Override
	public void setText(String text) {
		this.teText = text;
		animText.setText(text);
		animText.setPositionY(getHeight()-animText.getLineHeight());
		setTextWrap(textWrap);
	}
	
	@Override
	public void controlMoveHook() {
		animText.getMaterial().setVector4("Clipping", getClippingBounds());
	//	animText.getMaterial().setBoolean("UseClipping", true);
	}
	
	@Override
	public void controlResizeHook() {
		if (this.getIsResizable()) {
		//	animText.setPositionY(getHeight()-animText.getLineHeight());
			animText.setBounds(getDimensions());
			switch (textWrap) {
				case Character:
					animText.wrapTextToCharacter(getWidth());
					break;
				case Word:
					animText.wrapTextToWord(getWidth());
					break;
			}
			setTextAlign(textAlign);
			setTextVAlign(textVAlign);
			animText.getMaterial().setVector4("Clipping", getClippingBounds());
		//	animText.getMaterial().setBoolean("UseClipping", true);
			animText.update(0);
		}
	}
	
	public void setLineWrapMode(LineWrapMode textWrap) {
		setTextWrap(textWrap);
	}
	
	@Override
	public void setTextWrap(LineWrapMode textWrap) {
		this.textWrap = textWrap;
		animText.setTextWrap(textWrap);
		switch (textWrap) {
			case Character:
				animText.wrapTextToCharacter(getWidth());
				break;
			case Word:
				animText.wrapTextToWord(getWidth());
				break;
			case NoWrap:
				animText.wrapTextNoWrap();
				break;
			case Clip:
				animText.wrapTextNoWrap();
				setUseTextClipping(true);
				break;
		}
		animText.update(0);
	}
	
	public void setUseTextClipping(boolean clip) {
		useTextClipping = clip;
		animText.getMaterial().setVector4("Clipping", getClippingBounds());
		animText.getMaterial().setBoolean("UseClipping", clip);
	}
	
	public void setSize(float size) {
		setFontSize(size);
	}
	
	@Override
	public float getFontSize() { return size; }
	
	float size;
	@Override
	public void setFontSize(float fontSize) {
		this.size = fontSize;
		animText.setFontSize(fontSize);
		setTextWrap(textWrap);
	}
	
	public void setColor(ColorRGBA color) {
		setFontColor(color);
	}
	
	@Override
	public void setFontColor(ColorRGBA fontColor) {
		animText.setFontColor(fontColor);
		setTextWrap(textWrap);
	}
	
	public void setFont(BitmapFont font) {
		this.animText.setFont(font);
		setText(teText);
		setUseTextClipping(useTextClipping);
		setTextVAlign(textVAlign);
	}
	
	public void setSubStringColor(String subString, ColorRGBA color) {
		this.setSubStringColor(subString, color, false, 1);
	}
	
	public void setSubStringColor(String subString, ColorRGBA color, boolean allInstances) {
		this.setSubStringColor(subString, color, allInstances, 1);
	}
	
	public void setSubStringColor(String subString, ColorRGBA color, boolean allInstances, int... whichInstances) {
		animText.setSubStringColor(subString, color, allInstances, whichInstances);
	}
	
	public void setAlpha(float alpha) {
		animText.setAlpha(alpha);
		animText.update(0);
	}
	
	public void setAlignment(Align textAlign) {
		setTextAlign(textAlign);
	}
	
	@Override
	public void setTextAlign(Align textAlign) {
		this.textAlign = textAlign;
		animText.setTextAlign(textAlign);
	//	animText.alignToBoundsH(textAlign);
	}
	
	public void setVerticalAlignment(VAlign textVAlign) {
		setTextVAlign(textVAlign);
	}
	
	@Override
	public void setTextVAlign(VAlign textVAlign) {
		this.textVAlign = textVAlign;
		animText.setTextVAlign(textVAlign);
	}
	
	public void startEffect() {
		onEffectStart();
		this.addControl(this);
	}
	
	public void stopEffect() {
		this.removeControl(TextElement.class);
		onEffectStop();
		update(0);
	}
	
	public abstract void onUpdate(float tpf);
	public abstract void onEffectStart();
	public abstract void onEffectStop();
	
	@Override
	protected void validateClipSettings() {
		super.validateClipSettings();
		/*
		if (this.useTextClipping) {
			if (!clippingLayers.isEmpty()) {
				if (!(Boolean)animText.getMaterial().getParam("UseClipping").getValue())
					animText.getMaterial().setBoolean("UseClipping", true);
			} else {
				if ((Boolean)animText.getMaterial().getParam("UseClipping").getValue())
					animText.getMaterial().setBoolean("UseClipping", false);
			}
			animText.getMaterial().setVector4("Clipping", clippingBounds);
		}
		*/
	}
	
	@Override
	public void update(float tpf) {
		onUpdate(tpf);
		updateAnimText(tpf);
	}
	
	private void updateAnimText(float tpf) {
		animText.update(tpf);
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return this;
	}

	@Override
	public void setSpatial(Spatial spatial) {  }

	@Override
	public void render(RenderManager rm, ViewPort vp) {  }
}
