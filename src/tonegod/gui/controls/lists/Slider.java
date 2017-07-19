package tonegod.gui.controls.lists;

import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public abstract class Slider extends ButtonAdapter {
	protected List<Object> stepValues = new ArrayList<>();
	private Element elThumbLock;
	private ButtonAdapter elThumb;
	
	protected Orientation orientation;
	private int selectedIndex = 0;
	private boolean isStepped = false;
	private float stepSize = 1;
	private boolean trackSurroundsThumb;
	private Vector2f thumbLockSize = new Vector2f(),
					thumbLockPosition = new Vector2f(),
					thumbSize = new Vector2f(),
					thumbPosition = new Vector2f();
	private float controlSize, controlLength;
	private Vector2f evalDimensions = new Vector2f();
	private MouseButtonEvent trackEvt;
	private Vector2f startPosition;
	
	/**
	 * Creates a new instance of the Slider control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param orientation Slider.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param trackSurroundsThumb Boolean used to determine thumb placement when control is configured
	 */
	public Slider(ElementManager screen, Orientation orientation, boolean trackSurroundsThumb) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Slider").getVector2f("defaultSize"),
			screen.getStyle("Slider").getVector4f("resizeBorders"),
			screen.getStyle("Slider").getString("defaultImg"),
			orientation,
			trackSurroundsThumb
		);
	}
	
	/**
	 * Creates a new instance of the Slider control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param orientation Slider.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param trackSurroundsThumb Boolean used to determine thumb placement when control is configured
	 */
	public Slider(ElementManager screen, Vector2f position, Orientation orientation, boolean trackSurroundsThumb) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Slider").getVector2f("defaultSize"),
			screen.getStyle("Slider").getVector4f("resizeBorders"),
			screen.getStyle("Slider").getString("defaultImg"),
			orientation,
			trackSurroundsThumb
		);
	}
	
	/**
	 * Creates a new instance of the Slider control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param orientation Slider.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param trackSurroundsThumb  Boolean used to determine thumb placement when control is configured
	 */
	public Slider(ElementManager screen, Vector2f position, Vector2f dimensions, Orientation orientation, boolean trackSurroundsThumb) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Slider").getVector4f("resizeBorders"),
			screen.getStyle("Slider").getString("defaultImg"),
			orientation,
			trackSurroundsThumb
		);
	}
	
	/**
	 * Creates a new instance of the Slider control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 * @param orientation Slider.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param trackSurroundsThumb  Boolean used to determine thumb placement when control is configured
	 */
	public Slider(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Orientation orientation, boolean trackSurroundsThumb) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg, orientation, trackSurroundsThumb);
	}
	
	/**
	 * Creates a new instance of the Slider control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param orientation Slider.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param trackSurroundsThumb Boolean used to determine thumb placement when control is configured
	 */
	public Slider(ElementManager screen, String UID, Vector2f position, Orientation orientation, boolean trackSurroundsThumb) {
		this(screen, UID, position,
			screen.getStyle("Slider").getVector2f("defaultSize"),
			screen.getStyle("Slider").getVector4f("resizeBorders"),
			screen.getStyle("Slider").getString("defaultImg"),
			orientation,
			trackSurroundsThumb
		);
	}
	
	/**
	 * Creates a new instance of the Slider control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param orientation Slider.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param trackSurroundsThumb  Boolean used to determine thumb placement when control is configured
	 */
	public Slider(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Orientation orientation, boolean trackSurroundsThumb) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Slider").getVector4f("resizeBorders"),
			screen.getStyle("Slider").getString("defaultImg"),
			orientation,
			trackSurroundsThumb
		);
	}
	
	/**
	 * Creates a new instance of the Slider control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 * @param orientation Slider.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param trackSurroundsThumb  Boolean used to determine thumb placement when control is configured
	 */
	public Slider(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Orientation orientation, boolean trackSurroundsThumb) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.orientation = orientation;
		this.trackSurroundsThumb = trackSurroundsThumb;
		this.setScaleNS(false);
		this.setScaleEW(false);
		this.setDocking(Docking.NW);
		
		if (orientation == Orientation.VERTICAL) {
			controlSize = dimensions.x;
			controlLength = dimensions.y;
		} else {
			controlSize = dimensions.y;
			controlLength = dimensions.x;
		}
		
		this.configureControl();
		
		elThumbLock = new Element(
			screen,
			UID + ":Thumb",
			thumbLockPosition,
			thumbLockSize,
			new Vector4f(5,5,5,5),
			null
		) { 
			@Override
			public void moveTo(float x, float y) {
				Slider slider = ((Slider)getElementParent());
				float nextX = x-(x-getX());
				if (slider.trackSurroundsThumb) nextX -= controlSize/2;
				float nextY = y-(y-getY());
				if (slider.trackSurroundsThumb) nextY -= controlSize/2;
				
				if (isStepped) {
					int index;
					if (slider.orientation == Slider.Orientation.HORIZONTAL) {
						index = Math.round(x/stepSize);
						x = index*stepSize;
						if (slider.trackSurroundsThumb) x += controlSize/2;
					} else {
						index = Math.round(y/stepSize);
						y = index*stepSize;
						if (slider.trackSurroundsThumb) y += controlSize/2;
					}
					if (slider.getSelectedIndex() != index && index > -1 && index < slider.stepValues.size()) {
						slider.setInternalSelectedIndex(index);
					}
				} else {
					int percent;
					if (slider.orientation == Slider.Orientation.HORIZONTAL) {
						if (slider.trackSurroundsThumb) 
							percent = (int)((nextX/(slider.getWidth()-controlSize-1))*100);
						else
							percent = (int)((nextX/(slider.getWidth()-1))*100);
					} else {
						if (slider.trackSurroundsThumb) 
							percent = (int)((nextY/(slider.getHeight()-controlSize-1))*100);
						else
							percent = (int)((nextY/(slider.getHeight()-1))*100);
					}
					if (slider.getSelectedIndex() != percent && percent >= 0 && percent <= 100) {
						slider.setInternalSelectedIndex(percent);
					} else if (slider.getSelectedIndex() != percent && percent < 0) {
						slider.setInternalSelectedIndex(0);
					} else if (slider.getSelectedIndex() != percent && percent > 100) {
						slider.setInternalSelectedIndex(100);
					}
				}
				if (getLockToParentBounds()) {
					if (slider.trackSurroundsThumb) {
						if (slider.orientation == Slider.Orientation.HORIZONTAL) {
							if (x < controlSize/2) { x = controlSize/2; }
							if (y < 0) { y = 0; }
							if (x > getElementParent().getWidth()-controlSize/2) {
								x = getElementParent().getWidth()-controlSize/2;
							}
							if (y > getElementParent().getHeight()-getHeight()) {
								y = getElementParent().getHeight()-getHeight();
							}
						} else {
							if (x < 0) { x = 0; }
							if (y < controlSize/2) { y = controlSize/2; }
							if (x > getElementParent().getWidth()-getWidth()) {
								x = getElementParent().getWidth()-getWidth();
							}
							if (y > getElementParent().getHeight()-controlSize/2) {
								y = getElementParent().getHeight()-controlSize/2;
							}
						}
					} else {
						if (x < 0) { x = 0; }
						if (y < 0) { y = 0; }
						if (x > getElementParent().getWidth()-getWidth()) {
							x = getElementParent().getWidth()-getWidth();
						}
						if (y > getElementParent().getHeight()-getHeight()) {
							y = getElementParent().getHeight()-getHeight();
						}
					}
				}
				this.setX(x);
				this.setY(y);
				controlMoveHook();
			}
		};
		elThumbLock.setScaleNS(false);
		elThumbLock.setScaleEW(false);
		elThumbLock.setDocking(Docking.SW);
		elThumbLock.setLockToParentBounds(true);
		addChild(elThumbLock);
		
		String texThumb = screen.getStyle("Button").getString("defaultImg");
		if (screen.getStyle("Slider").getString("thumbHoverImg") != null) {
			texThumb = screen.getStyle("Slider").getString("thumbImg");
		}
		
		elThumb = new ButtonAdapter(
			screen,
			UID + ":Thumb",
			thumbPosition,
			thumbSize,
			new Vector4f(5,5,5,5),
			texThumb
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				screen.setTabFocusElement(getElementParent());
			}
		};
		if (screen.getStyle("Slider").getString("thumbHoverImg") != null) {
			setButtonHoverInfo(
				screen.getStyle("Slider").getString("thumbHoverImg"),
				screen.getStyle("Button").getColorRGBA("hoverColor")
			);
		}
		if (screen.getStyle("Slider").getString("thumbPressedImg") != null) {
			setButtonPressedInfo(
				screen.getStyle("Slider").getString("thumbPressedImg"),
				screen.getStyle("Button").getColorRGBA("pressedColor")
			);
		}
		
		elThumbLock.addChild(elThumb);
		
		elThumb.setScaleNS(false);
		elThumb.setScaleEW(false);
		elThumb.setDocking(Docking.SW);
		elThumb.setIsMovable(true);
		elThumb.setEffectParent(true);
		
		removeEffect(Effect.EffectEvent.Hover);
		removeEffect(Effect.EffectEvent.Press);
		removeEffect(Effect.EffectEvent.LoseFocus);
		
		this.setInterval(100);
	}
	
	private void configureControl() {
		if (orientation == Orientation.HORIZONTAL) {
			if (trackSurroundsThumb) {
				thumbLockSize.set(1, controlSize);
				thumbLockPosition.set(controlSize/2,0);
				thumbSize.set(controlSize, controlSize);
				thumbPosition.set(-(controlSize/2),0);
			} else {
				setY(getY()+(controlSize/3));
				setDimensions(controlLength, controlSize/3);
				thumbLockSize.set(1, controlSize/3);
				thumbLockPosition.set(0, 0);
				thumbSize.set(controlSize/3*2, controlSize);
				thumbPosition.set(-(controlSize/4), -(controlSize/3));
			//	layoutHints.setElementPadY(controlSize/3);
			}
		} else {
			if (trackSurroundsThumb) {
				thumbLockSize.set(controlSize, 1);
				thumbLockPosition.set(0,controlSize/2);
				thumbSize.set(controlSize, controlSize);
				thumbPosition.set(0,-(controlSize/2));
			} else {
				setX(getX()+(controlSize/3));
				setDimensions(controlSize/3, controlLength);
				thumbLockSize.set(controlSize/3, 1);
				thumbLockPosition.set(0,0);
				thumbSize.set(controlSize, controlSize/3*2);
				thumbPosition.set(-(controlSize/3), -(controlSize/4));
			//	layoutHints.setElementPadX(controlSize/3);
			}
		}
	}
	
	/**
	 * Adds a step value to the Slider.  When 2 or more step values are associated with
	 * a slider, the thumb movement becomes stepped and advances to the next/previous slot
	 * position as the mouse is moved.  Each slot added has an associated value that is
	 * returned via the onChange event or getSelectedValue() method.
	 * 
	 * @param value The string value to add for the next step.
	 */
	public void addStepValue(Object value) {
		stepValues.add(value);
		if (stepValues.size() >= 2) {
			isStepped = true;
			setStepSize();
			this.setInterval(1);
		}
	}
	
	/**
	 * Auto-populates stepValue with Integer values using the provided parameters
	 * @param min Lowest number
	 * @param max Highest number
	 * @param inc Incremental step
	 */
	public void setStepIntegerRange(int min, int max, int inc) {
		stepValues.clear();
		isStepped = false;
		selectedIndex = -1;
		for (int i = min; i <= max; i += inc) {
			addStepValue(i);
		}
		if (selectedIndex == -1)
			setSelectedIndexWithCallback(0);
	}
	
	/**
	 * Auto-populates stepValue with Float values using the provided parameters
	 * @param min Lowest number
	 * @param max Highest number
	 * @param inc Incremental step
	 */
	public void setStepFloatRange(float min, float max, float inc) {
		stepValues.clear();
		isStepped = false;
		selectedIndex = -1;
		for (float i = min; i <= max; i += inc) {
			addStepValue(i);
		}
		if (selectedIndex == -1)
			setSelectedIndexWithCallback(0);
	}
	
	/**
	 * Removes a step value by the value originally added.
	 * 
	 * @param value The string value of the step to be removed.
	 */
	public void removeStepValue(Object value) {
		if (!stepValues.isEmpty()) {
			stepValues.remove(value);
			setStepSize();
			if (stepValues.size() < 2) {
				isStepped = false;
				this.setInterval(100);
			}
			if (selectedIndex > stepValues.size()-1) {
				selectedIndex = stepValues.size()-1;
			}
			this.setSelectedIndexWithCallback(selectedIndex);
		}
	}
	
	private void setStepSize() {
		if (orientation == Orientation.HORIZONTAL) {
			if (trackSurroundsThumb) {
				stepSize = (getWidth()-controlSize)/(stepValues.size()-1);
			} else {
				stepSize = getWidth()/(stepValues.size()-1);
			}
		} else {
			if (trackSurroundsThumb) {
				stepSize = (getHeight()-controlSize)/(stepValues.size()-1);
			} else {
				stepSize = getHeight()/(stepValues.size()-1);
			}
		}
	}
	
	/**
	 * Returns the current selected stepValue's value
	 * @return Object
	 */
	public Object getSelectedValue() {
		return stepValues.get(selectedIndex);
	}
	
	/**
	 * Returns the text value of the current selected step.
	 * 
	 * @return String stepValue
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	/**
	 * Sets the Slider's selected index to the selected step index specified and
	 * moves the Slider's thumb to appropriate x/y coordinates to reflect this change.
	 * 
	 * @param selectedIndex desired index
	 */
	private void setInternalSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		if( isStepped)	onChange(selectedIndex, stepValues.get(selectedIndex));
		else			onChange(selectedIndex, selectedIndex);
	}
	
	/**
	 * Sets the selectedIndex to the specified index and updates appropriately then calling the onChange event
	 * @param selectedIndex desired index
	 */
	public void setSelectedIndexWithCallback(int selectedIndex) {
		if (isStepped) {
			if (selectedIndex < 0)							selectedIndex = 0;
			else if (selectedIndex > stepValues.size()-1)	selectedIndex = stepValues.size()-1;
		} else {
			if (selectedIndex < 0)							selectedIndex = 0;
			else if (selectedIndex > 100)					selectedIndex = 100;
		}
		
		this.selectedIndex = selectedIndex;
		
		float step = (isStepped) ? stepSize : ((trackSurroundsThumb) ? (getWidth()-controlSize)/100 : getWidth()/100);
		step *= selectedIndex;
		if (orientation == Orientation.HORIZONTAL) {
			if (trackSurroundsThumb)elThumbLock.setX(step+(controlSize/2));
			else					elThumbLock.setX(step);
		} else {
			if (trackSurroundsThumb)elThumbLock.setY(step+(controlSize/2));
			else					elThumbLock.setY(step);
		}
		
		if( isStepped)	onChange(selectedIndex, stepValues.get(selectedIndex));
		else			onChange(selectedIndex, selectedIndex);
	}
	
	/**
	 * Sets the selectedIndex to the specified index and updates appropriately
	 * @param selectedIndex desired index
	 */
	public void setSelectedIndex(int selectedIndex) {
		if (isStepped) {
			if (selectedIndex < 0)							selectedIndex = 0;
			else if (selectedIndex > stepValues.size()-1)	selectedIndex = stepValues.size()-1;
		} else {
			if (selectedIndex < 0)							selectedIndex = 0;
			else if (selectedIndex > 100)					selectedIndex = 100;
		}
		
		this.selectedIndex = selectedIndex;
		
		float step = (isStepped) ? stepSize : ((trackSurroundsThumb) ? (getWidth()-controlSize)/100 : getWidth()/100);
		step *= selectedIndex;
		if (orientation == Orientation.HORIZONTAL) {
			if (trackSurroundsThumb)elThumbLock.setX(step+(controlSize/2));
			else					elThumbLock.setX(step);
		} else {
			if (trackSurroundsThumb)elThumbLock.setY(step+(controlSize/2));
			else					elThumbLock.setY(step);
		}
	}
	
	/**
	 * Attempts to set the selectedIndex to the index of the provided value Object and updates appropriately
	 * @param value desired value
	 */
	public void setSelectedByValue(Object value) {
		String searchVal = value.toString();
		int index = -1;
		for (int i = 0; i < stepValues.size(); i++) {
			String val = stepValues.get(i).toString();
			if (val.contains(searchVal)) {
				index = i;
			}
		}
		
		if (index != -1) {
			this.selectedIndex = index;

			float step = (isStepped) ? stepSize : ((trackSurroundsThumb) ? (getWidth()-controlSize)/100 : getWidth()/100);
			step *= selectedIndex;
			if (orientation == Orientation.HORIZONTAL) {
				if (trackSurroundsThumb)elThumbLock.setX(step+(controlSize/2));
				else					elThumbLock.setX(step);
			} else {
				if (trackSurroundsThumb)elThumbLock.setY(step+(controlSize/2));
				else					elThumbLock.setY(step);
			}
		}
	}
	
	/**
	 * Event called when the Slider's selectedIndex changes.
	 * 
	 * @param selectedIndex The Slider's current selectedIndex
	 * @param value The string value associated with this index
	 */
	public abstract void onChange(int selectedIndex, Object value);
	
	@Override
	public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean toggled) {
		trackEvt = evt;
		startPosition = elThumbLock.getPosition().clone();
		updateThumbByTrackClick();
	}
	
	@Override
	public void onButtonStillPressedInterval() {
		updateThumbByTrackClick();
		screen.updateToolTipLocation();
	}
	
	private void updateThumbByTrackClick() {
		if (orientation == Orientation.HORIZONTAL) {
			if (trackSurroundsThumb) {
				if (elThumbLock.getX() > trackEvt.getX()-getAbsoluteX() && startPosition.x > trackEvt.getX()-getAbsoluteX()) {
					setSelectedIndexWithCallback(selectedIndex-1);
				} else if (elThumbLock.getX() < trackEvt.getX()-getAbsoluteX() && startPosition.x < trackEvt.getX()-getAbsoluteX()) {
					setSelectedIndexWithCallback(selectedIndex+1);
				}
			} else {
				if (elThumbLock.getX() > trackEvt.getX()-getAbsoluteX() && startPosition.x > trackEvt.getX()-getAbsoluteX()) {
					setSelectedIndexWithCallback(selectedIndex-1);
				} else if (elThumbLock.getX() < trackEvt.getX()-getAbsoluteX() && startPosition.x < trackEvt.getX()-getAbsoluteX()) {
					setSelectedIndexWithCallback(selectedIndex+1);
				}
			}
		} else {
			if (trackSurroundsThumb) {
				if (elThumbLock.getY() > trackEvt.getY()-getAbsoluteY() && startPosition.y > trackEvt.getY()-getAbsoluteY()) {
					setSelectedIndexWithCallback(selectedIndex-1);
				} else if (elThumbLock.getY() < trackEvt.getY()-getAbsoluteY() && startPosition.y < trackEvt.getY()-getAbsoluteY()) {
					setSelectedIndexWithCallback(selectedIndex+1);
				}
			} else {
				if (elThumbLock.getY() > trackEvt.getY()-getAbsoluteY() &&  startPosition.y > trackEvt.getY()-getAbsoluteY()) {
					setSelectedIndexWithCallback(selectedIndex-1);
				} else if (elThumbLock.getY() < trackEvt.getY()-getAbsoluteY() && startPosition.y < trackEvt.getY()-getAbsoluteY()) {
					setSelectedIndexWithCallback(selectedIndex+1);
				}
			}
		}
	}
	
	// Tab focus
	@Override
	public void setTabFocus() {
		screen.setKeyboardElement(this);
		Effect effect = elThumb.getEffect(Effect.EffectEvent.TabFocus);
		if (effect != null) {
			effect.setColor(ColorRGBA.DarkGray);
			screen.getEffectManager().applyEffect(effect);
		}
	}
	
	@Override
	public void resetTabFocus() {
		screen.setKeyboardElement(null);
		Effect effect = elThumb.getEffect(Effect.EffectEvent.LoseTabFocus);
		if (effect != null) {
			effect.setColor(ColorRGBA.White);
			screen.getEffectManager().applyEffect(effect);
		}
	}
	
	// Default keyboard interaction
	@Override
	public void onKeyPress(KeyInputEvent evt) {
		if (evt.getKeyCode() == KeyInput.KEY_LEFT) {
			trackEvt = new MouseButtonEvent(0,true,(int)this.getAbsoluteX(),(int)this.getAbsoluteY());
			updateThumbByTrackClick();
		} else if (evt.getKeyCode() == KeyInput.KEY_RIGHT) {
			trackEvt = new MouseButtonEvent(0,true,(int)this.getAbsoluteWidth()+1,(int)this.getAbsoluteHeight()+1);
			updateThumbByTrackClick();
		}
	}
	
	@Override
	public void onKeyRelease(KeyInputEvent evt) {
		
	}
	
	@Override
	public void setToolTipText(String tip) {
		this.elThumb.setToolTipText(tip);
	}
	
	@Override
	public String getToolTipText() {
		return this.elThumb.getToolTipText();
	}
	
	public ButtonAdapter getThumb() { return elThumb; }
}
