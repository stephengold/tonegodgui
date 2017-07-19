/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.listeners;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;

/**
 *
 * @author t0neg0d
 */
public interface MouseWheelListener {
	void onMouseWheelPressed(MouseButtonEvent evt);
	void onMouseWheelReleased(MouseButtonEvent evt);
	void onMouseWheelUp(MouseMotionEvent evt);
	void onMouseWheelDown(MouseMotionEvent evt);
}
