/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.buttons;

import java.util.ArrayList;
import java.util.List;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class RadioButtonGroup {
	private ElementManager screen;
	private String UID;
	protected List<Button> radioButtons = new ArrayList<>();
	protected int selectedIndex = -1;
	private Button selected = null;
	
	public RadioButtonGroup(ElementManager screen) {
		this.screen = screen;
		this.UID = UIDUtil.getUID();
	}
	
	public RadioButtonGroup(ElementManager screen, String UID) {
		this.screen = screen;
		this.UID = UID;
	}
	
	/**
	 * Returns the String unique ID of the RadioButtonGroup
	 * @return String
	 */
	public String getUID() {
		return this.UID;
	}
	
	/**
	 * Adds any Button or extended class and enables the Button's Radio state
	 * @param button 
	 */
	public void addButton(Button button) {
		button.setRadioButtonGroup(this);
		radioButtons.add(button);
		
		if (selectedIndex == 0)
			setSelected(0);
	}
	
	/**
	 * Sets the current selected Radio Button to the Button associated with the provided index
	 * @param index 
	 */
	public void setSelected(int index) {
		if (this.selectedIndex != index) {
			if (index >= 0 && index < radioButtons.size()) {
				Button rb = radioButtons.get(index);
				this.selected = rb;
				this.selectedIndex = index;
				for (Button rb2 : radioButtons) {
					if (rb2 != this.selected)
						rb2.setIsToggled(false);
					else
						rb2.setIsToggled(true);
				}
				onSelect(selectedIndex, rb);
			}
		}
	}
	
	/**
	 * Sets the current selected Radio Button to the Button instance provided
	 * @param button 
	 */
	public void setSelected(Button button) {
		if (this.selected != button) {
			this.selected = button;
			this.selectedIndex = radioButtons.indexOf(button);
			for (Button rb : radioButtons) {
				if (rb != this.selected) {
					if (rb.getIsToggled())
						rb.setIsToggled(false);
				} else
					rb.setIsToggled(true);
			}
			onSelect(selectedIndex, button);
		}
	}
	
	public Button getSelected() { return this.selected; }
	
	/**
	 * Abstract event method for change in selected Radio Button
	 * @param index The index of the selected button
	 * @param value The selected button instance
	 */
	public abstract void onSelect(int index, Button value);
	
	/**
	 * An alternate way to add all Radio Buttons as children to the provided Element
	 * @param element The element to add Radio Button's to.  null = Screen
	 */
	public void setDisplayElement(Element element) {
		for (Button rb : radioButtons) {
			if (screen.getElementById(rb.getUID()) == null) {
				if (element != null) {
					element.addChild(rb);
				} else {
					screen.addElement(rb);
				}
			}
		}
	}
	
	
}
