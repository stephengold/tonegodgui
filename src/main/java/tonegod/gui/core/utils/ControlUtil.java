/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core.utils;

import com.jme3.font.BitmapFont;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.text.LabelElement;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.layouts.LayoutHelper;

/**
 *
 * @author t0neg0d
 */
public class ControlUtil {
        /**
         * A private constructor to inhibit instantiation of this class.
         */
        private ControlUtil() {
        }
	
	public static Element getContainer(ElementManager screen) {
		Element el = new Element(
			screen,
			UIDUtil.getUID(),
			Vector2f.ZERO,
			Vector2f.ZERO,
			Vector4f.ZERO,
			null
		);
		el.setAsContainerOnly();
		return el;
	}
	
	public static LabelElement getLabel(ElementManager screen, String text) {
		LabelElement te = new LabelElement(screen, LayoutHelper.position(), LayoutHelper.dimensions(150,20));
		te.setSizeToText(true);
		te.setText(text);
		return te;
	}
	
	public static LabelElement getCenteredLabel(ElementManager screen, String text) {
		LabelElement te = new LabelElement(screen, LayoutHelper.position(), LayoutHelper.dimensions(150,20));
		te.setSizeToText(true);
		te.setAlignment(BitmapFont.Align.Center);
		te.setText(text);
		return te;
	}
}
