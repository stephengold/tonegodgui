package tonegod.gui.controls.text;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.util.ArrayList;
import java.util.List;
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


/**
 *
 * @author t0neg0d
 */
public class TextField extends Element implements Control, KeyboardListener, TabFocusListener, MouseFocusListener, MouseButtonListener {

	public static enum Type {
		DEFAULT,
		ALPHA,
		ALPHA_NOSPACE,
		NUMERIC,
		ALPHANUMERIC,
		ALPHANUMERIC_NOSPACE,
		EXCLUDE_SPECIAL,
		EXCLUDE_CUSTOM,
		INCLUDE_CUSTOM
	};
	private String validateAlpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
	private String validateAlphaNoSpace = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String validateNumeric = "0123456789.-";
	private String validateSpecChar = "`~!@#$%^&*()-_=+[]{}\\|;:'\",<.>/?";
	private String validateCustom = "";
	private String testString = "Gg|/X";
	private Element caret;
	private Material caretMat;
	protected int caretIndex = 0, head = 0, tail = 0;
	protected int rangeHead = -1, rangeTail = -1;
	protected int visibleHead = -1, visibleTail = -1;
	protected List<String> textFieldText = new ArrayList<>();
	protected String finalText = "", visibleText = "", textRangeText = "";
	protected BitmapText widthTest;
	private boolean hasTabFocus = false;
	protected float caretX = 0;
	private Type type = Type.DEFAULT;
	protected boolean ctrl = false, shift = false, alt = false, meta = false;
	private boolean isEnabled = true;
	private boolean forceUpperCase = false, forceLowerCase = false;
	private int maxLength = 0;
	private String nextChar;
	private boolean valid;
	private boolean copy = true, paste = true;
	private float firstClick = 0, secondClick = 0, compareClick = 0;
	private float firstClickDiff = 0, secondClickDiff = 0;
	private boolean doubleClick = false, tripleClick = false;
	private int clickCount = 0;
	private boolean isPressed = false;
	
	/**
	 * Creates a new instance of the TextField control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public TextField(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the TextField control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public TextField(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the TextField control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public TextField(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the TextField control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the TextField
	 */
	public TextField(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the TextField control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public TextField(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the TextField control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public TextField(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the TextField control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public TextField(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.setScaleEW(true);
		this.setScaleNS(false);
		this.setDocking(Docking.NW);
		
		compareClick = screen.getApplication().getTimer().getTimeInSeconds();
		
		float padding = screen.getStyle("TextField").getFloat("textPadding");
		
		this.setFontSize(screen.getStyle("TextField").getFloat("fontSize"));
		this.setTextPadding(padding);
		this.setTextWrap(LineWrapMode.valueOf(screen.getStyle("TextField").getString("textWrap")));
		this.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("TextField").getString("textAlign")));
		this.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("TextField").getString("textVAlign")));
		
		this.setMinDimensions(dimensions.clone());
		
		caret = new Element(screen, UID + ":Caret", new Vector2f(padding,padding), new Vector2f(dimensions.x-(padding*2), dimensions.y-(padding*2)), new Vector4f(0,0,0,0), null);
		
		caretMat = caret.getMaterial().clone();
		caretMat.setBoolean("IsTextField", true);
		caretMat.setTexture("ColorMap", null);
		caretMat.setColor("Color", getFontColor());
		
		caret.setLocalMaterial(caretMat);
		caret.setIgnoreMouse(true);
		caret.setScaleEW(true);
		caret.setScaleNS(false);
		caret.setDocking(Docking.SW);
		
		setTextFieldFontColor(screen.getStyle("TextField").getColorRGBA("fontColor"));
		this.addChild(caret);
		
		this.updateText("");
		
		populateEffects("TextField");
	}

	// Validation
	/**
	 * Sets the TextField.Type of the text field.  This can be used to enforce rules on the inputted text
	 * @param type Type
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * Returns the current Type of the TextField
	 * @return Type
	 */
	public Type getType() {
		return this.type;
	}
	
	/**
	 * Sets a custom validation rule for the TextField.
	 * @param grabBag String A list of character to either allow or disallow as input
	 */
	public void setCustomValidation(String grabBag) {
		validateCustom = grabBag;
	}
	
	/**
	 * Attempts to parse an int from the inputted text of the TextField
	 * @return int
	 * @throws NumberFormatException from parseInt
	 */
	public int parseInt() throws NumberFormatException {
		return Integer.parseInt(getText());
	}
	
	/**
	 * Attempts to parse a float from the inputted text of the TextField
	 * @return float
	 * @throws NumberFormatException from parseFloat
	 */
	public float parseFloat() throws NumberFormatException {
		return Float.parseFloat(getText());
	}
	
	/**
	 * Attempts to parse a short from the inputted text of the TextField
	 * @return short
	 * @throws NumberFormatException from parseShort
	 */
	public short parseShort() throws NumberFormatException {
		return Short.parseShort(getText());
	}
	
	/**
	 * Attempts to parse a double from the inputted text of the TextField
	 * @return double
	 * @throws NumberFormatException from parseDouble
	 */
	public double parseDouble() throws NumberFormatException {
		return Double.parseDouble(getText());
	}
	
	/**
	 * Attempts to parse a long from the inputted text of the TextField
	 * @return long
	 * @throws NumberFormatException from parseLong
	 */
	public long parseLong() throws NumberFormatException {
		return Long.parseLong(getText());
	}
	
	// Interaction
	@Override
	public void onKeyPress(KeyInputEvent evt) {
		if (evt.getKeyCode() == KeyInput.KEY_F1 || evt.getKeyCode() == KeyInput.KEY_F2 ||
			evt.getKeyCode() == KeyInput.KEY_F3 || evt.getKeyCode() == KeyInput.KEY_F4 ||
			evt.getKeyCode() == KeyInput.KEY_F5 || evt.getKeyCode() == KeyInput.KEY_F6 ||
			evt.getKeyCode() == KeyInput.KEY_F7 || evt.getKeyCode() == KeyInput.KEY_F8 ||
			evt.getKeyCode() == KeyInput.KEY_F9 || evt.getKeyCode() == KeyInput.KEY_CAPITAL ||
			evt.getKeyCode() == KeyInput.KEY_ESCAPE || evt.getKeyCode() == KeyInput.KEY_TAB) {
		} else if (evt.getKeyCode() == KeyInput.KEY_LCONTROL || evt.getKeyCode() == KeyInput.KEY_RCONTROL) {
			ctrl = true;
		} else if (evt.getKeyCode() == KeyInput.KEY_LSHIFT || evt.getKeyCode() == KeyInput.KEY_RSHIFT) {
			shift = true;
		} else if (evt.getKeyCode() == KeyInput.KEY_LMENU || evt.getKeyCode() == KeyInput.KEY_RMENU) {
			alt = true;
		} else if (evt.getKeyCode() == KeyInput.KEY_LMETA || evt.getKeyCode() == KeyInput.KEY_RMETA) {
			meta = true;
		} else if (evt.getKeyCode() == KeyInput.KEY_RETURN) {
		} else if (evt.getKeyCode() == KeyInput.KEY_DELETE) {
			if (rangeHead != -1 && rangeTail != -1)	editTextRangeText("");
			else {
				if (caretIndex < finalText.length()) textFieldText.remove(caretIndex);
			}
		} else if (evt.getKeyCode() == KeyInput.KEY_BACK) {
			if (rangeHead != -1 && rangeTail != -1) {
				editTextRangeText("");
			} else {
				if (caretIndex > 0) {
					textFieldText.remove(caretIndex-1);
					caretIndex--;
				}
			}
		} else if (evt.getKeyCode() == KeyInput.KEY_LEFT) {
			if (!shift) resetTextRange();
			if (caretIndex > -1) {
				if (Screen.isMac()) {
					if (meta) {
						caretIndex = 0;
						getVisibleText();
						if (shift) setTextRangeEnd(caretIndex);
						else {
							resetTextRange();
							setTextRangeStart(caretIndex);
						}
						return;
					}
				}
				
				if ((Screen.isMac() && !alt) ||
					(Screen.isWindows() && !ctrl) ||
					(Screen.isUnix() && !ctrl) ||
					(Screen.isSolaris() && !ctrl))
					caretIndex--;
				else {
					int cIndex = caretIndex;
					if (cIndex > 0)
						if (finalText.charAt(cIndex-1) == ' ')
							cIndex--;
					int index = 0;
					if (cIndex > 0) index = finalText.substring(0,cIndex).lastIndexOf(' ')+1;
					if (index < 0)	index = 0;
					caretIndex = index;
				}
				if (caretIndex < 0)
					caretIndex = 0;
				
				if (!shift) setTextRangeStart(caretIndex);
			}
		} else if (evt.getKeyCode() == KeyInput.KEY_RIGHT) {
			if (!shift) resetTextRange();
			if (caretIndex <= textFieldText.size()) {
				if (Screen.isMac()) {
					if (meta) {
						caretIndex = textFieldText.size();
						getVisibleText();
						if (shift) setTextRangeEnd(caretIndex);
						else {
							resetTextRange();
							setTextRangeStart(caretIndex);
						}
						return;
					}
				}
				
				if ((Screen.isMac() && !alt) ||
					(Screen.isWindows() && !ctrl) ||
					(Screen.isUnix() && !ctrl) ||
					(Screen.isSolaris() && !ctrl))
					caretIndex++;
				else {
					int cIndex = caretIndex;
					if (cIndex < finalText.length())
						if (finalText.charAt(cIndex) == ' ')
							cIndex++;
					int index;
					if (cIndex < finalText.length()) {
						index = finalText.substring(cIndex, finalText.length()).indexOf(' ');
						if (index == -1)	index = finalText.length();
						else				index += cIndex;
					} else {
						index = finalText.length();
					}
					caretIndex = index;
				}
				if (caretIndex > finalText.length())
					caretIndex = finalText.length();
				
				if (!shift) {
					if (caretIndex < textFieldText.size())	setTextRangeStart(caretIndex);
					else									setTextRangeStart(textFieldText.size());
				}
			}
		} else if (evt.getKeyCode() == KeyInput.KEY_END || evt.getKeyCode() == KeyInput.KEY_NEXT || evt.getKeyCode() == KeyInput.KEY_DOWN) {
			caretIndex = textFieldText.size();
			getVisibleText();
			if (shift)	setTextRangeEnd(caretIndex);
			else {
				resetTextRange();
				setTextRangeStart(caretIndex);
			}
		} else if (evt.getKeyCode() == KeyInput.KEY_HOME || evt.getKeyCode() == KeyInput.KEY_PRIOR || evt.getKeyCode() == KeyInput.KEY_UP) {
			caretIndex = 0;
			getVisibleText();
			if (shift)	setTextRangeEnd(caretIndex);
			else {
				resetTextRange();
				setTextRangeStart(caretIndex);
			}
		} else {
			if (ctrl) {
				if (evt.getKeyCode() == KeyInput.KEY_C) {
					if (copy)
						screen.setClipboardText(textRangeText);
				} else if (evt.getKeyCode() == KeyInput.KEY_V) {
					if (paste)
						this.pasteTextInto();
				}
			} else {
				if (isEnabled) {
					if (rangeHead != -1 && rangeTail != -1) {
						editTextRangeText("");
					}
					if (!shift) resetTextRange();
					nextChar = String.valueOf(evt.getKeyChar());
					if (forceUpperCase)			nextChar = nextChar.toUpperCase();
					else if (forceLowerCase)	nextChar = nextChar.toLowerCase();
					valid = true;
					if (maxLength > 0) {
						if (getText().length() >= maxLength) valid =false;
					}
					if (valid) {
						if (type == Type.DEFAULT) {
							textFieldText.add(caretIndex, nextChar);
							caretIndex++;
						} else if (type == Type.ALPHA) {
							if (validateAlpha.contains(nextChar)) {
								textFieldText.add(caretIndex, nextChar);
								caretIndex++;
							}
						} else if (type == Type.ALPHA_NOSPACE) {
							if (validateAlpha.contains(nextChar)) {
								textFieldText.add(caretIndex, nextChar);
								caretIndex++;
							}
						} else if (type == Type.NUMERIC) {
							if (validateNumeric.contains(nextChar)) {
								textFieldText.add(caretIndex, nextChar);
								caretIndex++;
							}
						} else if (type == Type.ALPHANUMERIC) {
							if (validateAlpha.contains(nextChar) || validateNumeric.contains(nextChar)) {
								textFieldText.add(caretIndex, nextChar);
								caretIndex++;
							}
						} else if (type == Type.ALPHANUMERIC_NOSPACE) {
							if (validateAlphaNoSpace.contains(nextChar) || validateNumeric.contains(nextChar)) {
								textFieldText.add(caretIndex, nextChar);
								caretIndex++;
							}
						} else if (type == Type.EXCLUDE_SPECIAL) {
							if (validateSpecChar.contains(nextChar)) {
								textFieldText.add(caretIndex, nextChar);
								caretIndex++;
							}
						} else if (type == Type.EXCLUDE_CUSTOM) {
							if (validateCustom.contains(nextChar)) {
								textFieldText.add(caretIndex, nextChar);
								caretIndex++;
							}
						} else if (type == Type.INCLUDE_CUSTOM) {
							if (validateCustom.contains(nextChar)) {
								textFieldText.add(caretIndex, nextChar);
								caretIndex++;
							}
						}
					}
					if (!shift) {
						if (caretIndex < textFieldText.size())	setTextRangeStart(caretIndex);
						else									setTextRangeStart(textFieldText.size());
					}
				}
			}
		}
		this.updateText(getVisibleText());
		
		if (shift && (evt.getKeyCode() == KeyInput.KEY_LEFT || evt.getKeyCode() == KeyInput.KEY_RIGHT)) setTextRangeEnd(caretIndex);
		
		centerTextVertically();
		
		controlKeyPressHook(evt, getText());
		evt.setConsumed();
	}
	
	/**
	 * An overridable hook for the onKeyPress event of the TextField
	 * @param evt KeyInputEvent
	 * @param text String
	 */
	public void controlKeyPressHook(KeyInputEvent evt, String text) {  }
	
	@Override
	public void onKeyRelease(KeyInputEvent evt) {
		if (evt.getKeyCode() == KeyInput.KEY_LCONTROL || evt.getKeyCode() == KeyInput.KEY_RCONTROL) {
			ctrl = false;
		} else if (evt.getKeyCode() == KeyInput.KEY_LSHIFT || evt.getKeyCode() == KeyInput.KEY_RSHIFT) {
			shift = false;
		} else if (evt.getKeyCode() == KeyInput.KEY_LMENU || evt.getKeyCode() == KeyInput.KEY_RMENU) {
			alt = false;
		} else if (evt.getKeyCode() == KeyInput.KEY_LMETA || evt.getKeyCode() == KeyInput.KEY_RMETA) {
			meta = false;
		}
		evt.setConsumed();
	}
	
	/**
	 * Internal use - NEVER USE THIS!!
	 */
	protected void getTextFieldText() {
		String ret = "";
		int index = 0;
		for (String s : textFieldText) {
			ret += s;
			index++;
		}
		finalText = ret;
	}
	
	/**
	 * This method now forwards to setText.  Feel free to use setText directly.
	 * @param text String The text to set for the TextField
	 */
	@Deprecated
	public void setTextFieldText(String text) {
		setText(text);
	}
	
	@Override
	public void setText(String s) {
		caretIndex = 0;
		
		textFieldText.clear();
		for (int i = 0; i < s.length(); i++) {
			textFieldText.add(caretIndex, String.valueOf(s.charAt(i)));
			caretIndex++;
		}
		this.updateText(getVisibleText());
		
		setCaretPositionToEnd();
		
		centerTextVertically();
	}
	
	@Override
	public final void setFontSize(float fontSize) {
		this.fontSize = fontSize;
		
		if (textElement != null) {
			textElement.setSize(fontSize);
		}
	}
	
	@Override
	public String getText() {
		String ret = "";
		int index = 0;
		for (String s : textFieldText) {
			ret += s;
			index++;
		}
		return ret;
	}
	
	/**
	 * Returns the visible portion of the TextField's text
	 * @return String
	 */
	protected String getVisibleText() {
		getTextFieldText();
		
		widthTest = new BitmapText(font);
		widthTest.setBox(null);
		widthTest.setSize(getFontSize());

		int index1 = 0, index2;
		
		widthTest.setText(finalText);
		if (head == -1 || tail == -1 || widthTest.getLineWidth() < getWidth()) {
			head = 0;
			tail = finalText.length();
			if (head != tail && head != -1 && tail != -1)
				visibleText = finalText.substring(head, tail);
			else
				visibleText = "";
		} else if (caretIndex < head) {
			head = caretIndex;
			index2 = caretIndex;
			if (index2 == caretIndex && caretIndex != textFieldText.size()) {
				index2 = caretIndex+1;
				widthTest.setText(finalText.substring(caretIndex, index2));
				while(widthTest.getLineWidth() < getWidth()-(getTextPadding()*2)) {
					if (index2 == textFieldText.size())
						break;
					widthTest.setText(finalText.substring(caretIndex, index2+1));
					if (widthTest.getLineWidth() < getWidth()-(getTextPadding()*2)) {
						index2++;
					}
				}
			}
			if (index2 != textFieldText.size()) index2++;
			tail = index2;
			if (head != tail && head != -1 && tail != -1)
				visibleText = finalText.substring(head, tail);
			else
				visibleText = "";
		} else if (caretIndex > tail) {
			tail = caretIndex;
			index2 = caretIndex;
			if (index2 == caretIndex && caretIndex != 0) {
				index2 = caretIndex-1;
				widthTest.setText(finalText.substring(index2, caretIndex));
				while(widthTest.getLineWidth() < getWidth()-(getTextPadding()*2)) {
					if (index2 == 0)
						break;
					widthTest.setText(finalText.substring(index2-1, caretIndex));
					if (widthTest.getLineWidth() < getWidth()-(getTextPadding()*2)) {
						index2--;
					}
				}
			}
			head = index2;
			if (head != tail && head != -1 && tail != -1)
				visibleText = finalText.substring(head, caretIndex);
			else
				visibleText = "";
		} else {
			index2 = tail;
			if (index2 > finalText.length())
				index2 = finalText.length();
			if (tail != head) {
				widthTest.setText(finalText.substring(head, index2));
				if (widthTest.getLineWidth() > getWidth()-(getTextPadding()*2)) {
					while(widthTest.getLineWidth() > getWidth()-(getTextPadding()*2)) {
						if (index2 == head)
							break;
						widthTest.setText(finalText.substring(head, index2-1));
						if (widthTest.getLineWidth() > getWidth()-(getTextPadding()*2)) {
							index2--;
						}
					}
				} else if (widthTest.getLineWidth() < getWidth()-(getTextPadding()*2)) {
					while(widthTest.getLineWidth() < getWidth()-(getTextPadding()*2) && index2 < finalText.length()) {
						if (index2 == head)
							break;
						widthTest.setText(finalText.substring(head, index2+1));
						if (widthTest.getLineWidth() < getWidth()-(getTextPadding()*2)) {
							index2++;
						}
						
					}
				}
			}
			tail = index2;
			if (head != tail && head != -1 && tail != -1)
				visibleText = finalText.substring(head, tail);
			else
				visibleText = "";
		}
		
		String testString = "";
		widthTest.setText(".");
		float fixWidth = widthTest.getLineWidth();
		boolean useFix = false;
		
		if (!finalText.equals("")) {
			try {
				testString = finalText.substring(head, caretIndex);
				if (testString.charAt(testString.length()-1) == ' ') {
					testString += ".";
					useFix = true;
				}
			} catch (Exception ex) {  }
		}

		widthTest.setText(testString);
		float nextCaretX = widthTest.getLineWidth();
		if (useFix) nextCaretX -= fixWidth;

		caretX = nextCaretX;
		setCaretPosition(getAbsoluteX()+caretX);
		
		return visibleText;
	}
	
	private void setCaretPositionToIndex() {
		widthTest.setText(".");
		float fixWidth = widthTest.getLineWidth();
		boolean useFix = false;
		
		if (!finalText.equals("")) {
			String testString = finalText.substring(head, caretIndex);

			try {
				if (testString.charAt(testString.length()-1) == ' ') {
					testString += ".";
					useFix = true;
				}
			} catch (Exception ex) {  }

			widthTest.setText(testString);
			float nextCaretX = widthTest.getLineWidth();
			if (useFix) nextCaretX -= fixWidth;

			caretX = nextCaretX;
			setCaretPosition(getAbsoluteX()+caretX);
		}
	}
	
	/**
	 * For internal use - do not call this method
	 * @param caretX float
	 */
	protected void setCaretPosition(float caretX) {
		if (textElement != null) {
			if (hasTabFocus) {
				caret.getMaterial().setFloat("CaretX", caretX+getTextPadding());
				caret.getMaterial().setFloat("LastUpdate", app.getTimer().getTimeInSeconds());
			}
		}
	}
	
	/**
	 * For internal use - do not call this method
	 * @param x float
	 */
	private void setCaretPositionByX(float x) {
		int index1 = visibleText.length();
		if (visibleText.length() > 0) {
			widthTest.setSize(getFontSize());
			widthTest.setText(visibleText.substring(0, index1));
			while(caret.getAbsoluteX()+widthTest.getLineWidth() > (x+getTextPadding())) {
				index1--;
				widthTest.setText(visibleText.substring(0, index1));
			}
			caretX = widthTest.getLineWidth();
		}
		caretIndex = head+index1;
		setCaretPosition(getAbsoluteX()+caretX);
		if (!shift) {
			resetTextRange();
			setTextRangeStart(caretIndex);
		} else {
			setTextRangeEnd(caretIndex);
		}
	}
	
	private void setCaretPositionByXNoRange(float x) {
		int index1 = visibleText.length();
		if (visibleText.length() > 0) {
			String testString = "";
			widthTest.setText(".");
			float fixWidth = widthTest.getLineWidth();
			boolean useFix = false;
			
			widthTest.setSize(getFontSize());
			widthTest.setText(visibleText.substring(0, index1));
			while(caret.getAbsoluteX()+widthTest.getLineWidth() > (x+getTextPadding())) {
				if (index1 > 0) {
					index1--;
					testString = visibleText.substring(0, index1);
					widthTest.setText(testString);
				} else {
					break;
				}
			}
		
			try {
				testString = finalText.substring(head, caretIndex);
				if (testString.charAt(testString.length()-1) == ' ') {
					testString += ".";
					useFix = true;
				}
			} catch (Exception ex) {  }
		

			widthTest.setText(testString);
			float nextCaretX = widthTest.getLineWidth();
			if (useFix) nextCaretX -= fixWidth;

			caretX = nextCaretX;
		}
		caretIndex = head+index1;
		setCaretPosition(getAbsoluteX()+caretX);
	}
	
	/**
	 * Sets the caret position to the end of the TextField's text
	 */
	public void setCaretPositionToEnd() {
		int index1 = visibleText.length();
		if (visibleText.length() > 0) {
			widthTest.setText(visibleText.substring(0, index1));
			caretX = widthTest.getLineWidth();
		}
		caretIndex = head+index1;
		setCaretPosition(getAbsoluteX()+caretX);
		resetTextRange();
	}
	
	private void pasteTextInto() {
		String text = screen.getClipboardText();
		editTextRangeText(text);
	}
	
	/**
	 * Sets the ColorRGBA value used for text and caret
	 * @param fontColor ColorRGBA
	 */
	public final void setTextFieldFontColor(ColorRGBA fontColor) {
		setFontColor(fontColor);
		caretMat.setColor("Color", fontColor);
	}
	
	/**
	 * Enables/disables the TextField
	 * @param isEnabled boolean
	 */
        @Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	/**
	 * Returns if the TextField is currently enabled/disabled
	 * @return boolean
	 */
        @Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	/**
	 * Enables/disables the use of the Copy text feature
	 * @param copy boolean
	 */
	public void setAllowCopy(boolean copy) {
		this.copy = copy;
	}
	
	/**
	 * Returns if the Copy feature is enabled/disabled
	 * @return copy
	 */
	public boolean getAllowCopy() {
		return this.copy;
	}
	
	/**
	 * Eanbles/disables use of the Paste text feature
	 * @param paste boolean
	 */
	public void setAllowPaste(boolean paste) {
		this.paste = paste;
	}
	
	/**
	 * Returns if the Paste feature is enabled/disabled
	 * @return paste
	 */
	public boolean getAllowPaste() {
		return this.paste;
	}
	
	/**
	 * Enables/disables both the Copy and Paste feature
	 * @param copyAndPaste boolean
	 */
	public void setAllowCopyAndPaste(boolean copyAndPaste) {
		this.copy = copyAndPaste;
		this.paste = copyAndPaste;
	}
	
	/**
	 * Forces all text input to uppercase
	 * @param forceUpperCase boolean
	 */
	public void setForceUpperCase(boolean forceUpperCase) {
		this.forceUpperCase = forceUpperCase;
		this.forceLowerCase = false;
	}
	
	/**
	 * Returns if the TextField is set to force uppercase
	 * @return boolean
	 */
	public boolean getForceUpperCase() {
		return this.forceUpperCase;
	}
	
	/**
	 * Forces all text input to lowercase.
         * @param forceLowerCase true &rarr; force lowercase, false &rarr; don't force
	 */
	public void setForceLowerCase(boolean forceLowerCase) {
		this.forceLowerCase = forceLowerCase;
		this.forceUpperCase = false;
	}
	
	/**
	 * Returns if the TextField is set to force lowercase
	 * @return boolean true if forcing lowercase, otherwise false
	 */
	public boolean getForceLowerCase() {
		return this.forceLowerCase;
	}
	
	/**
	 * Set the maximum character limit for the TextField.  0 = unlimited
	 * @param maxLength int
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	/**
	 * Returns the maximum limit of character allowed for this TextField
	 * @return int
	 */
	public int getMaxLength() {
		return this.maxLength;
	}
	
	@Override
	public void setTabFocus() {
		hasTabFocus = true;
		setTextRangeStart(caretIndex);
		if (isEnabled)
			caret.getMaterial().setBoolean("HasTabFocus", true);
		screen.setKeyboardElement(this);
		controlTextFieldSetTabFocusHook();
		Effect effect = getEffect(Effect.EffectEvent.TabFocus);
		if (effect != null) {
			effect.setColor(ColorRGBA.DarkGray);
			screen.getEffectManager().applyEffect(effect);
		}
		if (isEnabled && !this.controls.contains(this)) {
			addControl(this);
		}
	}
	
	@Override
	public void resetTabFocus() {
		hasTabFocus = false;
		shift = false;
		ctrl = false;
		alt = false;
		caret.getMaterial().setBoolean("HasTabFocus", false);
		screen.setKeyboardElement(null);
		controlTextFieldResetTabFocusHook();
		Effect effect = getEffect(Effect.EffectEvent.LoseTabFocus);
		if (effect != null) {
			effect.setColor(ColorRGBA.White);
			screen.getEffectManager().applyEffect(effect);
		}
		if (isEnabled && this.controls.contains(this)) {
			removeControl(this);
		}
	}
	
	/**
	 * Overridable hook for receive tab focus event
	 */
	public void controlTextFieldSetTabFocusHook() {  }
	
	/**
	 * Overridable hook for lose tab focus event
	 */
	public void controlTextFieldResetTabFocusHook() {  }

	@Override
	public void onGetFocus(MouseMotionEvent evt) {
		if (getIsEnabled()) screen.setCursor(CursorType.TEXT);
		setHasFocus(true);
	}

	@Override
	public void onLoseFocus(MouseMotionEvent evt) {
		if (getIsEnabled()) screen.setCursor(CursorType.POINTER);
		setHasFocus(false);
	}
	
	public final void updateText(String text) {
		this.text = text;
		if (textElement == null) {
			textElement = new BitmapText(font);
			textElement.setBox(new Rectangle(0,0,getDimensions().x,getDimensions().y));
		//	textElement = new TextElement(screen, Vector2f.ZERO);
			centerTextVertically();
		}
		textElement.setLineWrapMode(textWrap);
		textElement.setAlignment(textAlign);
		textElement.setVerticalAlignment(textVAlign);
		textElement.setSize(fontSize);
		textElement.setColor(fontColor);
		textElement.setText(text);
		updateTextElement();
		if (textElement.getParent() == null) {
			this.attachChild(textElement);
		}
		centerTextVertically();
	}
	
	private void centerTextVertically() {
		
		float height = BitmapTextUtil.getTextLineHeight(this, testString);
		float nextY = height-FastMath.floor(getHeight());
		nextY /= 2;
		nextY = (float)FastMath.ceil(nextY)+1;
		
		setTextPosition(getTextPosition().x, -nextY);
	}
	
	@Override
	public void onMouseLeftPressed(MouseButtonEvent evt) {
		if (this.isEnabled) {
			float time = screen.getApplication().getTimer().getTimeInSeconds();
			if (time-compareClick > .2f) resetClickCounter();
			compareClick = time;
			
			isPressed = true;
			clickCount++;
			
			switch (clickCount) {
				case 1:
					firstClick = time;
					resetTextRange();
					setCaretPositionByXNoRange(evt.getX());
					if (caretIndex >= 0)
						this.setTextRangeStart(caretIndex);
					else
						this.setTextRangeStart(0);
					break;
				case 2:
					secondClick = time;
					firstClickDiff = time-firstClick;
					if (firstClickDiff <= 0.2f) {
						doubleClick = true;
					} else {
						resetClickCounter();
					}
					break;
				case 3:
					secondClickDiff = time-secondClick;
					if (secondClickDiff <= 0.2f) {
						tripleClick = true;
					}
					resetClickCounter();
					break;
				default:
					resetClickCounter();
			}
		}
	}
	
	private void resetClickCounter() {
		clickCount = 0;
		firstClick = 0;
		secondClick = 0;
		firstClickDiff = 0;
		secondClickDiff = 0;
	}
	
	@Override
	public void onMouseLeftReleased(MouseButtonEvent evt) {
		if (isEnabled) {
			if (isPressed) {
				isPressed = false;
				if (doubleClick) {
					selectTextRangeDoubleClick();
					doubleClick = false;
				} else if (tripleClick) {
					selectTextRangeTripleClick();
					tripleClick = false;
				} else {
					setCaretPositionByXNoRange(evt.getX());
					if (caretIndex >= 0)
						this.setTextRangeEnd(caretIndex);
					else
						this.setTextRangeEnd(0);
				}
			}
		}
	}

	@Override
	public void onMouseRightPressed(MouseButtonEvent evt) {  }

	@Override
	public void onMouseRightReleased(MouseButtonEvent evt) {  }
	
	private void stillPressedInterval() {
		if (screen.getMouseXY().x > getAbsoluteWidth() && caretIndex < finalText.length())
			caretIndex++;
		else if (screen.getMouseXY().x < getAbsoluteX() && caretIndex > 0)
			caretIndex--;
		updateText(getVisibleText());
		setCaretPositionByXNoRange(screen.getMouseXY().x);
		if (caretIndex >= 0)
			this.setTextRangeEnd(caretIndex);
		else
			this.setTextRangeEnd(0);
	}
	
	// Text Range methods
	/**
	 * Sets the current text range to all text within the TextField
	 */
	public void selectTextRangeAll() {
		setTextRangeStart(0);
		setTextRangeEnd(finalText.length());
		caretIndex = finalText.length();
		getVisibleText();
	}
	
	/**
	 * Resets the current text range
	 */
	public void selectTextRangeNone() {
		this.resetTextRange();
	}
	
	/**
	 * Sets the current text range to the first instance of the provided string, if found
	 * @param s The String to search for
	 */
	public void selectTextRangeBySubstring(String s) {
		int head = finalText.indexOf(s);
		if (head != -1) {
			setTextRangeStart(head);
			int tail = head+s.length();
			setTextRangeEnd(tail);
			caretIndex = tail;
			getVisibleText();
		}
	}
	
	/**
	 * Sets the selected text range to head-tail or tail-head depending on the provided indexes.
	 * Selects nothing if either of the provided indexes are out of range
	 * @param head The start or end index of the desired text range
	 * @param tail The end or start index of the desired text range
	 */
	public void selectTextRangeByIndex(int head, int tail) {
		int nHead = head;
		int nTail = tail;
		if (head > tail) {
			nHead = tail;
			nTail = head;
		}
		if (nHead < 0) nHead = 0;
		if (nTail > finalText.length()) nTail = finalText.length();
		
		this.setTextRangeStart(nHead);
		this.setTextRangeEnd(nTail);
		caretIndex = nTail;
		getVisibleText();
	}
	
	private void selectTextRangeDoubleClick() {
		if (!finalText.equals("")) {
			int end;
			if (finalText.substring(caretIndex, finalText.length()).indexOf(' ') != -1)
				end = caretIndex+finalText.substring(caretIndex, finalText.length()).indexOf(' ');
			else
				end = caretIndex+finalText.substring(caretIndex, finalText.length()).length();
			int start = finalText.substring(0,caretIndex).lastIndexOf(' ')+1;
			if (start == -1) start = 0;
			setTextRangeStart(start);
			caretIndex = end;
			updateText(getVisibleText());
			setTextRangeEnd(end);
		}
	}
	
	private void selectTextRangeTripleClick() {
		if (!finalText.equals("")) {
			caretIndex = finalText.length();
			updateText(getVisibleText());
			setTextRangeStart(0);
			setTextRangeEnd(finalText.length());
		}
	}
	
	private void setTextRangeStart(int head) {
		if (!visibleText.equals("")) {
			rangeHead = head;
		}
	}
	
	private void setTextRangeEnd(int tail) {
		if (!visibleText.equals("") && rangeHead != -1) {
			widthTest.setSize(getFontSize());
			
			widthTest.setText(".");
			float diff = widthTest.getLineWidth();
			
			float rangeX;
			
			if (rangeHead-this.head <= 0) {
				widthTest.setText("");
				rangeX = widthTest.getLineWidth();
			} else if(rangeHead-this.head < visibleText.length()) {
				widthTest.setText(visibleText.substring(0, rangeHead-this.head));
				float width = widthTest.getLineWidth();
				if (widthTest.getText().length() > 0) {
					if (widthTest.getText().charAt(widthTest.getText().length()-1) == ' ') {
						widthTest.setText(widthTest.getText() + ".");
						width = widthTest.getLineWidth()-diff;
					}
				}
				rangeX = width;
			} else {
				widthTest.setText(visibleText);
				rangeX = widthTest.getLineWidth();
			}
			
			if (rangeHead >= this.head)
				rangeX = getAbsoluteX()+rangeX+getTextPadding();
			else
				rangeX = getTextPadding();
			
			rangeTail = tail;
			if (tail-this.head <= 0)
				widthTest.setText("");
			else if (tail-this.head < visibleText.length())
				widthTest.setText(visibleText.substring(0, tail-this.head));
			else
				widthTest.setText(visibleText);
			
			textRangeText = (rangeHead < rangeTail) ? finalText.substring(rangeHead, rangeTail) : finalText.substring(rangeTail, rangeHead);
			
			float rangeW = getTextPadding();
			if (rangeTail <= this.tail) {
				float width = widthTest.getLineWidth();
				if (widthTest.getText().length() > 0) {
					if (widthTest.getText().charAt(widthTest.getText().length()-1) == ' ') {
						widthTest.setText(widthTest.getText() + ".");
						width = widthTest.getLineWidth()-diff;
					}
				}
				rangeW = getAbsoluteX()+width+getTextPadding();
			}
			
			if (rangeHead > rangeTail) {
				caret.getMaterial().setFloat("TextRangeStart", rangeW);
				caret.getMaterial().setFloat("TextRangeEnd", rangeX);
			} else {
				caret.getMaterial().setFloat("TextRangeStart", rangeX);
				caret.getMaterial().setFloat("TextRangeEnd", rangeW);
			}
			
			caret.getMaterial().setBoolean("ShowTextRange", true);
		}
	}
	
	private void resetTextRange() {
		textRangeText = "";
		rangeHead = -1;
		rangeTail = -1;
		caret.getMaterial().setFloat("TextRangeStart", 0);
		caret.getMaterial().setFloat("TextRangeEnd", 0);
		caret.getMaterial().setBoolean("ShowTextRange", false);
	}
	
	private void editTextRangeText(String insertText) {
		int head, tail;
		if (rangeHead != -1 && rangeTail != -1) {
			head = rangeHead;
			tail = rangeTail;
			if (head < 0) head = 0;
			else if (head > finalText.length()) head = finalText.length();
			if (tail < 0) tail = 0;
			else if (tail > finalText.length()) tail = finalText.length();
			resetTextRange();
		} else {
			head = caretIndex-1;
			if (head == -1)
				head = 0;
			tail = caretIndex;
		}
		String newText;
		int tempIndex;
		if (tail > head)	{
			newText = finalText.substring(0,head) + insertText + finalText.substring(tail, finalText.length());
			tempIndex = head+insertText.length();
		} else {
			newText = finalText.substring(0,tail) + insertText + finalText.substring(head, finalText.length());
			tempIndex = tail+insertText.length();
		}
		
		try { newText = newText.replace("\r", ""); }
		catch (Exception ex) {  }
		
		try { newText = newText.replace("\n", ""); }
		catch (Exception ex) {  }
		
		if (this.type != Type.DEFAULT) {
			String grabBag = "";
			switch (type) {
				case EXCLUDE_CUSTOM:
					grabBag = validateCustom;
					break;
				case EXCLUDE_SPECIAL:
					grabBag = validateSpecChar;
					break;
				case ALPHA:
					grabBag = validateAlpha;
					break;
				case ALPHA_NOSPACE:
					grabBag = validateAlphaNoSpace;
					break;
				case NUMERIC:
					grabBag = validateNumeric;
					break;
				case ALPHANUMERIC:
					grabBag = validateAlpha + validateNumeric;
					break;
				case ALPHANUMERIC_NOSPACE:
					grabBag = validateAlphaNoSpace + validateNumeric;
					break;
			}
			if (this.type == Type.EXCLUDE_CUSTOM || this.type == Type.EXCLUDE_SPECIAL) {
				for (int i = 0; i < grabBag.length(); i++) {
					try {
						String ret = newText.replace(String.valueOf(grabBag.charAt(i)), "");
						if (ret != null)
							newText = ret;
					} catch (Exception ex) {  }
				}
			} else {
				String ret = newText;
				for (int i = 0; i < newText.length(); i++) {
					try {
						int index = grabBag.indexOf(String.valueOf(newText.charAt(i)));
						if (index == -1) {
							String temp = ret.replace(String.valueOf(String.valueOf(newText.charAt(i))), "");
							if (temp != null)
								ret = temp;
						}
					} catch (Exception ex) {  }
				}
				if (!ret.equals(""))
					newText = ret;
			}
			tempIndex = newText.length();
		}
		
		if (maxLength != 0 && newText.length() > maxLength) {
			newText = newText.substring(0, maxLength);
			tempIndex = maxLength;
		}
		
		int testIndex = (head > tail) ? tail : head;
		
		setText(newText);
		
		caretIndex = testIndex;
	}
	
	// Control methods
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return this;
	}
	
	@Override
	public void setSpatial(Spatial spatial) {  }
	
	@Override
	public void update(float tpf) {
		if (isPressed) {
			stillPressedInterval();
		}
	}
	
	@Override
	public void render(RenderManager rm, ViewPort vp) {  }
}
