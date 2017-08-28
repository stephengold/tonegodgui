/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.scrolling;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class ScrollAreaAdapter extends ScrollArea {
	Map<String, ChildInfo> childInfo = new HashMap<>();
	List<Element> scrollableChildren = new ArrayList<>();
	
	/**
	 * Creates a new instance of the ScrollAreaAdapter control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public ScrollAreaAdapter(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollAreaAdapter control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ScrollAreaAdapter(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollAreaAdapter control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ScrollAreaAdapter(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollAreaAdapter control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the ScrollAreaAdapter
	 */
	public ScrollAreaAdapter(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the ScrollAreaAdapter control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ScrollAreaAdapter(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollAreaAdapter control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ScrollAreaAdapter(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ScrollAreaAdapter control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the ScrollAreaAdapter
	 */
	public ScrollAreaAdapter(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super (screen, UID, position, dimensions, resizeBorders, defaultImg, false);
		scrollableArea.setText("");
		scrollableArea.setIgnoreMouse(true);
		scrollableArea.setTextPosition(8,8);
		setClipPadding(4);
		setPadding(0);
	}
	
	@Override
	public void addScrollableChild(Element child) {
		child.setDockS(true);
		child.setControlClippingLayer(this);
		child.setClipPadding(15);
		childInfo.put(child.getUID(), new ChildInfo(child, child.getPosition().x, child.getPosition().y, child.getDimensions().x, child.getDimensions().y));
		scrollableChildren.add(child);
		scrollableArea.addChild(child);
		pack();
	}
	
	public void removeScrollableChild(String UID) {
		ChildInfo ci = childInfo.get(UID);
		if (ci != null) {
			Element el = ci.el;
			scrollableChildren.remove(el);
			childInfo.remove(UID);
			scrollableArea.removeChild(el);
			pack();
		}
	}
	
	public void removeScrollableChild(Element el) {
		String UID = el.getUID();
		if (childInfo.get(UID) != null) {
			childInfo.remove(UID);
			scrollableChildren.remove(el);
			scrollableArea.removeChild(el);
			pack();
		}
	}
	
	@Override
	public void setText(String text) {
		scrollableArea.setText(text);
		pack();
	}
	
	private void pack() {
		scrollableArea.setHeight(0);
		
		float nWidth = 0;
		float nHeight = 0;
		
	//	Set<String> keys = childInfo.keySet();
		for (ChildInfo el : childInfo.values()) {
		//	ChildInfo el = childInfo.get(key);
			float w = el.x+el.w;
			float h = el.y+el.h;
			nWidth = (w > nWidth) ? w : nWidth;
			nHeight = (h > nHeight) ? h : nHeight;
		}
		
		this.resize(getAbsoluteWidth(), getAbsoluteHeight(), Borders.SE);
		
		scrollableArea.setWidth(getWidth());
		scrollableArea.setHeight(nHeight+(scrollableArea.getTextPosition().y*2));
		
		if (scrollableArea.getTextElement().getHeight() > nHeight)
			scrollableArea.setHeight(scrollableArea.getTextElement().getHeight());
		
		for (Element el : scrollableChildren) {
			ChildInfo info = childInfo.get(el.getUID());
			el.setY(scrollableArea.getHeight()-info.y-info.h-getPadding());
		}
		
		scrollToTop();
	}
	
	public class ChildInfo {
		public Element el;
		public float x;
		public float y;
		public float w;
		public float h;
		
		public ChildInfo(Element el, float x, float y, float w, float h) {
			this.el = el;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}
}
