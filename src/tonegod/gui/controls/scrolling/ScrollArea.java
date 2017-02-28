package tonegod.gui.controls.scrolling;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.menuing.Menu;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.framework.animation.Interpolation;
import tonegod.gui.framework.core.util.GameTimer;
import tonegod.gui.listeners.FlingListener;
import tonegod.gui.listeners.MouseWheelListener;
import tonegod.gui.listeners.TouchListener;

/**
 *
 * @author t0neg0d
 */
public class ScrollArea extends Element implements MouseWheelListener, TouchListener, FlingListener {
	protected Element scrollableArea;
	private boolean isTextOnly = true;
	private boolean isScrollable = true;
	private VScrollBar vScrollBar;
	protected float scrollSize;
	private boolean scrollHidden = false;
    protected float scrollBarGap = 0;
	protected GameTimer flingTimer;
	private boolean flingEnabled = true;
	float flingSpeed = 0;
	boolean flingDir = true;
	float touchOffsetY = 0;
	float touchStartY = 0;
	float touchEndY = 0;
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollArea(ElementManager screen, boolean isTextOnly) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg"),
			isTextOnly
		);
	}
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollArea(ElementManager screen, Vector2f position, boolean isTextOnly) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg"),
			isTextOnly
		);
	}
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollArea(ElementManager screen, Vector2f position, Vector2f dimensions, boolean isTextOnly) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg"),
			isTextOnly
		);
	}
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollArea(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, boolean isTextOnly) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg, isTextOnly);
	}
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollArea(ElementManager screen, String UID, Vector2f position, boolean isTextOnly) {
		this(screen, UID, position,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg"),
			isTextOnly
		);
	}
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollArea(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, boolean isTextOnly) {
		this(screen, UID, position, dimensions,
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg"),
			isTextOnly
		);
	}
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollArea (ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, boolean isTextOnly) {
		super (screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.isTextOnly = isTextOnly;
		
		setIgnoreMouseWheelMove(false);
		
		// Load default font info
		setFontColor(screen.getStyle("ScrollArea").getColorRGBA("fontColor"));
		setFontSize(screen.getStyle("ScrollArea").getFloat("fontSize"));
		setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("ScrollArea").getString("textAlign")));
		setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("ScrollArea").getString("textVAlign")));
		setTextWrap(LineWrapMode.valueOf(screen.getStyle("ScrollArea").getString("textWrap")));
		
		scrollBarGap = screen.getStyle("ScrollArea#VScrollBar").getFloat("gap");
		scrollSize = screen.getStyle("ScrollArea#VScrollBar").getFloat("defaultControlSize");
		
	//	orgWidth = getWidth();
		
		if (!(this instanceof Menu)) setWidth(getWidth()-scrollSize);
		
		if (!isTextOnly) {
			createScrollableArea();
		} else {
			setTextPaddingByKey("ScrollArea","textPadding");
			setTextClipPaddingByKey("ScrollArea","textPadding");
			setText(" ");
		}
		
		vScrollBar = new VScrollBar(screen, UID + ":vScroll",
			new Vector2f(getWidth() + scrollBarGap, 0),
			new Vector2f(scrollSize, getHeight())
		);
		
		addChild(vScrollBar);
		
		setVScrollBar(vScrollBar);
		
		flingTimer = new GameTimer() {
			@Override
			public void timerUpdateHook(float tpf) {
				float currentY = getScrollablePosition();
				float nextInc = 15*flingSpeed*(1f-this.getPercentComplete());
				
				if (flingDir) {
					float nextY = currentY+nextInc;
					if (nextY <= getScrollableHeight() && nextY >= getHeight()-getPadding()) {
						scrollYTo(nextY);
						vScrollBar.setThumbByPosition();
					}
				} else {
					float nextY = currentY-nextInc;
					if (nextY <= getScrollableHeight() && nextY >= getHeight()-getPadding()) {
						scrollYTo(nextY);
						vScrollBar.setThumbByPosition();
					}
				}
			}
			@Override
			public void onComplete(float time) {
				
			}
		};
		flingTimer.setInterpolation(Interpolation.exp5Out);
	}
	
	public void setScrollBarWidth(float width) {
		this.scrollSize = width;
		this.getVScrollBar().setWidth(width);
		this.getVScrollBar().getScrollTrack().setWidth(width);
		this.getVScrollBar().getScrollTrack().setHeight(getHeight()-(width*2));
		this.getVScrollBar().getScrollTrack().setY(width);
		this.getVScrollBar().getScrollButtonUp().setDimensions(width, width);
		this.getVScrollBar().getScrollButtonUp().setY(getHeight()-width);
		this.getVScrollBar().getScrollThumb().setWidth(width);
		this.getVScrollBar().getScrollButtonDown().setDimensions(width, width);
		
		this.getVScrollBar().getScrollButtonUp().getButtonIcon().centerToParent();
		this.getVScrollBar().getScrollButtonDown().getButtonIcon().centerToParent();
		
		if (getVScrollBar().getIsVisible())
			this.setWidth(this.orgDimensions.x-width);
		else
			this.setWidth(this.orgDimensions.x);
	//	this.adjustWidthForScroll();
	}
	
	private void createScrollableArea() {
		scrollableArea = new Element(screen, getUID() + ":scrollable", new Vector2f(0, 0), new Vector2f(getWidth(), 25), new Vector4f(14,14,14,14), null);
		scrollableArea.setIsResizable(false);
		scrollableArea.setIsMovable(false);
		scrollableArea.setScaleEW(true);
		scrollableArea.setScaleNS(false);
		scrollableArea.setDocking(Docking.NW);
		
		// Load default font info
		scrollableArea.setFontColor(screen.getStyle("ScrollArea").getColorRGBA("fontColor"));
		scrollableArea.setFontSize(screen.getStyle("ScrollArea").getFloat("fontSize"));
		scrollableArea.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("ScrollArea").getString("textAlign")));
		scrollableArea.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("ScrollArea").getString("textVAlign")));
		scrollableArea.setTextWrap(LineWrapMode.valueOf(screen.getStyle("ScrollArea").getString("textWrap")));
		scrollableArea.setTextPaddingByKey("ScrollArea","textPadding");
		scrollableArea.setTextClipPaddingByKey("ScrollArea","textPadding");
		
		scrollableArea.addClippingLayer(this);
	//	scrollableArea.setTextClipPadding(screen.getStyle("ScrollArea").getFloat("scrollAreaPadding"));
		
		this.addChild(scrollableArea);
	}
	
	/**
	 * Returns the Element that was created as a scrollable area for ScrollArea NOT flagged as isTextOnly
	 * @return Element
	 */
	public Element getScrollableArea() {
		return this.scrollableArea;
	}
	
	/**
	 * Returns if the ScrollArea is text only
	 * @return boolean
	 */
	public boolean getIsTextOnly() {
		return isTextOnly;
	}
	
	public void setFlingEnabled(boolean enabled) {
		this.flingEnabled = enabled;
	}
	
	public boolean getFlingEnabled() { return this.flingEnabled; }
	
	private void setVScrollBar(VScrollBar vScrollBar) {
		this.vScrollBar = vScrollBar;
		vScrollBar.setScrollableArea(this);
	}
	
	/**
	 * Returns the Vertical Scroll Bar
	 * @return VScrollBar
	 */
	public VScrollBar getVScrollBar() {
		return this.vScrollBar;
	}
	
	/**
	 * Adds an Element as a child to the ScrollArea.  This is usable by ScrollAreas NOT flagged for isTextOnly
	 * @param child Element
	 */
	public void addScrollableChild(Element child) {
		scrollableArea.addChild(child);
		child.setIgnoreMouseWheel(true);
	}
	
	/**
	 * Sets the padding for the ScrollArea
	 * @param padding float 
	 */
	public void setPadding(float padding) {
		if (isTextOnly) {
			setTextPadding(padding);
			setTextClipPadding(padding);
		} else {
			scrollableArea.setTextPadding(padding);
			scrollableArea.setTextClipPadding(padding);
		}
	}
	
	/**
	 * Returns the padding used for the ScollArea
	 * @return float
	 */
	public float getPadding() {
		if (isTextOnly) {
			return getTextPadding();
		} else {
			return scrollableArea.getTextPadding();
		}
	}
	
	/**
	 * Returns the current height of the scrollable area
	 * @return float
	 */
	public float getScrollableHeight() {
		if (isTextOnly) {
			return textElement.getHeight()+getTextPaddingVec().z+getTextPaddingVec().w;
		} else {
			return scrollableArea.getHeight()+(scrollableArea.getTextPadding()*2);
		}
	}
	
	/**
	 * Returns the current y position of the scrollable area
	 * @return float
	 */
	public float getScrollablePosition() {
		if (isTextOnly) {
			return textElement.getLocalTranslation().y;
		} else {
			return scrollableArea.getY();
		}
	}
	
	@Override
	public void controlResizeHook() {
		if (vScrollBar != null) {
			vScrollBar.setThumbScale();
		}
		adjustWidthForScroll();
		if (scrollableArea != null)
			if (scrollableArea.getY() > getResizeBorderNorthSize() && getScrollableHeight()+getResizeBorderNorthSize() > getHeight())
				scrollToBottom();
	}
	
	@Override
	public void setControlClippingLayer(Element clippingLayer) {
		for (Element el : elementChildren.values()) {
			el.setControlClippingLayer(clippingLayer);
		}
	}
	
	/**
	 * Internal use - Used to readjust the width of the scrollarea when hiding/showing scroll bars
	 */
	public final void adjustWidthForScroll() {
		if (vScrollBar.getParent() == null && !scrollHidden) {
			setWidth(getWidth()+vScrollBar.getWidth() + scrollBarGap);
			scrollHidden = true;
		} else if (vScrollBar.getParent() != null && scrollHidden) {
			setWidth(getWidth()-vScrollBar.getWidth() - scrollBarGap);
			vScrollBar.setX(getWidth() + scrollBarGap);
			scrollHidden = false;
		}
        onAdjustWidthForScroll();
	}
	
	/**
	 * Scrolls the scrollbar thumb to the specified Y coordinate
	 * @param y desired Y-coordinate
	 */
	public void scrollThumbYTo(float y) {
		adjustWidthForScroll();
		vScrollBar.scrollYTo(y);
	}
	
	/**
	 * Scrolls the Scrollable Area to the specified Y coordinate
	 * @param y desired Y-coordinate
	 */
	public void scrollYTo(float y) {
		adjustWidthForScroll();
		if (scrollableArea == null) {
			textElement.setLocalTranslation(textElement.getLocalTranslation().setY(y));
		} else {
			scrollableArea.setY(0);
			scrollableArea.setY(y);
		}
		controlScrollHook();
	}
	
	/**
	 * Overridable method for hooking the scroll event
	 */
	public void controlScrollHook() {  }
	
	/**
	 * To be used with interval calls.  Scrolls the Scrollable Area by the provided value
	 * @param yInc float
	 */
	public void scrollYBy(float yInc) {
		adjustWidthForScroll();
		if (scrollableArea == null) {
			float nextY = textElement.getLocalTranslation().getY() + yInc;
			textElement.setLocalTranslation(textElement.getLocalTranslation().setY(yInc));
		} else {
			scrollableArea.setY(scrollableArea.getY()+yInc);
		}
	}
	
	/**
	 * Scrolls to the bottom of the Scrollable Area
	 */
	public void scrollToBottom() {
		adjustWidthForScroll();
		vScrollBar.scrollToBottom();
	}
	
	/**
	 * Scrolls to the top of the Scrollable Area
	 */
	public void scrollToTop() {
		adjustWidthForScroll();
		vScrollBar.scrollToTop();
	}

	@Override
	public void onMouseWheelPressed(MouseButtonEvent evt) {
		evt.setConsumed();}
	@Override
	public void onMouseWheelReleased(MouseButtonEvent evt) {
		evt.setConsumed();}
	@Override
	public void onMouseWheelUp(MouseMotionEvent evt) {
		if (vScrollBar != null) {
			vScrollBar.scrollByYInc(-vScrollBar.getTrackInc());
		}
		evt.setConsumed();
	}
	@Override
	public void onMouseWheelDown(MouseMotionEvent evt) {
		if (vScrollBar != null) {
			vScrollBar.scrollByYInc(vScrollBar.getTrackInc());
		}
		evt.setConsumed();
	}
        
	protected void onAdjustWidthForScroll() {
		// Hook called when width adjusts because of scrollbar visibility
	}

	@Override
	public void onFling(TouchEvent evt) {
		if (flingEnabled && (evt.getDeltaY() > 0.2f || evt.getDeltaY() < -0.2f)) {
			if (!screen.getAnimManager().hasGameTimer(flingTimer)) {
				flingTimer.reset(false);
				flingDir  = (evt.getDeltaY() < 0) ? true : false;
				flingSpeed = FastMath.abs(evt.getDeltaY());
				screen.getAnimManager().addGameTimer(flingTimer);
			}
		}
	}

	@Override
	public void onTouchDown(TouchEvent evt) {
		if (screen.getAnimManager().hasGameTimer(flingTimer)) {
			flingTimer.endGameTimer();
			screen.getAnimManager().removeGameTimer(flingTimer);
		}
		if (flingEnabled) {
			touchStartY = getScrollablePosition();
			touchOffsetY = evt.getY()-touchStartY;
		}
	}

	@Override
	public void onTouchMove(TouchEvent evt) {
		if (flingEnabled) {
			float nextY = evt.getY()-touchOffsetY;
			if (nextY <= getScrollableHeight() && nextY >= getHeight()-(this.getPadding())) {
				scrollYTo(nextY);
				vScrollBar.setThumbByPosition();
				touchEndY = getScrollablePosition();
			}
		}
	}

	@Override
	public void onTouchUp(TouchEvent evt) {
		
	}
}
