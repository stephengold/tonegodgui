/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.windows;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.form.Form;
import tonegod.gui.controls.scrolling.ScrollPanel;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class AlertBox extends Window {
	private ScrollPanel dlg;
	private ButtonAdapter btnOk;
	protected Form form;
	
	/**
	 * Creates a new instance of the AlertBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public AlertBox(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the AlertBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public AlertBox(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the AlertBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public AlertBox(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the AlertBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the AlertBox window
	 */
	public AlertBox(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the AlertBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public AlertBox(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the AlertBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public AlertBox(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the AlertBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the AlertBox window
	 */
	public AlertBox(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		form = new Form(screen);
		
		Vector4f indents = screen.getStyle("Window").getVector4f("contentIndents");
		float controlSpacing = screen.getStyle("Common").getFloat("defaultControlSpacing");
		
		dlg = new ScrollPanel(screen, UID + ":dialog",
			new Vector2f(
				indents.y,
				indents.x+controlSpacing
			),
			new Vector2f(
				contentArea.getWidth()-indents.y-indents.z,
				contentArea.getHeight()-indents.x-indents.w-screen.getStyle("Window").getFloat("buttonAreaHeight")-(controlSpacing*2)
			)
		);
		dlg.setIsResizable(false);
		dlg.setScaleEW(true);
		dlg.setScaleNS(true);
		dlg.setUseVerticalWrap(true);
		addWindowContent(dlg);
		
		btnOk = new ButtonAdapter(screen,  UID + ":btnOk",
			new Vector2f(
				contentArea.getWidth()-screen.getStyle("Button").getVector2f("defaultSize").x-indents.z,
				contentArea.getHeight()-screen.getStyle("Button").getVector2f("defaultSize").y-indents.w
			)
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				onButtonOkPressed(evt, toggled);
			}
		};
		btnOk.setText("Ok");
		btnOk.setDocking(Docking.SE);
		addWindowContent(btnOk);
		form.addFormElement(btnOk);
	}
	
	public ScrollPanel getMessageArea() {
		return this.dlg;
	}
	
	public ButtonAdapter getButtonOk() {
		return this.btnOk;
	}
	
	/**
	 * Sets the message to display in the AlertBox
	 * @param text String The message
	 */
	public void setMsg(String text) {
		dlg.setText(text);
	}
	
	/**
	 * Access the ScrollArea containing the window message text.
	 * @return the pre-existing instance
	 */
	public ScrollPanel getTextArea() { return this.dlg; }
	
	/**
	 * Sets the text of the Ok button
	 * @param text String
	 */
	public void setButtonOkText(String text) {
		btnOk.setText(text);
	}
	
	/**
	 * Abstract method for handling Ok button click event
	 * @param evt MouseButtonEvent
	 * @param toggled boolean
	 */
	public abstract void onButtonOkPressed(MouseButtonEvent evt, boolean toggled);
	
	/**
	 * Sets the tooltip text to display when mouse hovers over the Ok button
	 * @param tip String
	 */
	public void setToolTipOkButton(String tip) {
		this.btnOk.setToolTipText(tip);
	}
}
