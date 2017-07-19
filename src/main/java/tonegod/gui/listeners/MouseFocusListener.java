/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.listeners;

import com.jme3.input.event.MouseMotionEvent;

/**
 *
 * @author t0neg0d
 */
public interface MouseFocusListener {
	void onGetFocus(MouseMotionEvent evt);
	void onLoseFocus(MouseMotionEvent evt);
}
