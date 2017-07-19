/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.menuing;

import tonegod.gui.controls.buttons.CheckBox;

/**
 *
 * @author t0neg0d
 */
public class MenuItem {
	Menu menu, subMenu;
	String caption;
	Object value;
	boolean isToggleItem = false;
	CheckBox toggle = null;
	boolean isToggled = false;
	
	public MenuItem(Menu menu, String caption, Object value, Menu subMenu, boolean isToggleItem, boolean isToggled) {
		this.menu = menu;
		this.subMenu = subMenu;
		this.caption = caption;
		this.value = value;
		this.isToggleItem = isToggleItem;
		this.isToggled = isToggled;
	}
	
	/**
	 * Returns the Menu that owns this MenuItem
	 * @return Menu
	 */
	public Menu getMenu() {
		return this.menu;
	}
	
	/**
	 * Returns the SubMenu set for this MenuItem
	 * @return Menu
	 */
	public Menu getSubMenu() {
		return this.subMenu;
	}
	
	/**
	 * Returns the MenuItem's caption
	 * @return String
	 */
	public String getCaption() {
		return this.caption;
	}
	
	/**
	 * Returns the value associated with this MenuItem
	 * @return Object
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Sets if the MenuItem should be toggleable
	 * @param isToggleItem boolean
	 */
	public void setIsToggleItem(boolean isToggleItem) {
		this.isToggleItem = isToggleItem;
	}
	
	/**
	 * Returns true if the MenuItem is set to toggleable
	 * @return boolean
	 */
	public boolean getIsToggleItem() {
		return this.isToggleItem;
	}
	
	/**
	 * Toggles/Untoggles the MenuItem
	 * @param isToggled boolean
	 */
	public void setIsToggled(boolean isToggled) {
		this.isToggled = isToggled;
		if (toggle != null) {
			toggle.setIsChecked(isToggled);
		}
	}
	
	/**
	 * Test whether the MenuItem is currently toggled.
	 * @return true if toggled, otherwise false
	 */
	public boolean getIsToggled() {
		return this.isToggled;
	}
	
	/**
	 * For internal use. DO NOT CALL THIS!
	 * @param toggle CheckBox
	 */
	public void setCheckBox(CheckBox toggle) {
		this.toggle = toggle;
	}
}
