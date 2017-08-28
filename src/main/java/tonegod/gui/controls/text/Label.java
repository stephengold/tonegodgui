/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.text;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class Label extends Element {
	
	/**
	 * Creates a new instance of the Label control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Label(ElementManager screen, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO, dimensions,
			screen.getStyle("Label").getVector4f("resizeBorders"),
			screen.getStyle("Label").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Label control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Label(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Label").getVector4f("resizeBorders"),
			screen.getStyle("Label").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Label control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Label
	 */
	public Label(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the Label control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Label(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Label").getVector4f("resizeBorders"),
			screen.getStyle("Label").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Label control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public Label(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		// Load default font info
		this.setFontColor(screen.getStyle("Label").getColorRGBA("fontColor"));
		this.setFontSize(screen.getStyle("Label").getFloat("fontSize"));
		this.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Label").getString("textAlign")));
		this.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Label").getString("textVAlign")));
		this.setTextWrap(LineWrapMode.valueOf(screen.getStyle("Label").getString("textWrap")));
		this.setTextPadding(screen.getStyle("Label").getFloat("textPadding"));
		this.setTextClipPadding(screen.getStyle("Label").getFloat("textPadding"));
		
		this.setIsResizable(false);
		this.setScaleNS(false);
		this.setScaleEW(false);
		this.setDocking(Docking.NW);
	}
	
}
