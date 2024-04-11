/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.animation;

import tonegod.gui.framework.core.QuadData;

/**
 *
 * @author t0neg0d
 */
public class ShowAction extends TemporalAction {
	@Override
	protected void begin() {
		((QuadData)quad).show();
		setDuration(0);
	}
	
	@Override
	protected void update(float percent) {  }
	
	@Override
	protected void end() {  }

	@Override
	public void restart() {
		setTime(0);
		setComplete(false);
		setDuration(1);
		reset();
	}
}