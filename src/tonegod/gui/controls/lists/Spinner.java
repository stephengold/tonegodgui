package tonegod.gui.controls.lists;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class Spinner extends TextField {
	/*
	public static enum Orientation {
		VERTICAL,
		HORIZONTAL
	}
	*/
	protected List<String> stepValues = new ArrayList<>();
	private boolean cycle = false;
	private int selectedIndex = -1;
	private Orientation orientation;
	
	private float btnWidth;
	private float btnIncX, btnIncY, btnIncH, btnIncIconSize;
	private float btnDecX, btnDecY, btnDecH, btnDecIconSize;
	private String btnIncIcon, btnDecIcon;
	
	private ButtonAdapter btnInc, btnDec;
	
	/**
	 * Creates a new instance of the Spinner control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param orientation Spinner.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param cycle Boolean used to determine if the spinner should cycle back through values
	 */
	public Spinner(ElementManager screen, Spinner.Orientation orientation, boolean cycle) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Spinner").getVector2f("defaultSize"),
			screen.getStyle("Spinner").getVector4f("resizeBorders"),
			screen.getStyle("Spinner").getString("defaultImg"),
			orientation,
			cycle
		);
	}
	
	/**
	 * Creates a new instance of the Spinner control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param orientation Spinner.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param cycle Boolean used to determine if the spinner should cycle back through values
	 */
	public Spinner(ElementManager screen, Vector2f position, Spinner.Orientation orientation, boolean cycle) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Spinner").getVector2f("defaultSize"),
			screen.getStyle("Spinner").getVector4f("resizeBorders"),
			screen.getStyle("Spinner").getString("defaultImg"),
			orientation,
			cycle
		);
	}
	
	/**
	 * Creates a new instance of the Spinner control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param orientation Spinner.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param cycle Boolean used to determine if the spinner should cycle back through values
	 */
	public Spinner(ElementManager screen, Vector2f position, Vector2f dimensions, Spinner.Orientation orientation, boolean cycle) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Spinner").getVector4f("resizeBorders"),
			screen.getStyle("Spinner").getString("defaultImg"),
			orientation,
			cycle
		);
	}
	
	/**
	 * Creates a new instance of the Spinner control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Spinner
	 * @param orientation Spinner.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param cycle Boolean used to determine if the spinner should cycle back through values
	 */
	public Spinner(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Spinner.Orientation orientation, boolean cycle) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg, orientation, cycle);
	}
	
	/**
	 * Creates a new instance of the Spinner control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param orientation Spinner.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param cycle Boolean used to determine if the spinner should cycle back through values
	 */
	public Spinner(ElementManager screen, String UID, Vector2f position, Spinner.Orientation orientation, boolean cycle) {
		this(screen, UID, position,
			screen.getStyle("Spinner").getVector2f("defaultSize"),
			screen.getStyle("Spinner").getVector4f("resizeBorders"),
			screen.getStyle("Spinner").getString("defaultImg"),
			orientation,
			cycle
		);
	}
	
	/**
	 * Creates a new instance of the Spinner control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param orientation Spinner.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param cycle Boolean used to determine if the spinner should cycle back through values
	 */
	public Spinner(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Spinner.Orientation orientation, boolean cycle) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Spinner").getVector4f("resizeBorders"),
			screen.getStyle("Spinner").getString("defaultImg"),
			orientation,
			cycle
		);
	}
	
	/**
	 * Creates a new instance of the Spinner control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Spinner
	 * @param orientation Spinner.Orientation used to establish Horizontal/Vertical layout during control configuration
	 * @param cycle Boolean used to determine if the spinner should cycle back through values
	 */
	public Spinner(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Spinner.Orientation orientation, boolean cycle) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg
		);
		
		this.orientation = orientation;
		this.cycle = cycle;
		this.setScaleEW(false);
		this.setScaleNS(false);
		this.setDocking(Docking.NW);
		
		btnWidth = getHeight();
		
		this.setFontSize(screen.getStyle("Spinner").getFloat("fontSize"));
		this.setTextPaddingByKey("Spinner","textPadding");
		this.setTextWrap(LineWrapMode.valueOf(screen.getStyle("Spinner").getString("textWrap")));
		this.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Spinner").getString("textAlign")));
		this.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Spinner").getString("textVAlign")));
		
		if (orientation == Orientation.HORIZONTAL) {
			setWidth(getWidth()-(btnWidth*2));
			setX(getX()+btnWidth);
			btnIncX = getWidth();
			btnIncY = 0;
			btnIncH = getHeight();
			btnIncIcon = screen.getStyle("Common").getString("arrowRight");
			btnDecX = -getHeight();
			btnDecY = 0;
			btnDecH = getHeight();
			btnDecIcon = screen.getStyle("Common").getString("arrowLeft");
		//	layoutHints.setElementPadX(btnWidth*2);
		} else {
			setWidth(getWidth()-btnWidth);
			btnIncX = getWidth();
			btnIncY = 0;
			btnIncH = getHeight()/2;
			btnIncIcon = screen.getStyle("Common").getString("arrowUp");
			btnDecX = getWidth();
			btnDecY = getHeight()/2;
			btnDecH = getHeight()/2;
			btnDecIcon = screen.getStyle("Common").getString("arrowDown");
		//	layoutHints.setElementPadX(btnWidth);
		}
		btnIncIconSize = getHeight()/2;
		btnDecIconSize = getHeight()/2;
		
		btnInc = new ButtonAdapter(
			screen,
			UID + ":btnInc",
			new Vector2f(btnIncX, btnIncY),
			new Vector2f(getHeight(), btnIncH)
		) {
			@Override
			public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean toggled) {
				screen.setTabFocusElement((Spinner)getElementParent());
				((Spinner)getElementParent()).incStep();
			}
			@Override
			public void onButtonStillPressedInterval() {
				((Spinner)getElementParent()).incStep();
			}
		};
		btnInc.setButtonIcon(btnIncIconSize, btnIncIconSize, btnIncIcon);
		btnInc.setDocking(Docking.SW);
		addChild(btnInc);
		
		btnDec = new ButtonAdapter(
			screen,
			UID + ":btnDec",
			new Vector2f(btnDecX, btnDecY),
			new Vector2f(getHeight(), btnIncH)
		) {
			@Override
			public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean toggled) {
				screen.setTabFocusElement((Spinner)getElementParent());
				((Spinner)getElementParent()).decStep();
			}
			@Override
			public void onButtonStillPressedInterval() {
				((Spinner)getElementParent()).decStep();
			}
		};
		btnDec.setButtonIcon(btnDecIconSize, btnDecIconSize, btnDecIcon);
		btnDec.setDocking(Docking.SW);
		addChild(btnDec);
		
		setIsEnabled(false);
	}
	
	/**
	 * Returns the index of the selected stepValue
	 * @return int
	 */
	public int getSelectedIndex() {
		return this.selectedIndex;
	}
	
	/**
	 * Sets the interval speed for the spinner
	 * @param callsPerSecond float
	 */
	public void setInterval(float callsPerSecond) {
		btnInc.setInterval(callsPerSecond);
		btnDec.setInterval(callsPerSecond);
	}
	
	/**
	 * Adds a stepValue to the Spinner
	 * @param value String
	 */
	public void addStepValue(String value) {
		stepValues.add(value);
		if (selectedIndex == -1)
			selectedIndex = 0;
		displaySelectedStep();
	}
	
	/**
	 * Removes the provided value for the stepValues of the Spinner
	 * @param value String
	 */
	public void removeStepValue(String value) {
		stepValues.remove(value);
	}
	
	/**
	 * Populates the stepValues with an integer range using the provided information
	 * @param min int The minimum value of the range
	 * @param max int The maximum values of the range
	 * @param inc int the ammount to increment from step to step
	 */
	public void setStepIntegerRange(int min, int max, int inc) {
		stepValues.clear();
		selectedIndex = -1;
		for (int i = min; i <= max; i += inc) {
			stepValues.add(String.valueOf(i));
		}
		if (selectedIndex == -1)
			selectedIndex = 0;
		displaySelectedStep();
	}
	
	/**
	 * Populates the stepValues with an float range using the provided information
	 * @param min float The minimum value of the range
	 * @param max float The maximum values of the range
	 * @param inc float the ammount to increment from step to step
	 */
	public void setStepFloatRange(float min, float max, float inc) {
		stepValues.clear();
		selectedIndex = -1;
		for (float i = min; i <= max; i += inc) {
			stepValues.add(String.valueOf(i));
		}
		if (selectedIndex == -1)
			setSelectedIndexWithCallback(0);
		displaySelectedStep();
	}
	
	/**
	 * Sets the selected index of the Spinner to the provided index and calls the onChange event
	 * @param selectedIndex int
	 */
	public void setSelectedIndexWithCallback(int selectedIndex) {
		if (selectedIndex < 0)
			selectedIndex = 0;
		else if (selectedIndex > stepValues.size()-1)
			selectedIndex = stepValues.size()-1;
		ChangeType change = selectedIndex > this.selectedIndex ? ChangeType.INC : ChangeType.DEC;
		this.selectedIndex = selectedIndex;
		displaySelectedStep();
		onChange(selectedIndex, stepValues.get(selectedIndex), change);
	}
	
	/**
	 * Sets the selected index of the Spinner to the provided index
	 * @param selectedIndex int
	 */
	public void setSelectedIndex(int selectedIndex) {
		if (selectedIndex < 0)
			selectedIndex = 0;
		else if (selectedIndex > stepValues.size()-1)
			selectedIndex = stepValues.size()-1;
		
		this.selectedIndex = selectedIndex;
		displaySelectedStep();
	}
	
	private void incStep() {
		selectedIndex++;
		if (selectedIndex == stepValues.size()) {
			if (cycle) {
				selectedIndex = 0;
			} else {
				selectedIndex--;
			}
		}
		displaySelectedStep();
		onChange(selectedIndex, stepValues.get(selectedIndex), ChangeType.INC);
	}
	
	private void decStep() {
		selectedIndex--;
		if (selectedIndex == -1) {
			if (cycle) {
				selectedIndex = stepValues.size()-1;
			} else {
				selectedIndex = 0;
			}
		}
		displaySelectedStep();
		onChange(selectedIndex, stepValues.get(selectedIndex), ChangeType.DEC);
	}
	
	private void displaySelectedStep() {
		this.setText(stepValues.get(selectedIndex));
	}
	
	@Override
	public void controlKeyPressHook(KeyInputEvent evt, String text) {
		if (evt.getKeyCode() == KeyInput.KEY_LEFT || evt.getKeyCode() == KeyInput.KEY_DOWN) {
			decStep();
		} else if (evt.getKeyCode() == KeyInput.KEY_RIGHT || evt.getKeyCode() == KeyInput.KEY_UP) {
			incStep();
		} else {
			displaySelectedStep();
		}
	}
	
	/**
	 * Abstract method called when the selectedIndex changes.
	 * @param selectedIndex int The new selectedIndex
	 * @param value The value associated with the selected index
         * @param changeType direction of change
	 */
	public abstract void onChange(int selectedIndex, String value, ChangeType changeType);
        
        public enum ChangeType{
            INC,
            DEC;
        }
}
