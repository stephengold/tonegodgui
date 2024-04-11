/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter.animation;

import tonegod.gui.controls.extras.emitter.ElementEmitter;
import tonegod.gui.framework.animation.TemporalAction;

/**
 *
 * @author t0neg0d
 */
public class EmitterEmitAction extends TemporalAction {
	private int numParticles = 0;
	private boolean emitAll = false;
	
	@Override
	protected void begin() {
		if (emitAll)
			((ElementEmitter)quad).emitAllParticles();
		else
			((ElementEmitter)quad).emitNumParticles(numParticles);
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
	
	public void setEmitAllParticles() {
		emitAll = true;
		numParticles = 0;
		setDuration(1);
	}
	public void setEmitNumParticles(int count) {
		emitAll = false;
		numParticles = count;
		setDuration(1);
	}
}
