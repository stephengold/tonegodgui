/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter.animation;

import tonegod.gui.controls.extras.emitter.ElementEmitter;
import tonegod.gui.controls.extras.emitter.Influencer;
import tonegod.gui.framework.animation.TemporalAction;

/**
 *
 * @author t0neg0d
 */
public class EmitterInfluencerAction extends TemporalAction {
	Influencer influencer;
	
	@Override
	protected void begin() {
		setTime(1f);
		setDuration(0);
		((ElementEmitter)quad).removeInfluencer(influencer.getClass());
		((ElementEmitter)quad).addInfluencer(influencer);
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
	
	public void setInfluencer(Influencer influencer) {
		this.influencer = influencer;
		setDuration(1);
	}
}
