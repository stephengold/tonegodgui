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
public class DirectionInfluencer extends InfluencerBase {
	
	private boolean isEnabled = true;
	private Vector2f direction = Vector2f.ZERO.clone();
	private Vector2f temp = new Vector2f();
	private float strength = 1;
	
	public DirectionInfluencer(ElementEmitter emitter) {
		super(emitter);
	}
	
	@Override
	public void update(ElementParticle p, float tpf) {
		
	}

	@Override
	public void initialize(ElementParticle particle) {
		if (isEnabled) {
			if (direction != Vector2f.ZERO) {
				temp.set(direction.normalize()).multLocal(particle.randforce*strength);
                                Portability.interpolate(particle.velocity, temp, 
                                        0.5f, particle.velocity);
			}
		}
	}

	@Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public void setDirection(Vector2f direction) {
		this.direction.set(direction);
	}
	
	public void setDirection(float x, float y) {
		this.direction.set(x, y);
	}
	
	public Vector2f getDirection() {
		return this.direction;
	}
	
	public void setStrength(float strength) {
		this.strength = strength;
	}
	
	public float getStrength() {
		return this.strength;
	}
	
	@Override
	public DirectionInfluencer clone() {
		DirectionInfluencer clone = new DirectionInfluencer(emitter);
		clone.setDirection(direction);
		clone.setStrength(strength);
		clone.setIsEnabled(isEnabled);
		return clone;
	}
}
