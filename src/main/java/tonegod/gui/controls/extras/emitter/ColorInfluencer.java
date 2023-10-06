/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import com.jme3.math.ColorRGBA;
import tonegod.gui.controls.extras.Portability;
import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;
import tonegod.gui.framework.animation.Interpolation;

/**
 *
 * @author t0neg0d
 */
public class ColorInfluencer extends InfluencerBase {
	private boolean isEnabled = true;
	private ColorRGBA startColor = new ColorRGBA(ColorRGBA.Red);
	private ColorRGBA endColor = new ColorRGBA(ColorRGBA.Yellow);
	private Interpolation interpolation = Interpolation.linear;
	
	public ColorInfluencer(ElementEmitter emitter) {
		super(emitter);
	}
	
	@Override
	public void update(ElementParticle particle, float tpf) {
		if (isEnabled) {
                        Portability.interpolate(startColor, endColor, 
                                interpolation.apply(particle.blend), 
                                particle.color);
		}
	}

	@Override
	public void initialize(ElementParticle particle) {
		particle.color.set(startColor);
	}

	@Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public void setStartColor(ColorRGBA startColor) {
		this.startColor.set(startColor);
	}
	
	public void setEndColor(ColorRGBA endColor) {
		this.endColor.set(endColor);
	}
	
	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}
	
	@Override
	public ColorInfluencer clone() {
		ColorInfluencer clone = new ColorInfluencer(emitter);
		clone.setStartColor(startColor);
		clone.setEndColor(endColor);
		clone.setInterpolation(interpolation);
		clone.setIsEnabled(isEnabled);
		return clone;
	}
}
