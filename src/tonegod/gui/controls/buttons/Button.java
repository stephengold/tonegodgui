/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.buttons;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.Screen;
import tonegod.gui.style.StyleManager.CursorType;
import tonegod.gui.core.utils.BitmapTextUtil;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.Effect;
import tonegod.gui.listeners.KeyboardListener;
import tonegod.gui.listeners.MouseButtonListener;
import tonegod.gui.listeners.MouseFocusListener;
import tonegod.gui.listeners.TabFocusListener;
import tonegod.gui.style.Style;

/**
 *
 * @author t0neg0d
 */
public abstract class Button extends Element implements Control, MouseButtonListener, MouseFocusListener, KeyboardListener, TabFocusListener {
	protected String hoverSound, pressedSound;
	protected boolean useHoverSound, usePressedSound;
	protected float hoverSoundVolume, pressedSoundVolume;
	protected Element icon;
	protected Texture hoverImg = null, pressedImg = null;
	protected ColorRGBA hoverFontColor = null, pressedFontColor = null;
	protected boolean isToggleButton = false;
	protected boolean isToggled = false;
	private Spatial spatial;
	protected boolean isStillPressed = false;
	private boolean useInterval = false;
	private float intervalsPerSecond = 4;
	protected float trackInterval = (4/1000), currentTrack = 0;
	protected boolean initClickPause = false;
	protected float initClickInterval = 0.25f, currentInitClickTrack = 0;
	protected RadioButtonGroup radioButtonGroup = null;
	protected boolean isRadioButton = false;
	protected ColorRGBA originalFontColor;
	protected Vector2f hoverImgOffset, pressedImgOffset;
	// Optional LabelElement
	protected boolean useOptionalLabel = false;
	protected TextElement buttonLabel;
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public Button(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Button").getVector2f("defaultSize"),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public Button(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Button").getVector2f("defaultSize"),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Button(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Button
	 */
	public Button(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg);
	}
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public Button(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Button").getVector2f("defaultSize"),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Button(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Button
	 */
	public Button(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.setScaleNS(false);
		this.setScaleEW(false);
		this.setFontSize(screen.getStyle("Button").getFloat("fontSize"));
		this.setFontColor(screen.getStyle("Button").getColorRGBA("fontColor"));
		this.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Button").getString("textVAlign")));
		this.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Button").getString("textAlign")));
		this.setTextWrap(LineWrapMode.valueOf(screen.getStyle("Button").getString("textWrap")));
		setTextPaddingByKey("Button","textPadding");
		
		if (getUseLocalTexture()) {
		//	setColorMap(screen.getStyle("Button").getString("defaultImg"));
			boolean tile = false;
			try {
				tile = screen.getStyle("Button").getBoolean("tileImages");
			} catch (Exception ex) {  }
			this.setTileImage(tile);
		}
		
		this.setMinDimensions(dimensions.clone());
		
		if (screen.getStyle("Button").getString("hoverImg") != null) {
			setButtonHoverInfo(
				screen.getStyle("Button").getString("hoverImg"),
				screen.getStyle("Button").getColorRGBA("hoverColor")
			);
		}
		if (screen.getStyle("Button").getString("pressedImg") != null) {
			setButtonPressedInfo(
				screen.getStyle("Button").getString("pressedImg"),
				screen.getStyle("Button").getColorRGBA("pressedColor")
			);
		}
		
		originalFontColor = fontColor.clone();
		
		hoverSound = screen.getStyle("Button").getString("hoverSound");
		useHoverSound = screen.getStyle("Button").getBoolean("useHoverSound");
		hoverSoundVolume = screen.getStyle("Button").getFloat("hoverSoundVolume");
		pressedSound = screen.getStyle("Button").getString("pressedSound");
		usePressedSound = screen.getStyle("Button").getBoolean("usePressedSound");
		pressedSoundVolume = screen.getStyle("Button").getFloat("pressedSoundVolume");
		
		populateEffects("Button");
		if (Screen.isAndroid()) {
			removeEffect(Effect.EffectEvent.Hover);
			removeEffect(Effect.EffectEvent.TabFocus);
			removeEffect(Effect.EffectEvent.LoseTabFocus);
		}
		
		buttonLabel = new TextElement(screen, Vector2f.ZERO, getDimensions(), screen.getDefaultGUIFont()) {
			@Override
			public void onUpdate(float tpf) {  }
			@Override
			public void onEffectStart() {  }
			@Override
			public void onEffectStop() {  }
		};
		buttonLabel.setIgnoreMouse(true);
		buttonLabel.setIsResizable(false);
		buttonLabel.setIsMovable(false);
		buttonLabel.setScaleEW(true);
		buttonLabel.setScaleNS(true);
		buttonLabel.setFontSize(fontSize);
		buttonLabel.setFontColor(fontColor);
		buttonLabel.setUseTextClipping(true);
		buttonLabel.setTextAlign(textAlign);
		buttonLabel.setTextVAlign(textVAlign);
		buttonLabel.setDocking(Docking.SW);
		buttonLabel.addClippingLayer(this);
		
		/*
		String defaultIcon = screen.getStyle("Button").getString("defaultIcon");
		if (defaultIcon != null) {
			Vector2f size = getDimensions();
			try {
				size.set(screen.getStyle("Button").getVector2f("defaultIconSize"));
			} catch (Exception ex) {  }
			setButtonIcon(size.x, size.y,defaultIcon);
		}
		*/
	}
	
	@Override
	public void controlIsEnabledHook(boolean isEnabled) {
		if (!isEnabled) {
			runPressedEffect(false);
		} else {
			runLoseFocusEffect();
		}
	}
	
	/**
	 * Clears current hover and pressed images set by Style defines
	 */
	public void clearAltImages() {
		setButtonHoverInfo(null, null);
		setButtonPressedInfo(null, null);
	}
	
	/**
	 * Sets if the button is to interact as a Toggle Button
	 * Click once to activate / Click once to deactivate
	 * 
	 * @param isToggleButton boolean
	 */
	public void setIsToggleButton(boolean isToggleButton) {
		this.isToggleButton = isToggleButton;
	}
	
	/**
	 * Returns if the Button is flagged as a Toggle Button
	 * 
	 * @return boolean isToggleButton
	 */
	public boolean getIsToggleButton() {
		return this.isToggleButton;
	}
	
	/**
	 * Sets if the button is to interact as a Radio Button
	 * Click once to activate - stays active
	 * 
	 * @param isRadioButton boolean
	 */
	public void setIsRadioButton(boolean isRadioButton) {
		this.isRadioButton = isRadioButton;
	}
	
	/**
	 * Returns if the Button is flagged as a Toggle Button
	 * 
	 * @return boolean isRadioButton
	 */
	public boolean getIsRadioButton() {
		return this.isRadioButton;
	}
	
	/**
	 * Set a toggle button state to toggled/untoggled
	 * @param isToggled boolean
	 */
	public void setIsToggledNoCallback(boolean isToggled) {
		this.isToggled = isToggled;
		
		if (pressedImg != null && isToggled) {
			runPressedEffect(false);
		} else {
			runResetEffect();
		}
		
		if (radioButtonGroup != null) {
			if (isToggled)
				radioButtonGroup.setSelected(this);
		}
	}
	
	/**
	 * Set a toggle button state to toggled/untoggled and calls the user left mouse button event methods
	 * @param isToggled boolean
	 */
	public void setIsToggled(boolean isToggled) {
		this.isToggled = isToggled;
		
		if (pressedImg != null && isToggled) {
			runPressedEffect(false);
		} else {
			runResetEffect();
		}
		
		MouseButtonEvent evtd = new MouseButtonEvent(0,true,0,0);
		MouseButtonEvent evtu = new MouseButtonEvent(0,false,0,0);
		onButtonMouseLeftDown(evtd, isToggled);
		onButtonMouseLeftUp(evtu, isToggled);
		if (radioButtonGroup != null) {
			if (isToggled)
				radioButtonGroup.setSelected(this);
		}
		evtu.setConsumed();
		evtd.setConsumed();
	}
	
	/**
	 * Returns the current toggle state of the Button if the Button has been flagged as 
	 * isToggle
	 * @return boolean isToggle
	 */
	public boolean getIsToggled() {
		return this.isToggled;
	}
	
	/**
	 * Sets the texture image path and color to use when the button has mouse focus
	 * @param pathHoverImg String path to image for mouse focus event
	 * @param hoverFontColor ColorRGBA to use for mouse focus event
	 */
	public final void setButtonHoverInfo(String pathHoverImg, ColorRGBA hoverFontColor) {
		if (pathHoverImg != null) {
			if ((!screen.getUseTextureAtlas() && !getUseLocalAtlas()) || getUseLocalTexture()) {
				try {
					this.hoverImg = app.getAssetManager().loadTexture(pathHoverImg);
					this.hoverImg.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
					this.hoverImg.setMagFilter(Texture.MagFilter.Nearest);
					this.hoverImg.setWrap(Texture.WrapMode.Repeat);
				} catch (Exception ex) {  }
			} else {
				this.hoverImg = this.getElementTexture();
				hoverImgOffset = getAtlasTextureOffset(screen.parseAtlasCoords(pathHoverImg));
			}
		} else {
			this.hoverImg = null;
		}
		if (hoverFontColor != null) {
			this.hoverFontColor = hoverFontColor;
		} else {
			this.hoverFontColor = null;
		}
	}
	
	/**
	 * Returns the Texture used when button has mouse focus
	 * 
	 * @return Texture
	 */
	public Texture getButtonHoverImg() {
		return this.hoverImg;
	}
	
	protected void runHoverEffect(boolean audio) {
		if (hoverImg != null) {
			Effect effect = getEffect(Effect.EffectEvent.Hover);
			if (effect != null) {
				if (useHoverSound && screen.getUseUIAudio() && audio) {
					effect.setAudioFile(hoverSound);
					effect.setAudioVolume(hoverSoundVolume);
				}
				effect.setBlendImage(hoverImg);
				if ((screen.getUseTextureAtlas() || getUseLocalAtlas()) && !getUseLocalTexture()) effect.setBlendImageOffset(hoverImgOffset);
				screen.getEffectManager().applyEffect(effect);
			}
		}
		if (hoverFontColor != null) {
			if (!useOptionalLabel)
				setFontColor(hoverFontColor);
			else
				buttonLabel.setFontColor(hoverFontColor);
		}
	}
	
	/**
	 * Sets the image and font color to use when the button is depressed
	 * @param pathPressedImg Path to the image for pressed state
	 * @param pressedFontColor ColorRGBA to use for pressed state
	 */
	public final void setButtonPressedInfo(String pathPressedImg, ColorRGBA pressedFontColor) {
		if (pathPressedImg != null) {
			if ((!screen.getUseTextureAtlas() && !getUseLocalAtlas()) || getUseLocalTexture()) {
				try {
					this.pressedImg = app.getAssetManager().loadTexture(pathPressedImg);
					this.pressedImg.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
					this.pressedImg.setMagFilter(Texture.MagFilter.Nearest);
					this.pressedImg.setWrap(Texture.WrapMode.Repeat);
				} catch (Exception ex) {  }
			} else {
				this.pressedImg = this.getElementTexture();
				pressedImgOffset = getAtlasTextureOffset(screen.parseAtlasCoords(pathPressedImg));
			}
		} else {
			this.pressedImg = null;
		}
		if (pressedFontColor != null) {
			this.pressedFontColor = pressedFontColor;
		} else {
			this.pressedFontColor = null;
		}
	}
	
	/**
	 * Returns the texture to be used when the button is depressed
	 * 
	 * @return Texture
	 */
	public Texture getButtonPressedImg() {
		return this.pressedImg;
	}
	
	protected void runPressedEffect(boolean audio) {
		if (pressedImg != null) {
			Effect effect = getEffect(Effect.EffectEvent.Press);
			if (effect != null) {
				if (usePressedSound && screen.getUseUIAudio() && audio) {
					effect.setAudioFile(pressedSound);
					effect.setAudioVolume(pressedSoundVolume);
				}
				effect.setBlendImage(pressedImg);
				if ((screen.getUseTextureAtlas() || getUseLocalAtlas()) && !getUseLocalTexture()) effect.setBlendImageOffset(pressedImgOffset);
				screen.getEffectManager().applyEffect(effect);
			}
		}
		if (pressedFontColor != null) {
			if (!useOptionalLabel)
				setFontColor(pressedFontColor);
			else
				buttonLabel.setFontColor(pressedFontColor);
		}
	}
	
	protected void runLoseFocusEffect() {
		Effect effect = getEffect(Effect.EffectEvent.LoseFocus);
		if (effect != null) {
			effect.setBlendImage(getElementTexture());
			if ((screen.getUseTextureAtlas() || getUseLocalAtlas()) && !getUseLocalTexture()) effect.setBlendImageOffset(new Vector2f(0,0));
			screen.getEffectManager().applyEffect(effect);
		}
		if (originalFontColor != null) {
			if (!useOptionalLabel)
				setFontColor(originalFontColor);
			else
				buttonLabel.setFontColor(originalFontColor);
		}
	}
	
	protected void runResetEffect() {
		Effect effect = getEffect(Effect.EffectEvent.Press);
		if (effect != null) {
			effect.setBlendImage(getElementTexture());
			if ((screen.getUseTextureAtlas() || getUseLocalAtlas()) && !getUseLocalTexture()) effect.setBlendImageOffset(new Vector2f(0,0));
			screen.getEffectManager().applyEffect(effect);
		}
		if (originalFontColor != null) {
			if (!useOptionalLabel)
				setFontColor(originalFontColor);
			else
				buttonLabel.setFontColor(originalFontColor);
		}
	}
	
	/**
	 * If called, an overlay icon is added to the button.  This icon is centered by default
	 * 
	 * @param width width to display icon
	 * @param height to display icon
	 * @param texturePath The path of the image to use as the icon overlay
	 */
	public final void setButtonIcon(float width, float height, String texturePath) {
		/*
		if (icon != null) {
			if (icon.getParent() != null) {
				elementChildren.remove(icon.getUID());
				icon.removeFromParent();
			}
		}
		*/
		if (icon == null) {
			icon = new Element(
				screen,
				this.getUID() + ":btnIcon",
				new Vector2f((getWidth()/2)-(width/2),(getHeight()/2)-(height/2)),
				new Vector2f(width,height),
				new Vector4f(0,0,0,0),
				texturePath
			);
			icon.setIgnoreMouse(true);
			icon.setDocking(Docking.SW);
			icon.setScaleEW(false);
			icon.setScaleNS(false);
			this.addChild(icon);
		}
		if (screen.getUseTextureAtlas() || this.getUseLocalAtlas())
			icon.setTextureAtlasImage(icon.getElementTexture(), texturePath);
	}
	
	public Element getButtonIcon() {
		return this.icon;
	}
	
	@Override
	public void onMouseLeftPressed(MouseButtonEvent evt) {
		if (isEnabled) {
			if (isToggleButton) {
				if (isToggled) {
					if (!isRadioButton) isToggled = false;
				} else {
					isToggled = true;
				}
			}
			runPressedEffect(true);
			isStillPressed = true;
			initClickPause = true;
			currentInitClickTrack = 0;
			onButtonMouseLeftDown(evt, isToggled);
		}
		evt.setConsumed();
	}

	@Override
	public void onMouseLeftReleased(MouseButtonEvent evt) {
		if (isEnabled) {
			if (!isToggleButton) {
				if (getHasFocus()) {
					runLoseFocusEffect();
					runHoverEffect(false);
				} else {
					runLoseFocusEffect();
				}
			} else {
				if (!isToggled) {
					runLoseFocusEffect();
					runHoverEffect(false);
				}
			}
			isStillPressed = false;
			initClickPause = false;
			currentInitClickTrack = 0;
			onButtonMouseLeftUp(evt, isToggled);
			if (radioButtonGroup != null)
				radioButtonGroup.setSelected(this);
		}
		evt.setConsumed();
	}
	
	public void setRadioButtonGroup(RadioButtonGroup radioButtonGroup) {
		this.radioButtonGroup = radioButtonGroup;
		this.isToggleButton = true;
		this.isRadioButton = true;
	}
	
	@Override
	public void onMouseRightPressed(MouseButtonEvent evt) {
		if (isEnabled) {
			onButtonMouseRightDown(evt, isToggled);
			if (screen.getUseToolTips()) {
				
			}
		}
		evt.setConsumed();
	}

	@Override
	public void onMouseRightReleased(MouseButtonEvent evt) {
		if (isEnabled) {
			onButtonMouseRightUp(evt, isToggled);
		}
		evt.setConsumed();
	}

	@Override
	public void onGetFocus(MouseMotionEvent evt) {
		if (isEnabled) {
			if (!getHasFocus()) {
				if (!isToggled) {
					runHoverEffect(true);
				}
				screen.setCursor(CursorType.HAND);
				onButtonFocus(evt);
				if (screen.getUseToolTips()) {
					
				}
			}
			setHasFocus(true);
		}
	}

	@Override
	public void onLoseFocus(MouseMotionEvent evt) {
		if (isEnabled) {
			if (getHasFocus()) {
				if (!isToggled) {
					runLoseFocusEffect();
				}
				screen.setCursor(CursorType.POINTER);
				onButtonLostFocus(evt);
			}
			setHasFocus(false);
		}
	}
	
	/**
	 * Enables/disbale hover effect sound
	 * @param useHoverSound 
	 */
	public void setUseButtonHoverSound(boolean useHoverSound) {
		this.useHoverSound = useHoverSound;
	}
	
	/**
	 * Enable/disable pressed effect sound
	 * @param usePressedSound 
	 */
	public void setUseButtonPressedSound(boolean usePressedSound) {
		this.usePressedSound = usePressedSound;
	}
	
	public abstract void onButtonMouseLeftDown(MouseButtonEvent evt, boolean toggled);
	public abstract void onButtonMouseRightDown(MouseButtonEvent evt, boolean toggled);
	public abstract void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled);
	public abstract void onButtonMouseRightUp(MouseButtonEvent evt, boolean toggled);
	public abstract void onButtonFocus(MouseMotionEvent evt);
	public abstract void onButtonLostFocus(MouseMotionEvent evt);
	/**
	 * Abstract method for handling interval updates while the button is still pressed
	 * 
	 * NOTE: This is only called if the button's setInterval method has been previously called
	 */
	public void onButtonStillPressedInterval() {  }
	
	/**
	 * Returns if the button is still pressed
	 * @return boolean
	 */
	public boolean getIsStillPressed() {
		return this.isStillPressed;
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return this;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void render(RenderManager rm, ViewPort vp) {
		
	}
	
	@Override
	public void update(float tpf) {
		if (isEnabled) {
			if (useInterval && isStillPressed) {
				if (initClickPause) {
					currentInitClickTrack += tpf;
					if (currentInitClickTrack >= initClickInterval) {
						initClickPause = false;
						currentInitClickTrack = 0;
					}
				} else {
					currentTrack += tpf;
					if (currentTrack >= trackInterval) {
						onButtonStillPressedInterval();
						currentTrack = 0;
					}
				}
			}
		}
	}
	
	/**
	 * This method registers the button as a JME Control creating an interval event to be
	 * processed every time the interval limit has been reached.
	 * 
	 * See onButtonStillPressedInterval()
	 * 
	 * @param intervalsPerSecond The number of calls to onButtonStillPressedInterval per second.
	 */
	public void setInterval(float intervalsPerSecond) {
		if (intervalsPerSecond > 0) {
			this.useInterval = true;
			this.intervalsPerSecond = intervalsPerSecond;
			this.trackInterval = (float)(1/intervalsPerSecond);
			this.addControl(this);
		} else {
			this.useInterval = false;
			this.intervalsPerSecond = intervalsPerSecond;
			this.removeControl(Button.class);
		}
	}
	
	// Tab focus
	@Override
	public void setTabFocus() {
		screen.setKeyboardElement(this);
		if (isEnabled) {
			Effect effect = getEffect(Effect.EffectEvent.TabFocus);
			if (effect != null) {
				effect.setColor(ColorRGBA.DarkGray);
				screen.getEffectManager().applyEffect(effect);
			}
		}
	}
	
	@Override
	public void resetTabFocus() {
		screen.setKeyboardElement(null);
		if (isEnabled) {
			if (!getIsToggled()) {
				Effect effect = getEffect(Effect.EffectEvent.LoseTabFocus);
				if (effect != null) {
					effect.setColor(ColorRGBA.White);
					screen.getEffectManager().applyEffect(effect);
				}
			}
		}
	}
	
	// Default keyboard interaction
	@Override
	public void onKeyPress(KeyInputEvent evt) {
		if (evt.getKeyCode() == KeyInput.KEY_SPACE) {
			if (isEnabled && getParent() != null)
				onMouseLeftPressed(new MouseButtonEvent(0,true,0,0));
		}
	}
	
	@Override
	public void onKeyRelease(KeyInputEvent evt) {
		if (evt.getKeyCode() == KeyInput.KEY_SPACE) {
			if (isEnabled && getParent() != null)
				onMouseLeftReleased(new MouseButtonEvent(0,false,0,0));
		}
	}
	
	@Override
	public void setText(String text) {
		this.text = text;
		if (textElement == null) {
			textElement = new BitmapText(font, false);
			textElement.setBox(new Rectangle(0,0,getDimensions().x,getDimensions().y));
		//	textElement = new TextElement(screen, Vector2f.ZERO);
		}
		textElement.setLineWrapMode(textWrap);
		textElement.setAlignment(textAlign);
		textElement.setVerticalAlignment(textVAlign);
		textElement.setSize(fontSize);
		textElement.setColor(fontColor);
		
		if (textVAlign == BitmapFont.VAlign.Center) {
			textElement.setVerticalAlignment(BitmapFont.VAlign.Top);
			centerTextVertically(text);
		}
		
		textElement.setText(text);
		updateTextElement();
		if (textElement.getParent() == null) {
			this.attachChild(textElement);
		}
	}
	
	public void setLabelText(String text) {
		this.useOptionalLabel = true;
	//	buttonLabel.setSizeToText(true);
		buttonLabel.setDimensions(getDimensions());
		buttonLabel.getAnimText().setBounds(getDimensions());
		buttonLabel.setText(text);
		if (buttonLabel.getParent() == null) {
			addChild(buttonLabel);
		}
	}
	
	public void setFontColor(ColorRGBA fontColor, boolean makeDefault) {
		if (!useOptionalLabel)
			super.setFontColor(fontColor);
		else
			buttonLabel.setFontColor(fontColor);
		if (makeDefault)
			originalFontColor = fontColor.clone();
	}
	
	public void setStyles(String styleName) {
		Style style = screen.getStyle(styleName);

		// images and state colours
		String img = style.getString("defaultImg");
		
		if (img != null)
			setColorMap(img);
		if (style.getString("hoverImg") != null) {
			setButtonHoverInfo(
				style.getString("hoverImg"),
				style.getColorRGBA("hoverColor"));
		}
		if (style.getString("pressedImg") != null) {
			setButtonPressedInfo(
				style.getString("pressedImg"),
				style.getColorRGBA("pressedColor"));
		}

		// fonts and text
		setFontSize(style.getFloat("fontSize"));
	//	setFont((Screen)screen.getDefaultGUIFont());
		setFontColor(style.getColorRGBA("fontColor"));
		setTextVAlign(BitmapFont.VAlign.valueOf(style.getString("textVAlign")));
		setTextAlign(BitmapFont.Align.valueOf(style.getString("textAlign")));
		setTextWrap(LineWrapMode.valueOf(style.getString("textWrap")));
		setTextPaddingByKey("Button","textPadding");
	//	buttonTextInsets = style.getFloat("buttonTextInsets");

		// borders
		borders.set(style.getVector4f("resizeBorders"));

		// audio
		hoverSound = style.getString("hoverSound");
		useHoverSound = style.getBoolean("useHoverSound");
		hoverSoundVolume = style.getFloat("hoverSoundVolume");
		pressedSound = style.getString("pressedSound");
		usePressedSound = style.getBoolean("usePressedSound");
		pressedSoundVolume = style.getFloat("pressedSoundVolume");

		// Fx
		populateEffects(styleName);
		if (Screen.isAndroid()) {
			removeEffect(Effect.EffectEvent.Hover);
			removeEffect(Effect.EffectEvent.TabFocus);
			removeEffect(Effect.EffectEvent.LoseTabFocus);
		}

	//	defaultSize = style.getVector2f("defaultSize");
		setDimensions(getOrgDimensions());
		getModel().updateDimensions(getWidth(), getHeight());

		// TODO
		rebuildModel();
		
		originalFontColor = fontColor.clone();
    }
	
	/**
	 * Fix for BitmapFont.VAlign
	 * @param text 
	 */
	private void centerTextVertically(String text) {
		float height = BitmapTextUtil.getTextLineHeight(this, text);
		setTextPosition(getTextPosition().x, getHeight()/2-((height-(height*.1f))/2));
	}
}
