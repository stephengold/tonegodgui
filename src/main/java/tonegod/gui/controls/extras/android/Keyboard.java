/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.android;

import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.ElementManager;
import tonegod.gui.style.Style;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public class Keyboard extends Panel {
	private static enum KeyType {
		NUMERIC,
		ALPHA,
		SYMBOL,
		OTHER
	}
	
	private boolean Symbol = false;
	
	private List<KeyboardKey> keys = new ArrayList<>();
	
	/**
	 * Creates a new instance of the Panel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public Keyboard(ElementManager screen) {
		this(screen, UIDUtil.getUID(),
			new Vector2f(0,screen.getHeight()-(screen.getHeight()*0.45f)),
			new Vector2f(screen.getWidth(),(screen.getHeight()*0.45f)),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Panel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Panel
	 */
	private Keyboard(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.setIsMovable(false);
		this.setIsResizable(false);
		this.setScaleNS(false);
		this.setScaleEW(false);
		this.setClipPadding(screen.getStyle("Window").getFloat("clipPadding"));
		this.setResetKeyboardFocus(false);
		
		populateEffects("Window");
		
		float nWidth = getWidth()*0.1f-(110*0.1f);
		float nX = nWidth+10;
		float nHeight = getHeight()*0.2f-(60*0.2f);
		float nY = nHeight+10;
		float xGap = 10;
		
		KeyboardKey key;
		Style keyboard = screen.getStyle("Keyboard");
		
		for (int r = 0; r < 2; r++) {
			for (int i = 0; i < 10; i++) {
				KeyType type = KeyType.valueOf(keyboard.getString("R" + r + "K" + i + "KeyType"));
				String label = keyboard.getString("R" + r + "K" + i + "DefaultLabel");
				String shiftlabel = keyboard.getString("R" + r + "K" + i + "ShiftLabel");
				String symbollabel = keyboard.getString("R" + r + "K" + i + "SymbolLabel");
				symbollabel = validateSymbol(symbollabel);
				
				key = new KeyboardKey(type, KeyInput.KEY_UNLABELED, label.charAt(0), label);
				key.setShift(type, KeyInput.KEY_UNLABELED, shiftlabel.charAt(0), shiftlabel);
				key.setSymbol(KeyType.SYMBOL, KeyInput.KEY_UNLABELED, symbollabel.charAt(0), symbollabel);
				key.setPosition(xGap+(nX*i),10+(nY*r));
				key.setDimensions(nWidth,nHeight);
				key.createButton();
				keys.add(key);
				addChild(key.getButton());
			}
		}
		
		// Row 3 - Alpha
		xGap = 10+(nWidth*0.5f);
		
		int r = 2;
		for (int i = 0; i < 9; i++) {
			KeyType type = KeyType.valueOf(keyboard.getString("R" + r + "K" + i + "KeyType"));
			String label = keyboard.getString("R" + r + "K" + i + "DefaultLabel");
			String shiftlabel = keyboard.getString("R" + r + "K" + i + "ShiftLabel");
			String symbollabel = keyboard.getString("R" + r + "K" + i + "SymbolLabel");
			symbollabel = validateSymbol(symbollabel);
			
			key = new KeyboardKey(type, KeyInput.KEY_UNLABELED, label.charAt(0), label);
			key.setShift(type, KeyInput.KEY_UNLABELED, shiftlabel.charAt(0), shiftlabel);
			key.setSymbol(KeyType.SYMBOL, KeyInput.KEY_UNLABELED, symbollabel.charAt(0), symbollabel);
			key.setPosition(xGap+(nX*i),10+(nY*r));
			key.setDimensions(nWidth,nHeight);
			key.createButton();
			keys.add(key);
			addChild(key.getButton());
		}
		
		r = 3;
		for (int i = 0; i < 7; i++) {
			KeyType type = KeyType.valueOf(keyboard.getString("R" + r + "K" + i + "KeyType"));
			String label = keyboard.getString("R" + r + "K" + i + "DefaultLabel");
			String shiftlabel = keyboard.getString("R" + r + "K" + i + "ShiftLabel");
			String symbollabel = keyboard.getString("R" + r + "K" + i + "SymbolLabel");
			symbollabel = validateSymbol(symbollabel);
			
			key = new KeyboardKey(type, KeyInput.KEY_UNLABELED, label.charAt(0), label);
			key.setShift(type, KeyInput.KEY_UNLABELED, shiftlabel.charAt(0), shiftlabel);
			key.setSymbol(KeyType.SYMBOL, KeyInput.KEY_UNLABELED, symbollabel.charAt(0), symbollabel);
			key.setPosition(xGap+(nX*(i+1)),10+(nY*r));
			key.setDimensions(nWidth,nHeight);
			key.createButton();
			keys.add(key);
			addChild(key.getButton());
		}
		
		// Fixed Function Keys
		key = new KeyboardKey(KeyType.OTHER, KeyInput.KEY_LSHIFT, '^', keyboard.getString("ShiftLabel"));
		key.setPosition(10,10+(nY*3));
		key.setDimensions(nWidth+(nWidth*0.5f),nHeight);
		key.createShiftButton();
		keys.add(key);
		addChild(key.getButton());
		
		key = new KeyboardKey(KeyType.OTHER, KeyInput.KEY_BACK, '^', keyboard.getString("BackspaceLabel"));
		key.setPosition(xGap+(nX*8),10+(nY*3));
		key.setDimensions(nWidth+(nWidth*0.5f),nHeight);
		key.createBackButton();
		keys.add(key);
		addChild(key.getButton());
		
		key = new KeyboardKey(KeyType.OTHER, KeyInput.KEY_UNLABELED, '^', keyboard.getString("SymbolLabel"));
		key.setPosition(10,10+(nY*4));
		key.setDimensions(nWidth+(nWidth*0.5f),nHeight);
		key.createSymbolButton();
		keys.add(key);
		addChild(key.getButton());
		
		key = new KeyboardKey(KeyType.OTHER, KeyInput.KEY_SPACE, ' ', keyboard.getString("SpacebarLabel"));
		key.setPosition(xGap+(nX),10+(nY*4));
		key.setDimensions(nWidth+(nX*4),nHeight);
		key.createSpaceButton();
		keys.add(key);
		addChild(key.getButton());
		
		key = new KeyboardKey(KeyType.OTHER, KeyInput.KEY_UNLABELED, ',', ",");
		key.setPosition(xGap+(nX*6),10+(nY*4));
		key.setDimensions(nWidth,nHeight);
		key.createButton();
		keys.add(key);
		addChild(key.getButton());
		
		key = new KeyboardKey(KeyType.OTHER, KeyInput.KEY_UNLABELED, '.', ".");
		key.setPosition(xGap+(nX*7),10+(nY*4));
		key.setDimensions(nWidth,nHeight);
		key.createButton();
		keys.add(key);
		addChild(key.getButton());
		
		key = new KeyboardKey(KeyType.OTHER, KeyInput.KEY_RETURN, '^', keyboard.getString("EnterLabel"));
		key.setPosition(xGap+(nX*8),10+(nY*4));
		key.setDimensions(nWidth+(nWidth*0.5f),nHeight);
		key.createEnterButton();
		keys.add(key);
		addChild(key.getButton());
	}
	
	public void setUseIcons(boolean useIcons) {
		if (useIcons) {
			KeyboardKey shift = getFunctionKey(KeyInput.KEY_LSHIFT);
			shift.getButton().setButtonIcon(16, 16, screen.getStyle("Keyboard").getString("shiftImg"));
			shift.getButton().setText("");
			KeyboardKey ret = getFunctionKey(KeyInput.KEY_RETURN);
			ret.getButton().setButtonIcon(16, 16, screen.getStyle("Keyboard").getString("returnImg"));
			ret.getButton().setText("");
			KeyboardKey bs = getFunctionKey(KeyInput.KEY_BACK);
			bs.getButton().setButtonIcon(32, 16, screen.getStyle("Keyboard").getString("backspaceImg"));
			bs.getButton().setText("");
			KeyboardKey space = getFunctionKey(KeyInput.KEY_SPACE);
			space.getButton().setButtonIcon(32, 8, screen.getStyle("Keyboard").getString("spaceImg"));
			space.getButton().setText("");
		} else {
			KeyboardKey shift = getFunctionKey(KeyInput.KEY_LSHIFT);
			shift.getButton().setButtonIcon(16, 16, screen.getStyle("Common").getString("blankImg"));
			shift.getButton().setText(shift.label);
			KeyboardKey ret = getFunctionKey(KeyInput.KEY_RETURN);
			ret.getButton().setButtonIcon(16, 16, screen.getStyle("Common").getString("blankImg"));
			ret.getButton().setText(ret.label);
			KeyboardKey bs = getFunctionKey(KeyInput.KEY_BACK);
			bs.getButton().setButtonIcon(32, 16, screen.getStyle("Common").getString("blankImg"));
			bs.getButton().setText(bs.label);
			KeyboardKey space = getFunctionKey(KeyInput.KEY_SPACE);
			space.getButton().setButtonIcon(32, 8, screen.getStyle("Common").getString("blankImg"));
			space.getButton().setText(space.label);
		}
	}
	
	private KeyboardKey getFunctionKey(int keyCode) {
		KeyboardKey ret = null;
		for (KeyboardKey xKey : keys) {
			if (xKey.getKeyCode() == keyCode) {
				ret = xKey;
				break;
			}
		}
		return ret;
	}
	
	private String validateSymbol(String symbol) {
		if (symbol.equals("amp")) symbol= "&";
		else if (symbol.equals("lt")) symbol= "<";
		else if (symbol.equals("gt")) symbol= ">";
		else if (symbol.equals("bslash")) symbol= "\\";
		else if (symbol.equals("quot")) symbol= "\"";
		return symbol;
	}
	
	public void setGlobalShift(boolean shift) {
		if (!Symbol) {
			for (KeyboardKey key : keys) {
				if (key.getKeyType() == KeyType.ALPHA)
					key.setShift(shift);
			}
		}
	}
	
	public void setGlobalSymbol(boolean symbol) {
		this.Symbol = symbol;
		for (KeyboardKey key : keys) {
			if (key.getKeyType() == KeyType.ALPHA || key.getKeyType() == KeyType.NUMERIC)
				key.setSymbol(symbol);
		}
	}
	
	private class KeyboardKey {
		KeyType type, shiftType, symbolType;
		int keyCode, shiftKeyCode, symbolKeyCode;
		char character, shiftCharacter, symbolCharacter;
		String label, shiftLabel, symbolLabel;
		float x, y, w, h;
		boolean shift = false;
		boolean symbol = false;
		ButtonAdapter button;
		
		public KeyboardKey(KeyType type, int code, char character, String label) {
			this.type = type;
			this.keyCode = code;
			this.character = character;
			this.label = label;
		}
		
		public void setShift(KeyType type, int code, char character, String label) {
			this.shiftKeyCode = code;
			this.shiftCharacter = character;
			this.shiftLabel = label;
		}
		
		public void setSymbol(KeyType type, int code, char character, String label) {
			this.symbolKeyCode = code;
			this.symbolCharacter = character;
			this.symbolLabel = label;
		}
		
		public void setPosition(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		public void setDimensions(float w, float h) {
			this.w = w;
			this.h = h;
		}
		
		public KeyType getKeyType() { return type; }
		public int getKeyCode() { return keyCode; }
		public char getCharacter() { return character; }
		public String getLabel() { return label; }
		
		public ButtonAdapter createButton() {
			button = new ButtonAdapter(screen,
				new Vector2f(x,y),
				new Vector2f(w,h)
			) {
				@Override
				public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
					KeyInputEvent nEvt;
					if (symbol) {
						nEvt = new KeyInputEvent(symbolKeyCode, symbolCharacter, true, false);
					} else if (shift) {
						nEvt = new KeyInputEvent(shiftKeyCode, shiftCharacter, true, false);
					} else {
						nEvt = new KeyInputEvent(keyCode, character, true, false);
					}
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
					KeyInputEvent nEvt;
					if (symbol) {
						nEvt = new KeyInputEvent(symbolKeyCode, symbolCharacter, false, false);
					} else if (shift) {
						nEvt = new KeyInputEvent(shiftKeyCode, shiftCharacter, false, false);
					} else {
						nEvt = new KeyInputEvent(keyCode, character, false, false);
					}
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
				@Override
				public void onButtonStillPressedInterval() {
					KeyInputEvent nEvt;
					if (symbol) {
						nEvt = new KeyInputEvent(symbolKeyCode, symbolCharacter, true, false);
					} else if (shift) {
						nEvt = new KeyInputEvent(shiftKeyCode, shiftCharacter, true, false);
					} else {
						nEvt = new KeyInputEvent(keyCode, character, true, false);
					}
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
			};
			button.setText(label);
			button.setResetKeyboardFocus(false);
			button.removeEffect(Effect.EffectEvent.Hover);
			return button;
		}
		
		public ButtonAdapter createShiftButton() {
			button = new ButtonAdapter(screen,
				new Vector2f(x,y),
				new Vector2f(w,h)
			) {
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
					setGlobalShift(isToggled);
				}
			};
			button.setText(label);
			button.setIsToggleButton(true);
			button.setResetKeyboardFocus(false);
			button.removeEffect(Effect.EffectEvent.Hover);
			return button;
		}
		
		public ButtonAdapter createSymbolButton() {
			button = new ButtonAdapter(screen,
				new Vector2f(x,y),
				new Vector2f(w,h)
			) {
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
					setGlobalSymbol(isToggled);
				}
			};
			button.setText(label);
			button.setIsToggleButton(true);
			button.setResetKeyboardFocus(false);
			button.removeEffect(Effect.EffectEvent.Hover);
			return button;
		}
		
		public ButtonAdapter createBackButton() {
			button = new ButtonAdapter(screen,
				new Vector2f(x,y),
				new Vector2f(w,h)
			) {
				@Override
				public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
					KeyInputEvent nEvt;
					nEvt = new KeyInputEvent(KeyInput.KEY_BACK, symbolCharacter, true, false);
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
					KeyInputEvent nEvt;
					nEvt = new KeyInputEvent(KeyInput.KEY_BACK, symbolCharacter, false, false);
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
				@Override
				public void onButtonStillPressedInterval() {
					KeyInputEvent nEvt;
					nEvt = new KeyInputEvent(KeyInput.KEY_BACK, symbolCharacter, true, false);
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
			};
			button.setText(label);
			button.setResetKeyboardFocus(false);
			button.setInterval(15);
			button.removeEffect(Effect.EffectEvent.Hover);
			return button;
		}
		
		public ButtonAdapter createSpaceButton() {
			button = new ButtonAdapter(screen,
				new Vector2f(x,y),
				new Vector2f(w,h)
			) {
				@Override
				public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
					KeyInputEvent nEvt;
					nEvt = new KeyInputEvent(KeyInput.KEY_S, ' ', true, false);
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
					KeyInputEvent nEvt;
					nEvt = new KeyInputEvent(KeyInput.KEY_S, ' ', false, false);
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
				@Override
				public void onButtonStillPressedInterval() {
					KeyInputEvent nEvt;
					nEvt = new KeyInputEvent(KeyInput.KEY_S, ' ', true, false);
					nEvt.setTime(System.currentTimeMillis());
					screen.onKeyEvent(nEvt);
				}
			};
			button.setText(label);
			button.setResetKeyboardFocus(false);
		//	button.setInterval(15);
			button.removeEffect(Effect.EffectEvent.Hover);
			return button;
		}
		
		public ButtonAdapter createEnterButton() {
			button = new ButtonAdapter(screen,
				new Vector2f(x,y),
				new Vector2f(w,h)
			) {
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
					screen.hideVirtualKeyboard();
				}
			};
			button.setText(label);
			button.setResetKeyboardFocus(false);
			return button;
		}
		
		public ButtonAdapter getButton() { return this.button; }
		
		public void setShift(boolean shift) {
			this.shift = shift;
			if (shift)	button.setText(shiftLabel);
			else		button.setText(label);
		}
		
		public void setSymbol(boolean symbol) {
			this.symbol = symbol;
			if (symbol)	button.setText(symbolLabel);
			else {
				if (shift)	button.setText(shiftLabel);
				else		button.setText(label);
			}
		}
	}
}
