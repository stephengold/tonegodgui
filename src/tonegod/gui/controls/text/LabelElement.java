package tonegod.gui.controls.text;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class LabelElement extends TextElement {
	private boolean sizeToText = false;
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public LabelElement(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Label").getVector2f("defaultSize"),
			Vector4f.ZERO,
			null
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public LabelElement(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Label").getVector2f("defaultSize"),
			Vector4f.ZERO,
			null
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public LabelElement(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			Vector4f.ZERO,
			null
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the TextLabel's background
	 */
	public LabelElement(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public LabelElement(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Label").getVector2f("defaultSize"),
			Vector4f.ZERO,
			null
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public LabelElement(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			Vector4f.ZERO,
			null
		);
	}
	
	/**
	 * Creates a new instance of the TextElement control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the TextLabel's background
	 */
	public LabelElement(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg,
			screen.getApplication().getAssetManager().loadFont(
				screen.getStyle("Font").getString("defaultFont")
			)
		);
	//	if (defaultImg == null)
	//		setAsContainerOnly();
		
		setFontSize(screen.getStyle("Label").getFloat("fontSize"));
		setFontColor(screen.getStyle("Label").getColorRGBA("fontColor"));
		setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Label").getString("textAlign")));
		setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Label").getString("textVAlign")));
		setTextWrap(LineWrapMode.valueOf(screen.getStyle("Label").getString("textWrap")));
		setScaleEW(false);
		setScaleNS(false);
		setIsResizable(false);
		setIsMovable(false);
		setDocking(Docking.NW);
		
		setUseTextClipping(true);
	}
	
	/**
	 * Enables/disables resizing the Label to length
	 * @param sizeToText true &rarr; resize, false &rarr; don't resize
	 */
	public void setSizeToText(boolean sizeToText) {
		this.sizeToText = sizeToText;
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		if (sizeToText) {
			this.setWidth(animText.getTotalWidth());
		}
		if (this.getTextWrap() != LineWrapMode.NoWrap) {
			this.setHeight(animText.getTotalHeight());
		}
	}
	
	@Override
	public void onUpdate(float tpf) {  }

	@Override
	public void onEffectStart() {  }

	@Override
	public void onEffectStop() {  }
	
}
