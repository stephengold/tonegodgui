/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import com.jme3.math.Vector2f;
import tonegod.gui.controls.extras.Portability;
import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;

/**
 *
 * @author t0neg0d
 */
public class DestinationInfluencer extends InfluencerBase {
	
	private boolean isEnabled = true;
	private Vector2f destination = Vector2f.ZERO.clone();
	private Vector2f temp = new Vector2f();
	private Vector2f temp2 = new Vector2f();
	private float strength = .25f;
	
	public DestinationInfluencer(ElementEmitter emitter) {
		super(emitter);
	}
	
	@Override
	public void update(ElementParticle p, float tpf) {
		if (isEnabled) {
			if (destination != Vector2f.ZERO) {
				temp.set(p.initialPosition);
				temp2.set(destination).subtractLocal(p.particle.getOrigin());
                                Portability.interpolate(temp, temp2, 
                                        p.blend, temp);
                                Portability.interpolate(p.position, temp, 
                                        strength, p.position);
			}
		}
	}

	@Override
	public void initialize(ElementParticle particle) {  }

	@Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public void setDestination(Vector2f direction) {
		this.destination.set(direction);
	}
	
	public Vector2f getDestination() {
		return this.destination;
	}
	
	public void setStrength(float strength) {
		this.strength = strength;
	}
	
	public float getStrength() {
		return this.strength;
	}
	
	@Override
	public DestinationInfluencer clone() {
		DestinationInfluencer clone = new DestinationInfluencer(emitter);
		clone.setDestination(destination);
		clone.setStrength(strength);
		return clone;
	}
}
