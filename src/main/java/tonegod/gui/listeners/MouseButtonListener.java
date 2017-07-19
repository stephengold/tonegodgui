/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.listeners;

import com.jme3.input.event.MouseButtonEvent;

/**
 *
 * @author t0neg0d
 */
public interface MouseButtonListener {
	void onMouseLeftPressed(MouseButtonEvent evt);
	void onMouseLeftReleased(MouseButtonEvent evt);
	void onMouseRightPressed(MouseButtonEvent evt);
	void onMouseRightReleased(MouseButtonEvent evt);
}
