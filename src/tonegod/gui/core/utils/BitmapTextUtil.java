/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core.utils;

import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import tonegod.gui.core.Element;

/**
 *
 * @author t0neg0d
 */
public class BitmapTextUtil {
        /**
         * A private constructor to inhibit instantiation of this class.
         */
        private BitmapTextUtil() {
        }
        
	/**
	 * Returns the width of the provided text
	 * @param ref Element The element the text will be added to (reference for font settings)
	 * @param text String the text to be evaluated
	 * @return float The width
	 */
	public static float getTextWidth(Element ref, String text) {
		BitmapText eval = new BitmapText(ref.getFont());
		eval.setSize(ref.getFontSize());
		eval.setLineWrapMode(LineWrapMode.NoWrap);
		eval.setBox(null);
		eval.setText(text);
		
		return eval.getLineWidth();
	}
	
	/**
	 * Returns the width of the provided text or the maxwidth, which ever is less
	 * @param ref Element The element the text will be added to (reference for font settings)
	 * @param text String the text to be evaluated
	 * @param maxWidth The maximum width considered a valid return value
	 * @return float The width
	 */
	public static float getTextWidth(Element ref, String text, float maxWidth) {
		BitmapText eval = new BitmapText(ref.getFont());
		eval.setSize(ref.getFontSize());
		eval.setText("Xg");
		eval.setText(text);
		
		return (eval.getLineWidth() < maxWidth) ? eval.getLineWidth() : maxWidth;
	}
	
	/**
	 * Returns the height value of a single line of text
	 * @param ref Element The element the text will be added to (reference for font settings)
	 * @param text String the text to be evaluated
	 * @return float
	 */
	public static float getTextLineHeight(Element ref, String text) {
		BitmapText eval = new BitmapText(ref.getFont());
		eval.setSize(ref.getFontSize());
		eval.setLineWrapMode(LineWrapMode.NoWrap);
		eval.setBox(null);
		eval.setText(text);
		
		return eval.getLineHeight();
	}
	
	/**
	 * Returns the total height of a wrapped text string
	 * @param ref Element The element the text will be added to (reference for font settings)
	 * @param text String the text to be evaluated
	 * @param maxWidth The maximum width considered a valid return value
	 * @return float
	 */
	public static float getTextTotalHeight(Element ref, String text, float maxWidth) {
		BitmapText eval = new BitmapText(ref.getFont());
		eval.setSize(ref.getFontSize());
		eval.setText("Xg");
		eval.setBox(new Rectangle(0,0,maxWidth, eval.getLineHeight()));
		eval.setText(text);
		
		return eval.getLineWidth()*eval.getLineCount();
	}
}
