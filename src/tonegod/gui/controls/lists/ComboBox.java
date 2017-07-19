package tonegod.gui.controls.lists;

import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.menuing.Menu;
import tonegod.gui.controls.menuing.MenuItem;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class ComboBox extends TextField {
	protected ButtonAdapter btnArrowDown;
	private Menu DDList = null;
	float btnHeight;
	String ddUID;
	private int selectedIndex = -1;
	private Object selectedValue;
	private String selectedCaption;
	
	private int hlIndex;
	private Object hlValue;
	private String hlCaption;
	
	private int ssIndex;
	private Object ssValue;
	private String ssCaption;
	
	private boolean DDListIsShowing = false;
	
	protected boolean selectEnabled = true;
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public ComboBox(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ComboBox(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ComboBox(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the ComboBox
	 */
	public ComboBox(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ComboBox(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ComboBox(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the ComboBox
	 */
	public ComboBox(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		setScaleNS(false);
		setScaleEW(false);
		
		ddUID = UID + ":ddMenu";
		
		btnHeight = getHeight();
		
		setWidth(getWidth()-btnHeight);
		
	//	layoutHints.setElementPadX(btnHeight);
		
		btnArrowDown = new ButtonAdapter(screen, UID + ":ArrowDown",
			new Vector2f(
				getWidth(),
				0
			),
			new Vector2f(
				btnHeight,
				btnHeight
			)
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (validateListSize()) {
					if (screen.getElementById(DDList.getUID()) == null)
						screen.addElement(DDList);
					if (!DDList.getIsVisible()) {
						DDList.showMenu(
							null,
							getElementParent().getAbsoluteX(),
							getElementParent().getAbsoluteY()-DDList.getHeight()
						);
					} else {
						DDList.hide();
					}
				}
				screen.setTabFocusElement(getElementParent());
			}
		};
		btnArrowDown.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowDown"));
		btnArrowDown.setDocking(Docking.SW);
		this.addChild(btnArrowDown);
	}
	
	/**
	 * Adds a new list item to the drop-down list associated with this control
	 * 
	 * @param caption The String to display as the list item
	 * @param value A String value to associate with this list item
	 */
	public void addListItem(String caption, Object value) {
		if (DDList == null) {
			DDList = new Menu(screen, ddUID, new Vector2f(0,0), true) {
				@Override
				public void onMenuItemClicked(int index, Object value, boolean isToggled) {
					((ComboBox)getCallerElement()).setSelectedWithCallback(index, DDList.getMenuItem(index).getCaption(), value);
					screen.setTabFocusElement(getCallerElement());
					hide();
				}
			};
			DDList.setCallerElement(this);
			DDList.setPreferredSize(new Vector2f(getWidth()+btnHeight,DDList.getMenuItemHeight()*5));
		}
		DDList.setFontSize(fontSize);
		DDList.getScrollableArea().setFontSize(fontSize);
		DDList.addMenuItem(caption, value, null);
		
		if (screen.getElementById(DDList.getUID()) == null) {
			screen.addElement(DDList);
		}
		pack();
	//	refreshSelectedIndex();
	}
	
	/**
	 * Inserts a new List Item at the specified index
	 * @param index - List index to insert new List Item
	 * @param caption - Caption for new List Item
	 * @param value - Object to store as value
	 */
	public void insertListItem(int index, String caption, Object value) {
		if (DDList != null) {
			DDList.insertMenuItem(index, caption, value, null);
			pack();
			refreshSelectedIndex();
		}
	}
	
	/**
	 * Removes the List Item at the specified index
	 * @param index index of item to remove
	 */
	public void removeListItem(int index) {
		if (DDList != null) {
			DDList.removeMenuItem(index);
			pack();
			refreshSelectedIndex();
		}
	}
	
	/**
	 * Removes the first instance of a list item with the specified caption
	 * @param caption caption text to search for
	 */
	public void removeListItem(String caption) {
		if (DDList != null) {
			DDList.removeMenuItem(caption);
		}
	}
	
	/**
	 * Removes the first instance of a list item with the specified value
	 * @param value item to remove
	 */
	public void removeListItem(Object value) {
		if (DDList != null) {
			DDList.removeMenuItem(value);
		}
	}
	
	/**
	 * Removes all list items
	 */
	public void removeAllListItems() {
		if (DDList != null) {
			DDList.removeAllMenuItems();
		}
	}
	
	private void refreshSelectedIndex() {
		if (DDList != null) {
			if (selectedIndex > DDList.getMenuItems().size()-1)
				this.setSelectedIndexWithCallback(DDList.getMenuItems().size()-1);
		//	if (!DDList.getMenuItems().isEmpty())
		//		this.setSelectedIndex(selectedIndex);
		//	else
			if (DDList.getMenuItems().isEmpty())
				setText("");
		} else {
			setText("");
		}
	}
	
	/**
	 * Method needs to be called once last list item has been added.  This eventually
	 * will be updated to automatically be called when a new item is added to, instert into
	 * the list or an item is removed from the list.
	 */
	public void pack() {
		if (selectedIndex == -1) {
			setSelectedIndexWithCallback(0);
		}
		int rIndex = DDList.getMenuItems().size()-selectedIndex;
		float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);

		DDList.scrollThumbYTo(
			( DDList.getHeight()-diff )
		);
		refreshSelectedIndex();
	}
	
	/**
	 * Returns false if list is empty, true if list contains List Items
	 * @return boolean
	 */
	public boolean validateListSize() {
		if (DDList == null)
			return false;
		else if (DDList.getMenuItems().isEmpty())
			return false;
		else
			return true;
	}
	
	public void setSelectedByCaption(String caption, boolean useCallback) {
		MenuItem mItem = null;
		for (MenuItem mi : DDList.getMenuItems()) {
			if (mi.getCaption().equals(caption)) {
				mItem = mi;
				break;
			}
		}
		
		if (mItem != null) {
			if (useCallback)	setSelectedIndexWithCallback(DDList.getMenuItems().indexOf(mItem));
			else				setSelectedIndex(DDList.getMenuItems().indexOf(mItem));
		}
	}
	
	public void setSelectedByValue(Object value, boolean useCallback) {
		MenuItem mItem = null;
		for (MenuItem mi : DDList.getMenuItems()) {
			if (mi.getValue().equals(value)) {
				mItem = mi;
				break;
			}
		}
		
		if (mItem != null) {
			if (useCallback)	setSelectedIndexWithCallback(DDList.getMenuItems().indexOf(mItem));
			else				setSelectedIndex(DDList.getMenuItems().indexOf(mItem));
		}
	}
	
	/**
	 * Selects the List Item at the specified index and call the onChange event
	 * @param selectedIndex index of item to select
	 */
	public void setSelectedIndexWithCallback(int selectedIndex) {
		if (validateListSize()) {
			if (selectedIndex < 0)
				selectedIndex = 0;
			else if (selectedIndex > DDList.getMenuItems().size()-1)
				selectedIndex = DDList.getMenuItems().size()-1;
		
			MenuItem mi = DDList.getMenuItem(selectedIndex);
			String caption = mi.getCaption();
			Object value = mi.getValue();
			setSelectedWithCallback(selectedIndex, caption, value);
		}
	}
	
	protected void setSelectedWithCallback(int index, String caption, Object value) {
		this.hlIndex = index;
		this.selectedIndex = index;
		this.selectedCaption = caption;
		this.selectedValue = value;
		setText(selectedCaption);
		
		int rIndex = DDList.getMenuItems().size()-index;
		float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);

		DDList.scrollThumbYTo(
			( DDList.getHeight()-diff )
		);
		
		onChange(selectedIndex, selectedValue);
	}
	
	/**
	 * Select the List Item at the specified index.
	 * @param selectedIndex index of item to select
	 */
	public void setSelectedIndex(int selectedIndex) {
		if (validateListSize()) {
			if (selectedIndex < 0)
				selectedIndex = 0;
			else if (selectedIndex > DDList.getMenuItems().size()-1)
				selectedIndex = DDList.getMenuItems().size()-1;
		
			MenuItem mi = DDList.getMenuItem(selectedIndex);
			String caption = mi.getCaption();
			Object value = mi.getValue();
			setSelected(selectedIndex, caption, value);
		}
	}
	
	protected void setSelected(int index, String caption, Object value) {
		this.hlIndex = index;
		this.selectedIndex = index;
		this.selectedCaption = caption;
		this.selectedValue = value;
		setText(selectedCaption);
		
		int rIndex = DDList.getMenuItems().size()-index;
		float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);

		DDList.scrollThumbYTo(
			( DDList.getHeight()-diff )
		);
		
	//	onChange(selectedIndex, selectedValue);
	}
	
	/**
	 * Hides the ComboBox drop-down list
	 */
	public void hideDropDownList() {
		this.DDList.hideMenu();
	}
	
	@Override
	public void controlKeyPressHook(KeyInputEvent evt, String text) {
		if (validateListSize()) {
			if (evt.getKeyCode() != KeyInput.KEY_UP && evt.getKeyCode() != KeyInput.KEY_DOWN && evt.getKeyCode() != KeyInput.KEY_RETURN) {
				int miIndexOf = 0;
				int strIndex;
				for (MenuItem mi : DDList.getMenuItems()) {
					strIndex = mi.getCaption().toLowerCase().indexOf(text.toLowerCase());
					if (strIndex == 0) {
						ssIndex = miIndexOf;
						hlIndex = ssIndex;
						hlCaption = ssCaption = DDList.getMenuItem(miIndexOf).getCaption();
						hlValue = ssValue = DDList.getMenuItem(miIndexOf).getValue();

						int rIndex = DDList.getMenuItems().size()-miIndexOf;
						float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);

						DDList.scrollThumbYTo(
							( DDList.getHeight()-diff )
						);
						break;
					}
					miIndexOf++;
				}
				if (miIndexOf > -1 && miIndexOf < DDList.getMenuItems().size()-1)
					handleHightlight(miIndexOf);
				if (screen.getElementById(DDList.getUID()) == null)
						screen.addElement(DDList);
				if (!DDList.getIsVisible() && evt.getKeyCode() != KeyInput.KEY_LSHIFT && evt.getKeyCode() != KeyInput.KEY_RSHIFT)
					DDList.showMenu(null, getAbsoluteX(), getAbsoluteY()-DDList.getHeight());
			} else {
				if (evt.getKeyCode() == KeyInput.KEY_UP) {
					if (hlIndex > 0) {
						hlIndex--;
						hlCaption = DDList.getMenuItem(hlIndex).getCaption();
						hlValue = DDList.getMenuItem(hlIndex).getValue();
						int rIndex = DDList.getMenuItems().size()-hlIndex;
						float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);

						DDList.scrollThumbYTo(
							( DDList.getHeight()-diff )
						);
						handleHightlight(hlIndex);
						setSelectedWithCallback(hlIndex, hlCaption, hlValue);
					}
				} else if (evt.getKeyCode() == KeyInput.KEY_DOWN) {
					if (hlIndex < DDList.getMenuItems().size()-1) {
						hlIndex++;
						hlCaption = DDList.getMenuItem(hlIndex).getCaption();
						hlValue = DDList.getMenuItem(hlIndex).getValue();
						int rIndex = DDList.getMenuItems().size()-hlIndex;
						float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);

						DDList.scrollThumbYTo(
							( DDList.getHeight()-diff )
						);
						handleHightlight(hlIndex);
						setSelectedWithCallback(hlIndex, hlCaption, hlValue);
					}
				}
				if (evt.getKeyCode() == KeyInput.KEY_RETURN) {
					updateSelected();
				}
			}
		}
	}
	
	private void updateSelected() {
		setSelectedWithCallback(hlIndex, hlCaption, hlValue);
		if (DDList.getIsVisible()) DDList.hide();
	}
	
	private void handleHightlight(int index) {
		if (DDList.getIsVisible()) DDList.setHighlight(index);
	}
	
	/**
	 * Abstract event method called when a list item is selected/navigated to.
	 * @param selectedIndex index of item
	 * @param value value of item
	 */
	public abstract void onChange(int selectedIndex, Object value);
	
	/**
	 * Read the currently selected index.
	 * @return selectedIndex
	 */
	public int getSelectIndex() {
		return this.selectedIndex;
	}
	
	/**
	 * Access currently selected MenuItem.
	 * @return the pre-existing instance
	 */
	public MenuItem getSelectedListItem() {
		return this.DDList.getMenuItem(selectedIndex);
	}
	
	/**
	 * Access the menu item at the specified index.
	 * @param index index of desired item
	 * @return the pre-existing instance
	 */
	public MenuItem getListItemByIndex(int index) {
		return this.DDList.getMenuItem(index);
	}
	
	/**
	 * Returns a List of all ListItems 
	 * @return List or null
	 */
	public List<MenuItem> getListItems() {
                if(DDList != null) {
                    return DDList.getMenuItems();
                }
                return null;
	}
	
	/**
	 * Returns a pointer to the dropdown list (Menu)
	 * @return DDList
	 */
	public Menu getMenu() {
		return this.DDList;
	}
	
	/**
	 * Sorts the associated drop-down list alphanumerically
	 */
	public void sortList() {
		Object[] orgList = DDList.getMenuItems().toArray();
		List<MenuItem> currentList = new ArrayList<>();
		List<MenuItem> finalList = new ArrayList<>();
		List<String> map = new ArrayList<>();
		for (int i = 0; i < orgList.length; i++) {
			currentList.add((MenuItem)orgList[i]);
			map.add(((MenuItem)orgList[i]).getCaption());
		}
		Collections.sort(map);
		for (String caption : map) {
			int index;
			for (MenuItem mi : currentList) {
				if (mi.getCaption().equals(caption)) {
					index = currentList.indexOf(mi);
					finalList.add(mi);
					DDList.removeMenuItem(index);
					currentList.remove(mi);
					break;
				}
			}
		}
		for (MenuItem mi : finalList) {
			addListItem(mi.getCaption(), mi.getValue());
		}
	}
	
	/**
	 * Sorts drop-down list by true numeric values.  This should only be used
	 * with lists that start with numeric values
	 */
	public void sortListNumeric() {
		Object[] orgList = DDList.getMenuItems().toArray();
		List<MenuItem> currentList = new ArrayList<>();
		List<MenuItem> finalList = new ArrayList<>();
		List<Integer> map = new ArrayList<>();
		for (int i = 0; i < orgList.length; i++) {
			currentList.add((MenuItem)orgList[i]);
			
			boolean NaN = true;
			String tempCaption = ((MenuItem)orgList[i]).getCaption();
			while(NaN && tempCaption.length() != 0) {
				try {
					Integer.parseInt(tempCaption);
					NaN = false;
				} catch (Exception ex) {
					tempCaption = tempCaption.substring(0,tempCaption.length()-2);
				}
			}
			map.add(Integer.parseInt(tempCaption));
		}
		Collections.sort(map);
		for (Integer caption : map) {
			int index;
			for (MenuItem mi : currentList) {
				boolean NaN = true;
				String tempCaption = mi.getCaption();
				while(NaN && tempCaption.length() != 0) {
					try {
						Integer.parseInt(tempCaption);
						NaN = false;
					} catch (Exception ex) {
						tempCaption = tempCaption.substring(0,tempCaption.length()-2);
					}
				}
				if (Integer.parseInt(tempCaption) == caption) {
					index = currentList.indexOf(mi);
					finalList.add(mi);
					DDList.removeMenuItem(index);
					currentList.remove(mi);
					break;
				}
			}
		}
		for (MenuItem mi : finalList) {
			addListItem(mi.getCaption(), mi.getValue());
		}
	}
	
	@Override
	public void controlTextFieldResetTabFocusHook() {
	//	DDList.hideMenu();
	}
	
	@Override
	public void controlCleanupHook() {
		if (DDList != null)
			screen.removeElement(DDList);
	}
	
	@Override
	public void setIsEnabled(boolean isEnabled) {
		super.setIsEnabled(isEnabled);
		selectEnabled = isEnabled;
		this.btnArrowDown.setIsEnabled(isEnabled);
	}
	
	@Override
	public void onKeyPress(KeyInputEvent evt) {
		if (selectEnabled) {
			super.onKeyPress(evt);
		}
	}
}
