/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.listeners;

import com.jme3.input.event.TouchEvent;

/**
 *
 * @author t0neg0d
 */
public interface TouchListener {
	void onTouchDown(TouchEvent evt);
	void onTouchMove(TouchEvent evt);
	void onTouchUp(TouchEvent evt);
}
