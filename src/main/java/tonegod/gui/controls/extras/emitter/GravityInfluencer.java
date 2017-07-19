/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import com.jme3.math.Vector2f;
import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;

/**
 *
 * @author t0neg0d
 */
public class GravityInfluencer extends InfluencerBase {
	
	private boolean isEnabled = true;
	private Vector2f gravity = new Vector2f(0,1);
	private Vector2f temp = new Vector2f();
	
	public GravityInfluencer(ElementEmitter emitter) {
		super(emitter);
	}
	
	@Override
	public void update(ElementParticle p, float tpf) {
		if (isEnabled) {
			temp.set(gravity.mult(tpf));
			p.velocity.subtractLocal(temp);
			temp.set(p.velocity).multLocal(tpf);
			p.position.addLocal(temp);
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
	
	public void setGravity(Vector2f gravity) {
		this.gravity.set(gravity.mult(100));
	}
	
	public void setGravity(float x, float y) {
		this.gravity.set(x,y).multLocal(100);
	}
	
	public Vector2f getGravity() {
		return this.gravity.mult(0.001f);
	}
	
	@Override
	public GravityInfluencer clone() {
		GravityInfluencer clone = new GravityInfluencer(emitter);
		clone.setGravity(gravity);
		clone.setIsEnabled(isEnabled);
		return clone;
	}
}
