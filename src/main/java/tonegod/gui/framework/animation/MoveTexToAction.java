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
public class MoveTexToAction extends TemporalAction {

	private float x, y;
	
	@Override
	protected void begin() {
		quad.setTCOffsetX(x);
		quad.setTCOffsetY(y);
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
	
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
		setDuration(1);
	}
	
	@Override
	public MoveTexToAction clone() {
		MoveTexToAction mta = new MoveTexToAction();
		mta.setPosition(x, y);
		return mta;
	}
}
