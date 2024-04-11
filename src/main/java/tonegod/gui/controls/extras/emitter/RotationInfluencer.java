/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;

/**
 *
 * @author t0neg0d
 */
public class RotationInfluencer extends InfluencerBase {
	private boolean isEnabled = true;
	private float maxRotationSpeed;
	private boolean useRandomStartAngle = true;
	private boolean useFixedRotationSpeed = false;
	private boolean rotateFromEmitterPosition = false;
	private boolean rotateToVelocity = false;
	private Vector2f tempV2a = new Vector2f();
	private Vector2f tempV2b = new Vector2f();
	
	public RotationInfluencer(ElementEmitter emitter) {
		super(emitter);
	}
	
	@Override
	public void update(ElementParticle particle, float tpf) {
		if (isEnabled) {
			if (rotateFromEmitterPosition) {
				tempV2a.set(
					emitter.getPositionX(),
					emitter.getPositionY()
				);
				tempV2b.set(particle.position);
				particle.angle = getRotationBetween(
					tempV2a, tempV2b
				)+90;
			} else if (rotateToVelocity) {
				particle.angle = getRotationFromVelocity(particle.velocity);
			} else {
				if (particle.rotateDir)
					particle.angle += particle.rotateSpeed * tpf * FastMath.RAD_TO_DEG;
				else
					particle.angle -= particle.rotateSpeed * tpf * FastMath.RAD_TO_DEG;
			}
		}
	}
	
	@Override
	public void initialize(ElementParticle particle) {
		if (useRandomStartAngle)
			particle.angle = FastMath.rand.nextFloat()*360;
		else
			particle.angle = 0;
		if (!useFixedRotationSpeed)
			particle.rotateSpeed = FastMath.rand.nextFloat()*maxRotationSpeed;
		else
			particle.rotateSpeed = maxRotationSpeed;
	}

	@Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public void setRotateFromEmitterPosition(boolean rotateFromEmitterPosition) {
		this.rotateFromEmitterPosition = rotateFromEmitterPosition;
		if (this.rotateToVelocity) this.rotateToVelocity = false;
	}
	
	public void setRotateToVelocity(boolean rotateToVelocity) {
		this.rotateToVelocity = rotateToVelocity;
		if (this.rotateFromEmitterPosition) this.rotateFromEmitterPosition = false;
	}
	
	public void setUseFixedRotationSpeed(boolean useFixedRotationSpeed) {
		this.useFixedRotationSpeed = useFixedRotationSpeed;
	}
	
	public boolean getUseFixedRotationSpeed() {
		return this.useFixedRotationSpeed;
	}
	
	public void setMaxRotationSpeed(float maxRotationSpeed) {
		this.maxRotationSpeed = maxRotationSpeed;
	}
	
	public float getMaxRotationSpeed() { return maxRotationSpeed; }
	
	public void setUseRandomStartAngle(boolean useRandomStartAngle) {
		this.useRandomStartAngle = useRandomStartAngle;
	}
	
	public boolean getUseRandomStartAngle() { return useRandomStartAngle; }
	
	private float getRotationBetween(Vector2f v1, Vector2f v2) {
		float deltaY = v2.y - v1.y;
		float deltaX = v2.x - v1.x;
		
		return FastMath.atan2(deltaY,deltaX) * FastMath.RAD_TO_DEG;
	}
	
	private float getRotationFromVelocity(Vector2f velocity) {
		tempV2a.set(velocity).normalizeLocal();
		float angle = FastMath.atan2(tempV2a.y, tempV2a.x)*FastMath.RAD_TO_DEG;
		angle += 90;
		return angle;
		
	}
	
	@Override
	public RotationInfluencer clone() {
		RotationInfluencer clone = new RotationInfluencer(emitter);
		clone.setMaxRotationSpeed(maxRotationSpeed);
		clone.setRotateFromEmitterPosition(rotateFromEmitterPosition);
		clone.setRotateToVelocity(rotateToVelocity);
		clone.setUseFixedRotationSpeed(useFixedRotationSpeed);
		clone.setUseRandomStartAngle(useRandomStartAngle);
		clone.setIsEnabled(isEnabled);
		return clone;
	}
}
