package tonegod.gui.controls.scrolling;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
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
public class ScrollPanel extends Element {
	public static enum ScrollDirection {
		Up,
		Down,
		Left,
		Right
	}
	
	protected ScrollPanelBounds innerBounds;
	protected Element scrollableArea;
	protected ScrollPanelBarV vScrollBar;
	protected ScrollPanelBarH hScrollBar;
	private float scrollSize = 25;
	private int buttonInc = 1;
	private int trackInc = 10;
	private boolean verticalWrap = false;
	private boolean pagingEnabled = false;
	private boolean flingEnabled = true;
	private GameTimer flingTimer;
	private float touchStartY = 0;
	private float touchOffsetY = 0;
	private boolean flingDir = true;
	private float flingSpeed = 1;
	
	/**
	 * Creates a new instance of the ScrollPanel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public ScrollPanel(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollPanel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ScrollPanel(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollPanel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ScrollPanel(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollPanel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the ScrollPanel's background
	 */
	public ScrollPanel(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the ScrollPanel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ScrollPanel(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollPanel control
	 * 
	 * @param screen The screen control
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ScrollPanel(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollPanel control
	 * 
	 * @param screen The screen control
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the ScrollPanel's background
	 */
	public ScrollPanel(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, Vector4f.ZERO, null);
		setAsContainerOnly();
		
		scrollSize = screen.getStyle("ScrollArea#VScrollBar").getFloat("defaultControlSize");
		
		innerBounds = new ScrollPanelBounds(screen, UID + "innerBounds", Vector2f.ZERO, dimensions, resizeBorders, defaultImg);	
		innerBounds.setScaleEW(true);
		innerBounds.setScaleNS(true);
		innerBounds.setDocking(Docking.SW);
		
		scrollableArea = new Element(screen, UID + "scrollableArea", Vector2f.ZERO, dimensions, Vector4f.ZERO, null) {
			@Override
			public void setControlClippingLayer(Element clippingLayer) {
				for (Element el : elementChildren.values()) {
					el.setControlClippingLayer(clippingLayer);
				}
			}
		};
		
		scrollableArea.setScaleEW(false);
		scrollableArea.setScaleNS(false);
		scrollableArea.setDocking(Docking.NW);
		scrollableArea.setAsContainerOnly();
		
		scrollableArea.setFontColor(screen.getStyle("ScrollArea").getColorRGBA("fontColor"));
		scrollableArea.setFontSize(screen.getStyle("ScrollArea").getFloat("fontSize"));
		scrollableArea.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("ScrollArea").getString("textAlign")));
		scrollableArea.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("ScrollArea").getString("textVAlign")));
		scrollableArea.setTextWrap(LineWrapMode.valueOf(screen.getStyle("ScrollArea").getString("textWrap")));
		scrollableArea.setTextPaddingByKey("ScrollArea","textPadding");
		scrollableArea.setTextClipPaddingByKey("ScrollArea","scrollAreaPadding");
		
		innerBounds.addChild(scrollableArea);
		scrollableArea.addClippingLayer(innerBounds);
		addChild(innerBounds);
		
		vScrollBar = new ScrollPanelBarV(this);
		addChild(vScrollBar, true);
		hScrollBar = new ScrollPanelBarH(this);
		addChild(hScrollBar, true);
		
		setTextPaddingByKey("ScrollArea","textPadding");
		
		addClippingLayer(this);
		
		initFlingTimer();
	}
	
	private void initFlingTimer() {
		flingTimer = new GameTimer() {
			@Override
			public void timerUpdateHook(float tpf) {
				float currentY = getScrollableAreaVerticalPosition();
				float nextInc = 15*flingSpeed*(1f-this.getPercentComplete());
				
				if (flingDir) {
					float nextY = currentY+nextInc;
					if (nextY <= scrollableArea.getHeight() && nextY >= innerBounds.getHeight()) {
						scrollYTo(nextY);
						setVThumbPositionToScrollArea();
					}
				} else {
					float nextY = currentY-nextInc;
					if (nextY <= scrollableArea.getHeight() && nextY >= innerBounds.getHeight()) {
						scrollYTo(nextY);
						setVThumbPositionToScrollArea();
					}
				}
			}
			@Override
			public void onComplete(float time) {
				
			}
		};
		flingTimer.setInterpolation(Interpolation.exp5Out);
	}
	
	public void addScrollableContent(Element el) {
		addScrollableContent(el, true);
	}
	
	public void addScrollableContent(Element el, boolean reshape) {
		scrollableArea.addChild(el);
		el.addClippingLayer(innerBounds);
		el.setClipPadding(innerBounds.getClipPaddingVec());
		el.setDocking(Docking.SW);
		if (reshape)
			reshape();
	}
	
	public void removeScrollableContent(Element el) {
		removeScrollableContent(el, true);
	}
	
	public void removeScrollableContent(Element el, boolean reshape) {
		scrollableArea.removeChild(el);
		if (reshape)
			reshape();
	}
	
	public void reshape() {
		scrollableArea.sizeToContent();
		setVThumbSize();
		setHThumbSize();
		updateForResize();
		scrollToTop();
		scrollToLeft();
	}
	
	public void setUseVerticalWrap(boolean verticalWrap) {
		this.verticalWrap = verticalWrap;
		if (this.verticalWrap) {
			scrollableArea.setScaleEW(true);
			scrollableArea.setWidth(innerBounds.getWidth());
		} else {
			scrollableArea.setScaleEW(false);
			reshape();
		}
	}
	
	public boolean getUseVerticalWrap() {
		return this.verticalWrap;
	}
	
	public void setScrollSize(float scrollSize) {
		this.scrollSize = scrollSize;
		vScrollBar.updateScrollSize();
		vScrollBar.setX(getWidth()-scrollSize);
		hScrollBar.updateScrollSize();
	}
	
	public float getScrollSize() {
		return this.scrollSize;
	}
	
	@Override
	public void setText(String text) {
		scrollableArea.removeTextElement();
		scrollableArea.setText(text);
		reshape();
		scrollableArea.setTextPaddingByKey("ScrollArea","textPadding");
	}
	
	public void updateForResize() { 
		boolean vHide = false,
				vShow = false,
				hHide = false,
				hShow = false;
		boolean vResize = false,
				hResize = false;
		boolean hDir = true;
		
		if (verticalWrap) {
			if (scrollableArea.getTextElement() != null) {
				if (scrollableArea.getElements().size() > 0) {
					if ((innerBounds.getWidth()-(textPadding.x+textPadding.y)) > scrollableArea.getWidth())
						scrollableArea.setWidth(innerBounds.getWidth()-(textPadding.x+textPadding.y));
					if (((scrollableArea.getTextElement().getLineHeight()*scrollableArea.getTextElement().getLineCount())+(textPadding.z+textPadding.w)) > scrollableArea.getHeight())
						scrollableArea.setHeight((scrollableArea.getTextElement().getLineHeight()*scrollableArea.getTextElement().getLineCount())+(textPadding.z+textPadding.w));
				} else {
					scrollableArea.setWidth(innerBounds.getWidth()-(textPadding.x+textPadding.y));
					scrollableArea.setHeight((scrollableArea.getTextElement().getLineHeight()*scrollableArea.getTextElement().getLineCount())+(textPadding.z+textPadding.w));
				}
			}
			scrollToTop();
		}
		
		boolean vDir;
		if (getHeight() < scrollableArea.getHeight()) {
			if (innerBounds.getWidth() == getWidth())
				vResize = true;
			if (!vScrollBar.getIsVisible())
				vShow = true;
			vDir = false;
		} else {
			if (innerBounds.getWidth() == getWidth()-scrollSize)
				vResize = true;
			if (vScrollBar.getIsVisible())
				vHide = true;
			vDir = true;
		}
		if (!verticalWrap) {
			if (getWidth() < scrollableArea.getWidth()) {
				if (innerBounds.getHeight() == getHeight())
					hResize = true;
				if (!hScrollBar.getIsVisible())
					hShow = true;
				hDir = false;
			} else {
				if (innerBounds.getHeight() == getHeight()-scrollSize)
					hResize = true;
				if (hScrollBar.getIsVisible())
					hHide = true;
				hDir = true;
			}
		}
		
		if (vResize) {
			if (!vDir) {
				innerBounds.setWidth(getWidth()-scrollSize);
			} else {
				innerBounds.setWidth(getWidth());
			}
			if (verticalWrap)
				scrollableArea.setWidth(innerBounds.getWidth());
		}
		if (!verticalWrap) {
			if (hResize) {
				if (!hDir) {
					innerBounds.setHeight(getHeight()-scrollSize);
					innerBounds.setY(scrollSize);
				} else {
					innerBounds.setHeight(getHeight());
					innerBounds.setY(0);
				}
			}
		}
		if (vShow)		vScrollBar.show();
		else if (vHide)	vScrollBar.hide();
		
		if (!verticalWrap) {
			if (hShow)		{
				hScrollBar.show();
				scrollableArea.setY(scrollableArea.getY()-scrollSize);
			}
			else if (hHide)	{
				hScrollBar.hide();
				scrollableArea.setY(scrollableArea.getY()+scrollSize);
			}
		}
		
		setVThumbSize();
		if (!verticalWrap)
			setHThumbSize();
		
		if (!verticalWrap) {
			if (scrollableArea.getWidth() > innerBounds.getWidth() && scrollableArea.getX() < 0) {
				scrollToRight();
			} else if (scrollableArea.getWidth() < innerBounds.getWidth()) {
				scrollToLeft();
			}
			setHThumbPositionToScrollArea();
		}
		if (scrollableArea.getHeight() > innerBounds.getHeight() && scrollableArea.getY() > 0) {
			scrollToBottom();
		} else if (scrollableArea.getHeight() < innerBounds.getHeight()) {
			scrollToTop();
		}
		setVThumbPositionToScrollArea();
	}
	
	@Override
	public void controlResizeHook() {
		updateForResize();
	}
	
	public void setScrollAreaPadding(float padding) {
		innerBounds.setClipPadding(padding);
	}
	
	//<editor-fold desc="Vertical Scrolling">
	public float getScrollableAreaVerticalPosition() {
		return innerBounds.getHeight()-(scrollableArea.getY()+scrollableArea.getHeight());
	}
	
	public float getScrollBoundsHeight() {
		return this.innerBounds.getHeight();
	}
	
	public float getScrollableAreaHeight() {
		return scrollableArea.getHeight();
	}
	
	/**
	 * Returns the height difference between the scrollable area's total height and the
	 * scroll panel's bounds.
	 * 
	 * Note: This returns a negative float value if the scrollable area is smaller than it's bounds.
	 * 
	 * @return height difference
	 */
	public float getVerticalScrollDistance() {
		float diff =  scrollableArea.getHeight()-innerBounds.getHeight();
		return diff;
	}
	
	public void scrollToTop() {
		scrollableArea.setY(-getVerticalScrollDistance());
		setVThumbPositionToScrollArea();
		onScrollContent(ScrollDirection.Up);
	}
	
	public void scrollToBottom() {
		scrollableArea.setY(0);
		setVThumbPositionToScrollArea();
		onScrollContent(ScrollDirection.Down);
	}
	
	public void scrollYTo(float y) {
		float lastY = scrollableArea.getY();
		scrollableArea.setY(y);
		if (lastY > y)
			onScrollContent(ScrollDirection.Down);
		else
			onScrollContent(ScrollDirection.Up);
	}
	
	public void scrollYBy(float incY) {
		if (incY < 0) {
			if (vScrollBar.getScrollThumb().getY() > 0) {
				if (vScrollBar.getScrollThumb().getY()+incY < 0)
					incY -= vScrollBar.getScrollThumb().getY()+incY;
				vScrollBar.getScrollThumb().setY(vScrollBar.getScrollThumb().getY()+incY);
			}
		} else {
			float scrollHeight = vScrollBar.getScrollTrack().getHeight()-vScrollBar.getScrollThumb().getHeight();
			if (vScrollBar.getScrollThumb().getY() < scrollHeight) {
				if (vScrollBar.getScrollThumb().getY()+incY > scrollHeight)
					incY -= vScrollBar.getScrollThumb().getY()+incY-scrollHeight;
				vScrollBar.getScrollThumb().setY(vScrollBar.getScrollThumb().getY()+incY);
			}
		}
		setScrollAreaPositionToVThumb();
	}
	
	private float getVThumbRatio() {
		float ratio = innerBounds.getHeight()/scrollableArea.getHeight();
		if (ratio > 1f)
			ratio = 1f;
		return ratio;
	}
	
	public void setVThumbSize() {
		float ratio = getVThumbRatio();
		vScrollBar.getScrollThumb().setWidth(scrollSize);
		vScrollBar.getScrollThumb().setHeight(vScrollBar.getScrollTrack().getHeight()*ratio);
	}
	
	public void setVThumbPositionToScrollArea() {
		float relY = (FastMath.abs(scrollableArea.getY())/getVerticalScrollDistance());
		vScrollBar.getScrollThumb().setY((vScrollBar.getScrollTrack().getHeight()-vScrollBar.getScrollThumb().getHeight())*relY);
	}
	
	public void setScrollAreaPositionToVThumb() {
		float lastY = scrollableArea.getY();
		float relY = (vScrollBar.getScrollThumb().getY()/(vScrollBar.getScrollTrack().getHeight()-vScrollBar.getScrollThumb().getHeight()));
		scrollableArea.setY(-(getVerticalScrollDistance()*relY));
		if (lastY > -(getVerticalScrollDistance()*relY))
			onScrollContent(ScrollDirection.Up);
		else
			onScrollContent(ScrollDirection.Down);
		
	}
	//</editor-fold>
	
	//<editor-fold desc="Horizontal Scrolling">
	public float getScrollableAreaHorizontalPosition() {
		return innerBounds.getWidth()-(scrollableArea.getX()+scrollableArea.getWidth());
	}
	
	public float getScrollBoundsWidth() {
		return this.innerBounds.getWidth();
	}
	
	public float getScrollableAreaWidth() {
		return scrollableArea.getWidth();
	}
	
	/**
	 * Returns the width difference between the scrollable area's total width and the
	 * scroll panel's bounds.
	 * 
	 * Note: This returns a negative float value if the scrollable area is smaller than its bounds.
	 * 
	 * @return width difference
	 */
	public float getHorizontalScrollDistance() {
		float diff =  scrollableArea.getWidth()-innerBounds.getWidth();
		return diff;
	}
	
	public void scrollToLeft() {
		scrollableArea.setX(0);
		setHThumbPositionToScrollArea();
		onScrollContent(ScrollDirection.Left);
	}
	
	public void scrollToRight() {
		scrollableArea.setX(-getHorizontalScrollDistance());
		setHThumbPositionToScrollArea();
		onScrollContent(ScrollDirection.Right);
	}
	
	public void scrollXTo(float x) {
		float lastX = scrollableArea.getX();
		scrollableArea.setX(x);
		if (lastX > x)
			onScrollContent(ScrollDirection.Left);
		else
			onScrollContent(ScrollDirection.Right);
	}
	
	public void scrollXBy(float incX) {
		float lastX = scrollableArea.getX();
		scrollableArea.setX(lastX+incX);
		if (lastX > lastX+incX)
			onScrollContent(ScrollDirection.Left);
		else
			onScrollContent(ScrollDirection.Right);
	}
	
	private float getHThumbRatio() {
		float ratio = innerBounds.getWidth()/scrollableArea.getWidth();
		if (ratio > 1f)
			ratio = 1f;
		return ratio;
	}
	
	public void setHThumbSize() {
		float ratio = getHThumbRatio();
		hScrollBar.getScrollThumb().setHeight(scrollSize);
		hScrollBar.getScrollThumb().setWidth(hScrollBar.getScrollTrack().getWidth()*ratio);
	}
	
	public void setHThumbPositionToScrollArea() {
		float relX = (FastMath.abs(scrollableArea.getX())/getHorizontalScrollDistance());
		hScrollBar.getScrollThumb().setX((hScrollBar.getScrollTrack().getWidth()-hScrollBar.getScrollThumb().getWidth())*relX);
	}
	
	public void setScrollAreaPositionToHThumb() {
		float lastX = scrollableArea.getX();
		float relX = (hScrollBar.getScrollThumb().getX()/(hScrollBar.getScrollTrack().getWidth()-hScrollBar.getScrollThumb().getWidth()));
		scrollableArea.setX(-(getHorizontalScrollDistance()*relX));
		if (lastX < -(getHorizontalScrollDistance()*relX))
			onScrollContent(ScrollDirection.Left);
		else
			onScrollContent(ScrollDirection.Right);
	}
	//</editor-fold>
	
	public void setUseContentPaging(boolean pagingEnabled) {
		this.pagingEnabled = pagingEnabled;
	}
	
	public boolean getUseContentPaging() {
		return pagingEnabled;
	}
	
	private void onScrollContent(ScrollDirection direction) {
		if (pagingEnabled) {
			for (Element el : scrollableArea.getElementsAsMap().values()) {
				if (direction == ScrollDirection.Up || direction == ScrollDirection.Down) {
					if (el.getY()+el.getHeight()+scrollableArea.getY() < 0 || el.getY()+scrollableArea.getY() > innerBounds.getHeight()) {
						if (el.getIsVisible())
							el.hide();
					} else {
						if (!el.getIsVisible())
							el.show();
					}
				} else {
					if (el.getX()+el.getWidth()+scrollableArea.getX() < 0 || el.getX()+scrollableArea.getX() > innerBounds.getWidth()) {
						if (el.getIsVisible())
							el.hide();
					} else {
						if (!el.getIsVisible())
							el.show();
					}
				}
			}
		}
		onScrollContentHook(direction);
	}
	
	public void onScrollContentHook(ScrollDirection direction) {  }
	
	public void setButtonInc(int buttonInc) { this.buttonInc = buttonInc; }
	
	public int getButtonInc() { return this.buttonInc; }
	
	public void setTrackInc(int trackInc) { this.trackInc = trackInc; }
	
	public int getTrackInc() { return this.trackInc; }

	public ScrollPanelBarV getVerticalScrollBar() { return this.vScrollBar; }
	
	public ScrollPanelBarH getHorizontalScrollBar() { return this.hScrollBar; }
	
	public Element getScrollBounds() { return this.innerBounds; }
	
	public Element getScrollableArea() { return this.scrollableArea; }
	
	public void setFlingEnabled(boolean flingEnabled) { this.flingEnabled = flingEnabled; }
	
	public boolean getFlingEnabled() { return this.flingEnabled; }
	
	public void configureAsChildOfScrollPanel() {
		setScaleEW(false);
		innerBounds.setScaleEW(false);
		scrollableArea.setScaleEW(false);
		setScaleNS(false);
		innerBounds.setScaleNS(false);
		scrollableArea.setScaleNS(false);
		scrollableArea.setControlClippingLayer(innerBounds, getElementParent().getElementParent());
		vScrollBar.setScalingEnabled(false);
		hScrollBar.setScalingEnabled(false);
		setDocking(Element.Docking.SW);
	}
	
	public class ScrollPanelBounds extends Element implements MouseWheelListener, TouchListener, FlingListener {
		public ScrollPanelBounds(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
			super(screen, UID, position, dimensions, resizeBorders, defaultImg);
			setIgnoreMouseWheelMove(false);
		}
		
		@Override
		public void onMouseWheelPressed(MouseButtonEvent evt) { evt.setConsumed(); }
		@Override
		public void onMouseWheelReleased(MouseButtonEvent evt) { evt.setConsumed(); }
		@Override
		public void onMouseWheelUp(MouseMotionEvent evt) {
			if (getVerticalScrollDistance() > 0) {
				scrollYBy(-getTrackInc());
				evt.setConsumed();
			}
		}
		@Override
		public void onMouseWheelDown(MouseMotionEvent evt) {
			if (getVerticalScrollDistance() > 0) {
				scrollYBy(getTrackInc());
				evt.setConsumed();
			}
		}
		//<editor-fold desc="Android Events">
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
				touchStartY = getScrollableAreaVerticalPosition();
				touchOffsetY = evt.getY()-touchStartY;
			}
		}

		@Override
		public void onTouchMove(TouchEvent evt) {
			if (flingEnabled) {
				float nextY = evt.getY()-touchOffsetY;
				if (nextY <= getScrollableAreaHeight() && nextY >= innerBounds.getHeight()) {
					scrollYTo(nextY);
					setVThumbPositionToScrollArea();
				}
			}
		}

		@Override
		public void onTouchUp(TouchEvent evt) {

		}
		//</editor-fold>
	}
}
