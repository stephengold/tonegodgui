/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import com.jme3.math.FastMath;
import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;
import tonegod.gui.framework.animation.Interpolation;

/**
 *
 * @author t0neg0d
 */
public class SizeInfluencer extends InfluencerBase {
	private boolean isEnabled = true;
	private float startSize = 1f;
	private float endSize = 0.01f;
	private Interpolation interpolation = Interpolation.linear;
	
	public SizeInfluencer(ElementEmitter emitter) {
		super(emitter);
	}
	
	@Override
	public void update(ElementParticle particle, float tpf) {
		if (isEnabled) {
			particle.size = FastMath.interpolateLinear(interpolation.apply(particle.blend), startSize, endSize);
		}
	}

	@Override
	public void initialize(ElementParticle particle) {
		particle.size = startSize;
	}

	@Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public void setStartSize(float startSize) {
		this.startSize = startSize;
	}
	
	public void setEndSize(float endSize) {
		this.endSize = endSize;
	}
	
	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}
	
	@Override
	public SizeInfluencer clone() {
		SizeInfluencer clone = new SizeInfluencer(emitter);
		clone.setStartSize(startSize);
		clone.setEndSize(endSize);
		clone.setInterpolation(interpolation);
		clone.setIsEnabled(isEnabled);
		return clone;
	}
}
