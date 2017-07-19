/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import com.jme3.math.Vector2f;
import tonegod.gui.controls.extras.Portability;
import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;
import tonegod.gui.framework.animation.Interpolation;

/**
 *
 * @author t0neg0d
 */
public class AlphaInfluencer extends InfluencerBase {
	private boolean isEnabled = true;
	private float startAlpha = 1.0f;
	private float endAlpha = 0.01f;
	private Interpolation interpolation = Interpolation.linear;
	private Vector2f tempV2a = new Vector2f();
	private Vector2f tempV2b = new Vector2f();
	
	public AlphaInfluencer(ElementEmitter emitter) {
		super(emitter);
	}
	
	@Override
	public void update(ElementParticle particle, float tpf) {
		if (isEnabled) {
			tempV2a.set(startAlpha, startAlpha);
			tempV2b.set(endAlpha, endAlpha);
                        Portability.interpolate(tempV2a, tempV2b, 
                                interpolation.apply(particle.blend), tempV2a);
			
			particle.color.set(
				particle.color.r,
				particle.color.g,
				particle.color.b,
				tempV2a.x
			);
		}
	}

	@Override
	public void initialize(ElementParticle particle) {
		particle.color.set(
				particle.color.r,
				particle.color.g,
				particle.color.b,
				startAlpha
			);
	}

	@Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public void setStartAlpha(float startAlpha) {
		this.startAlpha = startAlpha;
	}
	
	public void setEndAlpha(float endAlpha) {
		this.endAlpha = endAlpha;
	}
	
	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}
	
	@Override
	public AlphaInfluencer clone() {
		AlphaInfluencer clone = new AlphaInfluencer(emitter);
		clone.setStartAlpha(startAlpha);
		clone.setEndAlpha(endAlpha);
		clone.setInterpolation(interpolation);
		clone.setIsEnabled(isEnabled);
		return clone;
	}
}
