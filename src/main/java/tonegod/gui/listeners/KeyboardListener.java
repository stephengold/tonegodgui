/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.listeners;

import com.jme3.input.event.KeyInputEvent;

/**
 *
 * @author t0neg0d
 */
public interface KeyboardListener {
	void onKeyPress(KeyInputEvent evt);
	void onKeyRelease(KeyInputEvent evt);
}
