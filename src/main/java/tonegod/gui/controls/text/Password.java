/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.text;

import com.jme3.font.BitmapText;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class Password extends TextField {
	char mask = '*';
	String maskedText = "";
	
	public Password(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	public Password(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	public Password(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	public Password (ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	public Password(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	public Password(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	public Password (ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
	}
	
	/**
	 * Sets the mask character to use when hiding text input
	 * @param mask char
	 */
	public void setMask(char mask) {
		this.mask= mask;
	}
	
	/**
	 * Read the mask character currently used when hiding text input.
	 * @return string consisting of a single mask character
	 */
	public String getMask() {
		return String.valueOf(this.mask);
	}
	
	@Override
	protected String getVisibleText() {
		getTextFieldText();
		
		maskedText = "";
		for (int i = 0; i < finalText.length(); i++) {
			maskedText += String.valueOf(mask);
		}
		
		widthTest = new BitmapText(font);
		widthTest.setBox(null);
		widthTest.setSize(getFontSize());
		
		int index1 = 0, index2;
		widthTest.setText(maskedText.substring(index1));
		while(widthTest.getLineWidth() > getWidth()) {
			if (index1 == caretIndex)
				break;
			index1++;
			widthTest.setText(maskedText.substring(index1));
		}
		
		index2 = maskedText.length()-1;
		if (index2 == caretIndex && caretIndex != textFieldText.size()) {
			index2 = caretIndex+1;
			widthTest.setText(maskedText.substring(index1, index2));
			while(widthTest.getLineWidth() < getWidth()) {
				if (index2 == textFieldText.size())
					break;
				index2++;
				widthTest.setText(maskedText.substring(index1, index2));
			}
		}
		if (index2 != textFieldText.size())
			index2++;
		
		if (head != index1 || tail != index2) {
			head = index1;
			tail = index2;
		}
		if (head != tail && head != -1 && tail != -1) {
			visibleText = maskedText.substring(head, tail);
		} else {
			visibleText = "";
		}
		
		widthTest.setText(maskedText.substring(head, caretIndex));
		caretX = widthTest.getLineWidth();
		setCaretPosition(getAbsoluteX()+caretX);
		
		return visibleText;
	}
	
}
