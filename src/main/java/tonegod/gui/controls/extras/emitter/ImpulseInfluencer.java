/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.extras.Portability;
import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;

/**
 *
 * @author t0neg0d
 */
public class ImpulseInfluencer extends InfluencerBase {
	private boolean isEnabled = true;
	private Vector2f temp = new Vector2f();
	private Vector2f temp2 = new Vector2f();
	private float variationStrength = 0.35f;
	
	public ImpulseInfluencer(ElementEmitter emitter) {
		super(emitter);
	}
	
	@Override
	public void update(ElementParticle particle, float tpf) {
		if (isEnabled) {
			float incX = FastMath.nextRandomFloat();
			if (FastMath.rand.nextBoolean()) incX = -incX;
			float incY = FastMath.nextRandomFloat();
			if (FastMath.rand.nextBoolean()) incY = -incY;
			temp.set(particle.velocity).addLocal(incX, incY);
                        Portability.interpolate(particle.velocity, temp, 
                                variationStrength, particle.velocity);
		}
	}

	@Override
	public void initialize(ElementParticle particle) {
		
	}

	@Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public void setVariationStrength(float variationStrength) {
		this.variationStrength = variationStrength;
	}
	
	@Override
	public ImpulseInfluencer clone() {
		ImpulseInfluencer clone = new ImpulseInfluencer(emitter);
		clone.setVariationStrength(variationStrength);
		clone.setIsEnabled(isEnabled);
		return clone;
	}
}
