/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.form;

import java.util.ArrayList;
import java.util.List;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;

/**
 *
 * @author t0neg0d
 */
public class Form {
	private ElementManager screen;
	private List<Element> elements = new ArrayList<>();
	private int nextIndex = 0;
	private int currentTabIndex = 0;
	
	List<Character> gbSpecialCharacters;
	List<Character> gbNumeric;
	List<Character> gbAlpha;
	
	public Form(ElementManager screen) {
		this.screen = screen;
	}
	
	/**
	 * Adds the specified element and assigns it a Tab Order
	 * @param element Element to add to form
	 */
	public void addFormElement(Element element) {
		element.setForm(this);
		element.setTabIndex(nextIndex);
		nextIndex++;
		elements.add(element);
	}
	
	/**
	 * A useless, but very funny method
	 * @param element
	 * @return Element
	 */
	public Element getFormElement(Element element) {
		if (elements.contains(element))
			return elements.get(elements.indexOf(element));
		else
			return null;
	}
	
	/**
	 * Returns the element containing the specified UID, or null if it does not exist
	 * @param UID
	 * @return Element
	 */
	public Element getFormElementByID(String UID) {
		Element ret = null;
		for (Element el : elements) {
			if (el.getUID().equals(UID)) {
				ret = el;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * Removes the specified element if it exists
	 * @param element 
	 */
	public void removeFormElement(Element element) {
		int newIndex = 0;
		elements.remove(element);
		Object[] elArray = elements.toArray();
		elements.clear();
		for (int i = 0; i < elArray.length; i++) {
			((Element)elArray[i]).setTabIndex(newIndex);
			elements.add((Element)elArray[i]);
			newIndex++;
		}
	}
	
	/**
	 * Used by screen class.  Do not call directly
	 * @param element 
	 */
	public void setSelectedTabIndex(Element element) {
		currentTabIndex = element.getTabIndex();
	}
	
	/**
	 * Used by screen class.  Do not call directly
	 */
	public void tabNext() {
		currentTabIndex++;
		if (currentTabIndex == elements.size())
			currentTabIndex = 0;
		boolean elementFound = false;
		for (Element el : elements) {
			if (el.getTabIndex() == currentTabIndex) {
				screen.setTabFocusElement(el);
				elementFound = true;
			}
		}
		if (!elementFound) {
			screen.resetTabFocusElement();
			tabNext();
		}
	}
	
	/**
	 * Used by screen class.  Do not call directly
	 */
	public void tabPrev() {
		currentTabIndex--;
		if (currentTabIndex == -1)
			currentTabIndex = elements.size()-1;
		boolean elementFound = false;
		for (Element el : elements) {
			if (el.getTabIndex() == currentTabIndex) {
				screen.setTabFocusElement(el);
				elementFound = true;
			}
		}
		if (!elementFound) {
			screen.resetTabFocusElement();
			tabPrev();
		}
	}
	
	/**
	 * Placeholder, may be removed
	 */
	public void submitForm() {
		
	}
}
