/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.effects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author t0neg0d
 */
public class BatchEffect {
	private boolean isActive = true;
	private EffectManager effectManager;
	
	List<Effect> effects = new ArrayList<>();
	
	public BatchEffect() {
		
	}
	
	public void setEffectManager(EffectManager effectManager) {
		this.effectManager = effectManager;
	}
	
	public void addEffect(Effect effect) {
		if (effect.getEffectType() != Effect.EffectType.Pulse && effect.getEffectType() != Effect.EffectType.PulseColor) {
			effects.add(effect);
		}
	}
	
	public void startBatch() {
		for (Effect effect : effects) {
			effectManager.applyEffect(effect);
		}
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public void update(float tpf) {
		boolean stillActive = false;
		for (Effect effect : effects) {
			if (effect.getIsActive()) {
				stillActive = true;
				break;
			}
		}
		isActive = stillActive;
	}
}
