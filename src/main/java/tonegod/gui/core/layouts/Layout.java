/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core.layouts;

import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;

/**
 *
 * @author t0neg0d
 */
public interface Layout {
	Layout define(String... params);
	Layout set(String param);
	LayoutParam get(String key);
	void resize();
	void setHandlesResize(boolean handleResize);
	boolean getHandlesResize();
	void setOwner(Element el);
	ElementManager getScreen();
	void layoutChildren();
}