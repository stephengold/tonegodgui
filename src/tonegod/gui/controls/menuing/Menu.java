/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.menuing;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.scrolling.ScrollArea;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.BitmapTextUtil;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.Effect;
import tonegod.gui.listeners.MouseButtonListener;
import tonegod.gui.listeners.MouseMovementListener;
import tonegod.gui.listeners.MouseWheelListener;

/**
 *
 * @author t0neg0d
 */
public abstract class Menu extends ScrollArea implements AutoHide, MouseMovementListener, MouseWheelListener, MouseButtonListener {
	private List<MenuItem> menuItems = new ArrayList<>();
	private Element highlight;
	private float initWidth;
	protected float menuItemHeight;
	protected Vector4f menuPadding = new Vector4f(4,4,4,4);
	private Menu caller;
	protected Element callerElement;
	protected float menuOverhang;
	protected boolean isScrollable;
	protected ColorRGBA highlightColor;
	protected int currentMenuItemIndex = -1;
	protected int currentHighlightIndex = 0;
	protected Vector2f preferredSize = Vector2f.ZERO;
	protected boolean hasSubMenus = false;
	protected boolean hasToggleItems = false;
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param isScrollable Boolean defining if the menu is a scrollable list
	 */
	public Menu(ElementManager screen, boolean isScrollable) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			isScrollable
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param isScrollable Boolean defining if the menu is a scrollable list
	 */
	public Menu(ElementManager screen, Vector2f position, boolean isScrollable) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			isScrollable
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param isScrollable Boolean defining if the menu is a scrollable list
	 */
	public Menu(ElementManager screen, Vector2f position, Vector2f dimensions, boolean isScrollable) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			isScrollable
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 * @param isScrollable Boolean defining if the menu is a scrollable list
	 */
	public Menu(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, boolean isScrollable) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg, isScrollable);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param isScrollable Boolean defining if the menu is a scrollable list
	 */
	public Menu(ElementManager screen, String UID, Vector2f position, boolean isScrollable) {
		this(screen, UID, position,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			isScrollable
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param isScrollable Boolean defining if the menu is a scrollable list
	 */
	public Menu(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, boolean isScrollable) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			isScrollable
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 * @param isScrollable Boolean defining if the menu is a scrollable list
	 */
	public Menu(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, boolean isScrollable) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg, false);
		
		menuOverhang = screen.getStyle("Menu").getFloat("menuOverhang");
		try {
			menuPadding.set(
				screen.getStyle("Menu").getFloat("menuPadding"),
				screen.getStyle("Menu").getFloat("menuPadding"),
				screen.getStyle("Menu").getFloat("menuPadding"),
				screen.getStyle("Menu").getFloat("menuPadding")
			);
		} catch (Exception ex) {  }
		try {
			menuPadding.set(
				screen.getStyle("Menu").getVector4f("menuPadding")
			);
		} catch (Exception ex) {  }
		highlightColor = screen.getStyle("Menu").getColorRGBA("highlightColor");
		
		// Load default font info
		setFontColor(screen.getStyle("Menu").getColorRGBA("fontColor"));
		setFontSize(screen.getStyle("Menu").getFloat("fontSize"));
		setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Menu").getString("textAlign")));
		setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Menu").getString("textVAlign")));
		setTextWrap(LineWrapMode.valueOf(screen.getStyle("Menu").getString("textWrap")));
	//	setTextPadding(screen.getStyle("Menu").getFloat("textPadding"));
	//	setTextClipPaddingByKey("Menu","textPadding");
		scrollableArea.setFontColor(screen.getStyle("Menu").getColorRGBA("fontColor"));
		scrollableArea.setFontSize(screen.getStyle("Menu").getFloat("fontSize"));
		scrollableArea.setTextAlign(BitmapFont.Align.valueOf(screen.getStyle("Menu").getString("textAlign")));
		scrollableArea.setTextVAlign(BitmapFont.VAlign.valueOf(screen.getStyle("Menu").getString("textVAlign")));
		scrollableArea.setTextWrap(LineWrapMode.valueOf(screen.getStyle("Menu").getString("textWrap")));
		scrollableArea.setTextPaddingByKey("Menu","textPadding");
		scrollableArea.setTextClipPaddingByKey("Menu","textPadding");
		scrollableArea.getTextClipPaddingVec().addLocal(menuPadding);
		scrollableArea.setTextPosition(0, menuPadding.x);
		
		menuItemHeight = BitmapTextUtil.getTextLineHeight(this, "Xg");
		
		scrollableArea.setText(" ");
		scrollableArea.setIgnoreMouse(true);
		scrollableArea.setHeight(menuItemHeight);
		
		initWidth = menuItemHeight*3;
		this.isScrollable = isScrollable;
		
		if (!isScrollable) {
			getVScrollBar().removeFromParent();
		}
		setWidth(initWidth+(menuPadding.x+menuPadding.y));
		
		highlight = new Element(
			screen,
			UID + ":Highlight",
			new Vector2f(0,0),
			new Vector2f(10,10),
			new Vector4f(1,1,1,1),
			null
		);
		highlight.setScaleEW(true);
		highlight.setScaleNS(false);
		highlight.setDocking(Docking.NW);
		highlight.setIgnoreMouse(true);
		
		populateEffects("Menu");
	}
	
	/**
	 * Adds a MenuItem to the Menu
	 * @param caption The display caption of the MenuItem
	 * @param value The value to associate with the MenuItem
	 * @param subMenu The associated Sub-Menu that should be displayed with thisMenuItem. null if N/A
	 */
	public void addMenuItem(String caption, Object value, Menu subMenu) {
		addMenuItem(caption, value, subMenu, false, false);
	}
	
	/**
	 * Adds a MenuItem to the Menu
	 * @param caption The display caption of the MenuItem
	 * @param value The value to associate with the MenuItem
	 * @param subMenu The associated Sub-Menu that should be displayed with thisMenuItem. null if N/A
	 * @param isToggleItem Adds a toggleable CheckBox to the MenuItem is true
	 */
	public void addMenuItem(String caption, Object value, Menu subMenu, boolean isToggleItem) {
		addMenuItem(caption, value, subMenu, isToggleItem, false);
	}
	
	/**
	 * Adds a MenuItem to the Menu
	 * @param caption The display caption of the MenuItem
	 * @param value The value to associate with the MenuItem
	 * @param subMenu The associated Sub-Menu that should be displayed with thisMenuItem. null if N/A
	 * @param isToggleItem Adds a toggleable CheckBox to the MenuItem is true
	 * @param isToggled Sets the default state of the added CheckBox
	 */
	public void addMenuItem(String caption, Object value, Menu subMenu, boolean isToggleItem, boolean isToggled) {
		this.getVScrollBar().hide();
		MenuItem menuItem = new MenuItem(
			this,
			caption,
			value,
			subMenu,
			isToggleItem,
			isToggled
		);
		
		this.menuItems.add(menuItem);
		validateSettings();
		pack();
	}
	
	/**
	 * Inserts a new MenuItem at the provided index
	 * @param index The index to insert into
	 * @param caption The display caption of the MenuItem
	 * @param value The value to associate with the MenuItem
	 * @param subMenu The associated Sub-Menu that should be displayed with thisMenuItem. null if N/A
	 */
	public void insertMenuItem(int index, String caption, Object value, Menu subMenu) {
		insertMenuItem(index, caption, value, subMenu, false, false);
	}
	
	/**
	 * Inserts a new MenuItem at the provided index
	 * @param index The index to insert into
	 * @param caption The display caption of the MenuItem
	 * @param value The value to associate with the MenuItem
	 * @param subMenu The associated Sub-Menu that should be displayed with thisMenuItem. null if N/A
	 * @param isToggleItem Adds a toggleable CheckBox to the MenuItem is true
	 */
	public void insertMenuItem(int index, String caption, Object value, Menu subMenu, boolean isToggleItem) {
		insertMenuItem(index, caption, value, subMenu, isToggleItem, false);
	}
	
	/**
	 * Inserts a new MenuItem at the provided index
	 * @param index The index to insert into
	 * @param caption The display caption of the MenuItem
	 * @param value The value to associate with the MenuItem
	 * @param subMenu The associated Sub-Menu that should be displayed with thisMenuItem. null if N/A
	 * @param isToggleItem Adds a toggleable CheckBox to the MenuItem is true
	 * @param isToggled Sets the default state of the added CheckBox
	 */
	public void insertMenuItem(int index, String caption, Object value, Menu subMenu, boolean isToggleItem, boolean isToggled) {
		if (!menuItems.isEmpty()) {
			if (index >= 0 && index < menuItems.size()) {
				this.getVScrollBar().hide();
				MenuItem menuItem = new MenuItem(
					this,
					caption,
					value,
					subMenu,
					isToggleItem,
					isToggled
				);
				this.menuItems.add(index, menuItem);
				validateSettings();
				pack();
			}
		}
	}
	
	/**
	 * Remove the MenuItem at the provided index
	 * @param index int
	 */
	public void removeMenuItem(int index) {
		this.getVScrollBar().hide();
		if (!menuItems.isEmpty()) {
			if (index >= 0 && index < menuItems.size()) {
				menuItems.remove(index);
				validateSettings();
				pack();
			}
		}
	}
	
	/**
	 * Remove the first MenuItem that contains the provided value
	 * @param value Object
	 */
	public void removeMenuItem(Object value) {
		if (!menuItems.isEmpty()) {
			int index = -1;
			int count = 0;
			for (MenuItem mi : menuItems) {
				if (mi.getValue() == value) {
					index = count;
					break;
				}
				count++;
			}
			removeMenuItem(index);
		}
	}
	
	/**
	 * Remove the first MenuItem that contains the provided caption
	 * @param caption
	 */
	public void removeMenuItem(String caption) {
		if (!menuItems.isEmpty()) {
			int index = -1;
			int count = 0;
			for (MenuItem mi : menuItems) {
				if (mi.getCaption().equals(caption)) {
					index = count;
					break;
				}
				count++;
			}
			removeMenuItem(index);
		}
	}
	
	/**
	 * Removes the first MenuItem in the Menu
	 */
	public void removeFirstMenuItem() {
		removeMenuItem(0);
	}
	
	/**
	 * Removes the last MenuItem in the Menu
	 */
	public void removeLastMenuItem() {
		if (!menuItems.isEmpty()) {
			removeMenuItem(menuItems.size()-1);
		}
	}
	
	public void removeAllMenuItems() {
		this.getVScrollBar().hide();
		if (!menuItems.isEmpty()) {
			menuItems.clear();
			validateSettings();
			pack();
		}
	}
	/**
	 * Defines the number of pixels this Menu should overhang it's parent Menu when called as a Sub-Menu
	 * @param menuOverhang 
	 */
	public void setMenuOverhang(float menuOverhang) {
		this.menuOverhang = menuOverhang;
	}
	
	/**
	 * Returns the number of pixels this Menu should overhang its parent Menu when called as a Sub-Menu
	 * @return float
	 */
	public float getMenuOverhang() {
		return this.menuOverhang;
	}
	
	/**
	 * Returns the display height of a single MenuItem
	 * @return float
	 */
	public float getMenuItemHeight() {
		return this.menuItemHeight;
	}
	
	/**
	 * Read the initial width of the Menu prior to populating the MenuItems
	 * @return width
	 */
	public float getInitialWidth() {
		return this.initWidth;
	}
	
	/**
	 * Read the amount of padding this Menu uses before rendering the 
         * MenuItems.
	 * @return number of pixels
	 */
	public float getMenuPadding() {
		return this.menuPadding.x;
	}
	
	public Vector4f getMenuPaddingVec() {
		return this.menuPadding;
	}
	
	private float getMPWidth() {
		return menuPadding.x+menuPadding.y;
	}
	
	private float getMPHeight() {
		return menuPadding.z+menuPadding.w;
	}
	
	/**
	 * Validates flags for: contains subMenus, toggle checkboxes, etc
	 */
	public void validateSettings() {
		hasSubMenus = false;
		hasToggleItems = false;
		for (MenuItem mi : menuItems) {
			if (mi.isToggleItem)
				hasToggleItems = true;
			if (mi.subMenu != null)
				hasSubMenus = true;
		}
	}
	
	/**
	 * Sets the Menu's preferredSize which is used to set maximum width and height, forcing the menu to use Scrolling
	 * @param preferredSize Vector2f
	 */
	public void setPreferredSize(Vector2f preferredSize) {
		this.preferredSize = preferredSize;
	}
	
	/**
	 * Forces the Menu to rebuild all MenuItems.  This does not need to be called, however it will not effect anything negatively if it is.
	 */
	public void pack() {
		String finalString = "";
		
		menuItemHeight = BitmapTextUtil.getTextLineHeight(this, "Xg");
		
		scrollableArea.removeAllChildren();
		scrollableArea.setHeight(menuItemHeight);
		
		int index = 0;
		float currentHeight = 0;
		float width = menuItemHeight*3;
		boolean init = true;
		
		String leftSpacer = "  ";
		String rightSpacer = "";
		
		if (callerElement == null)	leftSpacer = "        ";
		else if (hasToggleItems)	leftSpacer = "        ";
		if (hasSubMenus)			rightSpacer = "  ";
		
		for (MenuItem mi : menuItems) {
			float tWidth = (menuItemHeight*2)+BitmapTextUtil.getTextWidth(this, leftSpacer + mi.getCaption() + rightSpacer);
			width = (tWidth > width) ? tWidth : width;
			if (init) {
				finalString = leftSpacer + mi.getCaption() + rightSpacer;
				init = false;
			} else {
				finalString += "\n" + leftSpacer + mi.getCaption() + rightSpacer;
			}
			currentHeight += menuItemHeight;
			
			if (mi.getSubMenu() != null) {
				this.addSubmenuArrow(index);
			}
			if (mi.getIsToggleItem()) {
				this.addCheckBox(index, mi);
			} else {
				mi.setCheckBox(null);
			}
			index++;
		}
		scrollableArea.setText(finalString);
		
		if (preferredSize == Vector2f.ZERO) {
			this.resize(getX()+width+getMPWidth(), getY()+currentHeight+getMPHeight(), Borders.SE);
			this.setHeight(currentHeight+(menuPadding.z+menuPadding.w));
		} else {
			float nextWidth = preferredSize.x;
			float nextHeight = (currentHeight > preferredSize.y+getMPHeight()) ? preferredSize.y : currentHeight+getMPHeight();
			this.resize(getX()+nextWidth, getY()+nextHeight, Borders.SE);
			this.setWidth(nextWidth);
			this.setHeight(nextHeight);
		}
		
		scrollableArea.setX(menuPadding.x);
		scrollableArea.setWidth( ((getWidth() > width) ? getWidth() : width)-getMPWidth() );
		scrollableArea.setY(getMPHeight());
		scrollableArea.setHeight(currentHeight);
		
		
		if (highlight.getParent() == null) {
			highlight.setX(menuPadding.x);
			highlight.setY(menuPadding.z);
			highlight.setWidth( ((getWidth() > width) ? getWidth() : width)-getMPWidth() );
			highlight.setHeight(menuItemHeight);
			highlight.getElementMaterial().setColor("Color", highlightColor);
			highlight.addClippingLayer(this);
			highlight.setClipPadding(menuPadding);
			scrollableArea.addChild(highlight);
			highlight.hide();
		} else {
			highlight.setWidth( ((getWidth() > width) ? getWidth() : width)-getMPWidth() );
		}
		
		if(getScrollableHeight() > getHeight()-getMPHeight()) {
			scrollToTop();
			setWidth(getWidth());
			getVScrollBar().setX(getWidth());
			setIsResizable(true);
			setResizeN(false);
			setResizeW(false);
			setResizeE(true);
			setResizeS(true);
		}
	}
	
	private void addSubmenuArrow(int index) {
		Element elArrow = new Element(screen, getUID() + ":Arrow:" + index,
				new Vector2f(getWidth()-menuItemHeight-getMPWidth(), -(menuItems.size()*menuItemHeight)+(menuItemHeight+(index*menuItemHeight)+(menuPadding.z))),
				new Vector2f(menuItemHeight, menuItemHeight),
				new Vector4f(0,0,0,0),
				screen.getStyle("Common").getString("arrowRight")
			);
			elArrow.setScaleEW(false);
			elArrow.setScaleNS(false);
			elArrow.setDocking(Docking.SE);
			elArrow.setIsResizable(false);
			elArrow.setIsMovable(false);
			elArrow.setIgnoreMouse(true);
			elArrow.setClippingLayer(this);
			elArrow.setTextClipPadding(this.getMenuPadding());
			
			addScrollableChild(elArrow);
			
			if (!getIsVisible())
				elArrow.hide();
	}
	
	private void addCheckBox(int index, MenuItem mi) {
		CheckBox checkbox = new CheckBox(screen, getUID() + ":CheckBox:" + index,
			new Vector2f(menuPadding.x, -(menuItems.size()*menuItemHeight)+(menuItemHeight+(index*menuItemHeight)+(menuPadding.z)))
		);
		checkbox.setScaleEW(false);
		checkbox.setScaleNS(false);
		checkbox.setDocking(Docking.SW);
		checkbox.setIsResizable(false);
		checkbox.setIsMovable(false);
		checkbox.setIgnoreMouse(true);
		checkbox.setClippingLayer(this);
		checkbox.setTextClipPadding(this.getMenuPadding());

		mi.setCheckBox(checkbox);
		
		addScrollableChild(checkbox);

		if (mi.getIsToggled())
			checkbox.setIsChecked(mi.getIsToggled());
		
		if (!getIsVisible())
			checkbox.hide();
	}
	
	/**
	 * Notifies the Menu that is has been called by an Element that is expecting notification of menu item clicks
	 * @param el Element
	 */
	public final void setCallerElement(Element el) {
		this.callerElement = el;
	}
	
	/**
	 * Returns the current Element waiting notification
	 * @return the pre-existing instance
	 */
	public Element getCallerElement() {
		return this.callerElement;
	}
	
	/**
	 * Returns a list of all MenuItems associated with this menu
	 * @return List
	 */
	public List<MenuItem> getMenuItems() {
		return this.menuItems;
	}
	
	/**
	 * Returns the MenuItem at the provided index
	 * @param index int Index of the MenuItem
	 * @return MenuItem
	 */
	public MenuItem getMenuItem(int index) {
		return this.menuItems.get(index);
	}
	
	/**
	 * Shows the Menu
	 * @param caller Menu The Parent Menu that is calling the menu. null if not called by another Menu
	 * @param x float The x coord to display the Menu at
	 * @param y float the Y coord to display the Menu at
	 */
	public void showMenu(Menu caller, float x, float y) {
		this.caller = caller;
		if (caller != null) {
			if (x < 0) x = 0;
			else if (x+getWidth() > screen.getWidth()) {
				x = caller.getAbsoluteX()-getWidth()+menuOverhang;
				if (x < 0) x = 0;
			}
			if (y < 0) y = 0;
			else if (y+getHeight() > screen.getHeight())
				y -= getAbsoluteHeight()-screen.getHeight();
		} else {
			if (x < 0) x = 0;
			else if (x+getWidth() > screen.getWidth())
				x = screen.getWidth()-getWidth();
			if (y < 0) y = 0;
			else if (y+getHeight() > screen.getHeight())
				y = screen.getHeight()-getHeight();
		}
		this.moveTo(x, y);
		Effect effect = getEffect(Effect.EffectEvent.Show);
		if (effect != null)
			if (effect.getEffectType() == Effect.EffectType.FadeIn)
				this.propagateEffect(effect, false);
			else
				screen.getEffectManager().applyEffect(effect);
		else
			this.show();
	}
	
	/**
	 * Hides the menu
	 */
	public void hideMenu() {
		Effect effect = getEffect(Effect.EffectEvent.Hide);
		if (effect != null)
			if (effect.getEffectType() == Effect.EffectType.FadeOut)
				this.propagateEffect(effect, true);
			else
				screen.getEffectManager().applyEffect(effect);
		else
			this.hide();
	}
	
	protected void hideAllSubmenus(boolean upChain) {
		for (MenuItem mi : menuItems) {
			if (mi.getSubMenu() != null)
				mi.getSubMenu().hideMenu();
		}
		if (caller != null && upChain) {
			caller.hideAllSubmenus(upChain);
		}
	}
	
	private void handleMenuItemClick(MenuItem menuItem, int menuItemIndex, Object value) {
		if (menuItem.getIsToggleItem())
			menuItem.setIsToggled(!menuItem.getIsToggled());
		onMenuItemClicked(menuItemIndex, value, menuItem.getIsToggled());
		if (!Screen.isAndroid())
			hide();
	}
        
	/**
	 * Abstract method for handling menu item selection
	 * 
	 * @param index Index of MenuItem clicked
	 * @param value String value of MenuItem clicked
	 */
	public abstract void onMenuItemClicked(int index, Object value, boolean isToggled);
	
	@Override
	public void onMouseMove(MouseMotionEvent evt) {
		if (!Screen.isAndroid()) {
			float x = evt.getX()-getX();
			float y = scrollableArea.getAbsoluteHeight()-menuPadding.z-evt.getY();

			if (currentMenuItemIndex != (int)Math.floor(y/menuItemHeight)) {
				currentMenuItemIndex = (int)Math.floor(y/menuItemHeight);

				if (currentMenuItemIndex > -1 && currentMenuItemIndex < menuItems.size()) {
					setHighlight(currentMenuItemIndex);
					this.hideAllSubmenus(false);
					Menu subMenu = menuItems.get(currentMenuItemIndex).getSubMenu();
					if (subMenu != null) {
						subMenu.showMenu(this, getAbsoluteWidth()-this.menuOverhang, scrollableArea.getAbsoluteHeight()-(menuItemHeight+(currentMenuItemIndex*menuItemHeight))-(subMenu.getHeight()-menuItemHeight));
					}
				}
			}
		}
	}
	
	/**
	 * Sets the highlight Element's current position to the Y position of the supplied
	 * MenuItem index
	 * 
	 * @param index int
	 */
	public void setHighlight(int index) {
		currentHighlightIndex = index;
		if (highlight.getParent() == null)
			this.attachChild(highlight);
		highlight.setY(scrollableArea.getHeight()+scrollableArea.getY()-(index*menuItemHeight)-menuItemHeight - menuPadding.z);
	}
	
	public Element getHighlight() {
		return highlight;
	}
	
	@Override
	public void onMouseLeftPressed(MouseButtonEvent evt) {
		
		evt.setConsumed();
	}
	@Override
	public void onMouseLeftReleased(MouseButtonEvent evt) {
		boolean hasSubMenu = false;
                        
		if (Screen.isAndroid()) {
			float x = evt.getX()-getX();
			float y = scrollableArea.getAbsoluteHeight()-menuPadding.z-evt.getY();

			if (currentMenuItemIndex != (int)Math.floor(y/menuItemHeight)) {
				currentMenuItemIndex = (int)Math.floor(y/menuItemHeight);

				if (currentMenuItemIndex > -1 && currentMenuItemIndex < menuItems.size()) {
					setHighlight(currentMenuItemIndex);
					this.hideAllSubmenus(false);
					Menu subMenu = menuItems.get(currentMenuItemIndex).getSubMenu();
					if (subMenu != null) {
						subMenu.showMenu(this, getAbsoluteWidth()-this.menuOverhang, scrollableArea.getAbsoluteHeight()-(menuItemHeight+(currentMenuItemIndex*menuItemHeight))-(subMenu.getHeight()-menuItemHeight));
						hasSubMenu = true;
					}
				}
			}
		}
		
		if (currentMenuItemIndex > -1 && currentMenuItemIndex < menuItems.size())
			this.handleMenuItemClick(menuItems.get(currentMenuItemIndex), currentMenuItemIndex, menuItems.get(currentMenuItemIndex).getValue());
		
		if (!hasSubMenu) {
			this.hideAllSubmenus(true);
			if (Screen.isAndroid()) screen.handleAndroidMenuState(this);
		}
		
		evt.setConsumed();
	}
	@Override
	public void onMouseRightPressed(MouseButtonEvent evt) {
		
		evt.setConsumed();
	}
	@Override
	public void onMouseRightReleased(MouseButtonEvent evt) {
		
		evt.setConsumed();
	}
	@Override
	public void controlHideHook() {
		highlight.removeFromParent();
		currentMenuItemIndex = -1;
	}
	@Override
	public void controlScrollHook() {
		highlight.setY(scrollableArea.getHeight()+scrollableArea.getY()-(currentHighlightIndex*menuItemHeight)-menuItemHeight);
		if (getCallerElement() != null)
			screen.setTabFocusElement(getCallerElement());
	}
        
	@Override
	protected void onAdjustWidthForScroll() {
		if(highlight != null) {
			highlight.setWidth( getWidth()-getMPWidth());
		}
	}
}
