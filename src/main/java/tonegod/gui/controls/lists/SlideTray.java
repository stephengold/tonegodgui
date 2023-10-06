package tonegod.gui.controls.lists;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.BatchEffect;
import tonegod.gui.effects.Effect;
import tonegod.gui.framework.core.util.GameTimer;

/**
 *
 * @author t0neg0d
 */
public class SlideTray extends Element {
	/*
	public static enum Orientation {
		VERTICAL,
		HORIZONTAL
	}
	*/
	
	public static enum ZOrderSort {
		FIRST_TO_LAST,
		LAST_TO_FIRST
	}
	private Orientation orientation;
	
	private ZOrderSort sort = ZOrderSort.FIRST_TO_LAST;
	
	protected ButtonAdapter btnPrevElement, btnNextElement;
	private Element elTray;
	private float btnSize;
	
	protected List<Element> trayElements = new ArrayList<>();
	protected int currentElementIndex = 0;
	
	protected float trayPadding = 5;
	
	private boolean useSlideEffect = false;
	private Effect slideEffect;
	
	private BatchEffect batch = null;
	private GameTimer timer;
	
	private float currentOffset = 0;
	private float currentPosition = 0;
	private float lastOffset = 0;
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, Orientation orientation) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, Vector2f position, Orientation orientation) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, Vector2f position, Vector2f dimensions, Orientation orientation) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the SlideTray's track
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Orientation orientation) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg, orientation);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, String UID, Vector2f position, Orientation orientation) {
		this(screen, UID, position,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Orientation orientation) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the SlideTray's track
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Orientation orientation) {
		super(screen, UID, position, dimensions, resizeBorders, null);
		this.orientation = orientation;
		
		this.setDocking(Docking.NW);
		
		if (orientation == Orientation.HORIZONTAL) {
			this.setScaleEW(true);
			this.setScaleNS(false);
		} else {
			this.setScaleEW(false);
			this.setScaleNS(true);
		}
//		this.setAsContainerOnly();
		
		initControl();
	}
	
	private void initControl() {
		btnSize = screen.getStyle("Button").getVector2f("defaultSize").y;
				
		slideEffect = new Effect(Effect.EffectType.SlideTo, Effect.EffectEvent.Show, .25f);
                
                // Note to tonegod
                
                // Must take longer than the slide itself or the buttons will still be
                // in the right place.
                //
                // Is there no way to execute code when an event finishes instead of
                // relying on a separate timed event?
                // 
                // Rockfire
		timer = new GameTimer(.26f) {
			@Override
			public void onComplete(float time) {
				if (orientation == Orientation.HORIZONTAL)
					currentPosition = trayElements.get(0).getX();
				else
					currentPosition = elTray.getHeight()-trayElements.get(0).getY();
				hideShowButtons();
			}
		};
		
		Vector2f pos = new Vector2f();
		Vector2f dim = new Vector2f();
		if (orientation == Orientation.HORIZONTAL) {
			pos.set(-btnSize,0);
			dim.set(btnSize, getHeight());
		} else {
			pos.set(0,-btnSize);
			dim.set(getWidth(),btnSize);
		}
			
		btnPrevElement = new ButtonAdapter(screen, getUID() + ":btnPrevElement",
			pos, dim, Vector4f.ZERO, null
		) {
			@Override
			public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
				if (batch == null)
					prevElement();
				else if (!batch.getIsActive())
					prevElement();
			}
		};
		if (orientation == Orientation.HORIZONTAL)
			btnPrevElement.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowLeft"));
		else
			btnPrevElement.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowUp"));
		btnPrevElement.clearAltImages();
		btnPrevElement.setDocking(Docking.SW);
		btnPrevElement.setScaleEW(false);
		btnPrevElement.setScaleNS(false);
		
		if (orientation == Orientation.HORIZONTAL) {
			pos.set(getWidth(),0);
		} else {
			pos.set(0,getHeight());
		}
		
		btnNextElement = new ButtonAdapter(screen, getUID() + ":btnNextElement",
			pos, dim, Vector4f.ZERO, null
		) {
			@Override
			public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
				if (batch == null)
					nextElement();
				else if (!batch.getIsActive())
					nextElement();
			}
		};
		if (orientation == Orientation.HORIZONTAL)
			btnNextElement.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		else
			btnNextElement.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowDown"));
		btnNextElement.clearAltImages();
		if (orientation == Orientation.HORIZONTAL)
			btnNextElement.setDocking(Docking.SE);
		else
			btnNextElement.setDocking(Docking.SW);
		btnNextElement.setScaleEW(false);
		btnNextElement.setScaleNS(false);
		
		elTray = new Element(screen, getUID() + ":elTray",
			new Vector2f(0,0),
			new Vector2f(getWidth(), getHeight()),
			new Vector4f(0,0,0,0),
			null
		) {
			@Override
			public void controlResizeHook() {
				if (orientation == Orientation.HORIZONTAL) {
					btnNextElement.setX(getWidth());
				} else {
					if (!trayElements.isEmpty()) {
						float nextY = currentPosition;
						int index = 0;
						for (Element el : trayElements) {
							if (index  > 0)
								nextY += el.getHeight()+trayPadding;
							el.setY(elTray.getHeight()-nextY);
							index++;
						}
					}
					btnPrevElement.setY(getHeight());
				}
				hideShowButtons();
			}
		};
		elTray.setDocking(Docking.SW);
		if (orientation == Orientation.HORIZONTAL) {
			elTray.setScaleEW(true);
			elTray.setScaleNS(false);
		} else {
			elTray.setScaleEW(false);
			elTray.setScaleNS(true);
		}
		elTray.setAsContainerOnly();
		
		addChild(elTray);
		addChild(btnPrevElement);
		addChild(btnNextElement);
	}
	
	public void setButtonSize(float size) {
		if (orientation == Orientation.HORIZONTAL) {
			btnPrevElement.setHeight(size);
			btnPrevElement.getButtonIcon().centerToParentV();
			btnNextElement.setHeight(size);
			btnNextElement.getButtonIcon().centerToParentV();
		} else {
			btnPrevElement.setWidth(size);
			btnPrevElement.getButtonIcon().centerToParentH();
			btnNextElement.setWidth(size);
			btnNextElement.getButtonIcon().centerToParentH();
		}
	}
	
	public void alignButtonsV(VAlign vAlign) {
		if (vAlign == VAlign.Top) {
			btnPrevElement.setY(getHeight()-btnPrevElement.getHeight());
			btnNextElement.setY(getHeight()-btnNextElement.getHeight());
		} else if (vAlign == VAlign.Center) {
			btnPrevElement.centerToParentV();
			btnNextElement.centerToParentV();
		} else if (vAlign == VAlign.Center) {
			btnPrevElement.setY(0);
			btnNextElement.setY(0);
		}	
	}
	
	public void alignButtonsH(Align align) {
		if (align == Align.Right) {
			btnPrevElement.setX(getWidth()-btnPrevElement.getWidth());
			btnNextElement.setX(getWidth()-btnNextElement.getWidth());
		} else if (align == Align.Center) {
			btnPrevElement.centerToParentH();
			btnNextElement.centerToParentH();
		} else if (align == Align.Left) {
			btnPrevElement.setX(0);
			btnNextElement.setX(0);
		}	
	}
	
	public void setZOrderSorting(ZOrderSort sort) {
		this.sort = sort;
	}
	
	public void resort(Element toFront) {
		float step = screen.getZOrderStepMinor();
		if (sort == ZOrderSort.FIRST_TO_LAST) {
			for (int i = 0; i < trayElements.size(); i++) {
				Element el = trayElements.get(i);
				el.setLocalTranslation(el.getLocalTranslation().setZ(step));
				step += screen.getZOrderStepMinor();
			}
		} else if (sort == ZOrderSort.LAST_TO_FIRST) {
			for (int i = trayElements.size()-1; i >= 0; i--) {
				Element el = trayElements.get(i);
				if (el != toFront) {
					el.setLocalTranslation(el.getLocalTranslation().setZ(step));
					step += screen.getZOrderStepMinor();
				}
			}
		}
		toFront.setLocalTranslation(toFront.getLocalTranslation().setZ(step));
	}
	
	/**
	 * Read the current slide tray padding value.
	 * @return padding value
	 */
	public float getTrayPadding() { return this.trayPadding; }
	
	/**
	 * Alter the padding between slide tray elements
	 * @param trayPadding desired amount of padding
	 */
	public void setTrayPadding(float trayPadding) {
		this.trayPadding = trayPadding;
	}
	
	/**
	 * Enables/disables the use of the SlideTo effect when using next/previous buttons
	 * @param useSlideEffect true &rarr; enable, false &rarr; disable
	 */
	public void setUseSlideEffect(boolean useSlideEffect) {
		this.useSlideEffect = useSlideEffect;
	}
	
	/**
	 * Adds the provided Element as a tray item
	 * @param element element to add
	 */
	public void addTrayElement(Element element) {
		if (orientation == Orientation.HORIZONTAL) {
			element.setPosition(getNextPosition(),0);
		} else {
			element.setPosition(0,getNextPosition());
		}
		element.addClippingLayer(elTray);
		element.setDocking(Docking.SW);
		element.setScaleEW(false);
		element.setScaleNS(false);
		trayElements.add(element);
		elTray.addChild(element);
		hideShowButtons();
		if (orientation == Orientation.HORIZONTAL) {
			currentPosition = elTray.getX();
		} else {
			currentPosition = elTray.getHeight()-trayElements.get(0).getY();
		}
	}
	
	public void nextElement() {
		if (currentElementIndex+1 < trayElements.size()) {
			float diff =		getNextOffset(true);
			if (useSlideEffect)	slideTabs(diff, true);
			else				moveTabs(diff, true);
			currentElementIndex++;
			if (useSlideEffect) {
				timer.reset(false);
				screen.getAnimManager().addGameTimer(timer);
			} else
				hideShowButtons();
		}
		if (orientation == Orientation.HORIZONTAL)
			currentPosition = trayElements.get(0).getX();
		else
			currentPosition = elTray.getHeight()-trayElements.get(0).getY();
	}
	
	public void prevElement() {
		if (currentElementIndex-1 > -1) {
			float diff =		getNextOffset(false);
			if (useSlideEffect)	slideTabs(diff, false);
			else				moveTabs(diff, false);
			currentElementIndex--;
			if (useSlideEffect) {
				timer.reset(false);
				screen.getAnimManager().addGameTimer(timer);
			} else
				hideShowButtons();
		}
		if (orientation == Orientation.HORIZONTAL)
			currentPosition = trayElements.get(0).getX();
		else
			currentPosition = elTray.getHeight()-trayElements.get(0).getY();
	}
	
	private float getNextOffset(boolean dir) {
		float diff;
		Element el = trayElements.get(trayElements.size()-1);
		if (orientation == Orientation.HORIZONTAL) {
			diff = (dir) ? 
				(int)(trayElements.get(currentElementIndex).getWidth()+trayPadding) :
				(int)(trayElements.get(currentElementIndex-1).getWidth()+trayPadding);
			if (dir) {
				if (lastOffset != 0)
					diff = (int)FastMath.abs(trayElements.get(currentElementIndex).getX());
				if ((el.getX()+el.getWidth())-diff < elTray.getWidth()) {
					diff = FastMath.abs(elTray.getWidth()-(el.getX()+el.getWidth()));
					lastOffset = diff;
				}
			} else {
				if (lastOffset != 0) {
					diff = (int)FastMath.abs(trayElements.get(currentElementIndex-1).getX());
					lastOffset = 0;
				}
			}
		} else {
			diff = (dir) ? 
				(int)(trayElements.get(currentElementIndex).getHeight()+trayPadding) :
				(int)(trayElements.get(currentElementIndex-1).getHeight()+trayPadding);
			if (dir) {
				if (lastOffset != 0)
					diff = (int)((elTray.getHeight()-trayElements.get(currentElementIndex).getY())-(trayElements.get(currentElementIndex).getHeight()+trayPadding));
				if (el.getY()+diff > 0) {
					diff -= (el.getY()+diff);
					lastOffset = diff;
				}
			} else {
				if (lastOffset != 0) {
					diff = -(int)((elTray.getHeight()-trayElements.get(currentElementIndex-1).getY())-(trayElements.get(currentElementIndex-1).getHeight()+trayPadding));
					lastOffset = 0;
				}
			}
		}
		return diff;
	}
	
	private void slideTabs(float diff, boolean dir) {
		batch = new BatchEffect();
		for (Element el : trayElements) {
			if (orientation == Orientation.HORIZONTAL) {
				float nextX = (!dir) ? el.getX()+diff : el.getX()-diff;
				Vector2f destination = new Vector2f(nextX,el.getY());
				Effect effect = slideEffect.clone();
				effect.setElement(el);
				effect.setEffectDestination(destination);
				batch.addEffect(effect);
			} else {
				float nextY = (!dir) ? el.getY()-diff : el.getY()+diff;
				Vector2f destination = new Vector2f(el.getX(),nextY);
				Effect effect = slideEffect.clone();
				effect.setElement(el);
				effect.setEffectDestination(destination);
				batch.addEffect(effect);
			}
		}
		screen.getEffectManager().applyBatchEffect(batch);
	}
	
	private void moveTabs(float diff, boolean dir) {
		for (Element el : trayElements) {
			if (orientation == Orientation.HORIZONTAL) {
				float nextX = (!dir) ? el.getX()+diff : el.getX()-diff;
				el.setX(nextX);
			} else {
				float nextY = (!dir) ? el.getY()-diff : el.getY()+diff;
				el.setY(nextY);
			}
		}
	}
	
	private void hideShowButtons() {
		if (currentElementIndex == 0)
			btnPrevElement.hide();
		else
			btnPrevElement.show();
		Element el = trayElements.get(trayElements.size()-1);
		if (orientation == Orientation.HORIZONTAL) {
			if (el.getX()+el.getWidth()-el.borders.z <= elTray.getWidth())
				btnNextElement.hide();
			else
				btnNextElement.show();
		} else {
			if (el.getY() >= 0)
				btnNextElement.hide();
			else
				btnNextElement.show();
		}
		if (!trayElements.isEmpty())
			currentOffset = (elTray.getHeight()-trayElements.get(0).getY());
	}
	
	private float getNextPosition() {
		float ret = 0;
		for (Element el : trayElements) {
			if (orientation == Orientation.HORIZONTAL) {
				ret += el.getWidth()+trayPadding;
			} else {
				ret += el.getHeight()+trayPadding;
			}
		}
		return ret;
	}
	
	@Override
	public void setControlClippingLayer(Element clippingLayer) {
		addClippingLayer(clippingLayer);
	}
}
