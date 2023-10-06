/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.windows;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.layouts.Layout;
import tonegod.gui.core.utils.ControlUtil;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public class Window extends Element {
	protected Element dragBar;
	protected Element contentArea;
	protected ButtonAdapter close, collapse;
	private boolean useShowSound, useHideSound;
	private String showSound, hideSound;
	private float showSoundVolume, hideSoundVolume;
	protected Vector4f dbIndents = new Vector4f();
	private Window self;
	private boolean useClose = false, useCollapse = false, isCollapsed = false;
	private float winDif = 0;
	
	/**
	 * Creates a new instance of the Window control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public Window(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Window control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public Window(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Window control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Window(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Window control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Window
	 */
	public Window(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the Window control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public Window(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Window control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Window(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Window control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public Window(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		self = this;
		
		this.setIsResizable(true);
		this.setScaleNS(false);
		this.setScaleEW(false);
		this.setClipPadding(screen.getStyle("Window").getFloat("clipPadding"));
		this.setMinDimensions(screen.getStyle("Window").getVector2f("minSize"));
	//	this.setClippingLayer(this);
		
		dbIndents.set(screen.getStyle("Window#Dragbar").getVector4f("indents"));
		
		dragBar = new Element(screen, UID + ":DragBar",
			new Vector2f(dbIndents.y, dbIndents.x),
			new Vector2f(getWidth()-dbIndents.y-dbIndents.z, screen.getStyle("Window#Dragbar").getFloat("defaultControlSize")),
			screen.getStyle("Window#Dragbar").getVector4f("resizeBorders"),
			screen.getStyle("Window#Dragbar").getString("defaultImg")
		);
		dragBar.setFontSize(screen.getStyle("Window#Dragbar").getFloat("fontSize"));
		dragBar.setFontColor(screen.getStyle("Window#Dragbar").getColorRGBA("fontColor"));
		dragBar.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Window#Dragbar").getString("textAlign")));
		dragBar.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Window#Dragbar").getString("textVAlign")));
		dragBar.setTextPosition(0,0);
		dragBar.setTextPaddingByKey("Window#Dragbar","textPadding");
		dragBar.setTextWrap(LineWrapMode.valueOf(screen.getStyle("Window#Dragbar").getString("textWrap")));
		dragBar.setIsResizable(false);
		dragBar.setScaleEW(true);
		dragBar.setScaleNS(false);
		dragBar.setIsMovable(true);
		dragBar.setEffectParent(true);
		dragBar.addClippingLayer(this);
		
		addChild(dragBar);
		
		float buttonHeight = (dragBar.getHeight() <= 25) ? 18 : dragBar.getHeight()-6;
	//	buttonHeight -= 2;
		
		close = new ButtonAdapter(screen, Vector2f.ZERO, new Vector2f(buttonHeight,buttonHeight)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				self.hideWindow();
			}
		};
		close.setText("X");
		close.setDocking(Docking.SE);
		
		collapse = new ButtonAdapter(screen, Vector2f.ZERO, new Vector2f(buttonHeight,buttonHeight)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!isCollapsed) {
					isCollapsed = true;
					contentArea.hide();
					winDif = self.getHeight();
					self.setHeight(getDragBarHeight()+(dbIndents.y*2));
					winDif -= self.getHeight();
					dragBar.setY(self.getHeight()-dragBar.getHeight()-dbIndents.y);
					self.setY(self.getY()+winDif);
					setButtonIcon(getWidth(), getHeight(), screen.getStyle("Common").getString("arrowDown"));
					self.setResizeN(false);
					self.setResizeS(false);
				} else {
					isCollapsed = false;
					contentArea.show();
					self.setHeight(getDragBarHeight()+contentArea.getHeight()+dbIndents.y+2);
					dragBar.setY(self.getHeight()-dragBar.getHeight()-dbIndents.y);
					self.setY(self.getY()-winDif);setButtonIcon(getWidth(), getHeight(), screen.getStyle("Common").getString("arrowUp"));
					self.setResizeN(self.getIsResizable());
					self.setResizeS(self.getIsResizable());
				}
			}
		};
		collapse.setButtonIcon(collapse.getWidth(), collapse.getHeight(), screen.getStyle("Common").getString("arrowUp"));
		collapse.setDocking(Docking.SE);
		
		contentArea = ControlUtil.getContainer(screen);
		contentArea.setDimensions(dimensions.subtract(0, dragBar.getHeight()+dbIndents.y+2));
		contentArea.setPosition(0,dragBar.getHeight()+dbIndents.y);
		contentArea.setScaleEW(true);
		contentArea.setScaleNS(true);
		
		addChild(contentArea);
		
		showSound = screen.getStyle("Window").getString("showSound");
		useShowSound = screen.getStyle("Window").getBoolean("useShowSound");
		showSoundVolume = screen.getStyle("Window").getFloat("showSoundVolume");
		hideSound = screen.getStyle("Window").getString("hideSound");
		useHideSound = screen.getStyle("Window").getBoolean("useHideSound");
		hideSoundVolume = screen.getStyle("Window").getFloat("hideSoundVolume");
		
		populateEffects("Window");
	}
	
	/**
	 * Returns a pointer to the Element used as a window dragbar
	 * @return Element
	 */
	public Element getDragBar() {
		return this.dragBar;
	}
	
	/**
	 * Returns the drag bar height
	 * @return float
	 */
	public float getDragBarHeight() {
		return dragBar.getHeight();
	}
	
	/**
	 * Sets the Window title text
	 * @param title String
	 */
	public void setWindowTitle(String title) {
		dragBar.setText(title);
	}
	
	/**
	 * Shows the window using the default Show Effect
	 */
	public void showWindow() {
		Effect effect = getEffect(Effect.EffectEvent.Show);
		if (effect != null) {
			if (useShowSound && screen.getUseUIAudio()) {
				effect.setAudioFile(showSound);
				effect.setAudioVolume(showSoundVolume);
			}
			if (effect.getEffectType() == Effect.EffectType.FadeIn) {
				Effect clone = effect.clone();
				clone.setAudioFile(null);
				this.propagateEffect(clone, false);
			} else
				screen.getEffectManager().applyEffect(effect);
		} else
			this.show();
	}
	
	/**
	 * Hides the Window using the default Hide Effect
	 */
	public void hideWindow() {
		Effect effect = getEffect(Effect.EffectEvent.Hide);
		if (effect != null) {
			if (useHideSound && screen.getUseUIAudio()) {
				effect.setAudioFile(hideSound);
				effect.setAudioVolume(hideSoundVolume);
			}
			if (effect.getEffectType() == Effect.EffectType.FadeOut) {
				Effect clone = effect.clone();
				clone.setAudioFile(null);
				this.propagateEffect(clone, true);
			} else
				screen.getEffectManager().applyEffect(effect);
		} else
			this.hide();
	}
	
	/**
	 * Enables/disables the Window dragbar
	 * @param isMovable boolean
	 */
	public void setWindowIsMovable(boolean isMovable) {
		this.dragBar.setIsMovable(isMovable);
	}
	
	/**
	 * Returns if the Window dragbar is currently enabled/disabled
	 * @return boolean
	 */
	public boolean getWindowIsMovable() {
		return this.dragBar.getIsMovable();
	}
	
	public void addWindowContent(Element el) {
		contentArea.addChild(el);
//		contentArea.addClippingLayer(contentArea); // Cause issue when used with Button
	}
	
	public void removeWindowContent(Element el) {
		contentArea.removeChild(el);
	}
	
	public void setContentLayout(Layout layout) {
		contentArea.setLayout(layout);
	}
	
	public Element getContentArea() { return contentArea; }
	
	public void setUseCloseButton(boolean use) {
		if (use) {
			this.useClose = true;
			dragBar.addChild(close);
			close.centerToParentV();
			close.setX(dragBar.getWidth()-close.getWidth()-close.getY());
			if (useCollapse) {
				close.centerToParentV();
				close.setX(dragBar.getWidth()-close.getWidth()-collapse.getWidth()-collapse.getY()-5);
			}
		} else {
			this.useClose = false;
			dragBar.removeChild(close);
			if (useCollapse) {
				close.centerToParentV();
				close.setX(dragBar.getWidth()-collapse.getWidth()-collapse.getY());
			}
		}
	}
	
	public void setUseCollapseButton(boolean use) {
		if (use) {
			this.useCollapse = true;
			dragBar.addChild(collapse);
			collapse.centerToParentV();
			if (useClose)
				collapse.setX(dragBar.getWidth()-collapse.getWidth()-close.getWidth()-collapse.getY()-5);
			else
				collapse.setX(dragBar.getWidth()-collapse.getWidth()-collapse.getY());
		} else {
			this.useCollapse = false;
			dragBar.removeChild(collapse);
		}
	}
	
	public void sizeWindowToContent() {
		contentArea.sizeToContent();
		setDimensions(
			contentArea.getWidth()+(dbIndents.z),
			contentArea.getHeight()+getDragBarHeight()+(dbIndents.y+dbIndents.w)
		);
		dragBar.setY(dbIndents.y+contentArea.getHeight());
		dragBar.setWidth(getWidth()-(dbIndents.x+dbIndents.z));
	}
}
