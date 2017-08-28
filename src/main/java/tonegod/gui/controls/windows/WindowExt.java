/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.windows;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.text.LabelElement;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.layouts.Layout;
import tonegod.gui.core.layouts.MigLayout;
import tonegod.gui.core.utils.ControlUtil;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class WindowExt extends Element {
	protected Element dragBar;
	protected LabelElement dragBarTitle;
	protected Element contentArea;
	private Vector4f dbIndents = new Vector4f();
	
	/**
	 * Creates a new instance of the WindowExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public WindowExt(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the WindowExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public WindowExt(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the WindowExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public WindowExt(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the WindowExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Window
	 */
	public WindowExt(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the WindowExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public WindowExt(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the WindowExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public WindowExt(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the WindowExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public WindowExt(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.setIsResizable(true);
		this.setScaleNS(false);
		this.setScaleEW(false);
		this.setClipPadding(screen.getStyle("Window").getFloat("clipPadding"));
		this.setMinDimensions(screen.getStyle("Window").getVector2f("minSize"));
		dbIndents.set(screen.getStyle("Window#Dragbar").getVector4f("indents"));
		float dragBarHeight = screen.getStyle("Window#Dragbar").getFloat("defaultControlSize");
		
		setLayout(new MigLayout(screen, "[]", "[" + dragBarHeight + "][]","margins " + dbIndents.y + " " + dbIndents.z + " " + dbIndents.x + " " + dbIndents.w));
		
		// Drag Bar
		dragBar = new Element(screen, UID + ":DragBar",
			new Vector2f(dbIndents.y, dbIndents.x),
			new Vector2f(getWidth()-dbIndents.y-dbIndents.z, screen.getStyle("Window#Dragbar").getFloat("defaultControlSize")),
			screen.getStyle("Window#Dragbar").getVector4f("resizeBorders"),
			screen.getStyle("Window#Dragbar").getString("defaultImg")
		);
		dragBar.getLayoutHints().define(
			"cell 0 0", "span 1 1",
		//	"pad " + dbIndents.y + " " + dbIndents.z + " " + dbIndents.x + " " + dbIndents.w,
			"dock left top", "grow true false"
		);
		dragBar.setIsMovable(true);
		dragBar.setEffectParent(true);
		addChild(dragBar);
		
		dragBarTitle = ControlUtil.getLabel(screen, " ");
		dragBarTitle.setFontSize(screen.getStyle("Window#Dragbar").getFloat("fontSize"));
		dragBarTitle.setFontColor(screen.getStyle("Window#Dragbar").getColorRGBA("fontColor"));
		dragBarTitle.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Window#Dragbar").getString("textAlign")));
		dragBarTitle.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Window#Dragbar").getString("textVAlign")));
		dragBarTitle.setTextPaddingByKey("Window#Dragbar","textPadding");
		dragBarTitle.setTextWrap(LineWrapMode.valueOf(screen.getStyle("Window#Dragbar").getString("textWrap")));
		dragBar.addChild(dragBarTitle);
		
		// Content Area
		contentArea = new Element(screen, UIDUtil.getUID(), Vector2f.ZERO, Vector2f.ZERO, Vector4f.ZERO, null) {
			@Override
			public void setControlClippingLayer(Element clippingLayer) {
				super.setControlClippingLayer(clippingLayer);
			//	setClippingLayer(clippingLayer);
				addClippingLayer(clippingLayer);
			}
			//ControlUtil.getContainer(screen);
		};
		contentArea.setAsContainerOnly();
		contentArea.getLayoutHints().define(
			"cell 1 0", "span 1 1", "pad 0 0 10 0",
			"dock left top", "grow true true"
		);
		
		addChild(contentArea);
		
		getLayout().layoutChildren();
	}
	
	public Element getContentArea() {
		return this.contentArea;
	}
	
	public void setContentLayout(Layout layout) {
		contentArea.setLayout(layout);
	}
	
	public void addWindowContent(Element el) {
		contentArea.addChild(el);
	}
}
