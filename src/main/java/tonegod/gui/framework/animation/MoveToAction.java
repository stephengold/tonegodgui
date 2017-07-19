/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.animation;

/**
 *
 * @author t0neg0d
 */
public class MoveToAction extends TemporalAction {

	private float x, y;
	
	@Override
	protected void begin() {
		quad.setPosition(x,y);
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
	public MoveToAction clone() {
		MoveToAction mta = new MoveToAction();
		mta.setPosition(x, y);
		return mta;
	}
}
